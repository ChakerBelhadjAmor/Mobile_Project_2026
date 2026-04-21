package com.supervision.livraison.ui.controller;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.supervision.livraison.databinding.ActivityDashboardBinding;
import com.supervision.livraison.model.DashboardStat;
import com.supervision.livraison.viewmodel.ControllerViewModel;

import java.util.List;

/**
 * Tabular dashboard showing the two aggregated views required by the spec:
 * deliveries per driver × state, and deliveries per client × state.
 */
public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Tableau de bord");

        ControllerViewModel vm = new ViewModelProvider(this).get(ControllerViewModel.class);
        vm.byLivreur().observe(this, stats -> render(b.tableLivreur, stats, "Livreur"));
        vm.byClient() .observe(this, stats -> render(b.tableClient,  stats, "Client"));
    }

    private void render(TableLayout table, List<DashboardStat> stats, String groupLabel) {
        table.removeAllViews();
        table.addView(headerRow(groupLabel, "État", "Nb"));
        if (stats == null) return;
        for (DashboardStat s : stats) {
            table.addView(row(s.groupLabel, s.etat, s.count == null ? "0" : s.count.toString()));
        }
    }

    private TableRow headerRow(String... cols) {
        TableRow row = new TableRow(this);
        for (String c : cols) row.addView(cell(c, true));
        return row;
    }

    private TableRow row(String... cols) {
        TableRow row = new TableRow(this);
        for (String c : cols) row.addView(cell(c, false));
        return row;
    }

    private TextView cell(String text, boolean header) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setPadding(12, 10, 12, 10);
        tv.setGravity(Gravity.START);
        if (header) {
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(getResources().getColor(com.supervision.livraison.R.color.primary, getTheme()));
        }
        return tv;
    }
}
