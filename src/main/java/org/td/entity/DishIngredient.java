package org.td.entity;

import java.util.Objects;

public class DishIngredient {

    private Integer id;
    private Dish dish;
    private Ingredient ingredient;
    private double quantityRequired;
    private UnitType unit;

    // ✅ CONSTRUCTEUR VIDE (OBLIGATOIRE)
    public DishIngredient() {
    }

    public DishIngredient(Integer id, Dish dish, Ingredient ingredient,
                          double quantityRequired, UnitType unit) {
        this.id = id;
        this.dish = dish;
        this.ingredient = ingredient;
        this.quantityRequired = quantityRequired;
        this.unit = unit;
    }

    // Constructeur sans id (insertion DB)
    public DishIngredient(Dish dish, Ingredient ingredient,
                          double quantityRequired, UnitType unit) {
        this(null, dish, ingredient, quantityRequired, unit);
    }

    /* GETTERS */

    public Integer getId() {
        return id;
    }

    public Dish getDish() {
        return dish;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantityRequired() {
        return quantityRequired;
    }

    public UnitType getUnit() {
        return unit;
    }

    /* SETTERS */

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantityRequired(double quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    /* UTILS */

    public Integer getDishId() {
        return dish != null ? dish.getId() : null;
    }

    public Integer getIngredientId() {
        return ingredient != null ? ingredient.getId() : null;
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", dishId=" + getDishId() +
                ", ingredientId=" + getIngredientId() +
                ", quantityRequired=" + quantityRequired +
                ", unit=" + unit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DishIngredient)) return false;
        DishIngredient that = (DishIngredient) o;

        if (this.id == null || that.id == null) {
            return false;
        }

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
