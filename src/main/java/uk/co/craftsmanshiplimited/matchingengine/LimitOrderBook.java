package uk.co.craftsmanshiplimited.matchingengine;

import uk.co.craftsmanshiplimited.matchingengine.order.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Henrik on 08/04/2017.
 */
public class LimitOrderBook {

    private OrderBookSide askSide;
    private OrderBookSide bidSide;
    private Deque<Trade> trades;

    public LimitOrderBook() {
        final Comparator<BigDecimal> comparator = Comparator.naturalOrder();
        this.askSide = new OrderBookSide(comparator);
        this.bidSide = new OrderBookSide(comparator.reversed());
        this.trades = new LinkedList<>();
    }

    public final void process(final Order newOrder) {
        if (newOrder.isAsk()) {
            processSide(newOrder, this.askSide, this.bidSide);
        } else if (newOrder.isBid()) {
            processSide(newOrder, this.bidSide, this.askSide);
        } else {
            throw new IllegalStateException(
                    "Malformed order received (neither ask nor bid)");
        }
    }

    public final Order peekBestBid() {
        return bidSide.peekBest();
    }

    public final Order peekBestAsk() {
        return askSide.peekBest();
    }

    public final Trade getLastTrade() {
        return trades.getLast();
    }

    public final int getNumberOfTrades() {
        return trades.size();
    }

    public final int getTradeVolume() {
        return trades.stream().mapToInt(trade -> trade.getQuantity()).sum();
    }

    private void processSide(
            final Order newOrder,
            final OrderBookSide selfSide,
            final OrderBookSide cpSide) {

        while (!newOrder.isFilled()
                && hasMoreEligibleCPOrders(newOrder, cpSide)) {

            final Order bestOrder = cpSide.peekBest();
            //Take filled orders off the order book.
            if (newOrder.getQuantity() >= bestOrder.getQuantity()) {
                cpSide.pollBest();
            }
            int tradeQuantity =
                    Math.min(newOrder.getQuantity(), bestOrder.getQuantity());
            bestOrder.fill(tradeQuantity);
            newOrder.fill(tradeQuantity);
            final Trade trade = createTrade(newOrder, bestOrder, tradeQuantity);

            this.trades.addLast(trade);
        }
        if (!newOrder.isFilled()) {
            if (!newOrder.isMarketOrder()) {
                selfSide.add(newOrder);
            } else {
                newOrder.cancel();
            }
        }
    }

    private Trade createTrade(
            final Order newOrder, final Order bestOrder,
            final int tradeQuantity) {

        return new Trade(
                bestOrder.getSubmitterId(),
                newOrder.getSubmitterId(),
                bestOrder.getPrice(),
                tradeQuantity,
                Instant.now());
    }

    private boolean hasMoreEligibleCPOrders(
            final Order newOrder, final OrderBookSide cpSide) {

        final Order bestOrder = cpSide.peekBest();

        return bestOrder != null
                && (newOrder.isMarketOrder()
                    || cpSide.isMatchingPrice(newOrder.getPrice()))
                && !(newOrder.isLP() && bestOrder.isLP());
    }
}
