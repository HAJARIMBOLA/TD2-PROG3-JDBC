package org.td.entity;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Order {

    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private List<DishOrder> dishOrders;
    private TableOrder tableOrder;

    public Order() {
    }

    public Order(Integer id, String reference, Instant creationDatetime, List<DishOrder> dishOrders, TableOrder tableOrder) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
        this.dishOrders = dishOrders;
        this.tableOrder = tableOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public List<DishOrder> getDishOrders() {
        return dishOrders;
    }

    public void setDishOrders(List<DishOrder> dishOrders) {
        this.dishOrders = dishOrders;
    }

    public TableOrder getTableOrder() {
        return tableOrder;
    }

    public void setTableOrder(TableOrder tableOrder) {
        this.tableOrder = tableOrder;
    }

    public Double getTotalAmountWithoutVAT() {
        if (dishOrders == null || dishOrders.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (DishOrder dishOrder : dishOrders) {
            total += dishOrder.getDish().getPrice() * dishOrder.getQuantity();
        }
        return total;
    }

    public Double getTotalAmountWithVAT() {
        return getTotalAmountWithoutVAT() * 1.2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", dishOrders=" + dishOrders +
                ", tableOrder=" + tableOrder +
                '}';
    }
}
