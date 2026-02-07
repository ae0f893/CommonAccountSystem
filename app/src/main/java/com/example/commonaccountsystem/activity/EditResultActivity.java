package com.example.commonaccountsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commonaccountsystem.R;

public class EditResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_result);
        TextView editResultTextTitleView = findViewById(R.id.edit_result_title);
        TextView editResultTextView = findViewById(R.id.edit_result);
        editResultTextTitleView.setText(this.getIntent().getStringExtra("title"));
        editResultTextView.setText(this.getIntent().getStringExtra("result"));
    }

    public void onClickReturnConfirmButton(View view){
        Intent intent = new Intent(this, ConfirmWithdrawalActivity.class);
        startActivity(intent);
    }
}