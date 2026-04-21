package com.supervision.livraison.ui.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supervision.livraison.LivraisonApp;
import com.supervision.livraison.databinding.ActivityControllerMessagingBinding;
import com.supervision.livraison.model.PersonnelSummary;
import com.supervision.livraison.ui.adapter.MessageAdapter;
import com.supervision.livraison.viewmodel.ControllerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Two-way messaging screen for the controller.
 *
 * <ul>
 *   <li>Send an INFO message to a driver picked from a dropdown.</li>
 *   <li>Poll the inbox every 10s to surface driver EMERGENCY alerts.</li>
 * </ul>
 */
public class ControllerMessagingActivity extends AppCompatActivity {

    private ActivityControllerMessagingBinding b;
    private ControllerViewModel vm;
    private final MessageAdapter adapter = new MessageAdapter();
    private final List<PersonnelSummary> drivers = new ArrayList<>();
    private final Handler poll = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityControllerMessagingBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Messagerie");

        vm = new ViewModelProvider(this).get(ControllerViewModel.class);

        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);

        ArrayAdapter<PersonnelSummary> spAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, drivers);
        b.spinnerDriver.setAdapter(spAdapter);

        vm.drivers().observe(this, list -> {
            drivers.clear();
            if (list != null) drivers.addAll(list);
            spAdapter.notifyDataSetChanged();
        });

        long me = LivraisonApp.get().session().getUserId();
        vm.inbox(me).observe(this, adapter::submit);

        b.btnSend.setOnClickListener(v -> {
            if (drivers.isEmpty()) {
                Toast.makeText(this, "Aucun livreur disponible", Toast.LENGTH_SHORT).show();
                return;
            }
            PersonnelSummary target = drivers.get(b.spinnerDriver.getSelectedItemPosition());
            String body = b.etBody.getText() == null ? "" : b.etBody.getText().toString().trim();
            if (body.isEmpty()) return;
            vm.sendMessage(me, target.id, null, body, "INFO", () -> {
                runOnUiThread(() -> {
                    b.etBody.setText("");
                    Toast.makeText(this, "Envoyé à " + target.nom, Toast.LENGTH_SHORT).show();
                });
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh every 10s — the closest we get to "real-time" without sockets.
        poll.post(pollRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        poll.removeCallbacks(pollRunnable);
    }

    private final Runnable pollRunnable = new Runnable() {
        @Override public void run() {
            long me = LivraisonApp.get().session().getUserId();
            vm.inbox(me).observe(ControllerMessagingActivity.this, adapter::submit);
            poll.postDelayed(this, 10_000);
        }
    };
}
