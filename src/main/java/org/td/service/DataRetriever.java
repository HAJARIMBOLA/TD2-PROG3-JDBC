package org.td.service;

import org.td.config.DBConnection;
import org.td.entity.*;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRetriever {

    /* ===================== DISH ===================== */

    public Dish findDishById(Integer id) {

        String sql = """
                select id, name, dish_type, selling_price
                from dish
                where id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Dish not found " + id);
            }

            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
            dish.setPrice(rs.getObject("selling_price") == null
                    ? null : rs.getDouble("selling_price"));

            dish.setDishIngredientList(findDishIngredientById(id));
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ===================== INGREDIENT ===================== */

    public Ingredient findIngredientById(Integer id) {

        String sql = """
                select id, name, price, category
                from ingredient
                where id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Ingredient not found " + id);
            }

            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getInt("id"));
            ingredient.setName(rs.getString("name"));
            ingredient.setPrice(rs.getDouble("price"));
            ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
            ingredient.setStockMovementList(getStockMovementByIngredientId(id));

            return ingredient;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockMovement> getStockMovementByIngredientId(int id) {

        String sql = """
                select id, quantity, type, unit, creation_datetime
                from stockmovement
                where id_ingredient = ?
                """;

        List<StockMovement> stockMovements = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StockMovement sm = new StockMovement();
                sm.setId(rs.getInt("id"));

                StockValue sv = new StockValue(
                        rs.getDouble("quantity"),
                        UnitType.valueOf(rs.getString("unit"))
                );

                sm.setValue(sv);
                sm.setType(MovementTypeEnum.valueOf(rs.getString("type")));
                sm.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());

                stockMovements.add(sm);
            }

            return stockMovements;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ===================== DISH INGREDIENT ===================== */

    private List<DishIngredient> findDishIngredientById(Integer dishId) {

        String sql = """
                select di.id, di.quantity_required, di.unit,
                       i.id as ing_id, i.name, i.price, i.category
                from dishingredient di
                join ingredient i on di.id_ingredient = i.id
                where di.id_dish = ?
                """;

        List<DishIngredient> dishIngredients = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, dishId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("ing_id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));
                ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));

                Dish dish = new Dish();
                dish.setId(dishId);

                DishIngredient di = new DishIngredient();
                di.setId(rs.getInt("id"));
                di.setDish(dish);
                di.setIngredient(ing);
                di.setQuantityRequired(rs.getDouble("quantity_required"));
                di.setUnit(UnitType.valueOf(rs.getString("unit")));

                dishIngredients.add(di);
            }

            return dishIngredients;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ===================== SAVE DISH ===================== */

    public Dish saveDish(Dish toSave) {

        String sql = """
                insert into dish (id, selling_price, name, dish_type)
                values (?, ?, ?, ?::dish_type)
                on conflict (id) do update
                set name = excluded.name,
                    dish_type = excluded.dish_type,
                    selling_price = excluded.selling_price
                returning id
                """;

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);
            Integer dishId;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, toSave.getId() != null
                        ? toSave.getId()
                        : getNextSerialValue(conn, "dish", "id"));

                if (toSave.getPrice() != null) {
                    ps.setDouble(2, toSave.getPrice());
                } else {
                    ps.setNull(2, Types.DOUBLE);
                }

                ps.setString(3, toSave.getName());
                ps.setString(4, toSave.getDishType().name());

                ResultSet rs = ps.executeQuery();
                rs.next();
                dishId = rs.getInt(1);
            }

            List<Ingredient> ingredients = toSave.getDishIngredientList()
                    .stream()
                    .map(DishIngredient::getIngredient)
                    .collect(Collectors.toList());

            detachIngredients(conn, dishId, ingredients);
            attachIngredients(conn, dishId, toSave.getDishIngredientList());

            conn.commit();
            return findDishById(dishId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void detachIngredients(Connection conn, Integer dishId, List<Ingredient> ingredients)
            throws SQLException {

        if (ingredients == null || ingredients.isEmpty()) {
            try (PreparedStatement ps =
                         conn.prepareStatement("delete from dishingredient where id_dish = ?")) {
                ps.setInt(1, dishId);
                ps.executeUpdate();
            }
            return;
        }

        String placeholders = ingredients.stream().map(i -> "?").collect(Collectors.joining(","));
        String sql = "delete from dishingredient where id_dish = ? and id_ingredient not in (" + placeholders + ")";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dishId);
            int idx = 2;
            for (Ingredient ing : ingredients) {
                ps.setInt(idx++, ing.getId());
            }
            ps.executeUpdate();
        }
    }

    private void attachIngredients(Connection conn, Integer dishId, List<DishIngredient> dishIngredients)
            throws SQLException {

        if (dishIngredients == null || dishIngredients.isEmpty()) return;

        String sql = """
                insert into dishingredient (id_dish, id_ingredient, quantity_required, unit)
                values (?, ?, ?, ?::unit_type)
                on conflict do update
                set quantity_required = excluded.quantity_required,
                    unit = excluded.unit
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            for (DishIngredient di : dishIngredients) {
                ps.setInt(1, dishId);
                ps.setInt(2, di.getIngredient().getId());
                ps.setDouble(3, di.getQuantityRequired());
                ps.setString(4, di.getUnit().name());
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    /* ===================== SEQUENCE ===================== */

    private String getSerialSequenceName(Connection conn, String table, String column)
            throws SQLException {

        try (PreparedStatement ps =
                     conn.prepareStatement("SELECT pg_get_serial_sequence(?, ?)")) {
            ps.setString(1, table);
            ps.setString(2, column);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }

    private int getNextSerialValue(Connection conn, String table, String column)
            throws SQLException {

        String seq = getSerialSequenceName(conn, table, column);
        if (seq == null) {
            throw new IllegalArgumentException("No sequence for " + table + "." + column);
        }

        conn.prepareStatement(
                "select setval('" + seq + "', (select coalesce(max(" + column + "),0) from " + table + "))"
        ).execute();

        ResultSet rs = conn.prepareStatement("select nextval('" + seq + "')").executeQuery();
        rs.next();
        return rs.getInt(1);
    }
}
