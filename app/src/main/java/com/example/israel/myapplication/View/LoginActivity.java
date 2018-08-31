package com.example.israel.myapplication.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.israel.myapplication.Presenter.Presenter;
import com.example.israel.myapplication.Presenter.PresenterLoginImpl;
import com.example.israel.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements Presenter.Login {
    private FirebaseAuth auth;
    private AnimationDrawable animationDrawable;
    private LinearLayout linearLayout;
    private EditText emailET;
    private TextInputLayout passwordTIL;
    private Button loginB;
    private PresenterLoginImpl presenter;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linearLayout = findViewById(R.id.linearLogin);
        emailET = findViewById(R.id.input_email);
        passwordTIL = findViewById(R.id.input_password_layout);
        loginB = findViewById(R.id.btn_login);
        initComponents();
    }

    private void initComponents() {
        auth = FirebaseAuth.getInstance();
        presenter = new PresenterLoginImpl(this, this, auth);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        animationStart();
    }

    private void animationStart() {
        animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(1000);
    }

    private void login() {
        String email = emailET.getText().toString();
        String password = passwordTIL.getEditText().getText().toString();
        presenter.signIn(email, password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        presenter.completeAuth(user);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }

    @Override
    public void disableInputs() {
        passwordTIL.getEditText().setEnabled(false);
        emailET.setEnabled(false);
        loginB.setEnabled(false);
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getBaseContext().getResources().getString(R.string.wait));
        progressDialog.show();
    }

    @Override
    public void enableInputs() {
        passwordTIL.getEditText().setEnabled(true);
        emailET.setEnabled(true);
        loginB.setEnabled(true);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onLogin() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError(String error, int TYPE) {
        switch (TYPE) {
            case 1:
                emailET.setError(error);
                break;
            case 2:
                passwordTIL.setError(error);
                break;
            case 3:
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Sobreescritura del método para que no se pueda acceder al MainActivity sin el login antes.
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
