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

    public List<DishIngredient> findByDishId(String dishId){
        List<DishIngredient> dishIngredients = new ArrayList<>();

        String query = """
            select * from "dish_ingredient"
                where "dish_ingredient"."id_dish" = ?
                order by "dish_ingredient"."id_ingredient" asc;
        """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dishId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                dishIngredients.add(resultSetToDishIngredient(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishIngredients;
    }

    @Override
    public DishIngredient findById(String id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public List<DishIngredient> findAll(Pagination pagination, Order order) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public DishIngredient deleteById(String id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public DishIngredient save(DishIngredient dishIngredient) {
        String query = """
            insert into "dish_ingredient"("id_dish", "id_ingredient", "required_quantity", "unit")
            values (?, ?, ?, ?);
        """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dishIngredient.getIdDish());
            preparedStatement.setString(2, dishIngredient.getIdIngredient());
            preparedStatement.setFloat(3, dishIngredient.getRequiredQuantity());
            preparedStatement.setObject(4, dishIngredient.getUnit(), Types.OTHER);
            preparedStatement.executeUpdate();
            return dishIngredient;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DishIngredient update(DishIngredient dishIngredient) {
        String query = """
            update "dish_ingredient"
            set "required_quantity" = ?, "unit" = ?
            where "id_dish" = ? and "id_ingredient" = ?;
        """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFloat(1, dishIngredient.getRequiredQuantity());
            preparedStatement.setObject(2, dishIngredient.getUnit(), Types.OTHER);
            preparedStatement.setString(3, dishIngredient.getIdDish());
            preparedStatement.setString(4, dishIngredient.getIdIngredient());
            preparedStatement.executeUpdate();
            return dishIngredient;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DishIngredient crupdate(DishIngredient dishIngredient) {
        if (findByDishId(dishIngredient.getIdDish()).stream().anyMatch(di -> di.getIdIngredient().equals(dishIngredient.getIdIngredient()))) {
            return update(dishIngredient);
        } else {
            return save(dishIngredient);
        }
    }

    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> list) {
        for (DishIngredient dishIngredient : list) {
            crupdate(dishIngredient);
        }
        return list;
    }

}
