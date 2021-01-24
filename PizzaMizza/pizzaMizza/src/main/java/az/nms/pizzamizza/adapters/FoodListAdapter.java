package az.nms.pizzamizza.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizza.Constants;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.SharedData;
import az.nms.pizzamizza.models.Food;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class FoodListAdapter extends ArrayAdapter<Food> implements
        StickyListHeadersAdapter {

    private Context context;
    private List<Food> foods;
    private int layoutResourceId;
    private ItemsHolder holder;
    public static final int SEARCH = 1;
    public static final int LIST = 0;
    private PizzaMizzaDatabase db;

    public FoodListAdapter(Context context, int layoutResourceId,
                           List<Food> foods, int type) {
        super(context, layoutResourceId, foods);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.foods = foods;
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
            holder.img = (ImageView) row.findViewById(R.id.pizza_icon);
            holder.title = (TextView) row.findViewById(R.id.pizza_title);
            holder.tops = (TextView) row.findViewById(R.id.pizza_top);
            holder.lr_quick_order = (LinearLayout) row
                    .findViewById(R.id.lr_quick_order);

            row.setTag(holder);

        } else
            holder = (ItemsHolder) row.getTag();


        Food food = foods.get(position);

        String url = Constants.SERVER_IMAGE_URL + food.getImage();

        Picasso.with(context).load(url).into(holder.img);


        // holder.img.setImageResource(foods.get(position).getIcon());
        holder.title.setText(food.getName());
        holder.tops.setText(db.getFoodToppingsAsString(food.getId(), SharedData.getLanguage()));
//        holder.title.setTransitionName("foodname"+position);

        return row;
    }

    static class ItemsHolder {
        ImageView img;
        LinearLayout rl, lr_quick_order;
        TextView title;
        TextView tops;
    }




    public void setFood(List<Food> food) {

        this.foods = new LinkedList<Food>(food);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return foods.size();
    }

    @Override
    public Food getItem(int position) {
        // TODO Auto-generated method stub
        return foods.get(position);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.pizza_list_header, parent,
                    false);
            holder.text = (TextView) convertView
                    .findViewById(R.id.category_name);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        // set header text as first char in name
        String headerText = "";

        Food food = foods.get(position);

        if (food.getPriceSmall() != 0) {
            headerText += context.getResources().getString(R.string.header_small);
        }
        if (food.getPriceMedium() != 0) {
            headerText += context.getResources().getString(R.string.header_medium);
        }
        if (food.getPriceLarge() != 0) {
            headerText += context.getResources().getString(R.string.header_large);
        }


//
//            headerText = headerText.replace("sss",String.valueOf(food.getPriceSmall()));
//        if(food.getPriceMedium() != 0)
//            headerText = headerText.replace("mmm",String.valueOf(food.getPriceMedium()));
//        if(food.getPriceLarge() != 0)
//            headerText = headerText.replace("lll",String.valueOf(food.getPriceLarge()));
//


        if (food.getType().equals("pizza")) {
            headerText = headerText.replace("sss", " - " + food.getPriceSmall() + " AZN | ");
            headerText = headerText.replace("mmm", " - " + food.getPriceMedium() + " AZN  \n");
            headerText = headerText.replace("lll", " - " + food.getPriceLarge() + " AZN");

        } else if (food.getType().equals("salad")) {
            headerText = headerText.replace("mmm", context.getResources().getString(R.string.salad_unit_medium) + "/ " + food.getPriceMedium() + " AZN \n");
            headerText = headerText.replace("lll", context.getResources().getString(R.string.salad_unit_large) + "/ " + food.getPriceLarge() + " AZN");
        } else if (food.getType().equals("panini")) {
            headerText = headerText.replace("mmm", "(" + context.getResources().getString(R.string.panini_unit_medium) + ") " + food.getPriceMedium() + " AZN \n");
            headerText = headerText.replace("lll", "(" + context.getResources().getString(R.string.panini_unit_large) + ") " + food.getPriceLarge() + " AZN");
        } else if (food.getType().equals("pasta"))
            headerText = headerText.replace("mmm", context.getResources().getString(R.string.pasta_unit_medium) + "/ " + food.getPriceMedium() + " AZN");
        else if (food.getType().equals("sauce"))
            headerText = context.getResources().getString(R.string.salad_sauce) + " - " + food.getPriceMedium() + " AZN";
        else if (food.getType().equals("panini_sauce"))
            headerText = context.getResources().getString(R.string.pannini_sauce) + " - " + food.getPriceMedium() + " AZN";

        else
            headerText = context.getResources().getString(R.string.price) + " - " + food.getPriceMedium() + " AZN";


        holder.text.setText(headerText);
        return convertView;
    }

    class HeaderViewHolder {
        TextView text;
    }


    @Override
    public long getHeaderId(int position) {
        // TODO Auto-generated method stub

        Food food = foods.get(position);

        String type = food.getType();

        double id = food.getPriceSmall() + food.getPriceLarge() + food.getPriceMedium() + type.substring(0,1).charAt(0);

        return Math.round(id);
    }

}
