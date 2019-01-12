package com.jct.gilad.getdriver.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jct.gilad.getdriver.R;
import com.jct.gilad.getdriver.model.backend.BackendFactorySingleton;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView EmailEditText;
    private EditText PasswordEditText;
    private Button SignUpButton;
    private Button LogInButton;
    Context co;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        EmailEditText = (AutoCompleteTextView) findViewById(R.id.email);
        PasswordEditText = (EditText) findViewById(R.id.password);
        SignUpButton = (Button) findViewById(R.id.sign_up_button);
        LogInButton = (Button) findViewById(R.id.Log_in_button);
        SignUpButton.setOnClickListener(this);
        LogInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == SignUpButton) {
            startActivity(new Intent(LoginActivity.this,signUpActivity.class));
        }

        if (v == LogInButton) {

            co = v.getContext();
            logIn();

        }

    }

    private void logIn() {
        String email = EmailEditText.getText().toString().trim();
        String password = PasswordEditText.getText().toString().trim();
        ArrayList<String> emails = BackendFactorySingleton.getBackend(this).getDriversEmails();
        if (BackendFactorySingleton.getBackend(this).chackPassword(password, email)) {
            Toast.makeText(getApplicationContext(), R.string.err_msg_det, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent myIntent = new Intent(co, MainActivity.class);
        myIntent.putExtra("driverEmail", email);
        startActivityForResult(myIntent, 0);


    }
}
