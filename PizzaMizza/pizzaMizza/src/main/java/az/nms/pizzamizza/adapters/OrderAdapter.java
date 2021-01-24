package az.nms.pizzamizza.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import az.nms.pizzamizza.Constants;
import az.nms.pizzamizza.models.OrderItems;
import az.nms.pizzamizza.models.OrderList;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.activities.HomeScreenActivity;
import az.nms.pizzamizza.activities.FoodDetailActivity;
import az.nms.pizzamizza.models.Food;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;

public class OrderAdapter extends ArrayAdapter<OrderItems> {

    Activity context;
    private ItemsHolder holder;
    private int layoutResourceId;
    private List<OrderItems> orders;
    private boolean editable = false;
    private PizzaMizzaDatabase db;

    public OrderAdapter(Activity context, int layoutResourceId,
                        List<OrderItems> orders) {
        super(context, layoutResourceId, orders);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.orders = orders;
        this.db = new PizzaMizzaDatabase(context);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemsHolder();
            holder.img = (ImageView) row.findViewById(R.id.order_icon);
            holder.title = (TextView) row.findViewById(R.id.order_name);
            holder.price = (TextView) row.findViewById(R.id.order_price);
            holder.tops = (TextView) row.findViewById(R.id.order_top);
            holder.size = (TextView) row.findViewById(R.id.order_size);
            holder.qty = (TextView) row.findViewById(R.id.order_qty);
            holder.edit = (TextView) row.findViewById(R.id.edit_order);
            holder.rl = (RelativeLayout) row.findViewById(R.id.order_select);
            holder.remove_rl = (RelativeLayout) row.findViewById(R.id.remove_rl);
            row.setTag(holder);

        } else
            holder = (ItemsHolder) row.getTag();

        OrderItems order = orders.get(position);

        String url = Constants.SERVER_IMAGE_URL + orders.get(position).getFood().getImage();
        Picasso.with(context).load(url).into(holder.img);


        Food food = db.getFood(order.getFood().getId());

        String tops = Utilites.foodToppingsWithSizeToString(context, order.getFood().getTops());

        holder.title.setText(food.getName());

        holder.tops.setText(tops);
        holder.price.setText(String.valueOf(orders.get(position)
                .getTotalPrice()) + " AZN");

        holder.qty.setText(String.valueOf(orders.get(position).getQuantity()));

        if (orders.get(position).getOrderSize() != -1) {


            String doubleCheese = "";

            if (orders.get(position).isDoubleCheese())
                doubleCheese = " - " + context.getResources().getString(R.string.double_cheese);

            holder.size.setText(context.getResources().getString(Utilites.getSizeString(orders.get(position).getOrderSize())) + doubleCheese);


        } else
            holder.size.setVisibility(View.GONE);

        holder.edit
                .setOnClickListener(new ItemOnClickListener(position, row, 0));
        holder.rl.setOnClickListener(new ItemOnClickListener(position, row, 1));

        if (isEditable())
            holder.remove_rl.setVisibility(View.VISIBLE);
        else
            holder.remove_rl.setVisibility(View.GONE);

        return row;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orders.size();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean bool) {
        this.editable = bool;
    }

    @Override
    public OrderItems getItem(int position) {
        // TODO Auto-generated method stub
        return orders.get(position);
    }

    public class ItemOnClickListener implements OnClickListener {

        public int index;
        public View row;
        private int type;

        public ItemOnClickListener(int index, View row, int type) {
            // TODO Auto-generated constructor stub
            this.index = index;
            this.row = row;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            switch (type) {
                case 0:
                    onCreateDialog(index).show();
                    break;

                default:

                    Intent i = new Intent(context, FoodDetailActivity.class);

                    i.putExtra("position", index);
                    i.putExtra("activity", "order");

                    context.startActivity(i);
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    break;
            }

        }
    }

    static class ItemsHolder {
        ImageView img;
        TextView title;
        TextView qty;
        TextView price;
        TextView tops;
        RelativeLayout rl;
        TextView size;
        TextView edit;
        RelativeLayout remove_rl;
    }


    private MaterialDialog.Builder onCreateDialog(int index) {
        final int pos = index;

        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context)
                .content(R.string.item_remove_message)
                .titleColorRes(R.color.app_base_color)
                .contentColorRes(R.color.dialog_text_color)
                .positiveColorRes(R.color.app_base_color)
                .negativeColorRes(R.color.app_base_accent)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        OrderList.removeOrder(pos);
                        HomeScreenActivity.updatePrice();

                        if (OrderList.getOrders().isEmpty()) {

                            HomeScreenActivity.basket_empty
                                    .setVisibility(View.VISIBLE);
                            HomeScreenActivity.remove_order.setVisibility(View.GONE);

                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                });

        return dialog;
    }

}
