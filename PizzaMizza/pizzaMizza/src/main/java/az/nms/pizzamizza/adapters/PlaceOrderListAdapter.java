package az.nms.pizzamizza.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import az.nms.pizzamizza.Constants;
import az.nms.pizzamizza.models.OrderItems;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.tools.Utilites;

/**
 * Created by anar on 5/12/15.
 */
public class PlaceOrderListAdapter extends ArrayAdapter<OrderItems> {

    private Context context;
    private int layoutResourceId;
    private List<OrderItems> orderItems;
    private double totalprice = 0.0;


    public PlaceOrderListAdapter(Context context, int layoutResourceId, List<OrderItems> orderItems) {
        super(context, layoutResourceId, orderItems);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.orderItems = orderItems;
        calculateTotal();
    }


    private void calculateTotal() {
        for (int i = 0; i < orderItems.size(); i++) {
            totalprice += orderItems.get(i).getTotalPrice();
        }
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        ItemHolder itemHolder = null;
        int type = getItemViewType(position);

        if (row == null) {

            itemHolder = new ItemHolder();

            if (type == 0) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);


                itemHolder.name = (TextView) row.findViewById(R.id.placeorder_food_title);
                itemHolder.size = (TextView) row.findViewById(R.id.placeorder_food_size);
                itemHolder.toppings = (TextView) row.findViewById(R.id.placeorder_food_topping);
                itemHolder.price = (TextView) row.findViewById(R.id.placeorder_food_price);
                itemHolder.quantity = (TextView) row.findViewById(R.id.placeorder_food_quantity);
                itemHolder.img = (ImageView) row.findViewById(R.id.placeorder_food_image);
            } else {

                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(R.layout.placeorder_list_item_total, parent, false);

                itemHolder.total = (TextView) row.findViewById(R.id.placeorder_total);

            }

            row.setTag(itemHolder);
        } else
            itemHolder = (ItemHolder) row.getTag();

        if (type == 0) {


            OrderItems orderItem = orderItems.get(position);


            if (orderItem.getOrderSize() != -1) {
                String doubleCheese = "";

                if (orderItem.isDoubleCheese())
                    doubleCheese = " - " + context.getResources().getString(R.string.double_cheese);

                itemHolder.size.setText(context.getResources().getString(Utilites.getSizeString(orderItem.getOrderSize())) + doubleCheese);
            }

            itemHolder.name.setText(orderItem.getFood().getName());

            itemHolder.toppings.setText(Utilites.foodToppingsWithSizeToString(context, orderItem.getFood().getTops()).toLowerCase());
            itemHolder.price.setText(orderItem.getTotalPrice() + " AZN");
            itemHolder.quantity.setText(orderItem.getQuantity() + " ");

            String foodURL = Constants.SERVER_IMAGE_URL + orderItem.getFood().getImage();

            Picasso.with(context)
                    .load(foodURL)
                    .into(itemHolder.img);

        } else {

            itemHolder.total.setText(context.getResources().getString(R.string.total) + ": " + Utilites.round(totalprice, 2) + " AZN");

        }

        return row;
    }

    @Override
    public int getCount() {
        return orderItems.size() + 1;
    }

    @Override
    public OrderItems getItem(int position) {
        return orderItems.get(position);
    }

    class ItemHolder {
        TextView name, price, size, toppings, quantity;
        ImageView img;
        TextView total;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == orderItems.size()) ? 1 : 0;
    }
}
