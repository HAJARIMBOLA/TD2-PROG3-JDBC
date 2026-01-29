package org.td.entity;

import java.util.List;
import java.util.Objects;

public class Dish {

    private Integer id;
    private Double price;
    private String name;
    private DishTypeEnum dishType;

    // Relation principale
    private List<DishIngredient> dishIngredientList;

    public Dish() {
    }

    public Dish(Integer id, String name, DishTypeEnum dishType,
                List<DishIngredient> dishIngredientList, Double price) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishIngredientList = dishIngredientList;
        this.price = price;
    }

    // ===== GETTERS / SETTERS =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<DishIngredient> getDishIngredientList() {
        return dishIngredientList;
    }

    public void setDishIngredientList(List<DishIngredient> dishIngredientList) {
        this.dishIngredientList = dishIngredientList;
    }

    // ===== MÉTHODES MÉTIER =====

    public Double getDishCost() {

        if (dishIngredientList == null || dishIngredientList.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;

        for (DishIngredient di : dishIngredientList) {

            if (di == null || di.getIngredient() == null) {
                continue;
            }

            Ingredient ingredient = di.getIngredient();

            if (ingredient.getPrice() == null) {
                continue;
            }

            total += ingredient.getPrice() * di.getQuantityRequired();
        }

        return total;
    }

    public Double getGrossMargin() {

        if (price == null) {
            return 0.0;
        }

        return price - getDishCost();
    }

    // ===== EQUALS / HASHCODE =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dish)) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ===== TO STRING =====

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", price=" + price +
                '}';
    }
}
