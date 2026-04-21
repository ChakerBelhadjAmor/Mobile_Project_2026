package com.supervision.livraison.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supervision.livraison.api.ApiClient;
import com.supervision.livraison.api.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel for the login screen. Exposes the server response as
 * {@link LiveData} and a human-readable error message on failure.
 */
public class LoginViewModel extends ViewModel {

    private final ApiService api = ApiClient.get();
    private final MutableLiveData<Map<String, Object>> result = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<Map<String, Object>> result() { return result; }
    public LiveData<String> error()              { return error; }

    public void login(String login, String motP) {
        Map<String, String> body = new HashMap<>();
        body.put("login", login);
        body.put("motP",  motP);
        api.login(body).enqueue(new Callback<Map<String, Object>>() {
            @Override public void onResponse(Call<Map<String, Object>> c, Response<Map<String, Object>> r) {
                if (r.isSuccessful() && r.body() != null) {
                    result.postValue(r.body());
                } else {
                    error.postValue("Identifiants invalides");
                }
            }
            @Override public void onFailure(Call<Map<String, Object>> c, Throwable t) {
                error.postValue("Serveur injoignable — vérifiez votre connexion.");
            }
        });
    }
}
