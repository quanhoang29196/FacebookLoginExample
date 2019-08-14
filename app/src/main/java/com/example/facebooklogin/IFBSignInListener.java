package com.example.facebooklogin;

import org.json.JSONObject;

public interface IFBSignInListener {

    void showProgressDialog();

    void hideProgressDialog();

    void sendToServer(JSONObject object);

    void saveSharedPreference(JSONObject object);

    void startActivity();
}
