package az.nms.pizzamizzaforserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import az.nms.pizzamizzaforserver.storage.Order;
import az.nms.pizzamizzaforserver.storage.PizzaServerDatabase;

/**
 * Created by anar on 5/18/15.
 */
public class  PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private RecyclerView orderList;
    private List<Order> orders;
    private TextView noOrders ;

    private PizzaServerDatabase db;
    private  OrderListAdapter orderListAdapter;


    private OrderUpdateReceiver orderUpdateReceiver;

    public static PageFragment newInstance(int page) {

        PageFragment fragment = new PageFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(ARG_PAGE, page+"");

        fragment.setArguments(bdl);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        db = new PizzaServerDatabase(getActivity());
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);


        noOrders = (TextView) view.findViewById(R.id.no_orders);
        noOrders.setVisibility(View.GONE);

        orderList = (RecyclerView) view.findViewById(R.id.order_list_fragment);
        orderList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        orderList.setLayoutManager(llm);


        switch (mPage){
            case 5 : orders = db.getOrders(5,6); break;
            case 6 : orders = db.getOrders(-1); break;
            case 7 : orders = db.getAllOrders(); break;
            default: orders = db.getOrders(mPage);
        }

        orderListAdapter = new OrderListAdapter(getActivity(), orders);
        orderList.setAdapter(orderListAdapter);

        orderList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        Order order = orders.get(position);

                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra("ORDER_ID", order.getId());
                        intent.putExtra("CUSTOMER_NAME", order.getCustomer().getName());
                        intent.putExtra("CUSTOMER_ADDRESS", order.getCustomer().getAddress());
                        intent.putExtra("CUSTOMER_PHONE", order.getCustomer().getPhone());
                        intent.putExtra("ORDER_TYPE", order.getType());
                        intent.putExtra("ORDER_DATE", order.getDate());
                        intent.putExtra("ORDER_TOTAL", order.getTotalPrice());
                        intent.putExtra("ORDER_NOTES", order.getNotes());
                        intent.putExtra("ORDER_REGID", order.getRegid());
                        intent.putExtra("ORDER_STATUS", order.getStatus());
                        intent.putExtra("ORDER_LANG", order.getLang());

                        intent.putExtra("current_position",mPage);
                        startActivity(intent);

                        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        getActivity().finish();


                    }
                })
        );

        if(orders.isEmpty() || orders == null){
            noOrders.setVisibility(View.VISIBLE);
        }else
            noOrders.setVisibility(View.GONE);



        orderListAdapter.notifyDataSetChanged();

        orderUpdateReceiver = new OrderUpdateReceiver();

        //register for updates
        getActivity().registerReceiver(orderUpdateReceiver, new IntentFilter("ORDER_UPDATES"));

        return view;
    }


    class OrderUpdateReceiver extends BroadcastReceiver
    {
        /**
         * When new updates are available, a broadcast is received here
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            //delete db rows

            switch (mPage){
                case 5 : orders = db.getOrders(5,6); break;
                case 6 : orders = db.getOrders(-1); break;
                case 7 : orders = db.getAllOrders(); break;
                default: orders = db.getOrders(mPage);
            }

            orderListAdapter = new OrderListAdapter(getActivity(), orders);
            orderList.setAdapter(orderListAdapter);
            orderListAdapter.notifyDataSetChanged();

            if(orders.isEmpty() || orders == null){
                noOrders.setVisibility(View.VISIBLE);
            }else
                noOrders.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(orderUpdateReceiver);
    }
}