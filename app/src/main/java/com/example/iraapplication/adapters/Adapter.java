package com.example.iraapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iraapplication.R;
import com.example.iraapplication.domain.HistoryItem;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<HistoryItem> historyItemList;

    public Adapter(List<HistoryItem> historyItemList) {
        this.historyItemList = historyItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_lines, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.time.setText(historyItemList.get(position).getTime());
        holder.from.setText(historyItemList.get(position).getFrom());
        holder.to.setText(historyItemList.get(position).getTo());
    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView time;
        final TextView from;
        final TextView to;

        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
        }
    }
}
