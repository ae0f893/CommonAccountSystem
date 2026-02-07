package com.example.commonaccountsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.commonaccountsystem.R;
import com.example.commonaccountsystem.entity.Withdrawal;
import com.example.commonaccountsystem.entity.WithdrawalWithItemAndPayer;
import com.example.commonaccountsystem.repository.ItemRepository;
import com.example.commonaccountsystem.repository.PayerRepository;
import com.example.commonaccountsystem.repository.WithdrawalRepository;
import com.example.commonaccountsystem.validation.EmptyValidation;
import com.example.commonaccountsystem.validation.Validation;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        liquidationDate.setText(withdrawal.withdrawal.liquidationDate);
        comment.setText(withdrawal.withdrawal.comment);

        // Google Pixel 9a対応
        if (withdrawal.withdrawal.comment.equals("")) {
            float density = getResources().getDisplayMetrics().density;
            int paddingTopPx = Math.round(1.5f * density);
            int paddingBottomPx = Math.round(0.5f * density);
            comment.setPadding(0, paddingTopPx, 0, paddingBottomPx);
        }
    }

    public void onClickDeleteButton(View view) {
        Intent intent = getIntent();
        WithdrawalWithItemAndPayer withdrawal = (WithdrawalWithItemAndPayer) intent.getSerializableExtra("withdrawal");

        Intent nextIntent = new Intent(this, EditResultActivity.class);
        nextIntent.putExtra("title", getString(R.string.delete_withdrawal_title));
        if (delete(withdrawal.withdrawal)) {
            nextIntent.putExtra("result", getString(R.string.delete_success));
        } else {
            nextIntent.putExtra("result", getString(R.string.delete_failure));
        }
        startActivity(nextIntent);
    }

    private boolean delete(Withdrawal withdrawal) {
        WithdrawalRepository wRep = new WithdrawalRepository(this);
        return wRep.delete(withdrawal);
    }

    public void onClickUpdateButton(View view) {
        Intent intent = getIntent();
        WithdrawalWithItemAndPayer withdrawal = (WithdrawalWithItemAndPayer) intent.getSerializableExtra("withdrawal");

        setUpdatedWithdrawal(withdrawal.withdrawal);

        Intent nextIntent = new Intent(this, EditResultActivity.class);
        nextIntent.putExtra("title", getString(R.string.update_withdrawal_title));
        if (update(withdrawal.withdrawal)) {
            nextIntent.putExtra("result", getString(R.string.update_success));
        } else {
            nextIntent.putExtra("result", getString(R.string.update_failure));
        }
        startActivity(nextIntent);
    }

    private void setUpdatedWithdrawal(Withdrawal withdrawal) {
        TextView withdrawalItem = (TextView) findViewById(R.id.withdrawal_item);
        withdrawal.itemId = ItemRepository.getInstance(getApplicationContext()).fetchIdByName(withdrawalItem.getText().toString());
        TextView withdrawalPayer = (TextView) findViewById(R.id.withdrawal_payer);
        withdrawal.payerId = PayerRepository.getInstance(getApplicationContext()).fetchIdByName(withdrawalPayer.getText().toString());
        TextView price = (TextView) findViewById(R.id.withdrawal_price);
        withdrawal.price = Integer.parseInt(price.getText().toString().replace(",", ""));
        TextView liquidationDate = (TextView) findViewById(R.id.withdrawal_liquidation_date);
        withdrawal.liquidationDate = liquidationDate.getText().toString();
        TextView comment = (TextView) findViewById(R.id.withdrawal_comment);
        withdrawal.comment = comment.getText().toString();
    }

    private boolean update(Withdrawal withdrawal) {
        WithdrawalRepository wRep = new WithdrawalRepository(this);
        return wRep.update(withdrawal);
    }

    public void onClickItem(View view) {
        TextView itemTextView = (TextView) view;

        ItemRepository itemRepository = ItemRepository.getInstance(getApplicationContext());
        List<String> items = itemRepository.fetchNamesWithVariableCost();
        String[] itemArray = items.toArray(new String[items.size()]);
        new AlertDialog.Builder(this)
                .setTitle("項目")
                .setItems(itemArray, (dialog, which) -> {
                    String selected = itemArray[which];
                    itemTextView.setText(selected);
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }

    public void onClickPayer(View view) {
        TextView payerTextView = (TextView) view;

        PayerRepository payerRepository = PayerRepository.getInstance(getApplicationContext());
        List<String> payers = payerRepository.fetchAllNames();
        String[] payerArray = payers.toArray(new String[payers.size()]);
        new AlertDialog.Builder(this)
                .setTitle("支払い者")
                .setItems(payerArray, (dialog, which) -> {
                    String selected = payerArray[which];
                    payerTextView.setText(selected);
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }

    public void onClickPrice(View view) {
        TextView priceTextView = (TextView) view;

        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setText(priceTextView.getText().toString().replace(",", ""));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("料金入力")
                .setView(editText)
                .setPositiveButton("確定", null) // ここではリスナーを null にしておく
                .setNegativeButton("キャンセル", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation validation = new EmptyValidation();
                validation.check(editText);
                if (validation.getFlag()) {
                    int value = Integer.parseInt(editText.getText().toString());
                    priceTextView.setText(String.format("%,d", value));
                    dialog.dismiss();
                }
            }
        });
    }

    public void onClickLiquidationDate(View view) {
        TextView liquidationDateTextView = (TextView) view;
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener =
                (datePicker, year, month, dayOfMonth)
                        -> (liquidationDateTextView)
                        .setText(String.format(Locale.JAPAN, "%02d-%02d-%02d", year, month + 1, dayOfMonth));

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void onClickComment(View view) {
        TextView commentTextView = (TextView) view;
        EditText editText = new EditText(this);

        editText.setText(commentTextView.getText());

        new AlertDialog.Builder(this)
                .setTitle("コメント入力")
                .setView(editText)
                .setPositiveButton("確定", (dialog, which) -> {
                    String input = editText.getText().toString();
                    commentTextView.setText(input);
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }
}