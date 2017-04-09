package uk.co.craftsmanshiplimited.matchingengine;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by Henrik on 08/04/2017.
 */
public class BidSideTest {

    //Object under test
    private OrderSide side = new BidSide();

    @Test
    public void shouldReturnLowestOrderAsBestOrder() throws Exception {
        this.side.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(3), 100));
        this.side.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(2), 100));
        this.side.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(1), 100));

        assertEquals(BigDecimal.valueOf(1), this.side.getBestOrder().getValue().getFirst().getPrice());
    }
}
