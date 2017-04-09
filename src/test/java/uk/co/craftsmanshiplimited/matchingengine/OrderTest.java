package uk.co.craftsmanshiplimited.matchingengine;

import org.junit.Test;
import uk.co.craftsmanshiplimited.matchingengine.order.Order;
import uk.co.craftsmanshiplimited.matchingengine.order.OrderStatus;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by Henrik on 09/04/2017.
 */
public class OrderTest {
    public static final int QUANTITY = 1000;
    //Object under test
    private Order order = TestObjectFactory.createLimitAskOrder(BigDecimal.ONE, QUANTITY);

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenFillingMoreThanQuantity() throws Exception {
        order.fill(QUANTITY+1);
    }

    @Test
    public void shouldSetStatusToFilledWhenReachedQuantity() throws Exception {
        order.fill(QUANTITY);
        assertEquals(OrderStatus.FILLED, order.getStatus());
    }

    @Test
    public void shouldSetStatusToPartialWhenNotReachedQuantity() throws Exception {
        order.fill(QUANTITY-1);
        assertEquals(OrderStatus.PARTIAL, order.getStatus());
    }

    @Test
    public void newOrderShouldHaveStatusOpen() throws Exception {
        assertEquals(OrderStatus.OPEN, order.getStatus());
    }

    @Test
    public void shouldHaveStatusCancelledOnceCancelled() throws Exception {
        order.cancel();
        assertEquals(OrderStatus.CANCEL, order.getStatus());
    }
}
