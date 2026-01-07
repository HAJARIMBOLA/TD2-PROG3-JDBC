
package service;

import entity.Dish;
import entity.DishTypeEnum;

import java.sql.ResultSet;

public class DataRetriever {
    public static Dish map(ResultSet rs) {
        try {
            Dish dish = new Dish();
            dish.setName(rs.getString("name"));
            dish.setDishCost(rs.getDouble("cost"));
            dish.setDishPrice(rs.getObject("price", Double.class));
            dish.setType(DishTypeEnum.valueOf(rs.getString("type")));
            return dish;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
