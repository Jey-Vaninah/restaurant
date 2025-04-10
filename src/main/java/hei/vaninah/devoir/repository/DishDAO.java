package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.Dish;
import hei.vaninah.devoir.entity.DishIngredient;
import hei.vaninah.devoir.entity.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishDAO implements RestaurantManagementDAO<Dish> {
    private final Connection connection;
    private final IngredientDAO ingredientDAO;
    private final DishIngredientDAO dishIngredientDAO;

    public Dish resultSetToDish(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        List<Ingredient> ingredients = this.ingredientDAO.findByDishId(id);
        List<DishIngredient> dishIngredients = this.dishIngredientDAO.findByDishId(id);

        return new Dish(
            id,
            rs.getString("name"),
            rs.getBigDecimal("unit_price"),
            ingredients,
            dishIngredients
        );

    }

    @Override
    public Dish findById(String id) throws SQLException {
        String query = """
            select * from "dish" where "id" = ?;
        """;
        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return resultSetToDish(rs);
        }
        return null;
    }

    @Override
    public List<Dish> findAll(Pagination pagination, Order order) throws SQLException {
        String query = """
            select * from "dish" order by "name" limit ? offset ? ;
        """;

        List<Dish> dishes = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, pagination.getLimit());
        preparedStatement.setInt(2, pagination.getOffset());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            dishes.add(resultSetToDish(rs));
        }
        return dishes;
    }

    @Override
    public Dish deleteById(String id) throws SQLException {
        String query = """
            delete from "dish" where "id" = ?;
        """;

        Dish toDelete = this.findById((id));
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString(1, toDelete.getId());
        prs.executeUpdate();
        return toDelete;
    }

    @Override
    public Dish save(Dish toCreate) throws SQLException {
        String query = """
            insert into "dish"("id", "name", "unit_price")
            values (?, ?, ?);
        """;
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString(1, toCreate.getId());
        prs.setString(2, toCreate.getName());
        prs.setBigDecimal(3, toCreate.getUnitPrice());
        prs.executeUpdate();

        dishIngredientDAO.saveAll(toCreate.getDishIngredients());
        return this.findById(toCreate.getId());
    }

    @Override
    public Dish update(Dish toUpdate) throws SQLException {
        String query = """
            update "dish"
                set "name" = ?,
                    "unit_price" = ?
                where "id" = ?
        """;
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString(1, toUpdate.getName());
        prs.setBigDecimal(2, toUpdate.getUnitPrice());
        prs.setString(3, toUpdate.getId());
        prs.executeUpdate();
        dishIngredientDAO.saveAll(toUpdate.getDishIngredients());
        return this.findById(toUpdate.getId());
    }

    @Override
    public Dish crupdate(Dish crupdateDish) throws SQLException {
        final boolean isCreate = this.findById(crupdateDish.getId()) == null;
        if (isCreate) {
            return this.save(crupdateDish);
        }

        return this.update(crupdateDish);
    }

    @Override
    public List<Dish> saveAll(List<Dish> list) throws SQLException {
        for(Dish dish: list) {
            this.crupdate(dish);
        }
        return list;
    }
}