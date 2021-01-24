package az.nms.pizzamizza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.adapters.FoodListAdapter;
import az.nms.pizzamizza.models.Food;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends Activity {

	private EditText search_text;
	private ListView search_list;
	private FoodListAdapter adapter;
	private List<Food> food;
    private ImageButton home;
	private PizzaMizzaDatabase db ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);

		db = new PizzaMizzaDatabase(this);

		search_text = (EditText) findViewById(R.id.search_text);
		search_list = (ListView) findViewById(R.id.search_list);
        home = (ImageButton) findViewById(R.id.home_button);

		Utilites.initAppBarFont(this);

		Utilites.hideButton(this, Utilites.ACTION_BAR_EDIT);
		
		food = db.findFood(search_text.getText().toString());

		adapter = new FoodListAdapter(this, R.layout.pizza_layout_adapter, food, FoodListAdapter.SEARCH);

		search_text.addTextChangedListener(new addListenerOnTextChange(this,
				search_text));
		
		search_list.setAdapter(adapter);
		
		search_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(SearchActivity.this,
						FoodDetailActivity.class);

				i.putExtra("id", food.get(position ).getId());
				i.putExtra("activity", "food");
                i.putExtra("launchedfromsearch",true);

				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

	}

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SearchActivity.this, HomeScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        finish();
        startActivity(i);
    }

    public class addListenerOnTextChange implements TextWatcher {
		private Context mContext;
		EditText mEdittext;

		public addListenerOnTextChange(Context context, EditText edittext) {
			super();
			this.mContext = context;
			this.mEdittext = edittext;
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// What you want to do\
            food = db.findFood(search_text.getText().toString());
			adapter.setFood(food);
			adapter.notifyDataSetChanged();
			//
		}
	}
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
}
