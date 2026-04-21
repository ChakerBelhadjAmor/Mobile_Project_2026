package com.supervision.livraison.ui.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.supervision.livraison.LivraisonApp;
import com.supervision.livraison.databinding.ActivityControllerHomeBinding;
import com.supervision.livraison.ui.LoginActivity;

/**
 * Controller landing screen — five actions matching the specification:
 * today's deliveries, period search, dashboard, messaging, logout.
 */
public class ControllerHomeActivity extends AppCompatActivity {

    private ActivityControllerHomeBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityControllerHomeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.tvGreeting.setText("Bonjour, " + LivraisonApp.get().session().getFullName());

        b.btnToday    .setOnClickListener(v -> openList(true));
        b.btnSearch   .setOnClickListener(v -> openList(false));
        b.btnDashboard.setOnClickListener(v -> startActivity(new Intent(this, DashboardActivity.class)));
        b.btnMessaging.setOnClickListener(v -> startActivity(new Intent(this, ControllerMessagingActivity.class)));

        b.btnLogout.setOnClickListener(v -> {
            LivraisonApp.get().session().clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void openList(boolean todayOnly) {
        Intent i = new Intent(this, DeliveriesListActivity.class);
        i.putExtra("todayOnly", todayOnly);
        startActivity(i);
    }
}
