package com.example.commonaccountsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.commonaccountsystem.R;
import com.example.commonaccountsystem.repository.WithdrawalRepository;
import com.example.commonaccountsystem.validation.Validation;
import com.example.commonaccountsystem.entity.Withdrawal;
import com.example.commonaccountsystem.validation.EmptyValidation;
import com.example.commonaccountsystem.repository.ItemRepository;
import com.example.commonaccountsystem.repository.PayerRepository;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterWithdrawalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_withdrawal);

        PayerRepository payerRepository = PayerRepository.getInstance(getApplicationContext());
        setSpinner(findViewById(R.id.payer_spinner), payerRepository.fetchAllNames());

        ItemRepository itemRepository = ItemRepository.getInstance(getApplicationContext());
        Spinner itemSpinner = (Spinner) findViewById(R.id.item_spinner);
        setSpinner(itemSpinner, itemRepository.fetchNamesWithVariableCost());
        itemSpinner.setOnItemSelectedListener(this);
    }

    private void setSpinner(Spinner spinner, List<String> items) {
        if (items != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner itemSpinner = (Spinner) parent;
        ItemRepository itemRepository = ItemRepository.getInstance(getApplicationContext());

        //項目毎にデフォルトの金額をセットする
        int cost = itemRepository.fetchCostByName(itemSpinner.getSelectedItem().toString());
        EditText price = findViewById(R.id.price_edittext);
        if (cost != 0) {
            price.setText(String.valueOf(cost));
        }

        //項目毎にデフォルトの支払い者をセットする
        int payerId = itemRepository.fetchPayerIdByName(itemSpinner.getSelectedItem().toString());
        // payerIdとSpinnerのindexが一致しない場合はコードに修正が必要
        // https://anadreline.blogspot.com/2013/07/spinner.html
        // PayerRepository payerRepository = PayerRepository.getInstance(getApplicationContext());
        // String payerName = payerRepository.fetchName(payerId);
        Spinner payerSpinner = (Spinner) findViewById(R.id.payer_spinner);
        int index = payerId - 1;
        payerSpinner.setSelection(index);

        // 項目毎にデフォルトの支払日をセットする
        LocalDate paymentDate = itemRepository.fetchPaymentDateByName(itemSpinner.getSelectedItem().toString());
        EditText liquidationDate = (EditText) findViewById(R.id.liquidation_date_edittext);
        liquidationDate.setText(paymentDate.toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // 選択が消えた時には何もしない。
    }

    public void inputLiquidationDate(View liquidationDateText) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener =
                (datePicker, year, month, dayOfMonth)
                        -> ((EditText) liquidationDateText)
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

    public void onClickRegistrationButton(View view) {
        Validation validation = new EmptyValidation();
        Spinner itemSpinner = (Spinner) findViewById(R.id.item_spinner);
        String itemName = (String) itemSpinner.getSelectedItem();
        Spinner payerSpinner = (Spinner) findViewById(R.id.payer_spinner);
        String payerName = (String) payerSpinner.getSelectedItem();
        EditText price = (EditText) findViewById(R.id.price_edittext);
        validation.check(price);
        EditText liquidationDate = (EditText) findViewById(R.id.liquidation_date_edittext);
        validation.check(liquidationDate);
        EditText comment = (EditText) findViewById(R.id.comment_edittext);
        if (!validation.getFlag()) {
            return;
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.itemId = ItemRepository.getInstance(getApplicationContext()).fetchIdByName(itemName);
        withdrawal.payerId = PayerRepository.getInstance(getApplicationContext()).fetchIdByName(payerName);
        withdrawal.price = Integer.parseInt(price.getText().toString());
        withdrawal.liquidationDate = liquidationDate.getText().toString();
        withdrawal.comment = comment.getText().toString();

        Intent intent = new Intent(this, RegisterResultActivity.class);
        if (register(withdrawal)) {
            intent.putExtra("result", getString(R.string.register_success));
        } else {
            intent.putExtra("result", getString(R.string.register_failure));
        }
        startActivity(intent);
    }

    private boolean register(Withdrawal withdrawal) {
        WithdrawalRepository wRep = new WithdrawalRepository(this);
        return wRep.register(withdrawal);
    }
}