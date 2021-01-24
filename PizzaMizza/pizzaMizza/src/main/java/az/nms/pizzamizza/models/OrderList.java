package az.nms.pizzamizza.models;

import java.util.LinkedList;
import java.util.List;

public class OrderList {

    public static List<OrderItems> orders = new LinkedList<OrderItems>();

    public static List<OrderItems> getOrders() {
        return orders;
    }

    public static void setOrders(List<OrderItems> orders) {
        OrderList.orders = orders;
    }

    public static void setOrder(int position, OrderItems order) {

        int index = -1;
        for (int i = 0; i < OrderList.orders.size(); i++) {
            if (OrderList.orders.get(i).equals(order)) {
                index = i;

            }
        }
        if (index == -1)
            OrderList.orders.set(position, order);
        else {
            OrderList.removeOrder(position);
            OrderList.updateOrder(index, order);
        }

    }

    public static void updateOrder(int position, OrderItems order) {
//		OrderList.orders.set(
//				position,
//				new OrderItems(order.getFood(), order.getOrderSize(), order
//						.getQuantity()
//						+ OrderList.orders.get(position).getQuantity(),
//						OrderList.orders.get(position).getTotalPrice()
//								+ order.getTotalPrice()
//
//				));
//
        /* update order quantity */
        OrderList.orders.get(position).setQuantity(order
                .getQuantity()
                + OrderList.orders.get(position).getQuantity());
        /* update order price */
        OrderList.orders.get(position).setTotalPrice(OrderList.orders.get(position).getTotalPrice()
                + order.getTotalPrice());
    }

    public static void addOrder(OrderItems order) {
        int index = -1;
        for (int i = 0; i < OrderList.orders.size(); i++) {
            if (OrderList.orders.get(i).equals(order)) {
                index = i;

            }
        }
        if (index == -1)
            OrderList.orders.add(order);
        else
            OrderList.updateOrder(index, order);
    }

    public static void removeOrder(int position) {
        OrderList.orders.remove(position);
    }


}
