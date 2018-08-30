package com.example.israel.myapplication.Presenter;

public interface Presenter {
    //view Chat
    interface Chat {
        void updateChatResponse(Object response);
        void deleteWait();

    }

    interface Login {
        void disableInputs();

        void showProgress();

        void enableInputs();

        void hideProgress();

        void onLogin();

        void onError(String error, int TYPE);
    }

    //model
    void doLogin(String email, String password);
}
