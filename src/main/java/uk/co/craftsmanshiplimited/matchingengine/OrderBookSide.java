package uk.co.craftsmanshiplimited.matchingengine;

import uk.co.craftsmanshiplimited.matchingengine.order.Order;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Deque;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.LinkedList;


/**
 * Created by Henrik on 08/04/2017.
 */
class OrderBookSide {

    private NavigableMap<BigDecimal, Deque<Order>> priceToOrderQueue;

    OrderBookSide(final Comparator<BigDecimal> comparator) {
        this.priceToOrderQueue = new TreeMap<>(comparator);
    }

    final Map.Entry<BigDecimal, Deque<Order>> getBestOrder() {
        return this.priceToOrderQueue.firstEntry();
    }

    final boolean isMatchingPrice(final BigDecimal orderPrice) {
        //I know we discussed to use priceToOrderQueue.headMap here.
        //But I think its actually even simpler to just use the comparator
        // we passed into the OrderBook itself.
        //That way we don't have to create a new Map
        return this.priceToOrderQueue.comparator()
                .compare(this.peekBest().getPrice(), orderPrice) <= 0;

    }

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
