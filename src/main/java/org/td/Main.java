package org.td;

import org.td.service.DataRetriever;
import org.td.entity.CategoryEnum;
import org.td.entity.Dish;
import org.td.entity.DishTypeEnum;
import org.td.entity.Ingredient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {

        /* comment out every test that would throw error before running all test to show all normal result */
        DataRetriever dr = new DataRetriever();

Dish dish = dr.findDishById(2);
        System.out.println(dish.getGrossMargin());

    }
}
