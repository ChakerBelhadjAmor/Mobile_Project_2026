package com.supervision.livraison.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.supervision.livraison.api.ApiClient;
import com.supervision.livraison.api.ApiService;
import com.supervision.livraison.db.AppDatabase;
import com.supervision.livraison.db.LivraisonDao;
import com.supervision.livraison.model.Livraison;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Offline-first data source for the driver module.
 *
 * <p>Strategy:
 * <ul>
 *   <li>The UI always observes {@link LivraisonDao#byDriver(long)} — the
 *       database is the single source of truth.</li>
 *   <li>On app open (and pull-to-refresh), {@link #refreshToday(long)} pulls
 *       today's deliveries once and writes them into Room.</li>
 *   <li>State updates happen in Room immediately ({@code dirty = true}) so
 *       the driver keeps working without network. {@link #syncPending()}
 *       flushes the dirty queue to the backend.</li>
 * </ul>
 */
public class DriverRepository {

    private static final String TAG = "DriverRepository";

    private final LivraisonDao    dao;
    private final ApiService      api;
    private final ExecutorService io;

    public DriverRepository(Context ctx) {
        this.dao = AppDatabase.get(ctx).livraisonDao();
        this.api = ApiClient.get();
        this.io  = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Livraison>> observeToday(long driverId) {
        return dao.byDriver(driverId);
    }

    public LiveData<Livraison> observe(long nocde) {
        return dao.byId(nocde);
    }

    /** Pull today's deliveries from the backend and cache them locally. */
    public void refreshToday(long driverId) {
        api.driverToday(driverId).enqueue(new Callback<List<Livraison>>() {
            @Override public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    io.execute(() -> dao.upsertAll(resp.body()));
                }
            }
            @Override public void onFailure(Call<List<Livraison>> call, Throwable t) {
                Log.w(TAG, "refreshToday failed — staying on cached data", t);
            }
        });
    }

    /**
     * Record a state change locally and attempt to push it immediately.
     * If the push fails (no network), the row keeps {@code dirty = true}
     * and will be re-tried by {@link #syncPending()}.
     */
    public void updateEtat(long nocde, String etat, String remarque) {
        io.execute(() -> {
            Livraison l = dao.byIdSync(nocde);
            if (l == null) return;
            l.etatliv         = etat;
            l.remarque        = remarque;
            l.pendingEtat     = etat;
            l.pendingRemarque = remarque;
            l.dirty           = true;
            dao.update(l);
            pushOne(l);
        });
    }

    /** Re-push every dirty row. Call on app resume / network regained. */
    public void syncPending() {
        io.execute(() -> {
            List<Livraison> dirty = dao.dirtyRows();
            if (dirty == null) dirty = Collections.emptyList();
            for (Livraison l : dirty) pushOne(l);
        });
    }

    private void pushOne(Livraison l) {
        Map<String, String> body = new HashMap<>();
        body.put("etatliv",  l.pendingEtat  != null ? l.pendingEtat  : l.etatliv);
        body.put("remarque", l.pendingRemarque);
        api.updateEtat(l.nocde, body).enqueue(new Callback<Livraison>() {
            @Override public void onResponse(Call<Livraison> call, Response<Livraison> resp) {
                if (resp.isSuccessful()) {
                    io.execute(() -> {
                        Livraison stored = dao.byIdSync(l.nocde);
                        if (stored == null) return;
                        stored.dirty = false;
                        stored.pendingEtat = null;
                        stored.pendingRemarque = null;
                        dao.update(stored);
                    });
                }
            }
            @Override public void onFailure(Call<Livraison> call, Throwable t) {
                Log.w(TAG, "pushOne failed — will retry on next sync", t);
            }
        });
    }
}
