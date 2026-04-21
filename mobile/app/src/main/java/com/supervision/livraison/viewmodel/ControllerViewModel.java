package com.supervision.livraison.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.supervision.livraison.model.DashboardStat;
import com.supervision.livraison.model.Livraison;
import com.supervision.livraison.model.Message;
import com.supervision.livraison.model.PersonnelSummary;
import com.supervision.livraison.repository.ControllerRepository;

import java.util.List;

/**
 * ViewModel backing the controller screens.
 */
public class ControllerViewModel extends ViewModel {

    private final ControllerRepository repo = new ControllerRepository();

    public LiveData<List<Livraison>> today() {
        return repo.today();
    }

    public LiveData<List<Livraison>> search(String from, String to, String etat,
                                            Long livreur, Long nocde) {
        return repo.search(from, to, etat, livreur, nocde);
    }

    public LiveData<List<DashboardStat>> byLivreur() { return repo.dashboardByLivreur(); }
    public LiveData<List<DashboardStat>> byClient()  { return repo.dashboardByClient(); }

    public LiveData<List<PersonnelSummary>> drivers() { return repo.drivers(); }
    public LiveData<List<Message>> inbox(long id)     { return repo.inbox(id); }

    public void sendMessage(long sender, long recipient, Long nocde,
                            String body, String type, Runnable done) {
        repo.sendMessage(sender, recipient, nocde, body, type, done);
    }
}
