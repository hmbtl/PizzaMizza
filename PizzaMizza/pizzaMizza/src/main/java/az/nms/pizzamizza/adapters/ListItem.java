package az.nms.pizzamizza.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.activities.FoodDetailActivity;
import az.nms.pizzamizza.adapters.DetailListAdapter.RowType;
import az.nms.pizzamizza.models.Top;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;

public class ListItem implements Item {
    private final Top top;
    private final int size;
    private ItemsHolder holder;
    private Activity context;
    private PizzaMizzaDatabase db;

    public ListItem(Activity context, Top top, int size) {
        this.top = top;
        this.context = context;
        this.size = size;
        this.db = new PizzaMizzaDatabase(context);
    }

    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View row) {
        holder = null;
        if (row == null) {
            row = (View) inflater.inflate(R.layout.detail_topping_select_item,
                    null);
            holder = new ItemsHolder();

            holder.top_name = (TextView) row.findViewById(R.id.detail_top_name);
            holder.top_size = (TextView) row.findViewById(R.id.detail_top_size);

            holder.lr_expanded = (LinearLayout) row
                    .findViewById(R.id.detail_top_expanded);
            holder.minus = (ImageView) row.findViewById(R.id.detail_size_minus);

            holder.plus = (ImageView) row.findViewById(R.id.detail_size_plus);

            row.setTag(holder);
            // Do some initialization
        } else {
            holder = (ItemsHolder) row.getTag();
        }


        holder.lr_expanded.setVisibility(View.VISIBLE);

        if (top.getEditable() == 1) {
            holder.minus.setOnClickListener(new ItemOnClickListener(row));
            holder.plus.setOnClickListener(new ItemOnClickListener(row));
        }

        holder.top_name.setText(db.getTop(top.getId()).getName());
        holder.top_size.setText(Utilites.getTopSizeAsString(context, size));

        return row;
    }

    static class ItemsHolder {
        TextView top_name, top_size;
        LinearLayout lr_expanded;
        ImageView plus, minus;

    }

    public int getTopSize(TextView txt, int action) {
        String size = txt.getText().toString();


        switch (action) {
            case -1:
                if (size.equalsIgnoreCase(context.getResources().getString(R.string.normal))) {
                    txt.setText(R.string.none);
                    return -1;
                } else if (size.equalsIgnoreCase(context.getResources().getString(R.string.extra))) {
                    txt.setText(R.string.normal);
                    return 0;
                } else if (size.equalsIgnoreCase(context.getResources().getString(R.string.none))) {
                    txt.setText(R.string.none);
                    return -1;
                }
                break;

            case 1:
                if (size.equalsIgnoreCase(context.getResources().getString(R.string.none))) {
                    txt.setText(R.string.normal);
                    return 0;
                } else if (size.equalsIgnoreCase(context.getResources().getString(R.string.normal))) {
                    txt.setText(R.string.extra);
                    return 1;
                } else if (size.equalsIgnoreCase(context.getResources().getString(R.string.extra))) {
                    txt.setText(R.string.extra);
                    return 1;
                }
                break;
        }

        return 0;
    }


    public class ItemOnClickListener implements OnClickListener {

        public View row;
        int quantity;

        public ItemOnClickListener(View row) {
            // TODO Auto-generated constructor stub
            this.row = row;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TextView size = (TextView) row.findViewById(R.id.detail_top_size);
            switch (v.getId()) {
                case R.id.detail_size_minus:
                    top.setSize(getTopSize(size, -1));

                    break;

                case R.id.detail_size_plus:
                    top.setSize(getTopSize(size, 1));
                    break;
            }
            ColorDrawable[] color = {new ColorDrawable(Color.RED),
                    new ColorDrawable(Color.WHITE)};
            TransitionDrawable trans = new TransitionDrawable(color);
            // This will work also on old devices. The latest API says you have
            // to use setBackground instead.

            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                FoodDetailActivity.detail_bg.setBackgroundDrawable(trans);
            } else {
                FoodDetailActivity.detail_bg.setBackground(trans);
            }
            trans.startTransition(400);
            FoodDetailActivity.tops_name.setText(Utilites.foodToppingsWithSizeToString(context, FoodDetailActivity.food.getTops()));

        }
    }

}