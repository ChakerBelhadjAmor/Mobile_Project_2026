package com.supervision.livraison.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.supervision.livraison.model.Livraison;
import com.supervision.livraison.repository.DriverRepository;

import java.util.List;

/**
 * ViewModel backing the driver screens. Owns a single {@link DriverRepository}
 * and delegates all business logic to it.
 */
public class DriverViewModel extends AndroidViewModel {

    private final DriverRepository repo;

    public DriverViewModel(@NonNull Application app) {
        super(app);
        this.repo = new DriverRepository(app);
    }

    public LiveData<List<Livraison>> today(long driverId) {
        repo.refreshToday(driverId);
        return repo.observeToday(driverId);
    }

    public LiveData<Livraison> one(long nocde) {
        return repo.observe(nocde);
    }

    public void refresh(long driverId)      { repo.refreshToday(driverId); }
    public void syncPending()               { repo.syncPending(); }
    public void updateEtat(long nocde, String etat, String remarque) {
        repo.updateEtat(nocde, etat, remarque);
    }
}
