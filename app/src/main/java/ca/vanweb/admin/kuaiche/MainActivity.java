package ca.vanweb.admin.kuaiche;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
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



import java.util.ArrayList;

import ca.vanweb.admin.kuaiche.FCM.MyFirebaseInstanceIdService;


public class MainActivity extends AppCompatActivity {

    Button sendBtn;
    TextView statusText;
    EditText username;
    TextView error_msg;
    EditText password;


    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    public MyFirebaseInstanceIdService myFirebaseInstanceIdService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        error_msg = (TextView)findViewById(R.id.error_msg);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);

        mHandler = new Handler();

        sendBtn = (Button) findViewById(R.id.btnSendSMS);

        myFirebaseInstanceIdService = new MyFirebaseInstanceIdService();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                myFirebaseInstanceIdService.onTokenRefresh();

                //repeatTask();
                //showMap();
                login();
                //getToken();
            }
        });

    }
    private void repeatTask(){
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

    private void login(){

        String strUsername, strPassword;

        strUsername = username.getText().toString().trim();
        strPassword = password.getText().toString().trim();

        if ("".equals(strUsername)) {
            error_msg.setText("请填写登录名");
            return;
        }
        if ("".equals(strPassword)) {
            error_msg.setText("请填写密码");
            return;
        }

        String http = "https://www.kuaiche.ca/app/login.php";

        HttpResponse<JsonNode> response;
        try {
            response = Unirest.post(http)
                    .header("accept", "application/json")
                    .field("username", strUsername)
                    .field("password", strPassword)
                    .field("token",myFirebaseInstanceIdService.getToken())
                    .asJson();


            JSONObject result = response.getBody().getObject();

            error_msg.setText("正在登陆。。。");



            String error = result.getString("Error");

            if(!"".equals(error)) {

                error_msg.setText("登陆失败");

            } else {
                //ok
                error_msg.setText("登陆成功");

                //remember login

                //redirect to map
                LoginToMain();

            }


        } catch (UnirestException e){
            Log.e("ERROR", e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    private void LoginToMain(){
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(i);
    }



    private void getToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
        Log.d("token", token);
        username.setText(token);

    }
}
