package com.example.israel.myapplication.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.israel.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PresenterLoginImpl {
    Context context;
    Presenter.Login delegate;
    FirebaseAuth auth;

    public PresenterLoginImpl(Context context, Presenter.Login delegate, FirebaseAuth auth) {
        this.context = context;
        this.delegate = delegate;
        this.auth = auth;
    }

    public void signIn(String email, String password) {
        if (!validate(email, password)) {
            completeAuth(null);
            return;
        } else {
            if (delegate != null) {
                delegate.disableInputs();
                delegate.showProgress();
            }
            authentication(email, password);
        }
    }

    private boolean validate(String email, String password) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            delegate.onError(context.getResources().getString(R.string.emailError), 1);
            return false;
        }
        if (password.isEmpty()) {
            delegate.onError(context.getResources().getString(R.string.passwordError), 2);
            return false;
        }
        return true;
    }

    public void completeAuth(FirebaseUser user) {
        if (user != null) {
            delegate.onLogin();
        } else {
            delegate.enableInputs();
        }
    }

    private void authentication(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            delegate.hideProgress();
                                            FirebaseUser user = auth.getCurrentUser();
                                            completeAuth(user);
                                        }
                                    }, 2000);

                        } else {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            delegate.onError(context.getResources().getString(R.string.errorLogin), 3);
                                            delegate.hideProgress();
                                            completeAuth(null);
                                        }
                                    }, 2000);
                        }

                    }
                });
    }

}
