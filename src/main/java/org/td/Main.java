package org.td;

import org.td.entity.*;
import org.td.service.DataRetriever;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever dr = new DataRetriever();

        try {
            Table table1 = dr.findTableById(3);

            TableOrder tableOrder1 = new TableOrder();
            tableOrder1.setTable(table1);
            tableOrder1.setArrivalDatetime(Instant.now());
            tableOrder1.setDepartureDatetime(
                    Instant.now().plus(2, ChronoUnit.HOURS)
            );

            Dish dish = dr.findDishById(1);
            DishOrder dishOrder = new DishOrder();
            dishOrder.setDish(dish);
            dishOrder.setQuantity(1);

            Order order1 = new Order();
            order1.setReference("CMD-001");
            order1.setCreationDatetime(Instant.now());
            order1.setDishOrders(List.of(dishOrder));
            order1.setTableOrder(tableOrder1);

            Order saved = dr.saveOrder(order1);
            System.out.println("Commande créée : " + saved);

        } catch (Exception e) {
            throw new RuntimeException("Test 1 échoué : " + e.getMessage());
        }


        try {
            Table table1 = dr.findTableById(1);

            TableOrder tableOrder2 = new TableOrder();
            tableOrder2.setTable(table1);
            tableOrder2.setArrivalDatetime(Instant.now());
            tableOrder2.setDepartureDatetime(
                    Instant.now().plus(1, ChronoUnit.HOURS)
            );

            Dish dish = dr.findDishById(1);
            DishOrder dishOrder = new DishOrder();
            dishOrder.setDish(dish);
            dishOrder.setQuantity(1);

            Order order2 = new Order();
            order2.setReference("CMD-002");
            order2.setCreationDatetime(Instant.now());
            order2.setDishOrders(List.of(dishOrder));
            order2.setTableOrder(tableOrder2);

            dr.saveOrder(order2);
            System.out.println("Test 2 devrait échouer mais n'a pas échoué");

        } catch (Exception e) {
            throw new RuntimeException("Test 2 Reussit : " + e.getMessage());
        }

    }
}
