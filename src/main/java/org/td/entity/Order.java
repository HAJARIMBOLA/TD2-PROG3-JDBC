package org.td.entity;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Order {

    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private List<DishOrder> dishOrders;

    public Order() {
    }

    public Order(Integer id, String reference, Instant creationDatetime, List<DishOrder> dishOrders) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
        this.dishOrders = dishOrders;
    }

    /* GETTERS / SETTERS */

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

    /* MÉTHODES MÉTIER */

    public Double getTotalAmountWithoutVAT() {

        if (dishOrders == null || dishOrders.isEmpty()) {
            return 0.0;
        }

        double totalAmount = 0.0;

        for (DishOrder dishOrder : dishOrders) {

            if (dishOrder == null || dishOrder.getDish() == null
                    || dishOrder.getDish().getPrice() == null) {
                continue;
            }

            totalAmount += dishOrder.getDish().getPrice()
                    * dishOrder.getQuantity();
        }

        return totalAmount;
    }

    public Double getTotalAmountWithVAT() {

        double amountHT = getTotalAmountWithoutVAT();
        return amountHT + (amountHT * 0.2);
    }

    /* EQUALS / HASHCODE */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;

        if (this.id == null || order.id == null) {
            return false;
        }

        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /* TO STRING */

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", dishOrders=" + dishOrders +
                '}';
    }
}
