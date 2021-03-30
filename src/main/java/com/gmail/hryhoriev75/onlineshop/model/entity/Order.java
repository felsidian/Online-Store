package com.gmail.hryhoriev75.onlineshop.model.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Order extends Entity {

    private long userId;
    private Date createTime;
    private Status status;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        CREATED, PAID, CANCELED, DONE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public String getStatus() {
            return toString();
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
