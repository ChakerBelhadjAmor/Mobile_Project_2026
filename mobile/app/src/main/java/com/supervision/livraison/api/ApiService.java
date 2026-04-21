package com.supervision.livraison.api;

import com.supervision.livraison.model.DashboardStat;
import com.supervision.livraison.model.Livraison;
import com.supervision.livraison.model.Message;
import com.supervision.livraison.model.PersonnelSummary;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Retrofit surface mirroring the backend REST API.
 * Every method returns a {@link Call}; the repositories translate those into
 * LiveData for the ViewModels.
 */
public interface ApiService {

    // ---- Auth ----------------------------------------------------------------
    @POST("api/auth/login")
    Call<Map<String, Object>> login(@Body Map<String, String> body);

    // ---- Deliveries ----------------------------------------------------------
    @GET("api/livraisons")
    Call<List<Livraison>> search(@Query("from")    String from,
                                 @Query("to")      String to,
                                 @Query("etat")    String etat,
                                 @Query("livreur") Long   livreur,
                                 @Query("nocde")   Long   nocde);

    @GET("api/livraisons/today")
    Call<List<Livraison>> today();

    @GET("api/livraisons/driver/{id}/today")
    Call<List<Livraison>> driverToday(@Path("id") long id);

    @GET("api/livraisons/{nocde}")
    Call<Livraison> one(@Path("nocde") long nocde);

    @PUT("api/livraisons/{nocde}/etat")
    Call<Livraison> updateEtat(@Path("nocde") long nocde, @Body Map<String, String> body);

    // ---- Dashboard -----------------------------------------------------------
    @GET("api/dashboard/by-livreur")
    Call<List<DashboardStat>> byLivreur();

    @GET("api/dashboard/by-client")
    Call<List<DashboardStat>> byClient();

    // ---- Personnel -----------------------------------------------------------
    @GET("api/personnel")
    Call<List<PersonnelSummary>> personnel(@Query("role") String role);

    // ---- Messages ------------------------------------------------------------
    @POST("api/messages")
    Call<Message> sendMessage(@Body Map<String, Object> body);

    @GET("api/messages/inbox/{userId}")
    Call<List<Message>> inbox(@Path("userId") long userId);

    @GET("api/messages/unread/{userId}")
    Call<List<Message>> unread(@Path("userId") long userId);

    @PUT("api/messages/{id}/read")
    Call<Void> markRead(@Path("id") long id);
}
