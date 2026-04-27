package com.supervision.livraison.ui.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.supervision.livraison.LivraisonApp;
import com.supervision.livraison.R;
import com.supervision.livraison.databinding.ActivityControllerHomeBinding;
import com.supervision.livraison.ui.LoginActivity;

public class ControllerHomeActivity extends AppCompatActivity {

    private ActivityControllerHomeBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityControllerHomeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.tvGreeting.setText("Bonjour, " + LivraisonApp.get().session().getFullName());

        b.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_today) {
                openList(true);
            } else if (id == R.id.nav_search) {
                openList(false);
            } else if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
            } else if (id == R.id.nav_messaging) {
                startActivity(new Intent(this, ControllerMessagingActivity.class));
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            LivraisonApp.get().session().clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openList(boolean todayOnly) {
        Intent i = new Intent(this, DeliveriesListActivity.class);
        i.putExtra("todayOnly", todayOnly);
        startActivity(i);
    }
}
