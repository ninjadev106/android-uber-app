package ca.vanweb.admin.kuaiche;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 6/6/2017.
 */

public class OrdersAdapter extends BaseAdapter {

    private ArrayList<OrdersBean> data = new ArrayList<OrdersBean>();
    private Context mContext;
    private LayoutInflater mInflater;

    public OrdersAdapter(Context context, ArrayList<OrdersBean> items) {
        this.mContext = context;
        this.data = items;
        //mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return data.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_orders, parent, false);


        return rowView;
    }
}

