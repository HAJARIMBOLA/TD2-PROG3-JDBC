package org.td.entity;

import java.util.Objects;

public class DishOrder {

    private Integer id;
    private Dish dish;
    private int quantity;

    public DishOrder() {
    }

    public DishOrder(Integer id, Dish dish, int quantity) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
    }

    /* GETTERS / SETTERS */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /* EQUALS / HASHCODE */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DishOrder)) return false;
        DishOrder that = (DishOrder) o;

        if (this.id == null || that.id == null) {
            return false;
        }

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /* TO STRING */

    @Override
    public String toString() {
        return "DishOrder{" +
                "id=" + id +
                ", dishId=" + (dish != null ? dish.getId() : null) +
                ", quantity=" + quantity +
                '}';
    }
}
