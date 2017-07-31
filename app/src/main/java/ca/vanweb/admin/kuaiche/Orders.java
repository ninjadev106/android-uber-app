package ca.vanweb.admin.kuaiche;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.vanweb.admin.kuaiche.Global;
import java.util.ArrayList;

import ca.vanweb.admin.kuaiche.FCM.MyFirebaseInstanceIdService;


public class Orders extends Activity {

    Button sendBtn;
    TextView statusText;
    EditText username;
    TextView error_msg;
    EditText password;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    public static ArrayList<OrdersBean> orderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_orders);


        error_msg = (TextView)findViewById(R.id.error_msg);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        getOrders();

    }


    public void getOrders(){

        Log.d("log", "try to load...");

        String http = "https://www.kuaiche.ca/app/list-orders2.php?driverId=1309&user_type=Driver";

        HttpResponse<JsonNode> response;
        try {


            response = Unirest.get(http)
                    .header("accept", "application/json")
                    //.field("username", strUsername)
                    //.field("password", strPassword)
                    .asJson();

            Log.d("log", "loading...");


            JsonNode body = response.getBody();
            JSONObject result = body.getObject();
            Log.d("result", result.toString());

            JSONArray aryUser = result.getJSONArray("message");

            Log.d("log", "show result...");

            for (int i = 0; i < aryUser.length(); i++) {
                JSONObject row = aryUser.getJSONObject(i);

                OrdersBean order = new OrdersBean();

                order.loadFromJSONObject(row);
                orderList.add(order);

                Log.d("address", row.getString("fromAddress"));

            }



        } catch (UnirestException e){
            Log.e("ERROR", e.getMessage());
        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }



    }

}
