package com.supervision.livraison.ui.driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supervision.livraison.LivraisonApp;
import com.supervision.livraison.databinding.ActivityDriverHomeBinding;
import com.supervision.livraison.ui.LoginActivity;
import com.supervision.livraison.ui.adapter.LivraisonAdapter;
import com.supervision.livraison.viewmodel.DriverViewModel;

/**
 * Driver landing screen — shows today's deliveries with minimal fields
 * (order number, client name + phone, city) and routes to the detail
 * screen on tap.
 */
public class DriverHomeActivity extends AppCompatActivity {

    private ActivityDriverHomeBinding b;
    private DriverViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityDriverHomeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        long driverId = LivraisonApp.get().session().getUserId();
        b.tvHeader.setText(getString(com.supervision.livraison.R.string.drv_home_title)
                + " — " + LivraisonApp.get().session().getFullName());

        vm = new ViewModelProvider(this).get(DriverViewModel.class);

        LivraisonAdapter adapter = new LivraisonAdapter(l -> {
            Intent i = new Intent(this, DeliveryDetailActivity.class);
            i.putExtra("nocde", l.nocde);
            startActivity(i);
        });
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);

        vm.today(driverId).observe(this, adapter::submit);
        vm.syncPending();   // push anything that stayed dirty after last close

        b.swipe.setOnRefreshListener(() -> {
            vm.refresh(driverId);
            vm.syncPending();
            b.swipe.setRefreshing(false);
        });

        b.btnLogout.setOnClickListener(v -> {
            LivraisonApp.get().session().clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
