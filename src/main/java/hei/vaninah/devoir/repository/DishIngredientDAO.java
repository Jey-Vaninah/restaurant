package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.DishIngredient;
import hei.vaninah.devoir.entity.Unit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishIngredientDAO implements RestaurantManagementDAO<DishIngredient> {
    private final Connection connection;

    DishIngredient resultSetToDishIngredient(ResultSet rs) throws SQLException {
        return new DishIngredient(
            rs.getString("id_dish"),
            rs.getString("id_ingredient"),
            rs.getFloat("required_quantity"),
            Unit.valueOf(rs.getString("unit"))
        );
    }

    public List<DishIngredient> findByDishId(String dishId) throws SQLException {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        String query = """
            select * from "dish_ingredient"
            where "dish_ingredient"."id_dish" = ?
            order by "dish_ingredient"."id_ingredient";
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, dishId);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            dishIngredients.add(resultSetToDishIngredient(rs));
        }
        return dishIngredients;
    }

    @Override
    public DishIngredient findById(String id) {
        throw new RuntimeException("Not Implemented");
    }

    public DishIngredient findByDishIdAndIngredientId(String dishId, String ingredientId) throws SQLException {
        String query = "select * from dish_ingredient where id_dish = ? and id_ingredient = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, dishId);
        preparedStatement.setString(2, ingredientId);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            return resultSetToDishIngredient(rs);
        }
        return null;
    }

    @Override
    public List<DishIngredient> findAll(Pagination pagination, Order order) throws SQLException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public DishIngredient deleteById(String id) throws SQLException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public DishIngredient save(DishIngredient dishIngredient) throws SQLException {
        String query = """
            insert into "dish_ingredient"("id_dish", "id_ingredient", "required_quantity", "unit")
            values (?, ?, ?, ?);
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, dishIngredient.getIdDish());
        preparedStatement.setString(2, dishIngredient.getIdIngredient());
        preparedStatement.setFloat(3, dishIngredient.getRequiredQuantity());
        preparedStatement.setObject(4, dishIngredient.getUnit(), Types.OTHER);
        preparedStatement.executeUpdate();
        return dishIngredient;
    }

    @Override
    public DishIngredient update(DishIngredient dishIngredient) throws SQLException {
        String query = """
            update "dish_ingredient"
            set "required_quantity" = ?, "unit" = ?
            where "id_dish" = ? and "id_ingredient" = ?;
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setFloat(1, dishIngredient.getRequiredQuantity());
        preparedStatement.setObject(2, dishIngredient.getUnit(), Types.OTHER);
        preparedStatement.setString(3, dishIngredient.getIdDish());
        preparedStatement.setString(4, dishIngredient.getIdIngredient());
        preparedStatement.executeUpdate();
        return dishIngredient;
    }

    @Override
    public DishIngredient crupdate(DishIngredient dishIngredient) throws SQLException {
        if (findByDishIdAndIngredientId(dishIngredient.getIdDish(), dishIngredient.getIdIngredient()) != null) {
            return update(dishIngredient);
        } else {
            return save(dishIngredient);
        }
    }

    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> list) throws SQLException {
        for (DishIngredient dishIngredient : list) {
            crupdate(dishIngredient);
        }
        return list;
    }
}
