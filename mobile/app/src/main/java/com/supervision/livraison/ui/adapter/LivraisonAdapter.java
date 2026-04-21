package com.supervision.livraison.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.supervision.livraison.R;
import com.supervision.livraison.model.Livraison;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for delivery cards. Used by both the controller list
 * and the driver daily view — the two screens differ in the data source
 * but share this row layout.
 */
public class LivraisonAdapter extends RecyclerView.Adapter<LivraisonAdapter.VH> {

    public interface OnClick { void onClick(Livraison l); }

    private final List<Livraison> items = new ArrayList<>();
    private final OnClick onClick;

    public LivraisonAdapter(OnClick onClick) { this.onClick = onClick; }

    public void submit(List<Livraison> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livraison, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Livraison l = items.get(position);
        h.tvOrder.setText("Cmd #" + l.nocde);
        h.tvClient.setText(l.clientNom == null ? "-" : l.clientNom);

        String meta = (l.clientVille == null ? "" : l.clientVille)
                + "  ·  " + (l.clientTel == null ? "" : l.clientTel)
                + (l.livreurNom != null ? "  ·  " + l.livreurNom : "");
        h.tvMeta.setText(meta);

        h.tvEtat.setText(l.etatliv == null ? "—" : l.etatliv);
        h.tvEtat.setBackgroundColor(colorFor(l.etatliv));

        h.itemView.setOnClickListener(v -> { if (onClick != null) onClick.onClick(l); });
    }

    @Override public int getItemCount() { return items.size(); }

    private int colorFor(String etat) {
        if (etat == null) return Color.GRAY;
        switch (etat) {
            case "LIVREE":     return 0xFF1F8B24;
            case "EN_COURS":   return 0xFF0B5FFF;
            case "NON_LIVREE": return 0xFFD7263D;
            default:           return 0xFF8C8C8C;
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvOrder, tvClient, tvMeta, tvEtat;
        VH(View v) {
            super(v);
            tvOrder  = v.findViewById(R.id.tvOrder);
            tvClient = v.findViewById(R.id.tvClient);
            tvMeta   = v.findViewById(R.id.tvMeta);
            tvEtat   = v.findViewById(R.id.tvEtat);
        }
    }
}
