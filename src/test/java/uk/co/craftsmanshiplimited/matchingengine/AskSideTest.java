package uk.co.craftsmanshiplimited.matchingengine;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by Henrik on 08/04/2017.
 */
public class AskSideTest {

    //Object under test
    private OrderSide side = new AskSide();

    @Test
    public void shouldReturnHighestBuyOrderAsBestOrder() throws Exception {
        this.side.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(3), 100));
        this.side.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(2), 100));
        this.side.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(1), 100));

        assertEquals(BigDecimal.valueOf(3), this.side.getBestOrder().getValue().getFirst().getPrice());
    }
}
