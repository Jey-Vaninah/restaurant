package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.IngredientStockMovement;
import hei.vaninah.devoir.entity.IngredientStockMovementType;
import hei.vaninah.devoir.entity.Unit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class IngredientStockMovementDAO implements RestaurantManagementDAO<IngredientStockMovement> {
    private final Connection connection;

    private IngredientStockMovement resultSetToIngredientStock(ResultSet rs) throws SQLException {
        return new IngredientStockMovement(
                rs.getString("id"),
                rs.getString("id_ingredient"),
                rs.getFloat("quantity"),
                rs.getTimestamp("movement_datetime").toLocalDateTime(),
                IngredientStockMovementType.valueOf(rs.getString("movement_type")),
                Unit.valueOf(rs.getString("unit"))
        );
    }

    public List<IngredientStockMovement> findByIngredientId(String idIngredient) {
        List<IngredientStockMovement> ingredientStockMovements = new ArrayList<>();
        String query = """
            select *
                from "ingredient_stock_movement"
                where "id_ingredient" = ?
                order by "id" asc 
        """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, idIngredient);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ingredientStockMovements.add(resultSetToIngredientStock(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientStockMovements;
    }

    @Override
    public IngredientStockMovement findById(String id) {
        String query = """
            select * from ingredient_stock_movement where id = ?";
        """;
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return resultSetToIngredientStock(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IngredientStockMovement> findAll(Pagination pagination, Order order) {
        List<IngredientStockMovement> stockMovements = new ArrayList<>();
        String query = """
        
                select * from "ingredient_stock_movement"
        order by "movement_datetime" """ + order.getOrderValue() + """
        limit ?
                offset ?
    """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, pagination.getPage());
            preparedStatement.setInt(2, pagination.getPageSize());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                stockMovements.add(resultSetToIngredientStock(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stockMovements;
    }

    @Override
    public IngredientStockMovement deleteById(String id) {
        String query = """
            delete from "ingredient_stock_movement" where "id"
                = ?;
         """;

        try{
            final IngredientStockMovement toDelete = this.findById((id));
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString (1, toDelete.id());
            prs.executeUpdate();
            return toDelete;
        }catch (SQLException error){
            throw new RuntimeException(error);
        }
    }

    @Override
    public IngredientStockMovement save(IngredientStockMovement toCreate) {
        String
                query = """
            insert into "ingredient_stock_movement"("id", "id_ingredient", "quantity", "movement_datetime", "mo
                e", "unit")
            values (?, ?, ?, ?, ?, ?);
         """;
        try{
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString (1, toCreate.id());
            prs.setString (2, toCreate.idIngredient());
            prs.setFloat(3, toCreate.quantity());
            prs.setTimestamp(4, Timestamp.valueOf(toCreate.movementDatetime()));
            prs.setObject(5, toCreate.movementType(), Types.OTHER);
            prs.setObject(6, toCreate.unit(), Types.OTHER);
            prs.executeUpdate();
            return this.findById(toCreate.id());
        }catch (SQLException error){
            throw new RuntimeException(error);
        }
    }

    @Override
    public IngredientStockMovement update(IngredientStockMovement toUpdate) {
        String query = """
        update
                "ingredient_stock_movement"
        set "id_ingredient" = ?, "quantity" = ?, "movement_datetime" = ?, "movemen
                e" = ?,
                "unit" = ?
        where "id" = ?
    """;

        try {
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString(1, toUpdate.idIngredient());
            prs.setFloat(2, toUpdate.quantity());
            prs.setTimestamp(3, Timestamp.valueOf(toUpdate.movementDatetime()));
            prs.setObject(4, toUpdate.movementType(), Types.OTHER);
            prs.setObject(5, toUpdate.unit(), Types.OTHER);
            prs.setString(6, toUpdate.id());
            prs.executeUpdate();
            return this.findById(toUpdate.id());
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }

    @Override
    public IngredientStockMovement crupdate(IngredientStockMovement stockMovement) {
        if (findById(stockMovement.id()) != null) {
            return update(stockMovement);
        } else {
            return save(stockMovement);
        }
    }

    @Override
    public List<IngredientStockMovement> saveAll(List<IngredientStockMovement>
                list) {
        String query = """
        insert into "ingredient_stock_movement"("id", "id_ingredient", "quantity", "movem
                atetime", "movement_type",
                "unit")
        values (?, ?, ?, ?, ?, ?)
    """;

        try {
            PreparedStatement prs = connection.prepareStatement(query);
            for (IngredientStockMovement stockMovement : list) {
                prs.setString(1, stockMovement.id());
                prs.setString(2, stockMovement.idIngredient());
                prs.setFloat(3, stockMovement.quantity());
                prs.setTimestamp(4, Timestamp.valueOf(stockMovement.movementDatetime()));
                prs.setObject(5, stockMovement.movementType(), Types.OTHER);
                prs.setObject(6, stockMovement.unit(), Types.OTHER);
                prs.addBatch();
            }
            prs.executeBatch();
        } catch (SQLException error) {
            throw new RuntimeException(
error);
        }
        return list;
    }
}
