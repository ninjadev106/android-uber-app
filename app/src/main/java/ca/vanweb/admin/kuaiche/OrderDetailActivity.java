package ca.vanweb.admin.kuaiche;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private TextView mTextViewTitle,mTextViewFrom,mTextViewTo,mTextViewPhone,mTextViewMoney,mTextViewDistance,
    mTextViewDuration,mTextViewCreate,mTextViewAccept,mTextViewPickup,mTextViewFinish;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mTextViewTitle = (TextView)findViewById(R.id.textview_title);
        mTextViewFrom = (TextView)findViewById(R.id.textview_from);
        mTextViewTo = (TextView)findViewById(R.id.textview_to);
        mTextViewPhone = (TextView)findViewById(R.id.textview_phone);
        mTextViewMoney = (TextView)findViewById(R.id.textview_money);
        mTextViewDistance = (TextView)findViewById(R.id.textview_distance);
        mTextViewDuration = (TextView)findViewById(R.id.textview_duration);
        mTextViewCreate = (TextView)findViewById(R.id.textview_create);
        mTextViewAccept = (TextView)findViewById(R.id.textview_accept);
        mTextViewPickup = (TextView)findViewById(R.id.textview_pickup);
        mTextViewFinish = (TextView)findViewById(R.id.textview_finish);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new OrderFetchTask(getIntent().getStringExtra(MapsActivity.ORDER_ID)).execute();
    }

    private void updateUI(Order order){
        mTextViewTitle.setText(order.getOrderStatusText());
        mTextViewFrom.setText(order.getFromAddress());
        mTextViewTo.setText(order.getToAddress());
        mTextViewPhone.setText(order.getPassengerMobile());
        mTextViewMoney.setText("$"+order.getTotalMoney());
        mTextViewDistance.setText(order.getDistance());
        mTextViewDuration.setText(order.getDuration());
        mTextViewCreate.setText(order.getCreateDateTime());
        mTextViewAccept.setText(order.getAcceptDateTime());
        mTextViewPickup.setText(order.getPickupDateTime());
        mTextViewFinish.setText(order.getFinishDateTime());

        getSupportActionBar().setTitle("单号 "+order.getOrderNumber());

        try {
            LatLng from = getLocationFromAddress(this, order.getFromAddress());
            LatLng to = getLocationFromAddress(this, order.getToAddress());
            mMap.addMarker(new MarkerOptions().position(from).title(getString(R.string.from)));
            mMap.addMarker(new MarkerOptions().position(to).title(getString(R.string.to)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(from));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(from,12.0f));
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(from);
            polylineOptions.add(to);
            polylineOptions.width(5);
            polylineOptions.color(Color.BLUE);
            polylineOptions.geodesic(true);
            mMap.addPolyline(polylineOptions);
        } catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public class OrderFetchTask extends AsyncTask<Void,Void,Order> {

        private ProgressDialog mProgressDialog= new ProgressDialog(getApplicationContext());
        private String mId;

        public OrderFetchTask(String id){
            mId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();
            } catch(Exception e){e.printStackTrace();}
        }

        @Override
        protected Order doInBackground(Void... voids) {
            return new DataFetcher().getOrderData(mId);

        }

        @Override
        protected void onPostExecute(Order order) {
            updateUI(order);
            try {
                mProgressDialog.dismiss();
            } catch (Exception e){e.printStackTrace();}
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }
}
