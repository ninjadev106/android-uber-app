package ca.vanweb.admin.kuaiche.FCM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ca.vanweb.admin.kuaiche.app.Config;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by mobile on 2017-05-25.
 */

public class MyFirebaseInstanceIdService  extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";
    private static String refreshedToken="";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token){

        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        //add code here

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF,
                0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
    public String getToken()
    {
        return refreshedToken;
    }
}
