package com.jct.gilad.getdriver.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jct.gilad.getdriver.R;
import com.jct.gilad.getdriver.model.backend.BackendFactorySingleton;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView EmailEditText;
    private EditText PasswordEditText;
    private Button SignUpButton;
    private Button LogInButton;
    Context co;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        EmailEditText = (AutoCompleteTextView) findViewById(R.id.EmailEditText);
        PasswordEditText = (EditText) findViewById(R.id.PasswordEditText);
        SignUpButton = (Button) findViewById(R.id.sign_up_button);
        LogInButton = (Button) findViewById(R.id.Log_in_button);
        SignUpButton.setOnClickListener(this);
        LogInButton.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
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
//        String email = EmailEditText.getText().toString().trim();
//        String password = PasswordEditText.getText().toString();
//        List<String> emails = BackendFactorySingleton.getBackend().getDriversEmails();
//        if (!BackendFactorySingleton.getBackend().checkPassword(password, email)) {
//            Toast toast = Toast.makeText(getApplicationContext(), R.string.err_msg_det, Toast.LENGTH_SHORT);
//            toast.setGravity(LogInButton.getGravity()|Gravity.CENTER_HORIZONTAL, 0, 0);
//            toast.show();
//        }
//        else {
//            Intent myIntent = new Intent(co, MainActivity.class);
//            myIntent.putExtra("driverEmail", email).putExtra("driverPassword", password);
//            startActivityForResult(myIntent, 0);
//        }

        final String email = EmailEditText.getText().toString().trim();
        final String Ipassword = PasswordEditText.getText().toString();
        try {
            auth.signInWithEmailAndPassword(email, Ipassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "the email or the password is not correct!", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = getIntent();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("driverEmail",email).putExtra("driverPassword",Ipassword));
                                finish();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void loginUser() {
        final String email = EmailEditText.getText().toString().trim();
        final String Ipassword = PasswordEditText.getText().toString();
        try {
            auth.signInWithEmailAndPassword(email, Ipassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "the email or the password is not correct!", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = getIntent();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("driverEmail",email).putExtra("driverPassword",Ipassword));
                                finish();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
