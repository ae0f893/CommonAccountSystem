package com.example.commonaccountsystem.view_util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.commonaccountsystem.R;
import com.example.commonaccountsystem.entity.WithdrawalWithItemAndPayer;

import java.util.List;

public class WithdrawalAdapter extends RecyclerView.Adapter<WithdrawalAdapter.WithdrawalViewHolder>{
    private List<WithdrawalWithItemAndPayer> withdrawals;
    private RecyclerViewClickListener listener;

    public WithdrawalAdapter(List<WithdrawalWithItemAndPayer> withdrawals, RecyclerViewClickListener listener) {
        this.withdrawals = withdrawals;
        this.listener = listener;
    }

    @Override
    public WithdrawalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdrawal, parent, false);
        return new WithdrawalViewHolder(itemView, this.listener);
    }

    @Override
    public void onBindViewHolder(WithdrawalViewHolder holder, int position) {
        WithdrawalWithItemAndPayer withdrawal = this.withdrawals.get(position);
        String itemName = withdrawal.itemName;
        String payerName = withdrawal.payerName;
        int price = withdrawal.withdrawal.price;
        String liquidationDate = withdrawal.withdrawal.liquidationDate;
        String[] dateParts = liquidationDate.split("-");
        String liquidationMonthDay = dateParts[1] + "/" + dateParts[2];
        String comment = withdrawal.withdrawal.comment;

        holder.itemName.setText(itemName);
        holder.payerName.setText(payerName);
//        holder.price.setText((String) String.valueOf(price));
        holder.price.setText(String.format("%,d", price));
        holder.liquidationDate.setText(liquidationMonthDay);
        holder.comment.setText(comment);
    }

    @Override
    public int getItemCount() {
        if(this.withdrawals == null){
            return 0;
        }
        return this.withdrawals.size();
    }

    public class WithdrawalViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName;
        public TextView payerName;
        public TextView price;
        public TextView liquidationDate;
        public TextView comment;

        public WithdrawalViewHolder(View itemView, final RecyclerViewClickListener listener) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.withdrawal_item);
            this.payerName = itemView.findViewById(R.id.withdrawal_payer);
            this.price = itemView.findViewById(R.id.withdrawal_price);
            this.liquidationDate = itemView.findViewById(R.id.withdrawal_liquidation_date);
            this.comment = itemView.findViewById(R.id.withdrawal_comment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        WithdrawalWithItemAndPayer withdrawal = withdrawals.get(position);
                        listener.onItemClick(withdrawal);
                    }
                }
            });
        }
    }
}
