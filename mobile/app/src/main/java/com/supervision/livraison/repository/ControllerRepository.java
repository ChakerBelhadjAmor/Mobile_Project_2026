package com.supervision.livraison.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.supervision.livraison.api.ApiClient;
import com.supervision.livraison.api.ApiService;
import com.supervision.livraison.model.DashboardStat;
import com.supervision.livraison.model.Livraison;
import com.supervision.livraison.model.Message;
import com.supervision.livraison.model.PersonnelSummary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Online repository for the controller module.
 *
 * <p>Unlike the driver, the controller works from a tablet in an office —
 * we assume network is available, so we serve data straight from the API
 * into {@link MutableLiveData} without caching.
 */
public class ControllerRepository {

    private final ApiService api = ApiClient.get();

    // ---- Deliveries ----------------------------------------------------------

    public LiveData<List<Livraison>> search(String from, String to, String etat,
                                            Long livreur, Long nocde) {
        MutableLiveData<List<Livraison>> data = new MutableLiveData<>();
        api.search(nz(from), nz(to), nz(etat), livreur, nocde)
                .enqueue(simpleList(data));
        return data;
    }

    public LiveData<List<Livraison>> today() {
        MutableLiveData<List<Livraison>> data = new MutableLiveData<>();
        api.today().enqueue(simpleList(data));
        return data;
    }

    // ---- Dashboard -----------------------------------------------------------

    public LiveData<List<DashboardStat>> dashboardByLivreur() {
        MutableLiveData<List<DashboardStat>> data = new MutableLiveData<>();
        api.byLivreur().enqueue(simpleList(data));
        return data;
    }

    public LiveData<List<DashboardStat>> dashboardByClient() {
        MutableLiveData<List<DashboardStat>> data = new MutableLiveData<>();
        api.byClient().enqueue(simpleList(data));
        return data;
    }

    // ---- Personnel / messaging ----------------------------------------------

    public LiveData<List<PersonnelSummary>> drivers() {
        MutableLiveData<List<PersonnelSummary>> data = new MutableLiveData<>();
        api.personnel("LIVREUR").enqueue(simpleList(data));
        return data;
    }

    public LiveData<List<Message>> inbox(long userId) {
        MutableLiveData<List<Message>> data = new MutableLiveData<>();
        api.inbox(userId).enqueue(simpleList(data));
        return data;
    }

    public void sendMessage(long sender, long recipient, Long nocde, String body,
                            String type, Runnable onDone) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("senderId",    sender);
        payload.put("recipientId", recipient);
        payload.put("nocde",       nocde);
        payload.put("body",        body);
        payload.put("type",        type);
        api.sendMessage(payload).enqueue(new Callback<Message>() {
            @Override public void onResponse(Call<Message> c, Response<Message> r) {
                if (onDone != null) onDone.run();
            }
            @Override public void onFailure(Call<Message> c, Throwable t) {
                if (onDone != null) onDone.run();
            }
        });
    }

    // ---- Helpers -------------------------------------------------------------

    private static String nz(String s) { return (s == null || s.isEmpty()) ? null : s; }

    private static <T> Callback<List<T>> simpleList(MutableLiveData<List<T>> target) {
        return new Callback<List<T>>() {
            @Override public void onResponse(Call<List<T>> c, Response<List<T>> r) {
                if (r.isSuccessful()) target.postValue(r.body());
            }
            @Override public void onFailure(Call<List<T>> c, Throwable t) {
                target.postValue(null);
            }
        };
    }
}
