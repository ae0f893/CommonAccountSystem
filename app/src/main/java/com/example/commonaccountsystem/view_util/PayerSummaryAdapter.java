package com.example.commonaccountsystem.view_util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.commonaccountsystem.R;
import com.example.commonaccountsystem.model.PayerSummary;

import java.util.List;

public class PayerSummaryAdapter extends RecyclerView.Adapter<PayerSummaryViewHolder>{
    private List<PayerSummary> payerSummaries;

    public PayerSummaryAdapter(List<PayerSummary> payerSummaries) {
        this.payerSummaries = payerSummaries;
    }

    @Override
    public PayerSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payer_summary, parent, false);
        return new PayerSummaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PayerSummaryViewHolder holder, int position) {
        PayerSummary payerSummary = this.payerSummaries.get(position);
        String name = payerSummary.getPayerName();
        int amount = payerSummary.getAmount();
        int difference = payerSummary.getDifference();

        holder.payerName.setText(name);
        holder.amount.setText(String.format("%,d", amount));
        holder.difference.setText(String.format("%,d", difference));
    }

    @Override
    public int getItemCount() {
        if(this.payerSummaries == null){
            return 0;
        }
        return this.payerSummaries.size();
    }
}
