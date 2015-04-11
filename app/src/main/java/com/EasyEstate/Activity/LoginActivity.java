package com.EasyEstate.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.EasyEstate.Model.User;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.ProgressLoading;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.PersonBuffer;

import org.json.JSONException;

import java.io.IOException;
// SHA1 KEY PATH keytool -list -v -keystore /Users/canturker/EasyEstate.jks -alias EasyEstate
public class LoginActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,ResultCallback<People.LoadPeopleResult> {
    private boolean mIntentInProgress;
    private SignInButton signInButton;
    private PendingIntent mSignInIntent;
    private int mSignInError;
    private int mSignInProgress;
    private static final int PROFILE_PIC_SIZE = 300;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 0;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final String TAG = "LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = (SignInButton)findViewById(R.id.btn_sign_in);
        signInButton.setOnClickListener(signInClickListener);
        mGoogleApiClient = buildGoogleApiClient();
    }
    private View.OnClickListener signInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mSignInProgress = STATE_SIGN_IN;
           if(!mGoogleApiClient.isConnecting()){
               resolveSignInError();
               mGoogleApiClient.connect();
           }
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        Plus.PeopleApi.loadVisible(mGoogleApiClient,"").setResultCallback(this);
        getUserInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    private void resolveSignInError() {
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mGoogleApiClient.connect();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    private void getUserInformation(){
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");
        // Retrieve some profile information to personalize our app for the user.
        User user = new User(Plus.AccountApi.getAccountName(mGoogleApiClient));
       /*user.setName(currentUser.getDisplayName());
        user.setImageURL(currentUser.getImage().getUrl().substring(0,currentUser.getImage().getUrl().length()-2)+PROFILE_PIC_SIZE);*/
        new LoginProcess().execute(user);
    }
    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_PROFILE)
                .addScope(Plus.SCOPE_PLUS_LOGIN);
        return builder.build();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // An API requested for GoogleApiClient is not available. The device's current
            // configuration might not be supported with the requested API or a required component
            // may not be installed, such as the Android Wear application. You may need to use a
            // second GoogleApiClient to manage the application's optional APIs.
            Log.w(TAG, "API Unavailable.");
        } else if (mSignInProgress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in button
                // so we should continue processing errors until the user is signed in
                // or they click cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG, "Error requesting people data: " + peopleData.getStatus());
        }
    }

    /*
    Make login process at background.On front ProgressLoading dialog will be activated.
     */
    private class LoginProcess extends AsyncTask<User,Void,Boolean>{
        ProgressLoading progressLoading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading = new ProgressLoading(LoginActivity.this,"Logging in Please Wait");
            progressLoading.show();

        }

        @Override
        protected Boolean doInBackground(User... params) {
            try {
                boolean result = MainActivity.connection.LoginUser(params[0]);
                SaveUser(params[0].getEmail());
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        //According to boolean value edit profile page might be open on main activity...
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progressLoading != null)progressLoading.dismiss();
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            Intent intent = new Intent();
            intent.putExtra(MainActivity.IS_IT_FIRST_LOGIN,aBoolean.booleanValue());
            setResult(RESULT_OK,intent);
            finish();
        }
    }
    private void SaveUser(String email){
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.SHARED_PREFERENCE_REF, MODE_PRIVATE).edit();
        editor.putString(MainActivity.EMAIL,email);
        editor.apply();
    }
}
