package com.example.ordermanager.entity;

import java.util.Date;

public class StockMovementDTO {
    private Long id;
    private Date creationDate;
    private Long itemId;
    private long quantity;
    private String status;
    private long quantityAvailable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(long quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
}
