package ca.vanweb.admin.kuaiche;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import ca.vanweb.admin.kuaiche.FCM.MyFirebaseInstanceIdService;


public class MainActivity extends AppCompatActivity {

    Button sendBtn;
    TextView statusText;
    EditText username;
    TextView error_msg;
    EditText password;


    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

//    public MyFirebaseInstanceIdService myFirebaseInstanceIdService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();

        Intent intent = new Intent(getBaseContext(), MyService.class);
        stopService(intent);
        startService(intent);


        error_msg = (TextView) findViewById(R.id.error_msg);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mHandler = new Handler();

        sendBtn = (Button) findViewById(R.id.btnSendSMS);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //repeatTask();
                //showMap();

                //getToken();
                login();
            }
        });

    }

    private void repeatTask() {
        mStatusChecker.run();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                //checkMessages(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    private void login() {

        String strUsername, strPassword;

        strUsername = "7786822682";//username.getText().toString().trim();
        strPassword = "google08";//password.getText().toString().trim();

        if ("".equals(strUsername)) {
            error_msg.setText("请填写登录名");
            return;
        }
        if ("".equals(strPassword)) {
            error_msg.setText("请填写密码");
            return;
        }

        String http = "https://www.kuaiche.ca/app/login.php?username=" + strUsername + "&password=" + strPassword;

        HttpResponse<JsonNode> response;
        try {
            response = Unirest.get(http)
                    .header("accept", "application/json")
                    //.field("username", strUsername)
                    //.field("password", strPassword)
                    .asJson();


            JSONObject result = response.getBody().getObject();

            error_msg.setText("正在登陆。。。");


            String error = result.getString("Error");

            if (!"".equals(error)) {

                error_msg.setText(error);

                Log.d("login", "failed");

            } else {

                Log.d("login", "success");

                //ok
                error_msg.setText("登陆成功");

                Global.driverId = result.getInt("userId");

                //remember login

                //redirect to map
                LoginToMain();


            }


        } catch (UnirestException e) {
            Log.e("ERROR", e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void LoginToMain() {

        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

    private void getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d("token", "tokens="+token);

        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();

        username.setText(token);

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("location")
                        .setMessage("location")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
