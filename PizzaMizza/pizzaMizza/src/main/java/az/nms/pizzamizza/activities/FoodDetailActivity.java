package az.nms.pizzamizza.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizza.Constants;
import az.nms.pizzamizza.models.OrderItems;
import az.nms.pizzamizza.models.OrderList;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.adapters.DetailListAdapter;
import az.nms.pizzamizza.adapters.Header;
import az.nms.pizzamizza.adapters.Item;
import az.nms.pizzamizza.adapters.ListItem;
import az.nms.pizzamizza.models.Food;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class FoodDetailActivity extends ActionBarActivity {

    private int id, orderIndex;
    public static ListView top_list;
    public static ListView select_list;
    public static Food food;

    private TextView cheeseText;

    private double cheesePriceAdd = 0;

    LinearLayout selectedSizeButton, smallSizeSelection, mediumSizeSelection, largeSizeSelection;
    private TextView selectedSizeText, smallSizeSelectionText, mediumSizeSelectionText, largeSizeSelectionText;
    private ImageView sizeSelectionIndicator;

    private boolean isDoubleCheese = false;

    private final double DOUBLE_CHEESE_PRICE_SMALL = 1;
    private final double DOUBLE_CHEESE_PRICE_MEDIUM = 1.5;
    private final double DOUBLE_CHEESE_PRICE_LARGE = 2;


    private RelativeLayout cheeseSelection;

    private ImageView cheeseSelectedIndicator;

    private boolean isSizeSelectionOpen;

    private List<FoodSize> foodSize = new LinkedList<>();

    private LinearLayout addToBasket;

    private ListView detail_list;

    private int selectedSize;

    ImageButton home_button;

    private boolean haveSize = false;

    public static TextView title, price, count, addToBasketText;
    public static TextView tops_name;

    public OrderItems order;

    public static RelativeLayout detail_bg;

    private View headerView;

    private ImageView plus, minus, icon;

    DetailListAdapter adapter_detail;

    private PizzaMizzaDatabase db;

    public FoodDetailActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pizza_buy_layout);

        // Set the fonts of the application
        Utilites.initAppBarFont(this);


        // Get the id of the food from pervious intent activity
        id = getIntent().getIntExtra("id", 0);

        // Get the position of the
        orderIndex = getIntent().getIntExtra("position", 0);

        // Initialize PizzaSQLiteHelper database to get food
        db = new PizzaMizzaDatabase(this);


        // Initialize listviews
        top_list = new ListView(this);
        select_list = new ListView(this);


        headerView = View.inflate(this, R.layout.detail_list_header, null);

        // Initialize layout
        initLayout();

        List<Item> items = new ArrayList<Item>();

        if (food.getType().equalsIgnoreCase("pizza")) {

            boolean haveEdit = false;

            for (int i = 0; i < food.getTops().size(); i++) {
                if (food.getTops().get(i).getEditable() == 1) {
                    if (!haveEdit) {
                        items.add(new Header(R.string.edit_toppings));
                        haveEdit = true;
                    }
                    items.add(new ListItem(this, FoodDetailActivity.food.getTops()
                            .get(i), food.getTops().get(i).getSize()));
                }

            }
        }

        adapter_detail = new DetailListAdapter(this, items);


        detail_list.addHeaderView(headerView);
        detail_list.setAdapter(adapter_detail);


        sizeSelectionIndicator = (ImageView) headerView
                .findViewById(R.id.size_indicator);

        addToBasket.setOnClickListener(onClick);
        plus.setOnClickListener(onClick);
        minus.setOnClickListener(onClick);

//        price.setText(String.valueOf(food.getPrice(selectedSize)));


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();


    }

    private void initLayout() {

        // Initialize layout views
        detail_list = (ListView) findViewById(R.id.detail_list_view);
        title = (TextView) findViewById(R.id.detail_food_name);
        tops_name = (TextView) findViewById(R.id.detail_topping);
        icon = (ImageView) findViewById(R.id.detail_icon);
        addToBasket = (LinearLayout) findViewById(R.id.detail_add_to_basket);
        addToBasketText = (TextView) findViewById(R.id.detail_add_text);
        price = (TextView) findViewById(R.id.detail_price);
        home_button = (ImageButton) findViewById(R.id.home_button);
        detail_bg = (RelativeLayout) findViewById(R.id.detail_bg_rl);

        home_button.setOnClickListener(onClick);


        Utilites.hideButton(this, Utilites.ACTION_BAR_EDIT);

        if ("order".equals(getIntent().getStringExtra("activity")))
            onOrderCall();
        else
            onFoodCall();

        title.setText(food.getName());
        String url = Constants.SERVER_IMAGE_URL + food.getImage();
        Picasso.with(this).load(url).into(icon);

        tops_name.setText(Utilites.foodToppingsWithSizeToString(this, food.getTops()));


        plus = (ImageView) headerView.findViewById(R.id.detail_count_plus);
        minus = (ImageView) headerView.findViewById(R.id.detail_count_minus);

        count = (TextView) headerView.findViewById(R.id.detail_count);

        cheeseText = (TextView) headerView.findViewById(R.id.detail_cheese_text);


        calculateDoubleCheese();


        if ("order".equals(getIntent().getStringExtra("activity")))
            count.setText(String.valueOf(order.getQuantity()));


        cheeseSelectedIndicator = (ImageView) headerView.findViewById(R.id.detail_cheese_success);
        cheeseSelection = (RelativeLayout) headerView.findViewById(R.id.detail_cheese_selection);
        cheeseSelection.setOnClickListener(onClick);

        if (isDoubleCheese) {
            cheeseSelectedIndicator.setVisibility(View.VISIBLE);
            cheeseSelection.setBackgroundResource(R.drawable.cheese_double_enabled_button_selector);
        } else
            cheeseSelectedIndicator.setVisibility(View.GONE);

        initSizeSelecetion();

    }

    private void onOrderCall() {
        order = OrderList.getOrders().get(orderIndex);
        food = order.getFood();
        addToBasketText.setText(R.string.save);
        selectedSize = order.getOrderSize();
        isDoubleCheese = order.isDoubleCheese();
    }

    private void onFoodCall() {

        food = db.getFood(id);
        addToBasketText.setText(R.string.add_to_basket);
        selectedSize = 1;
        food.setTops(db.getFoodToppings(food.getId()));
    }

    public void createOrder(boolean bool) {
        OrderItems order_item = new OrderItems(food, (haveSize ? selectedSize : -1),
                Integer.parseInt(count.getText().toString()), Utilites.round(
                Integer.parseInt(count.getText().toString())
                        * (food.getPrice(selectedSize)), 2), isDoubleCheese);

        if (!bool)
            OrderList.addOrder(order_item);
        else
            OrderList.setOrder(orderIndex, order_item);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        finish();
    }

    public void goMain() {

        Intent i = new Intent(FoodDetailActivity.this, HomeScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);


        // overridePendingTransition(R.anim.slide_in_bottom,
        // R.anim.slide_out_bottom);

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        public void onClick(View v) {
            // it was the 1st button

            int quant = 0;
            switch (v.getId()) {
                case R.id.home_button:
                    goMain();
                    break;

                case R.id.detail_add_to_basket:
                    HomeScreenActivity.basket_empty.setVisibility(View.GONE);

                    createOrder("order".equals(getIntent().getStringExtra(
                            "activity")));

                    goMain();
                    break;

                case R.id.detail_count_plus:
                    quant = Integer.parseInt(count.getText().toString()) + 1;
                    count.setText(quant + "");

                    break;
                case R.id.detail_count_minus:

                    if (Integer.parseInt(count.getText().toString()) > 1) {
                        quant = Integer.parseInt(count.getText().toString()) - 1;

                        count.setText(quant + "");

                    }
                    break;
                case R.id.detail_cheese_selection:

                    if (isDoubleCheese) {

                        isDoubleCheese = false;
                        cheeseSelection.setBackgroundResource(R.drawable.cheese_double_button_selector);
                        cheeseSelectedIndicator.setVisibility(View.GONE);
                    } else {

                        isDoubleCheese = true;
                        cheeseSelection.setBackgroundResource(R.drawable.cheese_double_enabled_button_selector);
                        cheeseSelectedIndicator.setVisibility(View.VISIBLE);
                    }

                    break;

            }
            updatePrice();
        }
    };

    public void updatePrice() {

        price.setText(Utilites.round(
                (food.getPrice(selectedSize) + (isDoubleCheese ? cheesePriceAdd : 0))
                        * Integer.parseInt(count.getText().toString()), 2)
                + " AZN");


    }

    private void initSizeSelecetion() {
        selectedSizeButton = (LinearLayout) headerView.findViewById(R.id.selected_size);
        smallSizeSelection = (LinearLayout) headerView.findViewById(R.id.size_selection_small);
        mediumSizeSelection = (LinearLayout) headerView.findViewById(R.id.size_selection_medium);
        largeSizeSelection = (LinearLayout) headerView.findViewById(R.id.size_selection_large);

        selectedSizeButton.setOnClickListener(onSizeClick);
        smallSizeSelection.setOnClickListener(onSizeClick);
        mediumSizeSelection.setOnClickListener(onSizeClick);
        largeSizeSelection.setOnClickListener(onSizeClick);

        selectedSizeText = (TextView) headerView.findViewById(R.id.selected_size_text);
        smallSizeSelectionText = (TextView) headerView.findViewById(R.id.size_selection_small_text);
        mediumSizeSelectionText = (TextView) headerView.findViewById(R.id.size_selection_medium_text);
        largeSizeSelectionText = (TextView) headerView.findViewById(R.id.size_selection_large_text);

        sizeSelectionIndicator = (ImageView) headerView.findViewById(R.id.size_indicator);


        selectedSizeButton.setVisibility(View.VISIBLE);
        smallSizeSelection.setVisibility(View.GONE);
        mediumSizeSelection.setVisibility(View.GONE);
        largeSizeSelection.setVisibility(View.GONE);


        if (!food.getType().equals("pizza")) {
            cheeseSelection.setVisibility(View.GONE);
            headerView.findViewById(R.id.detail_cheese_shadow).setVisibility(View.GONE);
            cheeseSelectedIndicator.setVisibility(View.GONE);
        }

        if (food.getType().equals("pizza")) {
            smallSizeSelectionText.setText(R.string.pizza_unit_small);
            mediumSizeSelectionText.setText(R.string.pizza_unit_medium);
            largeSizeSelectionText.setText(R.string.pizza_unit_large);
            haveSize = true;

            /*  Show selection of cheese size if it is pizza */

        } else if (food.getType().equals("salad")) {
            mediumSizeSelectionText.setText(R.string.salad_unit_medium);
            largeSizeSelectionText.setText(R.string.salad_unit_large);
            haveSize = true;
        } else if (food.getType().equals("panini")) {
            mediumSizeSelectionText.setText(R.string.panini_unit_medium);
            largeSizeSelectionText.setText(R.string.panini_unit_large);
            haveSize = true;
        } else {
            headerView.findViewById(R.id.size_selection).setVisibility(View.GONE);
            headerView.findViewById(R.id.detail_size_select_shadow).setVisibility(View.GONE);
            haveSize = false;
        }


        if (food.getPriceSmall() != 0)
            foodSize.add(new FoodSize(smallSizeSelection, 0));

        if (food.getPriceMedium() != 0)
            foodSize.add(new FoodSize(mediumSizeSelection, 1));

        if (food.getPriceLarge() != 0)
            foodSize.add(new FoodSize(largeSizeSelection, 2));

        selectedSizeText.setText(Utilites.getSizeString(selectedSize));


        isSizeSelectionOpen = false;

        updatePrice();

    }


    View.OnClickListener onSizeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            for (int i = 0; i < foodSize.size(); i++) {
                if (view == foodSize.get(i).getLayout()) {
                    selectedSize = foodSize.get(i).getSize();
                    selectedSizeText.setText(Utilites.getSizeString(selectedSize));
                }
            }


            toggleSize();

            calculateDoubleCheese();

            updatePrice();



        }
    };


    private void calculateDoubleCheese() {


        switch (selectedSize) {
            case 0:
                cheesePriceAdd = DOUBLE_CHEESE_PRICE_SMALL;
                break;
            case 1:
                cheesePriceAdd = DOUBLE_CHEESE_PRICE_MEDIUM;
                break;
            case 2:
                cheesePriceAdd = DOUBLE_CHEESE_PRICE_LARGE;
                break;
        }

        String cheeseString = getResources().getString(R.string.double_cheese_with_price);

        cheeseString = cheeseString.replace("xx", String.valueOf(cheesePriceAdd));

        cheeseText.setText(cheeseString);
    }

    private void toggleSize() {

        if (isSizeSelectionOpen) {
            sizeSelectionIndicator.setImageResource(R.drawable.down_arrow);
            for (int i = 0; i < foodSize.size(); i++) {
                foodSize.get(i).getLayout().setVisibility(View.GONE);
            }
            isSizeSelectionOpen = false;
        } else {
            sizeSelectionIndicator.setImageResource(R.drawable.up_arrow);
            for (int i = 0; i < foodSize.size(); i++) {
                foodSize.get(i).getLayout().setVisibility(View.VISIBLE);

            }
            isSizeSelectionOpen = true;
        }
    }


    private class FoodSize {

        private LinearLayout layout;
        private int size;

        public FoodSize(LinearLayout layout, int size) {
            this.layout = layout;
            this.size = size;
        }

        public LinearLayout getLayout() {
            return layout;
        }

        public int getSize() {
            return size;
        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
