package com.EasyEstate.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Model.User;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.ProgressLoading;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import org.json.JSONException;

import java.io.IOException;
// SHA1 KEY PATH keytool -list -v -keystore /Users/canturker/EasyEstate.jks -alias EasyEstate
public class LoginActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private boolean mIntentInProgress;
    private SignInButton signInButton;
    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    private static final int PROFILE_PIC_SIZE = 300;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 0;

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

            mSignInClicked = true;
           if(!mGoogleApiClient.isConnecting()){
               resolveSignInError();
               mGoogleApiClient.connect();
           }
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
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
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void getProfileInformation() {

            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;
                User user = new User(email);
                user.setImageURL(personPhotoUrl);
                user.setName(personName);
                new LoginProcess().execute(user);
            }

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
        /*Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
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
        }*/
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
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
                boolean result =  DatabaseConnection.getConnection().LoginUser(params[0]);
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
