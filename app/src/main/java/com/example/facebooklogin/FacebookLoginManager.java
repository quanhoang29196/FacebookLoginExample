package com.example.facebooklogin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLoginManager implements FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

    private static final String mTAG = FacebookLoginManager.class.getSimpleName();
    private final Activity mActivity;
    private final CallbackManager mFacebookCallbackManager;
    private final IFBSignInListener mIFBSignInListener;
    private final LoginManager mLoginManager;

    FacebookLoginManager(Activity activity, IFBSignInListener iFBSignInListener) {
        mActivity = activity;
        mIFBSignInListener = iFBSignInListener;
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();
    }

    public void logIn() {
        if (null != mLoginManager) {
            mLoginManager.logInWithReadPermissions(mActivity, Arrays.asList("public_profile" /*picture, name, gender, code*/, "email", "user_birthday"));
            mLoginManager.registerCallback(mFacebookCallbackManager, this);
        }
    }

    public void logOut() {
        if (null != mLoginManager) {
            mLoginManager.logOut();
        }
    }

    public CallbackManager getmFacebookCallbackManager() {
        return mFacebookCallbackManager;
    }

    private void newMeGraphRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture,gender,birthday"); // to reduce the response payload for performance reasons
        request.setParameters(parameters);
        request.executeAsync();
    }

    private boolean isFacebookSuccessfullyLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return (accessToken != null && !accessToken.isExpired());
    }


    @Override
    public void onSuccess(LoginResult loginResult) {
        if (isFacebookSuccessfullyLoggedIn() && null != mIFBSignInListener) {
            mIFBSignInListener.showProgressDialog();
            newMeGraphRequest();
        }
        Log.e(mTAG, "Facebook Login Success!");
    }

    @Override
    public void onCancel() {

        if (null != mIFBSignInListener) {
            mIFBSignInListener.hideProgressDialog();
        }
        Log.e(mTAG, "Login Cancelled!");

    }

    @Override
    public void onError(FacebookException error) {
        if (error instanceof FacebookAuthorizationException) {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
        }
        Toast.makeText(mActivity, "Internet Connection Error!", Toast.LENGTH_SHORT).show();
        if (null != mIFBSignInListener) {
            mIFBSignInListener.hideProgressDialog();
        }
        Log.e(mTAG, "Error : " + error.toString());
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        if (null != mIFBSignInListener) {
            mIFBSignInListener.saveSharedPreference(object);
            mIFBSignInListener.sendToServer(object);
            mIFBSignInListener.hideProgressDialog();
            mIFBSignInListener.startActivity();
        }
    }
}
