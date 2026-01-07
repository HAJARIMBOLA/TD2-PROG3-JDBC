
package entity;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum type;
    private Double dishCost;
    private Double dishPrice;

    public Double getDishCost() {
        return dishCost;
    }

    public Double getDishPrice() {
        return dishPrice;
    }

    public Double getGrossMargin() {
        if (dishPrice == null) {
            throw new RuntimeException(
                "Prix de vente non défini pour le plat : " + name
            );
        }
        return dishPrice - dishCost;
    }

    public void setDishCost(Double dishCost) { this.dishCost = dishCost; }
    public void setDishPrice(Double dishPrice) { this.dishPrice = dishPrice; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DishTypeEnum getType() { return type; }
    public void setType(DishTypeEnum type) { this.type = type; }
}
