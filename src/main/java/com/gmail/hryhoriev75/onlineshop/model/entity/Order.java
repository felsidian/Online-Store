package com.gmail.hryhoriev75.onlineshop.model.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Order extends Entity {

    private Instant createTime;

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public static class Record {

        private int quantity;
        private BigDecimal price;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

    }
}
