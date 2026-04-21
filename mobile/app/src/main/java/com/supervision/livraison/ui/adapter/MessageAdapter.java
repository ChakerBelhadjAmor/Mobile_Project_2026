package com.supervision.livraison.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.supervision.livraison.R;
import com.supervision.livraison.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared list adapter for inbox-style message views on both sides
 * (controller and driver).
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.VH> {

    private final List<Message> items = new ArrayList<>();

    public void submit(List<Message> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Message m = items.get(position);
        String prefix = "EMERGENCY".equals(m.type) ? "🚨 " : "✉ ";
        h.tvHeader.setText(prefix + (m.senderName == null ? "" : m.senderName)
                + (m.nocde != null ? "  ·  Cmd #" + m.nocde : ""));
        if ("EMERGENCY".equals(m.type)) {
            h.tvHeader.setTextColor(Color.parseColor("#D7263D"));
        } else {
            h.tvHeader.setTextColor(Color.parseColor("#0B5FFF"));
        }
        h.tvBody.setText(m.body == null ? "" : m.body);

        StringBuilder meta = new StringBuilder();
        if (m.createdAt != null) meta.append(m.createdAt);
        if (m.clientTel != null) meta.append("  ·  Tel: ").append(m.clientTel);
        h.tvMeta.setText(meta.toString());
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvHeader, tvBody, tvMeta;
        VH(View v) {
            super(v);
            tvHeader = v.findViewById(R.id.tvHeader);
            tvBody   = v.findViewById(R.id.tvBody);
            tvMeta   = v.findViewById(R.id.tvMeta);
        }
    }
}
