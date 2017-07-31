package ca.vanweb.admin.kuaiche.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.vanweb.admin.kuaiche.Order;
import ca.vanweb.admin.kuaiche.OrderDetailActivity;
import ca.vanweb.admin.kuaiche.R;

import static ca.vanweb.admin.kuaiche.MapsActivity.ORDER_ID;

/**
 * Created by Momen on 6/23/2017.
 */

public class mapActivity_adapter extends RecyclerView.Adapter<mapActivity_adapter.OrderHolder> {


    private List<Order> mOrders;
    Context context;

    public mapActivity_adapter(List<Order> orders, Context context) {
        mOrders = orders;
        this.context = context;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        Order order = mOrders.get(position);
        holder.bindOrder(order);
        //holder.itemView.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#f0e3b5") : Color.parseColor("#ed8413"));
    }

    @Override
    public int getItemCount() {
//        Toast.makeText(context, ""+mOrders.size(), Toast.LENGTH_SHORT).show();
        return mOrders.size();
    }


    public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Order mOrder;
        private TextView mTextViewFrom;
        private TextView mTextViewTo;
        private TextView mTextViewDate;
        private TextView mTextViewStatus;
        private TextView mTextViewTotal;

        OrderHolder(View itemView) {
            super(itemView);
            mTextViewFrom = (TextView) itemView.findViewById(R.id.textview_from);
            mTextViewTo = (TextView) itemView.findViewById(R.id.textview_to);
            mTextViewDate = (TextView) itemView.findViewById(R.id.textview_date);
            mTextViewStatus = (TextView) itemView.findViewById(R.id.textview_status);
            mTextViewTotal = (TextView) itemView.findViewById(R.id.textview_total_money);
            itemView.setOnClickListener(this);
        }

        void bindOrder(Order order) {
            mOrder = order;
            mTextViewFrom.setText(order.getFromAddress());
            mTextViewTo.setText(order.getToAddress());
            mTextViewDate.setText(order.getCreateDateTime());
            mTextViewStatus.setText(order.getOrderStatusText());
            mTextViewTotal.setText("$" + order.getTotalMoney());
        }

        @Override
        public void onClick(View view) {
            openDetails(mOrder);
        }


    }

    private void openDetails(Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(ORDER_ID, order.getID());
        context.startActivity(intent);
    }
}


