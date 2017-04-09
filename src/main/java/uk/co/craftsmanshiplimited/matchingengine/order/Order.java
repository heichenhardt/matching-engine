package uk.co.craftsmanshiplimited.matchingengine.order;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Created by Henrik on 08/04/2017.
 */
public class Order {

    private int id;
    private int submitterId;
    private BigDecimal price;
    private int quantity;
    private int tradedQuantity;
    private Instant createdAt;
    private OrderType type;
    private OrderSideType side;
    private OrderStatus status;
    private OrderOrigin origin;

    public Order(
            final int id, final BigDecimal price, final int quantity,
            final Instant createdAt, final OrderType type,
            final OrderSideType side, final OrderOrigin origin) {

        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.type = type;
        this.side = side;
        this.origin = origin;
        this.status = OrderStatus.OPEN;
    }

    public final int getId() {
        return id;
    }

    public final BigDecimal getPrice() {
        return price;
    }

    public final int getQuantity() {
        return quantity;
    }

    public final boolean isBid() {
        return this.side == OrderSideType.BID;
    }

    public final boolean isAsk() {
        return this.side == OrderSideType.ASK;
    }

    public final OrderStatus getStatus() {
        return status;
    }

    public final int getSubmitterId() {
        return submitterId;
    }

    public final void fill(final int quantity) {
        if (quantity > this.quantity) {
            throw new IllegalStateException("Quantity to fill ("
                    + quantity
                    + ") is greater than order quantity ("
                    + this.quantity
                    + ")");
        }
        this.quantity -= quantity;
        this.tradedQuantity += quantity;
        if (isFilled()) {
            this.status = OrderStatus.FILLED;
        } else {
            this.status = OrderStatus.PARTIAL;
        }
    }

    public final boolean isFilled() {
        return this.quantity == 0;
    }

    public final boolean isMarketOrder() {
        return OrderType.MARKET == this.type;
    }

    public final void cancel() {
        this.status = OrderStatus.CANCEL;
    }

    public final boolean isLP() {
        return this.origin == OrderOrigin.LP;
    }

    public final int getTradedQuantity() {
        return tradedQuantity;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }
}
