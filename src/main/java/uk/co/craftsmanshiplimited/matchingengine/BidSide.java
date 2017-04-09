package uk.co.craftsmanshiplimited.matchingengine;

import uk.co.craftsmanshiplimited.matchingengine.order.Order;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.Map;

/**
 * Created by Henrik on 08/04/2017.
 */
class BidSide extends OrderSide {

    @Override
    final Map.Entry<BigDecimal, Deque<Order>> getBestOrder() {
        final Map.Entry<BigDecimal, Deque<Order>> orderEntry =
                super.getPriceToOrderQueue().firstEntry();
        if (orderEntry != null) {
            return orderEntry;
        }
        return null;
    }

}
