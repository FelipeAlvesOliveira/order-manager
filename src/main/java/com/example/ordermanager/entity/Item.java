package com.example.ordermanager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.List;

@Entity
@Table(name="items")
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;

    @Transient
    @OneToMany(mappedBy = "item")
    private List<StockMovement> stockMovements;

    @Transient
    @OneToMany(mappedBy = "item")
    private List<Order> orders;

    public Item() {}

    public Item(String name) {
        this(null, name);
    }

    public Item(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StockMovement> getStockMovements() {
        return stockMovements;
    }

    public void setStockMovements(List<StockMovement> stockMovements) {
        this.stockMovements = stockMovements;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static ItemDTO toDTO(Item item) {
        if (item != null) {
            ItemDTO dto = new ItemDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            return dto;
        }
        return null;
    }

    public static Item fromDTO(ItemDTO dto) {
        if (dto != null) {
            return new Item(dto.getId(), dto.getName());
        }
        return null;
    }
}
