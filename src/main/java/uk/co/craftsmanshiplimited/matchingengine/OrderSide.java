package uk.co.craftsmanshiplimited.matchingengine;

import uk.co.craftsmanshiplimited.matchingengine.order.Order;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.LinkedList;


/**
 * Created by Henrik on 08/04/2017.
 */
abstract class OrderSide {

    private NavigableMap<BigDecimal, Deque<Order>> priceToOrderQueue;

    public OrderSide() {
        this.priceToOrderQueue = new TreeMap<>();
    }

    abstract Map.Entry<BigDecimal, Deque<Order>> getBestOrder();

    final void add(final Order newOrder) {
        Deque<Order> orderQueue =
                this.priceToOrderQueue.get(newOrder.getPrice());
        if (orderQueue == null) {
            orderQueue = new LinkedList<>();
            this.priceToOrderQueue.put(newOrder.getPrice(), orderQueue);
        }
        orderQueue.addLast(newOrder);
    }

    final Order peekBest() {
        final Map.Entry<BigDecimal, Deque<Order>> bestOrder =
                this.getBestOrder();

        if (bestOrder != null && bestOrder.getValue().peekFirst() != null) {
            return bestOrder.getValue().peekFirst();
        } else {
            return null;
        }
    }

    final Order pollBest() {
        final Map.Entry<BigDecimal, Deque<Order>> bestOrder =
                this.getBestOrder();

        if (bestOrder != null) {
            Order first = bestOrder.getValue().pollFirst();
            if (first != null) {
                if (bestOrder.getValue().isEmpty()) {
                    this.priceToOrderQueue.remove(bestOrder.getKey());
                }
                return first;
            }
        }
        return null;
    }

    final NavigableMap<BigDecimal, Deque<Order>> getPriceToOrderQueue() {
        return priceToOrderQueue;
    }
}
