package ca.vanweb.admin.kuaiche;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.design.widget.TabLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ca.vanweb.admin.kuaiche.ViewPager.viewPager;

import static ca.vanweb.admin.kuaiche.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;

    private double longitude;
    private double latitude;

    private OrdersAdapter sAdapter = null;
    public ArrayList<Order> mOrders = new ArrayList<>();
    public static final String ORDER_ID = "order_id";

    LinearLayout container_all;

    SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static Timer timer;
    MarkerOptions options;
    LatLng sydney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        container_all=(LinearLayout)findViewById(R.id.container_all);

        Log.d("log", "oncreate...");


//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(new viewPager(getSupportFragmentManager(),getBaseContext(),this));


//FOR TABS
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container_vi);
        mViewPager.setAdapter(new viewPager(getSupportFragmentManager(), getBaseContext(),this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Fir");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabOne2 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne2.setText("Sec");
        tabLayout.getTabAt(1).setCustomView(tabOne2);


        TextView tabOne3 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne3.setText("Thir");
        tabLayout.getTabAt(2).setCustomView(tabOne3);
////////




        prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor = prefs.edit();

    }


    /**
     * Manipulates the smap once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //getCurrentLocation();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        //Location myLocation = mMap.getMyLocation();
        //LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        //CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(17).bearing(90).tilt(30).build();
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(17).bearing(90).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        get_current_location();
        pin_marker();

// uppdate location
        if (timer == null) {
            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {


                public void run() {
                    if ((Double.parseDouble(prefs.getString("longitude", "0"))) != longitude ||
                            (Double.parseDouble(prefs.getString("latitude", "0"))) != latitude) {

                        get_current_location();
                        container_all.post(new Runnable() {
                            @Override
                            public void run() {


                                pin_marker();

                            }
                        });

                    }
                }
            }, 0, 5000);
        }

    }

    private void get_current_location() {
        longitude = Double.parseDouble(prefs.getString("longitude", "0"));
        latitude = Double.parseDouble(prefs.getString("latitude", "0"));

        sydney = new LatLng(latitude, longitude);

        options = new MarkerOptions();

        options.position(sydney);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

    }

    private void pin_marker() {
        mMap.clear();
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));

    }


    public void getOrders() {

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

//            for (int i = 0; i < aryUser.length(); i++) {
//                JSONObject row = aryUser.getJSONObject(i);
//                //updatedProfile user = new updatedProfile();
//                //user.loadFromJSONObjectExt(row);
//                //searchResult.add(user);
//                Log.d("address", row.getString("fromAddress"));
//
//            }


        } catch (UnirestException e) {
            Log.e("ERROR", e.getMessage());
        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }


    }

    private void openDetails(Order order) {
        Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
        intent.putExtra(ORDER_ID, order.getID());
        startActivity(intent);
    }


    //Background task to launch the DataFetcher. When finished sets the adapter to recyclerview with data obtained

//    //sets the layout for order item
//    public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        private Order mOrder;
//        private TextView mTextViewFrom;
//        private TextView mTextViewTo;
//        private TextView mTextViewDate;
//        private TextView mTextViewStatus;
//        private TextView mTextViewTotal;
//
//        OrderHolder(View itemView) {
//            super(itemView);
//            mTextViewFrom = (TextView) itemView.findViewById(R.id.textview_from);
//            mTextViewTo = (TextView) itemView.findViewById(R.id.textview_to);
//            mTextViewDate = (TextView) itemView.findViewById(R.id.textview_date);
//            mTextViewStatus = (TextView) itemView.findViewById(R.id.textview_status);
//            mTextViewTotal = (TextView) itemView.findViewById(R.id.textview_total_money);
//            itemView.setOnClickListener(this);
//        }
//
//        void bindOrder(Order order) {
//            mOrder = order;
//            mTextViewFrom.setText(order.getFromAddress());
//            mTextViewTo.setText(order.getToAddress());
//            mTextViewDate.setText(order.getCreateDateTime());
//            mTextViewStatus.setText(order.getOrderStatusText());
//            mTextViewTotal.setText("$" + order.getTotalMoney());
//        }
//
//        @Override
//        public void onClick(View view) {
//            openDetails(mOrder);
//        }
//    }
//
//    //binds orders from a list to the holder
//    public class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {
//
//        private List<Order> mOrders;
//
//        OrderAdapter(List<Order> orders) {
//            mOrders = orders;
//        }
//
//        @Override
//        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_order, parent, false);
//            return new OrderHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(OrderHolder holder, int position) {
//            Order order = mOrders.get(position);
//            holder.bindOrder(order);
//            //holder.itemView.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#f0e3b5") : Color.parseColor("#ed8413"));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mOrders.size();
//        }
//    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
