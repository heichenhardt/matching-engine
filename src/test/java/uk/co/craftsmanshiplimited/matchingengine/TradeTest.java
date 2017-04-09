package uk.co.craftsmanshiplimited.matchingengine;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * Created by Henrik on 09/04/2017.
 */
public class TradeTest {

    private static final int BID_SUBMITTER_ID = 1;
    private static final int ASK_SUBMITTER_ID = 2;
    private BigDecimal PRICE = BigDecimal.ONE;
    private int QUANTITY = 1000;
    private Instant TIMESTAMP = Instant.now();

    private Trade trade = new Trade(BID_SUBMITTER_ID, ASK_SUBMITTER_ID, PRICE, QUANTITY, TIMESTAMP);

    @Test
    public void testGetSet() throws Exception {
        assertEquals(BID_SUBMITTER_ID, trade.getBidSubmitterId());
        assertEquals(ASK_SUBMITTER_ID, trade.getAskSubmitterId());
        assertEquals(PRICE, trade.getPrice());
        assertEquals(QUANTITY, trade.getQuantity());
        assertEquals(TIMESTAMP, trade.getCreatedAt());
    }
}
