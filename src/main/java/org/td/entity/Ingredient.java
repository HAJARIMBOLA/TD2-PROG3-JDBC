package org.td.entity;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Ingredient {

    private Integer id;
    private String name;
    private CategoryEnum category;
    private Double price;
    private List<StockMovement> stockMovementList;

    public Ingredient() {
    }

    public Ingredient(Integer id) {
        this.id = id;
    }

    public Ingredient(Integer id, String name, CategoryEnum category, Double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    /* GETTERS / SETTERS */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<StockMovement> getStockMovementList() {
        return stockMovementList;
    }

    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList;
    }

    /* MÉTHODE MÉTIER */

    public StockValue getStockValueAt(Instant time) {

        if (stockMovementList == null || stockMovementList.isEmpty()) {
            return new StockValue(0.0, UnitType.KG);
        }

        double quantity = 0.0;
        UnitType unit = stockMovementList.get(0).getValue().getUnit();

        for (StockMovement sm : stockMovementList) {
            if (sm.getCreationDatetime().isBefore(time)
                    || sm.getCreationDatetime().equals(time)) {

                if (sm.getType() == MovementTypeEnum.IN) {
                    quantity += sm.getValue().getQuantity();
                } else {
                    quantity -= sm.getValue().getQuantity();
                }
            }
        }

        return new StockValue(quantity, unit);
    }

    /* EQUALS / HASHCODE */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;

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
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                '}';
    }
}
