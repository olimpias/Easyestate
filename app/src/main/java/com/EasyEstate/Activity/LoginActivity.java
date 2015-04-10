package com.EasyEstate.Activity;

import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
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

public class LoginActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private boolean mIntentInProgress;
    private SignInButton signInButton;
    private static final int PROFILE_PIC_SIZE = 300;
    private GoogleApiClient googleApiClient;
    private ConnectionResult connectionResult;
    private static final int RC_SIGN_IN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = (SignInButton)findViewById(R.id.btn_sign_in);
        signInButton.setOnClickListener(signInButtonListener);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }
    private View.OnClickListener signInButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!googleApiClient.isConnecting()){
                    resolveSignInError();
            }

        }
    };
    private void resolveSignInError() {
        if (connectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                googleApiClient.connect();
            }
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        StoreUserProfile();
        //Database Operations...
    }
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();
            return;
        }
        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            this.connectionResult = connectionResult;


        }
    }
    /*
    Get user profile information from google account.
     */
    private void StoreUserProfile(){
        User user;
        if(Plus.PeopleApi.getCurrentPerson(googleApiClient)!=null){
            Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
            user  = new User(Plus.AccountApi.getAccountName(googleApiClient));
            user.setName(person.getDisplayName());
            String photoURL=person.getImage().getUrl();
            photoURL = photoURL.substring(0,photoURL.length()-2)+PROFILE_PIC_SIZE;
            user.setImageURL(photoURL);
            new LoginProcess().execute(user);
        }
    }
    /*
    Make login process at background.On front ProgressLoading dialog will be activated.
     */
    private class LoginProcess extends AsyncTask<User,Void,Void>{
        ProgressLoading progressLoading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading = new ProgressLoading(LoginActivity.this,"Logging in Please Wait");
            progressLoading.show();

        }

        @Override
        protected Void doInBackground(User... params) {
            try {
                MainActivity.connection.LoginUser(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressLoading != null)progressLoading.dismiss();
            finish();
        }
    }
}
