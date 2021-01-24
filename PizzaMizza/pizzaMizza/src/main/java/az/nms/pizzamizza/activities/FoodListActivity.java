package az.nms.pizzamizza.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import java.util.List;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.adapters.FoodListAdapter;
import az.nms.pizzamizza.models.Food;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FoodListActivity extends AppCompatActivity {

    private StickyListHeadersListView pizza_list;
    private FoodListAdapter pizza_adapter;
    private ImageButton home_button;

    private String type;

    private int index;
    private PizzaMizzaDatabase db;
    private List<Food> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pizza_layout);

        // Set action bar title
        Utilites.initAppBarFont(this);

        // Get Intent
        type = getIntent().getStringExtra("food");

        initialiseLayout();

        //Initialize db
        db = new PizzaMizzaDatabase(this);

        // Hide edit button in action bar
        Utilites.hideButton(this, Utilites.ACTION_BAR_EDIT);

        //
        foods = db.getFoods(type);


        pizza_adapter = new FoodListAdapter(this,
                R.layout.pizza_layout_adapter, foods, FoodListAdapter.LIST);


        pizza_list.setAdapter(pizza_adapter);
        pizza_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(FoodListActivity.this,
                        FoodDetailActivity.class);

                i.putExtra("id", foods.get(position).getId());
//                i.putExtra("position",position);

                i.putExtra("activity", "food");
//                String transitionName = "foodname"+position;
//                View sharedView = view.findViewById(R.id.pizza_title);
//
//                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(FoodListActivity.this, sharedView, transitionName);
                startActivity(i);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        home_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goMain();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

//		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    public void goMain() {

        Intent i = new Intent(FoodListActivity.this, HomeScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(i);
    }

    private void initialiseLayout() {
        pizza_list = (StickyListHeadersListView) findViewById(R.id.pizza_list);
        home_button = (ImageButton) findViewById(R.id.home_button);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
