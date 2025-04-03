package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.Ingredient;
import hei.vaninah.devoir.entity.IngredientStockMovement;
import hei.vaninah.devoir.entity.PriceHistory;
import hei.vaninah.devoir.entity.Unit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class IngredientDAO implements RestaurantManagementDAO<Ingredient> {
    private final Connection connection;
    private final PriceHistoryDAO priceHistoryDAO;
    private final IngredientStockMovementDAO ingredientStockMovementDAO;

    private Ingredient resultSetToIngredient(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        List<PriceHistory> priceHistories = this.priceHistoryDAO.findByIngredientId(id);
        List<IngredientStockMovement> ingredientStockMovements = this.ingredientStockMovementDAO.findByIngredientId(id);

        return new Ingredient(
                id,
                rs.getString("name"),
                rs.getTimestamp("update_datetime").toLocalDateTime(),
                rs.getBigDecimal("unit_price"),
                Unit.valueOf(rs.getString("unit")),
                priceHistories,
                ingredientStockMovements
        );
    }

    @Override
    public Ingredient findById(String id) {
        String query = "select * from \"ingredient\" where \"id\" = ?";
        try{
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                return resultSetToIngredient(rs);
            }
            return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ingredient> findAll(Pagination pagination, Order order){
        StringBuilder query = new StringBuilder("select * from \"ingredient\"");
        query.append(" order by ").append(order.getOrderBy()).append(" ").append(order.getOrderValue());
        query.append(" limit ? offset ?");

        List<Ingredient> ingredients = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setInt(1, pagination.getPageSize());
            preparedStatement.setInt(2, (pagination.getPage() - 1) * pagination.getPageSize());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                ingredients.add(resultSetToIngredient(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    @Override
    public Ingredient deleteById(String id) {
        String query = """
            delete from "ingredient" where "id" = ?;
        """;

        try{
            final Ingredient toDelete = this.findById((id));
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString (1, toDelete.getId());
            prs.executeUpdate();
            return toDelete;
        }catch (SQLException error){
            throw new RuntimeException(error);
        }
    }

    @Override
    public Ingredient save(Ingredient toCreate) {
        String query = """
            insert into "ingredient"("id", "name", "update_datetime", "unit_price", "unit")
            values (?, ?, ?, ?, ?);
         """;
        try{
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString (1, toCreate.getId());
            prs.setString (2, toCreate.getName());
            prs.setTimestamp(3, Timestamp.valueOf(toCreate.getUpdateDatetime()));
            prs.setBigDecimal (4, toCreate.getUnitPrice());
            prs.setObject(5, toCreate.getUnit(), Types.OTHER);
            prs.executeUpdate();
            return this.findById(toCreate.getId());
        }catch (SQLException error){
            throw new RuntimeException(error);
        }
    }

    @Override
    public Ingredient update(Ingredient toUpdate) {
        String query = """
            update "ingredient"
                set "name" = ? ,
                    "update_datetime" = ?,
                    "unit_price" = ?,
                    "unit" = ?
                where "id" = ?
        """;
        try{
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString (1, toUpdate.getName());
            prs.setTimestamp(2, Timestamp.valueOf(toUpdate.getUpdateDatetime()));
            prs.setBigDecimal(3, toUpdate.getUnitPrice());
            prs.setObject(4, toUpdate.getUnit(), Types.OTHER);
            prs.setString (5, toUpdate.getId());
            prs.executeUpdate();
            return this.findById(toUpdate.getId());
        }catch (SQLException error){
            throw new RuntimeException(error);
        }
    }

    @Override
    public Ingredient crupdate(Ingredient crupdateIngredient) {
        final boolean isCreate = this.findById(crupdateIngredient.getId()) == null;
        if(isCreate) {
            return this.save(crupdateIngredient);
        }
        return this.update(crupdateIngredient);
    }

    @Override
    public List<Ingredient> saveAll(List<Ingredient> list) {
        for(Ingredient ingredient : list) {
            this.crupdate(ingredient);
        }

        return list;
    }


    public List<Ingredient> findByDishId(String dishId){
        List<Ingredient> ingredients = new ArrayList<>();
        String query = """
            select "ingredient".*
                from "dish_ingredient"
                inner join "ingredient"
                    on "ingredient"."id" = "dish_ingredient"."id_ingredient"
                where "dish_ingredient"."id_dish" = ?
                order by "ingredient"."name" asc;
        """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dishId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                ingredients.add(resultSetToIngredient(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }
}
