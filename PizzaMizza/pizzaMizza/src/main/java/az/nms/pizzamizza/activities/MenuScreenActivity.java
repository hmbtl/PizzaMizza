package az.nms.pizzamizza.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.adapters.MenuItemAdapter;
import az.nms.pizzamizza.models.MenuItems;
import az.nms.pizzamizza.tools.Utilites;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuScreenActivity extends ActionBarActivity {

    private MenuItemAdapter menu_adapter;
    private GridView menu_grid;
    private ImageButton home_button;
    private MenuItems data[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // TODO Auto-generated method stub
        setContentView(R.layout.menu_layout);

        // Set action bar title
        Utilites.initAppBarFont(this);
        Utilites.hideButton(this, Utilites.ACTION_BAR_EDIT);
        menu_grid = (GridView) findViewById(R.id.menu_grid);
        home_button = (ImageButton) this.findViewById(R.id.home_button);

        data = new MenuItems[]{
                new MenuItems(R.drawable.pizza, R.string.pizza),
                new MenuItems(R.drawable.salad, R.string.salad),
                new MenuItems(R.drawable.pastas, R.string.pasta),
                new MenuItems(R.drawable.desserts, R.string.dessert),
                new MenuItems(R.drawable.drinks, R.string.drink),
                new MenuItems(R.drawable.sauce, R.string.sauce),
                new MenuItems(R.drawable.panini, R.string.pannini),
                new MenuItems(R.drawable.wrap, R.string.wrap),
                new MenuItems(R.drawable.soups, R.string.soup),
        };
        menu_adapter = new MenuItemAdapter(this, R.layout.item_layout_adapter,
                data);

        menu_grid.setAdapter(menu_adapter);

        home_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goMain();

            }
        });


        menu_grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String type = null;

                switch (position) {
                    case 0:
                        type = "pizza";
                        break;

                    case 1:
                        type = "salad";
                        break;
                    case 2:
                        type = "pasta";
                        break;
                    case 3:
                        type = "cake";
                        break;
                    case 4:
                        type = "drink";
                        break;
                    case 5:
                        type = "sauce";
                        break;
                    case 6:
                        type = "panini";
                        break;
                    case 7:
                        type = "wrap";
                        break;
                    case 8:
                        type = "soup";
                        break;

                }

                Intent i = new Intent(MenuScreenActivity.this, FoodListActivity.class);
                i.putExtra("food", type);
                startActivity(i);

            }
        });

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent i = new Intent(MenuScreenActivity.this, HomeScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        finish();
        startActivity(i);

    }

    public void goMain() {

        Intent i = new Intent(MenuScreenActivity.this, HomeScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        finish();
        startActivity(i);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
