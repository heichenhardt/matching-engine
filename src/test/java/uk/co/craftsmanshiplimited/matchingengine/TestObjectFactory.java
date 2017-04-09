package uk.co.craftsmanshiplimited.matchingengine;

import uk.co.craftsmanshiplimited.matchingengine.order.Order;
import uk.co.craftsmanshiplimited.matchingengine.order.OrderOrigin;
import uk.co.craftsmanshiplimited.matchingengine.order.OrderSideType;
import uk.co.craftsmanshiplimited.matchingengine.order.OrderType;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Created by Henrik on 08/04/2017.
 */
public class TestObjectFactory {

    public static Order createMarketAskOrder(int quantity) {
        return new Order(createRandomInt(), null, quantity, Instant.now(), OrderType.MARKET, OrderSideType.ASK, OrderOrigin.CLIENT);
    }

    public static Order createMarketBidOrder(int quantity) {
        return new Order(createRandomInt(), null, quantity, Instant.now(), OrderType.MARKET, OrderSideType.BID, OrderOrigin.CLIENT);
    }

    public static Order createLimitAskOrder(BigDecimal price, int quantity) {
        return new Order(createRandomInt(), price, quantity, Instant.now(), OrderType.LIMIT, OrderSideType.ASK, OrderOrigin.CLIENT);
    }

    public static Order createLimitBidOrder(BigDecimal price, int quantity) {
        return new Order(createRandomInt(), price, quantity, Instant.now(), OrderType.LIMIT, OrderSideType.BID, OrderOrigin.CLIENT);
    }

    public static Order createLPLimitAskOrder(BigDecimal price, int quantity) {
        return new Order(createRandomInt(), price, quantity, Instant.now(), OrderType.LIMIT, OrderSideType.ASK, OrderOrigin.LP);
    }

    public static Order createLPLimitBidOrder(BigDecimal price, int quantity) {
        return new Order(createRandomInt(), price, quantity, Instant.now(), OrderType.LIMIT, OrderSideType.BID, OrderOrigin.LP);
    }


    private static int createRandomInt() {
        return (int)Math.round(Math.random()*100000);
    }



}
