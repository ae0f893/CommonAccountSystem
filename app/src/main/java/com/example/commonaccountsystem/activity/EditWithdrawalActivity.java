package com.example.commonaccountsystem.activity;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.commonaccountsystem.R;
import com.example.commonaccountsystem.entity.Withdrawal;
import com.example.commonaccountsystem.entity.WithdrawalWithItemAndPayer;
import com.example.commonaccountsystem.repository.WithdrawalRepository;

public class EditWithdrawalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_withdrawal);
        Intent intent = getIntent();
        WithdrawalWithItemAndPayer withdrawal = (WithdrawalWithItemAndPayer) intent.getSerializableExtra("withdrawal");

        TextView itemName = findViewById(R.id.withdrawal_item);
        TextView payerName = findViewById(R.id.withdrawal_payer);
        TextView price = findViewById(R.id.withdrawal_price);
        TextView liquidationDate = findViewById(R.id.withdrawal_liquidation_date);
        TextView comment = findViewById(R.id.withdrawal_comment);

        itemName.setText(withdrawal.itemName);
        payerName.setText(withdrawal.payerName);
        price.setText(String.format("%,d", withdrawal.withdrawal.price));
        String[] dateParts = withdrawal.withdrawal.liquidationDate.split("-");
        String liquidationMonthDay = dateParts[1] + "/" + dateParts[2];
        liquidationDate.setText(liquidationMonthDay);
        comment.setText(withdrawal.withdrawal.comment);

        // Google Pixel 9a対応
        if(withdrawal.withdrawal.comment == ""){
            float density = getResources().getDisplayMetrics().density;
            int paddingTopPx = Math.round(1.5f * density);
            int paddingBottomPx = Math.round(0.5f * density);
            comment.setPadding(0, paddingTopPx, 0, paddingBottomPx);
        }
    }

    public void onClickDeleteButton(View view){
        Intent intent = getIntent();
        WithdrawalWithItemAndPayer withdrawal = (WithdrawalWithItemAndPayer) intent.getSerializableExtra("withdrawal");

        Intent nextIntent = new Intent(this, DeleteResultActivity.class);
        if(delete(withdrawal.withdrawal)){
            nextIntent.putExtra("result", getString(R.string.delete_success));
        }else{
            nextIntent.putExtra("result", getString(R.string.delete_failure));
        }
        startActivity(nextIntent);
    }

    private boolean delete(Withdrawal withdrawal) {
        WithdrawalRepository wRep = new WithdrawalRepository(this);
        return wRep.delete(withdrawal);
    }
}