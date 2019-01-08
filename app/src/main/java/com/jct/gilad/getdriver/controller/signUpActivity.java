package com.jct.gilad.getdriver.controller;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.jct.gilad.getdriver.R;


public class signUpActivity extends AppCompatActivity {
    //edit text argument
    private EditText FirstNameEditText;
    private EditText LastNameEditText;
    private EditText IDEditText;
    private EditText PhoneEditText;
    private EditText CreditCardEditText;
    private EditText EmailEditText;
    private EditText PasswordEditText;
    private Button SignUpButton;

    //TextInputLayout  argument
    private TextInputLayout firstName_InputLayout;
    private TextInputLayout lastName_InputLayout;
    private TextInputLayout ID_InputLayout;
    private TextInputLayout Phone_InputLayout;
    private TextInputLayout CreditCard_InputLayout;
    private TextInputLayout Email_InputLayout;
    private TextInputLayout Password_InputLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
    }

    private void findViews() {
        //edit text
        FirstNameEditText=(EditText) findViewById(R.id.firstName);
        LastNameEditText=(EditText) findViewById(R.id.lastName);
        IDEditText=(EditText) findViewById(R.id.idEditText);
        PhoneEditText=(EditText) findViewById(R.id.PhoneEditText);
        CreditCardEditText=(EditText) findViewById(R.id.credit_card);
        EmailEditText=(EditText) findViewById(R.id.email);
        PasswordEditText=(EditText)findViewById(R.id.password);
        SignUpButton= (Button) findViewById(R.id.email_sign_up_button);
        //input layout
        firstName_InputLayout = (TextInputLayout) findViewById(R.id.firstName_InputLayout);
        lastName_InputLayout = (TextInputLayout) findViewById(R.id.lastName_InputLayout);;
        ID_InputLayout = (TextInputLayout) findViewById(R.id.ID_InputLayout);;
        Phone_InputLayout = (TextInputLayout) findViewById(R.id.Phone_InputLayout);;
        CreditCard_InputLayout = (TextInputLayout) findViewById(R.id.CreditCard_InputLayout);;
        Email_InputLayout = (TextInputLayout) findViewById(R.id.Email_InputLayout);;
        Password_InputLayout = (TextInputLayout) findViewById(R.id.Password_InputLayout);;

        FirstNameEditText.addTextChangedListener(new MyTextWatcher(FirstNameEditText));
        LastNameEditText.addTextChangedListener(new MyTextWatcher(LastNameEditText));
        IDEditText.addTextChangedListener(new MyTextWatcher(IDEditText));
        PhoneEditText.addTextChangedListener(new MyTextWatcher(PhoneEditText));
        PasswordEditText.addTextChangedListener(new MyTextWatcher(PasswordEditText));
        EmailEditText.addTextChangedListener(new MyTextWatcher(EmailEditText));

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.firstName:
                    validateFirstName();
                    break;
                case R.id.lastName:
                    validateLastName();
                    break;
                case R.id.idEditText:
                    validateId();
                    break;
                case R.id.PhoneEditText:
                    validatePhone();
                    break;
                    case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
}

    private boolean validatePassword() {
        String password = PasswordEditText.getText().toString().trim();

        if (password.length()<4) {
            Password_InputLayout.setError(getString(R.string.err_msg_pass));
            requestFocus(PasswordEditText);
            return false;
        } else {
            Password_InputLayout.setErrorEnabled(false);
        }

        return true;


    }

    private boolean validateEmail() {
        String email = EmailEditText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            Email_InputLayout.setError(getString(R.string.err_msg_email));
            requestFocus(EmailEditText);
            return false;
        } else {
            Email_InputLayout.setErrorEnabled(false);
        }

        return true;

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePhone() {
        String iD = PhoneEditText.getText().toString().trim();

        if (iD.length()<9) {
            Phone_InputLayout.setError(getString(R.string.err_msg_FirstName));
            requestFocus(PhoneEditText);
            return false;
        } else {
            Phone_InputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateId() {
        String iD = IDEditText.getText().toString().trim();

        if (iD.length()<9) {
            ID_InputLayout.setError(getString(R.string.err_msg_FirstName));
            requestFocus(IDEditText);
            return false;
        } else {
            ID_InputLayout.setErrorEnabled(false);
        }

        return true;

    }

    private boolean validateFirstName() {
        String name = FirstNameEditText.getText().toString().trim();

        if (name.length()<2) {
            firstName_InputLayout.setError(getString(R.string.err_msg_FirstName));
            requestFocus(FirstNameEditText);
            return false;
        } else {
            firstName_InputLayout.setErrorEnabled(false);
        }

        return true;

    }
    private boolean validateLastName() {
        String name = LastNameEditText.getText().toString().trim();

        if (name.length()<2) {
            lastName_InputLayout.setError(getString(R.string.err_msg_FirstName));
            requestFocus(LastNameEditText);
            return false;
        } else {
            lastName_InputLayout.setErrorEnabled(false);
        }

        return true;

    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    }
