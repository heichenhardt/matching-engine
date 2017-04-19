package uk.co.craftsmanshiplimited.matchingengine;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

/**
 * Created by Henrik on 19/04/2017.
 */
public class OrderBookSideTest {
    @Test
    public void shouldReturnHighestBuyOrderAsBestOrderForAskSide() throws Exception {
        final OrderBookSide askSide = new OrderBookSide(Comparator.naturalOrder());
        askSide.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(3), 100));
        askSide.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(2), 100));
        askSide.add(TestObjectFactory.createLimitAskOrder(BigDecimal.valueOf(1), 100));

        assertEquals(BigDecimal.valueOf(1), askSide.getBestOrder().getValue().getFirst().getPrice());
    }

    @Test
    public void shouldReturnLowestOrderAsBestOrderForBidSide() throws Exception {
        final OrderBookSide bidSide = new OrderBookSide((Comparator<BigDecimal>) Comparator.naturalOrder().reversed());
        bidSide.add(TestObjectFactory.createLimitBidOrder(BigDecimal.valueOf(3), 100));
        bidSide.add(TestObjectFactory.createLimitBidOrder(BigDecimal.valueOf(2), 100));
        bidSide.add(TestObjectFactory.createLimitBidOrder(BigDecimal.valueOf(1), 100));

        assertEquals(BigDecimal.valueOf(3), bidSide.getBestOrder().getValue().getFirst().getPrice());
    }
}
