package org.td.service;

import org.td.config.DBConnection;
import org.td.entity.*;
import org.td.util.UnitConverter;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRetriever {

    private final UnitConverter unitConverter = new UnitConverter();

    public Dish findDishById(Integer id) {
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("""
                select id, name, dish_type, selling_price
                from dish
                where id = ?
            """);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Dish not found: " + id);
            }

            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
            dish.setPrice(rs.getObject("selling_price") == null ? null : rs.getDouble("selling_price"));
            dish.setDishIngredientList(findDishIngredientById(id));

            return dish;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.closeConnection(conn);
        }
    }

    public Ingredient findIngredientById(Integer id) {
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("""
                select id, name, price, category
                from ingredient
                where id = ?
            """);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Ingredient not found: " + id);
            }

            Ingredient ing = new Ingredient();
            ing.setId(rs.getInt("id"));
            ing.setName(rs.getString("name"));
            ing.setPrice(rs.getDouble("price"));
            ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
            ing.setStockMovementList(getStockMovements(conn, id));

            return ing;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.closeConnection(conn);
        }
    }

    private List<StockMovement> getStockMovements(Connection conn, int ingredientId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("""
            select id, quantity, type, unit, creation_datetime
            from stockmovement
            where id_ingredient = ?
        """);
        ps.setInt(1, ingredientId);
        ResultSet rs = ps.executeQuery();

        List<StockMovement> list = new ArrayList<>();
        while (rs.next()) {
            StockMovement sm = new StockMovement();
            sm.setId(rs.getInt("id"));
            sm.setType(MovementTypeEnum.valueOf(rs.getString("type")));
            sm.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
            sm.setValue(new StockValue(
                    rs.getDouble("quantity"),
                    UnitType.valueOf(rs.getString("unit"))
            ));
            list.add(sm);
        }
        return list;
    }

    public Order saveOrder(Order order) throws SQLException {
        if (order.getTableOrder() == null) {
            throw new RuntimeException("Table not provided");
        }

        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        conn.setAutoCommit(false);

        try {
            for (DishOrder dOrder : order.getDishOrders()) {
                for (DishIngredient di : dOrder.getDish().getDishIngredientList()) {
                    double required = unitConverter.convertTo(
                            di.getIngredient().getName(),
                            di.getUnit(),
                            UnitType.KG,
                            di.getQuantityRequired()
                    ) * dOrder.getQuantity();

                    Ingredient ing = findIngredientById(di.getIngredient().getId());
                    double available = ing.getStockValueAt(Instant.now()).getQuantity();

                    if (available < required) {
                        throw new RuntimeException("Insufficient stock for ingredient: " + ing.getName());
                    }
                }
            }

            int orderId = order.getId() != null
                    ? order.getId()
                    : getNextSerialValue(conn, "Order", "id");

            PreparedStatement ps = conn.prepareStatement("""
                insert into "Order" (id, reference, creation_datetime, table_id)
                values (?, ?, ?, ?)
            """);

            ps.setInt(1, orderId);
            ps.setString(2, order.getReference());
            ps.setTimestamp(3, Timestamp.from(order.getCreationDatetime()));
            ps.setInt(4, order.getTableOrder().getId());
            ps.executeUpdate();

            saveDishOrders(conn, order.getDishOrders(), orderId);

            conn.commit();
            return order;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            db.closeConnection(conn);
        }
    }

    private void saveDishOrders(Connection conn, List<DishOrder> list, int orderId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("""
            insert into dishorder (id, id_order, id_dish, quantity)
            values (?, ?, ?, ?)
        """);

        for (DishOrder d : list) {
            int id = d.getId() != null
                    ? d.getId()
                    : getNextSerialValue(conn, "dishorder", "id");

            ps.setInt(1, id);
            ps.setInt(2, orderId);
            ps.setInt(3, d.getDish().getId());
            ps.setInt(4, d.getQuantity());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    public Order findOrderByReference(String reference) {
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("""
                select id, reference, creation_datetime, table_id
                from "Order"
                where reference = ?
            """);
            ps.setString(1, reference);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Order not found: " + reference);
            }

            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setReference(rs.getString("reference"));
            order.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
            order.setDishOrders(findDishOrdersByOrderReference(reference));

            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.closeConnection(conn);
        }
    }

    public List<DishOrder> findDishOrdersByOrderReference(String reference) {
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("""
                select d.id, d.id_dish, d.quantity
                from dishorder d
                join "Order" o on o.id = d.id_order
                where o.reference = ?
            """);
            ps.setString(1, reference);
            ResultSet rs = ps.executeQuery();

            List<DishOrder> list = new ArrayList<>();
            while (rs.next()) {
                DishOrder d = new DishOrder();
                d.setId(rs.getInt("id"));
                d.setDish(findDishById(rs.getInt("id_dish")));
                d.setQuantity(rs.getInt("quantity"));
                list.add(d);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.closeConnection(conn);
        }
    }

    public Table findTableById(int id) {
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("""
                select id, capacity
                from table_restaurant
                where id = ?
            """);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Table not found: " + id);
            }

            Table table = new Table();
            table.setId(rs.getInt("id"));
            table.setCapacity(rs.getInt("capacity"));
            return table;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.closeConnection(conn);
        }
    }

    private List<DishIngredient> findDishIngredientById(Integer dishId) {
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("""
                select d.id, quantity_required, unit,
                       i.id as ing_id, i.name, i.price, i.category
                from dishingredient d
                join ingredient i on d.id_ingredient = i.id
                where id_dish = ?
            """);
            ps.setInt(1, dishId);
            ResultSet rs = ps.executeQuery();

            List<DishIngredient> list = new ArrayList<>();
            while (rs.next()) {
                Ingredient ing = new Ingredient(
                        rs.getInt("ing_id"),
                        rs.getString("name"),
                        CategoryEnum.valueOf(rs.getString("category")),
                        rs.getDouble("price")
                );

                DishIngredient di = new DishIngredient();
                di.setId(rs.getInt("id"));
                di.setIngredient(ing);
                di.setQuantityRequired(rs.getDouble("quantity_required"));
                di.setUnit(UnitType.valueOf(rs.getString("unit")));
                list.add(di);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.closeConnection(conn);
        }
    }

    private String getSerialSequenceName(Connection conn, String table, String column) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select pg_get_serial_sequence(?, ?)");
        ps.setString(1, table);
        ps.setString(2, column);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString(1);
    }

    private int getNextSerialValue(Connection conn, String table, String column) throws SQLException {
        String seq = getSerialSequenceName(conn, table, column);
        PreparedStatement ps = conn.prepareStatement("select nextval(?)");
        ps.setString(1, seq);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public List<Integer> findAvailableTables(Instant arrival, Instant departure) {
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement("""
            select t.id
            from table_restaurant t
            where t.id not in (
                select o.table_id
                from "Order" o
                where not (
                    o.departure_datetime <= ?
                    or o.arrival_datetime >= ?
                )
            )
            order by t.id
        """);

            ps.setTimestamp(1, Timestamp.from(arrival));
            ps.setTimestamp(2, Timestamp.from(departure));

            ResultSet rs = ps.executeQuery();
            List<Integer> availableTables = new ArrayList<>();

            while (rs.next()) {
                availableTables.add(rs.getInt("id"));
            }

            return availableTables;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.closeConnection(conn);
        }
    }

}
