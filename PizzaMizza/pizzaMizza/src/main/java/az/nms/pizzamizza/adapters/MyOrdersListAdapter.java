package az.nms.pizzamizza.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.models.Food;
import az.nms.pizzamizza.models.MyOrders;
import az.nms.pizzamizza.models.OrderItems;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import az.nms.pizzamizza.tools.Utilites;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Azad on 1/22/15.
 */
public class MyOrdersListAdapter extends ArrayAdapter<MyOrders> implements
        StickyListHeadersAdapter {

    Context context;
    List<MyOrders> myOrders = new LinkedList<>();
    int layoutResourceId;
    ItemsHolder holder;

    List<HeaderHolder> headers = new LinkedList<>();

    private int totalCount,startingIndex,orderIndex;
    PizzaMizzaDatabase db;


    public MyOrdersListAdapter(Context context, int layoutResourceId, List<MyOrders> myOrders ){
        super(context, layoutResourceId,myOrders);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.myOrders = myOrders;

        for (int i = 0; i < myOrders.size();i++){
            totalCount += myOrders.get(i).getOrders().size();

            Log.d(" count : ", myOrders.get(i).getOrders().size() + "");
        }

        this.startingIndex = 0;
        this.orderIndex = 0;

        this.db = new PizzaMizzaDatabase(context);

        Log.d("totalCount",totalCount+"");

        countSectionHeaders();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.myorder_header, parent,
                    false);
            holder.orderid = (TextView) convertView.findViewById(R.id.myorders_id);
            holder.date = (TextView) convertView.findViewById(R.id.myorders_date);
            holder.type = (TextView) convertView.findViewById(R.id.myorders_type);
            holder.totalprice = (TextView) convertView.findViewById(R.id.myorders_total_price);
            holder.name = (TextView) convertView.findViewById(R.id.myorders_customer_name);
            holder.phone = (TextView) convertView.findViewById(R.id.myorders_customer_phone);
            holder.status = (TextView) convertView.findViewById(R.id.myorders_status);

            convertView.setTag(holder);
        } else
            holder = (HeaderViewHolder) convertView.getTag();

        HeaderHolder header = headers.get(position);

        MyOrders order = myOrders.get(header.getItemIndex());

       // Log.d("headear ",header.getItemIndex() +" : " + header.getOrderIndex());

        Log.d("order id",order.getOrderId() + "");

        String orderString = context.getResources().getString(R.string.order).toLowerCase();

        holder.date.setText(context.getResources().getString(R.string.date) + ": " +order.getDate().toLowerCase());
        holder.totalprice.setText(context.getResources().getString(R.string.total).toLowerCase() + ": " + order.getPrice() + " AZN");
        holder.orderid.setText(orderString + " #" + order.getOrderId());
        holder.type.setText((order.getType().equalsIgnoreCase("delivery") ? context.getResources().getString(R.string.delivery).toLowerCase() : context.getResources().getString(R.string.carry_out).toLowerCase()));

        holder.name.setText(context.getResources().getString(R.string.customer).toLowerCase() + ": "+ order.getCustomerName());
        holder.phone.setText(context.getResources().getString(R.string.phone).toLowerCase() + ": " + order.getCustomerPhone());
        holder.status.setText(Utilites.getStatus(context, order.getStatus()));

       // Log.d("nothing to display","s");

        return convertView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        holder = null;

        if (convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemsHolder();

            holder.name = (TextView) convertView.findViewById(R.id.myorders_food_name);
            holder.toppings = (TextView) convertView.findViewById(R.id.myorders_toppings);
            holder.price = (TextView) convertView.findViewById(R.id.myorders_price);
            holder.shadow = (ImageView) convertView.findViewById(R.id.myorder_shadow);

            convertView.setTag(holder);

        } else
            holder = (ItemsHolder) convertView.getTag();

        HeaderHolder header = headers.get(position);

        Log.d("header : ", header.getItemIndex() + " : " + header.getOrderIndex());

        OrderItems order = myOrders.get(header.getItemIndex()).getOrders().get(header.getOrderIndex());

        if(header.getOrderIndex() == 0)
            holder.shadow.setVisibility(View.VISIBLE);
        else
            holder.shadow.setVisibility(View.GONE);


        Food food = db.getFood(order.getFoodId());

        String small = context.getResources().getString(R.string.small);
        String medium = context.getResources().getString(R.string.medium);
        String sizeString = "";

        String doubleCheese = "";

        if(order.isDoubleCheese())
            doubleCheese = " - " + context.getResources().getString(R.string.double_cheese);

        if (order.getOrderSize() != -1)
            sizeString = context.getResources().getString(Utilites.getSizeString(order.getOrderSize()));


        String name = order.getQuantity() + " " + sizeString + " - " + food.getName().toLowerCase()  + doubleCheese ;
        String price = Utilites.round(order.getTotalPrice(), 2) + " AZN";
        String toppings = order.getToppings().toLowerCase();

        holder.name.setText(name);
        holder.price.setText(price);



        holder.toppings.setText(toppings);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return myOrders.get(headers.get(position).getItemIndex()).getOrderId();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return totalCount;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public MyOrders getItem(int position) {
        return myOrders.get(position);
    }


    private void countSectionHeaders(){

        int itemIndex = 0;
        int count = 0;
        int orderIndex = 0;
        int startingIndex = 0;

        for (int i = 0; i < totalCount;i++){

           count = myOrders.get(itemIndex).getOrders().size();

           Log.d(" header info","c: " + count + ",i: " + itemIndex + ",s: " + startingIndex + ",o: " + orderIndex);

           headers.add(new HeaderHolder(count, itemIndex, orderIndex));

           if(count + startingIndex == i + 1){
                itemIndex ++;
                startingIndex += count;
                orderIndex = 0;
           }else{
               orderIndex ++;
           }

        }
    }



    /*

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        holder = null;
        if (convertView == null) {
            holder = new ItemsHolder();

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder.orderId = (TextView) convertView.findViewById(R.id.myorders_item_id);
            holder.date = (TextView) convertView.findViewById(R.id.myorders_item_date);
            holder.price = (TextView) convertView.findViewById(R.id.myorders_item_total);
            holder.orderNames = (TextView) convertView.findViewById(R.id.myorders_item_orders);
            holder.list = (LinearLayout) convertView.findViewById(R.id.myorders_names_list);

            convertView.setTag(holder);

        } else
            holder = (ItemsHolder) convertView.getTag();

        holder.date.setText(orders.get(position).getDate());
        holder.price.setText(orders.get(position).getPrice() + " AZN");
        holder.orderId.setText("order id " + orders.get(position).getOrderId());
        holder.orderNames.setText(getOrderNameList(orders.get(position).getFood()));





        return convertView;
    }


*/


    class HeaderViewHolder {
        TextView type,totalprice,orderid,date,name, phone,status;
    }


    class ItemsHolder{
        TextView name,toppings,price;
        ImageView shadow;
    }


    private class HeaderHolder{

        private int count,itemIndex,orderIndex;

        public HeaderHolder(int count,int itemIndex,int orderIndex){
            this.count = count;
            this.itemIndex = itemIndex;
            this.orderIndex = orderIndex;
        }

        public int getCount() {
            return count;
        }

        public int getOrderIndex() {
            return orderIndex;
        }

        public int getItemIndex() {
            return itemIndex;
        }
    }



}
