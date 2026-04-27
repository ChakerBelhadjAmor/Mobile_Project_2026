package com.supervision.livraison.ui.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supervision.livraison.databinding.ActivityDeliveriesListBinding;
import com.supervision.livraison.model.Livraison;
import com.supervision.livraison.ui.adapter.LivraisonAdapter;
import com.supervision.livraison.viewmodel.ControllerViewModel;

import java.util.List;

/**
 * Controller list screen.
 *
 * <p>Two modes driven by the {@code todayOnly} intent extra:
 * <ul>
 *   <li>{@code true}  — real-time monitoring of today's deliveries.</li>
 *   <li>{@code false} — full search with period + state + order number filters.</li>
 * </ul>
 */
public class DeliveriesListActivity extends AppCompatActivity {

    private ActivityDeliveriesListBinding b;
    private ControllerViewModel vm;
    private LivraisonAdapter adapter;
    private LiveData<List<Livraison>> current;
    private Observer<List<Livraison>> observer;
    private final Handler poller = new Handler(Looper.getMainLooper());
    private Runnable pollTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityDeliveriesListBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        vm = new ViewModelProvider(this).get(ControllerViewModel.class);

        adapter = new LivraisonAdapter(l -> {
            // Controller list is read-only — tapping a card does nothing yet;
            // the driver's detail screen is used for modifications.
        });
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);

        boolean todayOnly = getIntent().getBooleanExtra("todayOnly", true);
        b.filterBar.setVisibility(todayOnly ? View.GONE : View.VISIBLE);
        setTitle(todayOnly ? "Livraisons du jour" : "Recherche des livraisons");

        if (todayOnly) {
            bind(vm.today());
        } else {
            b.btnApplyFilter.setOnClickListener(v -> {
                String from = text(b.etFrom);
                String to   = text(b.etTo);
                String etat = text(b.etEtat);
                Long   nocde = parseLong(text(b.etNoCde));
                bind(vm.search(from, to, etat, null, nocde));
            });
            // Initial unfiltered search
            bind(vm.search(null, null, null, null, null));
        }

        b.swipe.setOnRefreshListener(() -> {
            if (todayOnly) bind(vm.today());
            else bind(vm.search(text(b.etFrom), text(b.etTo), text(b.etEtat), null, parseLong(text(b.etNoCde))));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean todayOnly = getIntent().getBooleanExtra("todayOnly", true);
        pollTask = new Runnable() {
            @Override public void run() {
                if (todayOnly) bind(vm.today());
                else bind(vm.search(text(b.etFrom), text(b.etTo), text(b.etEtat), null, parseLong(text(b.etNoCde))));
                poller.postDelayed(this, 10_000);
            }
        };
        poller.post(pollTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        poller.removeCallbacks(pollTask);
    }

    private void bind(LiveData<List<Livraison>> next) {
        if (current != null && observer != null) current.removeObserver(observer);
        current  = next;
        observer = list -> {
            adapter.submit(list);
            b.swipe.setRefreshing(false);
        };
        current.observe(this, observer);
    }

    private static String text(com.google.android.material.textfield.TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private static Long parseLong(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }
}
