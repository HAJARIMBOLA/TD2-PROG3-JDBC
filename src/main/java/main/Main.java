
package main;

import dao.DishDao;
import entity.Dish;

public class Main {
    public static void main(String[] args) {
        DishDao dao = new DishDao();
        Dish dish = dao.findDishById(1);

        System.out.println("Plat : " + dish.getName());
        System.out.println("Coût : " + dish.getDishCost());
        try {
            System.out.println("Marge : " + dish.getGrossMargin());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
