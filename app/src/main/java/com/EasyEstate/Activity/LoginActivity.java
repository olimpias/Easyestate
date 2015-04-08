package com.EasyEstate.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import com.EasyEstate.Model.User;
import com.EasyEstate.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class LoginActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private boolean mIntentInProgress;
    private SignInButton signInButton;
    private static final int PROFILE_PIC_SIZE = 300;
    private GoogleApiClient googleApiClient;
    private ConnectionResult connectionResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = (SignInButton)findViewById(R.id.btn_sign_in);
        signInButton.setOnClickListener(signInButtonListener);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }
    private View.OnClickListener signInButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!googleApiClient.isConnecting()){

            }

        }
    };
    @Override
    public void onConnected(Bundle bundle) {
        StoreUserProfile();
        //Database Operations...
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
    private void StoreUserProfile(){
        User user;
        if(Plus.PeopleApi.getCurrentPerson(googleApiClient)!=null){
            Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
            user  = new User(Plus.AccountApi.getAccountName(googleApiClient));
            user.setName(person.getDisplayName());
            String photoURL=person.getImage().getUrl();
            photoURL = photoURL.substring(0,photoURL.length()-2)+PROFILE_PIC_SIZE;
            user.setImageURL(photoURL);
        }
    }
}
