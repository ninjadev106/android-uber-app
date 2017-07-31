package ca.vanweb.admin.kuaiche.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ca.vanweb.admin.kuaiche.Adapter.mapActivity_adapter;
import ca.vanweb.admin.kuaiche.DataFetcher;
import ca.vanweb.admin.kuaiche.Order;
import ca.vanweb.admin.kuaiche.R;

public class list_fragment extends Fragment {
    private RecyclerView mRecyclerView;
    int position;
    Context context;
    Activity activity;
    private static Timer timer;
    private  ProgressDialog mProgressDialog;

    public list_fragment() {
        // Required empty public constructor
    }

    public list_fragment(int position, Context context, Activity activity) {
        this.context = context;
        this.position = position;
        this.activity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.list_fragment, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_orders);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);


        new FetchTask().execute();


        if (timer == null) {
            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {


                public void run() {

//                        mRecyclerView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        });

                    new FetchTask().execute();

                }
            }, 0, 5000);
        }


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class FetchTask extends AsyncTask<Void, Void, ArrayList<Order>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Refresh_now", "yes");
            try {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(activity);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<Order> doInBackground(Void... voids) {
            if (position == 0) {
                return new DataFetcher().getFolows();

            } else if (position == 1) {
                return new DataFetcher().getOrders();

            } else {
                return new DataFetcher().getOrders3();

            }

        }

        @Override
        protected void onPostExecute(ArrayList<Order> orders) {
//            mOrders = orders;
            mapActivity_adapter mapActivity_adapter = new mapActivity_adapter(orders, context);
            mRecyclerView.setAdapter(mapActivity_adapter);
            Log.d("DONEREFRESH", "YEs");
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
