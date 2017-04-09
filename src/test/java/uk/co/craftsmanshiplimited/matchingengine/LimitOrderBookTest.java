package uk.co.craftsmanshiplimited.matchingengine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.co.craftsmanshiplimited.matchingengine.order.Order;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static uk.co.craftsmanshiplimited.matchingengine.TestObjectFactory.*;

/**
 * Created by Henrik on 08/04/2017.
 */
@RunWith(JUnit4.class)
public class LimitOrderBookTest {

    private static final BigDecimal LOWER_PRICE = BigDecimal.valueOf(10);
    private static final BigDecimal HIGHER_PRICE = BigDecimal.valueOf(11);
    private static final int QUANTITY = 1000;
    private static final int REMAINING_QUANTITY = 1;

    //Object under test
    private LimitOrderBook book = new LimitOrderBook();

    @Test
    public void shouldSubmitAskOrderToEmptyOrderBook() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));

        final Order bestAsk = this.book.peekBestAsk();
        assertEquals(bestAsk.getPrice(), LOWER_PRICE);
        assertEquals(bestAsk.getQuantity(), QUANTITY);
    }

    @Test
    public void shouldSubmitBidOrderToEmptyOrderBook() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));

        final Order bestBid = this.book.peekBestBid();
        assertEquals(bestBid.getPrice(), LOWER_PRICE);
        assertEquals(bestBid.getQuantity(), QUANTITY);
    }

    @Test
    public void shouldSubmitAskOrderToNonEmptyOrderBook() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitAskOrder(HIGHER_PRICE, QUANTITY));

        final Order bestAsk = this.book.peekBestAsk();
        assertEquals(HIGHER_PRICE, bestAsk.getPrice());
    }

    @Test
    public void shouldSubmitBidOrderToNonEmptyOrderBook() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitBidOrder(HIGHER_PRICE, QUANTITY));

        final Order bestBid = this.book.peekBestBid();
        assertEquals(LOWER_PRICE, bestBid.getPrice());
    }

    @Test
    public void shouldFollowFIFOForAskOrdersOfSamePrice() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY*2));

        final Order bestAsk = this.book.peekBestAsk();
        assertEquals(QUANTITY, bestAsk.getQuantity());
    }

    @Test
    public void shouldFollowFIFOForBidOrdersOfSamePrice() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY*2));

        final Order bestBid = this.book.peekBestBid();
        assertEquals(QUANTITY, bestBid.getQuantity());
    }

    @Test
    public void shouldMatchAskOrderFromBookWhenPriceAndQuantityMatch() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));

        assertNull(this.book.peekBestAsk());
        assertNull(this.book.peekBestBid());
        assertTrades(1, QUANTITY);
    }

    @Test
    public void shouldMatchBidOrderFromBookWhenPriceAndQuantityMatch() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));

        assertNull(this.book.peekBestAsk());
        assertNull(this.book.peekBestBid());
        assertTrades(1, QUANTITY);
    }

    @Test
    public void shouldMatchAskOrderFromBookWhenPriceMatchAndLeaveRemainingBid() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY+ REMAINING_QUANTITY));
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));

        assertNull(this.book.peekBestAsk());
        final Order bestBid = this.book.peekBestBid();
        assertNotNull(bestBid);
        assertEquals(REMAINING_QUANTITY, bestBid.getQuantity());
        assertNotNull(this.book.getLastTrade());
        assertTrades(1, QUANTITY);
    }

    @Test
    public void shouldMatchBidOrderFromBookWhenPriceMatchAndLeaveRemainingAsk() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY+ REMAINING_QUANTITY));
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));

        assertNull(this.book.peekBestBid());
        final Order bestAsk = this.book.peekBestAsk();
        assertNotNull(bestAsk);
        assertEquals(REMAINING_QUANTITY, bestAsk.getQuantity());
        assertTrades(1, QUANTITY);
    }

    @Test
    public void shouldMatchAskOrderFromBookWhenPriceMatchAndLeaveRemainingAsk() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY+ REMAINING_QUANTITY));

        assertNull(this.book.peekBestBid());
        final Order bestAsk = this.book.peekBestAsk();
        assertNotNull(bestAsk);
        assertEquals(REMAINING_QUANTITY, bestAsk.getQuantity());
        assertTrades(1, QUANTITY);
    }

    @Test
    public void shouldMatchBidOrderFromBookWhenPriceMatchAndLeaveRemainingBid() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY+ REMAINING_QUANTITY));

        assertNull(this.book.peekBestAsk());
        final Order bestBid = this.book.peekBestBid();
        assertNotNull(bestBid);
        assertEquals(REMAINING_QUANTITY, bestBid.getQuantity());
        assertTrades(1, QUANTITY);
    }

    @Test
    public void shouldMatchAskOrderFromBookWhenPriceMatchWithMultipleTrades() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitBidOrder(LOWER_PRICE, REMAINING_QUANTITY));
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY+ REMAINING_QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(2, QUANTITY+ REMAINING_QUANTITY);
    }

    @Test
    public void shouldMatchBidOrderFromBookWhenPriceMatchWithMultipleTrades() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitAskOrder(LOWER_PRICE, REMAINING_QUANTITY));
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY+ REMAINING_QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(2, QUANTITY+ REMAINING_QUANTITY);
    }

    @Test
    public void shouldNotMatchAskIfPriceIsTooLow() throws Exception {
        this.book.process(createLimitBidOrder(HIGHER_PRICE, QUANTITY));
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));

        assertNotNull(this.book.peekBestBid());
        assertNotNull(this.book.peekBestAsk());
        assertTrades(0, 0);
    }

    @Test
    public void shouldNotMatchBidIfPriceIsTooLow() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitBidOrder(HIGHER_PRICE, QUANTITY));

        assertNotNull(this.book.peekBestBid());
        assertNotNull(this.book.peekBestAsk());
        assertTrades(0, 0);
    }

    @Test
    public void shouldMatchAskIfPriceIsTooHigh() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitAskOrder(HIGHER_PRICE, QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(1, QUANTITY);
        assertEquals(LOWER_PRICE, this.book.getLastTrade().getPrice());
    }

    @Test
    public void shouldMatchBidIfPriceIsTooLow() throws Exception {
        this.book.process(createLimitAskOrder(HIGHER_PRICE, QUANTITY));
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(1, QUANTITY);
        assertEquals(HIGHER_PRICE, this.book.getLastTrade().getPrice());
    }

    @Test
    public void shouldMatchAnyMarketAskOrderIfThereExistsBid() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createMarketAskOrder(QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(1, QUANTITY);
        assertEquals(LOWER_PRICE, this.book.getLastTrade().getPrice());
    }

    @Test
    public void shouldMatchAnyMarketBidOrderIfThereExistsAsk() throws Exception {
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createMarketBidOrder(QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(1, QUANTITY);
        assertEquals(LOWER_PRICE, this.book.getLastTrade().getPrice());
    }

    @Test
    public void shouldNotSubmitMarketAskOrderIntoEmptyOrderBook() throws Exception {
        this.book.process(createMarketAskOrder(QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(0, 0);
    }

    @Test
    public void shouldNotSubmitMarketBidOrderIntoEmptyOrderBook() throws Exception {
        this.book.process(createMarketBidOrder(QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(0, 0);
    }

    @Test
    public void shouldMatchClientBidOrderWithLPAskOrder() throws Exception {
        this.book.process(createLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLPLimitAskOrder(LOWER_PRICE, QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(1, QUANTITY);
        assertEquals(LOWER_PRICE, this.book.getLastTrade().getPrice());
    }

    @Test
    public void shouldMatchLPBidOrderWithClientAskOrder() throws Exception {
        this.book.process(createLPLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLimitAskOrder(LOWER_PRICE, QUANTITY));

        assertNull(this.book.peekBestBid());
        assertNull(this.book.peekBestAsk());
        assertTrades(1, QUANTITY);
        assertEquals(LOWER_PRICE, this.book.getLastTrade().getPrice());
    }

    @Test
    public void shouldNotMatchLPBidOrderWithLPAskOrder() throws Exception {
        this.book.process(createLPLimitBidOrder(LOWER_PRICE, QUANTITY));
        this.book.process(createLPLimitAskOrder(LOWER_PRICE, QUANTITY));

        assertNotNull(this.book.peekBestBid());
        assertNotNull(this.book.peekBestAsk());
        assertTrades(0, 0);
    }

    private void assertTrades(int numberOfTrades, int volume) {
        assertEquals(volume, this.book.getTradeVolume());
        assertEquals(numberOfTrades, this.book.getNumberOfTrades());
    }
}
