package ca.vanweb.admin.kuaiche;


import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataFetcher {

    private Uri mUri;
    private String TAG = "DataFetcher";

    public DataFetcher() {
    }


    //gets data from url in bytes
    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new IOException(connection.getResponseMessage() + ": with" + urlSpec);
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }

    }

    //transforms data to string
    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    //used to call the fetcher from activity
    public ArrayList<Order> getFolows() {
        String url = "https://www.kuaiche.ca/app/get-follows2.php?driverId=1319";
        return downloadItems(url);
    }

    public ArrayList<Order> getOrders() {
        String url = "https://www.kuaiche.ca/app/list-orders2.php?user_type=Driver&driverId=1319";
        return downloadItems(url);
    }

    public ArrayList<Order> getOrders3() {
        String url = "https://www.kuaiche.ca/app/list-orders2.php?user_type=Driver&orderType=1&driverId=1319";
        return downloadItems(url);
    }

    Order getOrderData(String orderId) {
        String url = "https://www.kuaiche.ca/app/get-order.php?orderId=" + orderId;
        return downloadData(url);
    }

    //creates JSON from string
    private ArrayList<Order> downloadItems(String url) {
        ArrayList<Order> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse Json", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }

    private Order downloadData(String url) {
        Order order = new Order();
        try {
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            order = parseOrder(jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse Json", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return order;
    }

    //creates order objects from JSON
    private void parseItems(ArrayList<Order> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray itemArray = jsonBody.getJSONArray("message");

        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject item = itemArray.getJSONObject(i);
            final Order order = new Order();
            order.setID(item.getString("orderId"));
            order.setCreateDateTime(item.getString("createDatetime"));
            order.setFromAddress(item.getString("fromAddress"));
            order.setToAddress(item.getString("toAddress"));
            order.setTotalMoney(item.getString("totalMoney"));
            order.setOrderStatusText(item.getString("orderStatusText"));
            items.add(order);

        }

    }

    private Order parseOrder(JSONObject jsonBody) throws IOException, JSONException {
        Order order = new Order();
        order.setID(jsonBody.getString("orderId"));
        order.setCreateDateTime(jsonBody.getString("createDatetime"));
        order.setFromAddress(jsonBody.getString("fromAddress"));
        order.setToAddress(jsonBody.getString("toAddress"));
        order.setTotalMoney(jsonBody.getString("totalMoney"));
        order.setOrderStatusText(jsonBody.getString("orderStatusText"));
        order.setAcceptDateTime(jsonBody.getString("acceptDatetime"));
        order.setPickupDateTime(jsonBody.getString("pickupDatetime"));
        order.setFinishDateTime(jsonBody.getString("finishDatetime"));
        order.setDistance(jsonBody.getString("distanceText"));
        order.setDuration(jsonBody.getString("durationText"));
        order.setOrderNumber(jsonBody.getString("orderNumber"));
        order.setPassengerMobile(jsonBody.getString("passengerMobile"));
        return order;
    }
}
