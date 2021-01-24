package az.nms.pizzamizzaforserver;

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

import az.nms.pizzamizzaforserver.Utitilies.Constants;
import az.nms.pizzamizzaforserver.storage.OrderItem;

/**
 * Created by anar on 5/12/15.
 */
public class OrderDetailAdapter extends ArrayAdapter<OrderItem> {

    private Context context;
    private int layoutResourceId;
    private List<OrderItem> orderItems;


    public OrderDetailAdapter(Context context, int layoutResourceId, List<OrderItem> orderItems){
        super(context, layoutResourceId, orderItems);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.orderItems = orderItems;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        ItemHolder itemHolder = null;

        if(row == null){

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            itemHolder = new ItemHolder();
            itemHolder.name = (TextView) row.findViewById(R.id.food_title);
            itemHolder.size = (TextView) row.findViewById(R.id.food_size);
            itemHolder.toppings = (TextView) row.findViewById(R.id.food_topping);
            itemHolder.price = (TextView) row.findViewById(R.id.food_price);
            itemHolder.quantity = (TextView) row.findViewById(R.id.food_quantity);
            itemHolder.img = (ImageView) row.findViewById(R.id.food_image);

            row.setTag(itemHolder);
        } else
            itemHolder = (ItemHolder) row.getTag();

        OrderItem orderItem = orderItems.get(position);

        itemHolder.name.setText(orderItem.getFood_name());
        itemHolder.size.setText(orderItem.getFood_size() + (orderItem.isDouble_cheese() ? " - DOUBLE CHEESE":""));
        itemHolder.toppings.setText(orderItem.getFood_toppings());
        itemHolder.price.setText(orderItem.getFood_total() + " AZN");
        itemHolder.quantity.setText(orderItem.getFood_quantity() + " ");

        String foodURL = Constants.IMAGES_URL + orderItem.getFood_icon();

        Picasso.with(context)
                .load(foodURL)
                .into(itemHolder.img);

        return row;
    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public OrderItem getItem(int position) {
        return orderItems.get(position);
    }

    class ItemHolder{
        TextView name, price, size, toppings, quantity;
        ImageView img;
    }
}
