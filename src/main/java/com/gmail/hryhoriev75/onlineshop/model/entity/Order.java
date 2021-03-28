package com.gmail.hryhoriev75.onlineshop.model.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Order extends Entity {

    private Long userId;
    private Instant createTime;
    private Status status;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        CREATED, PAID, CANCELED;

        public boolean isCreated() {
            return this == CREATED;
        }
        public boolean isPaid() {
            return this == PAID;
        }
        public boolean isCanceled() {
            return this == CANCELED;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public String getStatus() {
            return name().toLowerCase();
        }
    }

    public static class Record {

        private Product product;
        private int quantity;
        private BigDecimal price;

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

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
