package com.jct.gilad.getdriver.controller;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.jct.gilad.getdriver.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView EmailEditText;
    private EditText PasswordEditText;
    private Button SignUpButton;
    private Button LogInButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
    }

    private void findViews(){
        EmailEditText=(AutoCompleteTextView)findViewById(R.id.email);
        PasswordEditText=(EditText)findViewById(R.id.password);
        SignUpButton= (Button) findViewById(R.id.sign_up_button);
        LogInButton= (Button) findViewById(R.id.Log_in_button);



    }

    @Override
    public void onClick(View v) {
        if(v==SignUpButton){
            Intent myIntent = new Intent(v.getContext(), signUpActivity.class);
            startActivityForResult(myIntent, 0);
        }

        if(v==LogInButton){

        }

    }
}
