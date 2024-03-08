package com.example.ordermanager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Date creationDate;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private Item item;
    private long quantity;
    @ManyToOne(cascade = CascadeType.DETACH)//TODO FELIPE: CHECK THIS CASCADE HERE
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    private String status;

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static void mergeWithDTO(Order order, OrderDTO dto) {
        if (order != null && dto != null) {
            if (dto.getItemId() != null) {
                order.setItem(new Item(dto.getItemId(), null));
            }

            if (dto.getUserId() != null) {
                order.setUser(new User(dto.getUserId(), null, null));
            }

            if (dto.getQuantity() > 0) {
                order.setQuantity(dto.getQuantity());
            }
        }
    }

    public static Order fromDTO(OrderDTO dto) {
        if (dto != null) {
            Order order = new Order();
            mergeWithDTO(order, dto);
            return order;
        }

        return null;
    }

    public static OrderDTO toDTO(Order order) {
        if (order != null) {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setCreationDate(order.getCreationDate());
            if (order.getUser() != null) {
                dto.setUserId(order.getUser().getId());
            }
            if (order.getItem() != null) {
                dto.setItemId(order.getItem().getId());
            }
            dto.setQuantity(order.getQuantity());
            dto.setStatus(order.getStatus());
            return dto;
        }
        return null;
    }
}
