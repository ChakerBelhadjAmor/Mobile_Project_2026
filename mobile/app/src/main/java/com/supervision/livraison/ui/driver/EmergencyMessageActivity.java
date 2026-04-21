package com.supervision.livraison.ui.driver;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.supervision.livraison.LivraisonApp;
import com.supervision.livraison.api.ApiClient;
import com.supervision.livraison.api.ApiService;
import com.supervision.livraison.databinding.ActivityEmergencyMessageBinding;
import com.supervision.livraison.model.Message;
import com.supervision.livraison.model.PersonnelSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Lets a driver fire an EMERGENCY message to a controller. The order number
 * and client phone are prefilled from the selected delivery — requirement
 * 2.4 of the spec.
 */
public class EmergencyMessageActivity extends AppCompatActivity {

    private ActivityEmergencyMessageBinding b;
    private final List<PersonnelSummary> controllers = new ArrayList<>();
    private final ApiService api = ApiClient.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityEmergencyMessageBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Alerte urgente");

        long   nocde     = getIntent().getLongExtra("nocde", -1L);
        String clientTel = getIntent().getStringExtra("clientTel");
        b.tvContext.setText("Alerte pour la commande #" + nocde
                + (clientTel != null ? "\nTel client : " + clientTel : ""));

        ArrayAdapter<PersonnelSummary> spAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, controllers);
        b.spinnerController.setAdapter(spAdapter);

        api.personnel("CONTROLEUR").enqueue(new Callback<List<PersonnelSummary>>() {
            @Override public void onResponse(Call<List<PersonnelSummary>> c, Response<List<PersonnelSummary>> r) {
                if (r.isSuccessful() && r.body() != null) {
                    controllers.clear();
                    controllers.addAll(r.body());
                    spAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<List<PersonnelSummary>> c, Throwable t) { /* ignore */ }
        });

        b.btnSend.setOnClickListener(v -> {
            if (controllers.isEmpty()) {
                Toast.makeText(this, "Aucun contrôleur disponible", Toast.LENGTH_SHORT).show();
                return;
            }
            PersonnelSummary target = controllers.get(b.spinnerController.getSelectedItemPosition());
            String body = b.etBody.getText() == null ? "" : b.etBody.getText().toString().trim();
            if (body.isEmpty()) return;

            Map<String, Object> payload = new HashMap<>();
            payload.put("senderId",    LivraisonApp.get().session().getUserId());
            payload.put("recipientId", target.id);
            payload.put("nocde",       nocde);
            payload.put("clientTel",   clientTel);
            payload.put("body",        body);
            payload.put("type",        "EMERGENCY");

            api.sendMessage(payload).enqueue(new Callback<Message>() {
                @Override public void onResponse(Call<Message> c, Response<Message> r) {
                    Toast.makeText(EmergencyMessageActivity.this, "Alerte envoyée.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                @Override public void onFailure(Call<Message> c, Throwable t) {
                    Toast.makeText(EmergencyMessageActivity.this, "Échec de l'envoi.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
