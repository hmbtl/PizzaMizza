package az.nms.pizzamizzaforserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizzaforserver.storage.PizzaServerDatabase;

/**
 * Created by anar on 5/18/15.
 */
public class OrderListActivity extends FragmentActivity {

    ViewPager viewPager;
    private OrderUpdateReceiver orderUpdateReceiver;

    private List<Section> section = new LinkedList<>();

    private PizzaServerDatabase db;
    private ImageView navdrawer;

    private SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_layout);


        db = new PizzaServerDatabase(this);

        navdrawer = (ImageView) findViewById(R.id.navdrawer);

        section.add(new Section(((TextView) findViewById(R.id.new_click)), ((ImageView) findViewById(R.id.new_indicator)), ((TextView) findViewById(R.id.new_count))));
        section.add(new Section(((TextView) findViewById(R.id.accepted_click)), ((ImageView) findViewById(R.id.accepted_indicator)), ((TextView) findViewById(R.id.accepted_count))));
        section.add(new Section(((TextView) findViewById(R.id.making_click)), ((ImageView) findViewById(R.id.making_indicator)), ((TextView) findViewById(R.id.making_count))));
        section.add(new Section(((TextView) findViewById(R.id.ready_click)), ((ImageView) findViewById(R.id.ready_indicator)), ((TextView) findViewById(R.id.ready_count))));
        section.add(new Section(((TextView) findViewById(R.id.ontheway_click)), ((ImageView) findViewById(R.id.ontheway_indicator)), ((TextView) findViewById(R.id.ontheway_count))));
        section.add(new Section(((TextView) findViewById(R.id.finished_click)), ((ImageView) findViewById(R.id.finished_indicator)), ((TextView) findViewById(R.id.finished_count))));
        section.add(new Section(((TextView) findViewById(R.id.declined_click)), ((ImageView) findViewById(R.id.declined_indicator)), ((TextView) findViewById(R.id.declined_count))));
        section.add(new Section(((TextView) findViewById(R.id.all_click)), ((ImageView) findViewById(R.id.all_indicator)), ((TextView) findViewById(R.id.all_count))));


        for (int i = 0; i < section.size(); i++) {
            section.get(i).getTitle().setOnClickListener(onClick);
        }

        navdrawer.setOnClickListener(onClick);


        setCountSections();


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), section));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.d(
                        "position", "" + position
                );


                for (int i = 0; i < section.size(); i++) {
                    if (position != i) {
                        section.get(i).getTitle().setTextColor(Color.parseColor("#ff898989"));
                        section.get(i).getIndicator().setVisibility(View.GONE);
                        section.get(i).getCount().setVisibility(View.VISIBLE);
                    } else {
                        section.get(i).getTitle().setTextColor(Color.parseColor("#ffffff"));
                        section.get(i).getIndicator().setVisibility(View.VISIBLE);
                        section.get(i).getCount().setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        // Give the PagerSlidingTabStrip the ViewPager
//         tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//        // Attach the view pager to the tab strip
//        tabsStrip.setViewPager(viewPager);
//
//        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            // This method will be invoked when a new page becomes selected.
//            @Override
//            public void onPageSelected(int position) {
//
////                String color="#de1623";
//////
//////                switch (position){
//////                    case 0: color = "#de1623"; break;
//////                    case 1: color = "#FE9A2E"; break;
//////                    case 2: color = "#0B0B61"; break;
//////                    case 3: color = "#610B21"; break;
//////                    case 4: color = "#38610B"; break;
//////                    case 5: color = "#151515"; break;
//////                    default: color = "#de1623";
//////
//////
//////                }
////                orderBackground.setBackgroundColor(Color.parseColor(color));
////                tabsStrip.setBackgroundColor(Color.parseColor(color));
//
//
//            }
//
//            // This method will be invoked when the current page is scrolled
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                // Code goes here
//            }
//
//            // Called when the scroll state changes:
//            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                // Code goes here
//            }
//        });

        orderUpdateReceiver = new OrderUpdateReceiver();

        //register for updates
        registerReceiver(orderUpdateReceiver, new IntentFilter("ORDER_UPDATES"));

        viewPager.setCurrentItem(getIntent().getIntExtra("current_position", 0));


        //start the service for updates now
        this.getApplicationContext().startService(new Intent(this.getApplicationContext(), OrderListenerService.class));

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow_right);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_back);

    }

    class OrderUpdateReceiver extends BroadcastReceiver {
        /**
         * When new updates are available, a broadcast is received here
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            //delete db rows

            viewPager.setCurrentItem(0);

            setCountSections();
        }
    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            for (int i = 0; i < section.size(); i++) {
                if (v == section.get(i).getTitle())
                    viewPager.setCurrentItem(i);
            }

            if(v == navdrawer)
                menu.toggle();

        }
    };

    private void setCountSections() {

        for (int i = 0; i < section.size(); i++) {

            int count = 0;

            if (i == 5)
                count = db.orderCount(5, 6);
            else if (i == 6)
                count = db.orderCount(-1);
            else if (i == 7)
                count = db.orderCount();
            else
                count = db.orderCount(i);

            if (count == 0)
                section.get(i).getCount().setVisibility(View.GONE);
            else {
                section.get(i).getCount().setVisibility(View.VISIBLE);
                section.get(i).getCount().setText(count + "");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(orderUpdateReceiver);
    }

    public class Section {

        private TextView title, count;
        private ImageView indicator;

        public Section(TextView title, ImageView indicator, TextView count) {
            this.title = title;
            this.indicator = indicator;
            this.count = count;

        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getIndicator() {
            return indicator;
        }

        public TextView getCount() {
            return count;
        }
    }

}
