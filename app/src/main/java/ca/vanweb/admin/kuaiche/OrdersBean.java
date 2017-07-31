package ca.vanweb.admin.kuaiche;

/**
 * Created by admin on 6/6/2017.
 */


import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class OrdersBean {

    public String orderId = "";
    public String fromAddress = "";
    public String toAddress = "";

    public OrdersBean() {



    }

    public void loadFromJSONObject(JSONObject obj){

        try{

            orderId = obj.getString("orderId");
            fromAddress = obj.getString("fromAddress");
            toAddress = obj.getString("toAddress");

        } catch(JSONException e){
            Log.d("ERROR", e.getMessage());
        }

    }



}

