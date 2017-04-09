package uk.co.craftsmanshiplimited.matchingengine;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Created by Henrik on 08/04/2017.
 */
public class Trade {
    private final int bidSubmitterId;
    private final int askSubmitterId;

    private final BigDecimal price;
    private final int quantity;
    private final Instant createdAt;

    public Trade(
            final int bidSubmitterId, final int askSubmitterId,
            final BigDecimal price, final int quantity,
            final Instant createdAt) {

        this.bidSubmitterId = bidSubmitterId;
        this.askSubmitterId = askSubmitterId;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public final int getBidSubmitterId() {
        return bidSubmitterId;
    }

    public final int getAskSubmitterId() {
        return askSubmitterId;
    }

    public final BigDecimal getPrice() {
        return price;
    }

    public final int getQuantity() {
        return quantity;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }
}
