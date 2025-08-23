package com.example.commonaccountsystem.view_util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.commonaccountsystem.R;
import com.example.commonaccountsystem.model.ItemSummary;

import java.util.List;

public class ItemSummaryAdapter extends RecyclerView.Adapter<ItemSummaryViewHolder>{
    private List<ItemSummary> itemSummaries;

    public ItemSummaryAdapter(List<ItemSummary> itemSummaries) {
        this.itemSummaries = itemSummaries;
    }

    @Override
    public ItemSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent, false);
        return new ItemSummaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemSummaryViewHolder holder, int position) {
        ItemSummary itemSummary = this.itemSummaries.get(position);
        String name = itemSummary.getItemName();
        int amount = itemSummary.getAmount();
        holder.itemName.setText(name);
        holder.amount.setText(String.format("%,d", amount));
    }

    @Override
    public int getItemCount() {
        if(this.itemSummaries == null){
            return 0;
        }
        return this.itemSummaries.size();
    }
}
