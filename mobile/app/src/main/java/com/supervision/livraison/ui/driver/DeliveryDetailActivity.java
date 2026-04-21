package com.supervision.livraison.ui.driver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.supervision.livraison.databinding.ActivityDeliveryDetailBinding;
import com.supervision.livraison.model.Livraison;
import com.supervision.livraison.viewmodel.DriverViewModel;

/**
 * Driver detail screen for one delivery — all the fields required by the
 * specification (client contact, address, Google Maps link, articles,
 * total amount, payment mode) and the actions (call, maps, update state,
 * send emergency message).
 */
public class DeliveryDetailActivity extends AppCompatActivity {

    private ActivityDeliveryDetailBinding b;
    private Livraison current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityDeliveryDetailBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle(getString(com.supervision.livraison.R.string.drv_detail_title));

        long nocde = getIntent().getLongExtra("nocde", -1L);
        if (nocde < 0) { finish(); return; }

        DriverViewModel vm = new ViewModelProvider(this).get(DriverViewModel.class);
        vm.one(nocde).observe(this, this::render);

        b.btnCall.setOnClickListener(v -> {
            if (current == null || current.clientTel == null) return;
            Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + current.clientTel));
            startActivity(call);
        });

        b.btnMaps.setOnClickListener(v -> {
            if (current == null) return;
            String q = Uri.encode(safe(current.clientAdresse) + " "
                    + safe(current.clientCodePostal) + " " + safe(current.clientVille));
            Intent maps = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + q));
            startActivity(maps);
        });

        b.btnDelivered.setOnClickListener(v -> {
            if (current == null) return;
            vm.updateEtat(current.nocde, "LIVREE", null);
            Toast.makeText(this, "État mis à jour — synchronisation en arrière-plan.", Toast.LENGTH_SHORT).show();
            finish();
        });

        b.btnNotDelivered.setOnClickListener(v -> {
            if (current == null) return;
            String reason = b.etReason.getText() == null ? "" : b.etReason.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(this, "La raison est obligatoire.", Toast.LENGTH_LONG).show();
                return;
            }
            vm.updateEtat(current.nocde, "NON_LIVREE", reason);
            Toast.makeText(this, "Non livrée — synchronisation en arrière-plan.", Toast.LENGTH_SHORT).show();
            finish();
        });

        b.btnEmergency.setOnClickListener(v -> {
            if (current == null) return;
            Intent i = new Intent(this, EmergencyMessageActivity.class);
            i.putExtra("nocde",     current.nocde);
            i.putExtra("clientTel", current.clientTel);
            startActivity(i);
        });
    }

    private void render(Livraison l) {
        if (l == null) return;
        current = l;
        b.tvTitle.setText("Cmd #" + l.nocde + "  ·  " + safe(l.etatliv));
        b.tvClient.setText(safe(l.clientNom) + "\n" + safe(l.clientTel));
        b.tvAddress.setText(safe(l.clientAdresse) + "\n"
                + safe(l.clientCodePostal) + " " + safe(l.clientVille));
        b.tvPayment.setText("Mode de paiement : " + safe(l.modepay));
        b.tvTotals.setText("Articles : " + (l.nbArticles == null ? 0 : l.nbArticles)
                + "   ·   Montant : " + String.format("%.2f", l.montantTotal == null ? 0d : l.montantTotal));
    }

    private static String safe(String s) { return s == null ? "" : s; }
}
