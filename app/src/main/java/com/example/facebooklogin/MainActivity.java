package com.example.facebooklogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String mTAG = MainActivity.class.getSimpleName();
    private FacebookLoginManager mFacebookLoginManager;

    private static void printUserDetails(JSONObject object) {
        try {
            if (null != object) {
                Log.e(mTAG, "ID : " + Long.toString(object.getLong("id")));

                if (null != object.getJSONObject("picture") && null != object.getJSONObject("picture").getJSONObject("data"))
                    Log.e(mTAG, "PictureUrl : " + object.getJSONObject("picture").getJSONObject("data").getString("url"));

                Log.e(mTAG, "Birthday : " + object.getString("birthday"));
                Log.e(mTAG, "Email : " + object.getString("email"));
                Log.e(mTAG, "Gender : " + object.getString("gender"));
                Log.e(mTAG, "Name : " + object.getString("name"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(mTAG, "Error " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.facebookLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mFacebookLoginManager) {
                    mFacebookLoginManager.logIn();
                }
            }
        });


        mFacebookLoginManager = new FacebookLoginManager(this, new IFBSignInListener() {
            @Override
            public void showProgressDialog() {
                //Show Custom Progress Bar
            }

            @Override
            public void hideProgressDialog() {
                //Hide Custom Progress Bar
            }

            @Override
            public void sendToServer(JSONObject object) {
                //Send JSON Response to your Server
                printUserDetails(object); // For Testing Purpose Only
            }

            @Override
            public void saveSharedPreference(JSONObject object) {
                //Save JSON Response to Shared Preference
            }

            @Override
            public void startActivity() {
                //Start new Activity
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((mFacebookLoginManager != null) && (null != mFacebookLoginManager.getmFacebookCallbackManager())) {
            mFacebookLoginManager.getmFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }
}
