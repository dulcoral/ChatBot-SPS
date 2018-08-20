package com.example.israel.myapplication;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth _auth;
    private EditText _emailET;
    private EditText _passwordET;
    private Button _loginB;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _emailET = findViewById(R.id.input_email);
        _passwordET = findViewById(R.id.input_password);
        _loginB = findViewById(R.id.btn_login);
        _auth = FirebaseAuth.getInstance();

        _loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        completeAuth(user);
    }


    public void login() {
        String email = _emailET.getText().toString();
        String password = _passwordET.getText().toString();

        if (!validate(email, password)) {
            completeAuth(null);
            return;
        }

        _loginB.setEnabled(false);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Espere");
        progressDialog.show();
        authentication(email, password);
    }


    public boolean validate(String email, String password) {
        boolean valid = true;
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailET.setError("Email Incorrecto");
            valid = false;
        } else {
            _emailET.setError(null);
        }
        if (password.isEmpty()) {
            _passwordET.setError("El password debe existir");
            valid = false;
        } else {
            _passwordET.setError(null);
        }
        return valid;
    }


    private void authentication(String email, String password) {
        _auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            progressDialog.dismiss();
                                            FirebaseUser user = _auth.getCurrentUser();
                                            completeAuth(user);
                                        }
                                    }, 2000);

                        } else {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),("Datos incorrectos"), Toast.LENGTH_LONG).show();
                                            completeAuth(null);
                                        }
                                    }, 2000);
                        }

                    }
                });

    }

    private void completeAuth(FirebaseUser user) {
        if (user != null) {
            finish();
        } else {
            _loginB.setEnabled(true);
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
