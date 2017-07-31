package ca.vanweb.admin.kuaiche;


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by deepshikha on 24/11/16.
 */

public class MyService extends IntentService implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 5000;
    //    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;

    SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public MyService() {
        super("");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor = prefs.edit();

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//
////        intent = new Intent(str_receiver);
////        fn_getlocation();
//    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void fn_getlocation() {

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

            }


            if (isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }
            }


        }

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location) {

//        Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
//        intent.putExtra("latutide",location.getLatitude()+"");
//        intent.putExtra("longitude",location.getLongitude()+"");
//        sendBroadcast(intent);

        send_data(location.getLongitude() + "", location.getLatitude() + "");
    }


    void send_data(final String log, final String Lat) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String http = "https://www.kuaiche.ca/app/save-gps.php?longitude=" + log + "&latitude=" + Lat + "&driverId=" + 2;
                HttpResponse<String> response;
                try {
                    response = Unirest.get(http)
                            .header("accept", "application/json")
                            //.field("username", strUsername)
                            //.field("password", strPassword)
                            .asString();


                    String result = response.getBody();

                    return result;

                } catch (UnirestException e) {
                    Log.e("ERRORUNIRE", e.getMessage());
                }
                return "failed";
            }

            @Override
            protected void onPostExecute(String state) {

                if (state.equals("OK")) {
                    Log.e("Finish", "Success")
                    ;
                } else {
                    Log.e("Finish", "Faild");

                }

                super.onPostExecute(state);
            }
        }.

                execute();


        editor.putString("longitude", log);
        editor.putString("latitude", Lat);
        editor.commit();
//        Global.driverId = result.getInt("userId");

//            if(!"".equals(error)) {
//
//
////                Log.e("login", "failed");
//
//            } else {
//
//                Log.d("login", "success");
//
//                //ok
//                error_msg.setText("登陆成功");
//
//                Global.driverId = result.getInt("userId");
//
//                //remember login
//
//                //redirect to map
//                LoginToMain();
//
//            }


    }
}