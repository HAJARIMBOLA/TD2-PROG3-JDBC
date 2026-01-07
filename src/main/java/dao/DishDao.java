
package dao;

import config.DBConnection;
import entity.Dish;
import service.DataRetriever;

import java.sql.*;

public class DishDao {

    public Dish findDishById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement stmt =
                con.prepareStatement("SELECT * FROM dish WHERE id=?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return DataRetriever.map(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish saveDish(Dish dish) {
        if (dish.getDishPrice() == null) {
            throw new RuntimeException(
                "Impossible de sauvegarder un plat sans prix"
            );
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO dish(name, type, cost, price) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, dish.getName());
            stmt.setString(2, dish.getType().name());
            stmt.setDouble(3, dish.getDishCost());
            stmt.setDouble(4, dish.getDishPrice());
            stmt.executeUpdate();
            return dish;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
