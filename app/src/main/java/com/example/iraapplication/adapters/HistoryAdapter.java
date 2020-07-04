package com.example.iraapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iraapplication.R;
import com.example.iraapplication.domain.HistoryItem;

import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryItem> items;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<HistoryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView time;
        private final TextView descr;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_lines, parent, false));
            time = itemView.findViewById(R.id.time);
            descr = itemView.findViewById(R.id.descr);
        }

        void bindData(HistoryItem data) {
            time.setText(data.getTime());
            descr.setText(String.format(Locale.getDefault(), "%s = %s", data.getFrom(), data.getTo()));
        }
    }
}
