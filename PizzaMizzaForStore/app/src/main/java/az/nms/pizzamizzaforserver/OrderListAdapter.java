package az.nms.pizzamizzaforserver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import az.nms.pizzamizzaforserver.storage.Customer;
import az.nms.pizzamizzaforserver.storage.Order;

/**
 * Created by anar on 5/8/15.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;


    OrderListAdapter(Context context, List<Order> orders){
        this.orders = orders;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //inflate recyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_item, parent, false);
        OrderViewHolder holder  = new OrderViewHolder(context,view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {

        // Get the order details for each postion
        Order order = orders.get(position);

        // Set order details to row view
        holder.orderId.setText("Order #" + order.getId());
        holder.orderPrice.setText(order.getTotalPrice() + " AZN");
        holder.orderType.setText(order.getType());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getDate());

        holder.orderDate.setText(sdf.format(calendar.getTime()));

        // Get the customer details of each order for position
        Customer customer = order.getCustomer();

        // Set customer details to row view
        holder.customerName.setText(customer.getName());
        holder.customerAddress.setText(customer.getAddress());
        holder.customerPhone.setText(customer.getPhone());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView orderDate;
        TextView orderType;
        TextView customerName;
        TextView customerAddress;
        TextView orderPrice;
        TextView customerPhone;
        Context context;


        OrderViewHolder(Context context,View itemView) {
            super(itemView);

            this.context = context;

            orderDate = (TextView) itemView.findViewById(R.id.order_date);
            orderType = (TextView) itemView.findViewById(R.id.order_type);
            orderId = (TextView) itemView.findViewById(R.id.order_id);
            orderPrice = (TextView) itemView.findViewById(R.id.order_price);
            orderDate = (TextView) itemView.findViewById(R.id.order_date);
            orderDate = (TextView) itemView.findViewById(R.id.order_date);
            customerName = (TextView) itemView.findViewById(R.id.order_name);
            customerAddress = (TextView) itemView.findViewById(R.id.order_address);
            customerPhone = (TextView) itemView.findViewById(R.id.order_phone);



        }
    }


}
