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

    public List<IngredientStockMovement> findByIngredientId(String idIngredient) throws SQLException {
        List<IngredientStockMovement> ingredientStockMovements = new ArrayList<>();
        String query = """
            select *
                from "ingredient_stock_movement"
                where "id_ingredient" = ?
                order by "movement_datetime" desc
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, idIngredient);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            ingredientStockMovements.add(resultSetToIngredientStock(rs));
        }
        return ingredientStockMovements;
    }

    @Override
    public IngredientStockMovement findById(String id) throws SQLException {
        String query = """
            select * from ingredient_stock_movement where "id" = ?;
        """;

        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return resultSetToIngredientStock(rs);
        }
        return null;
    }

    @Override
    public List<IngredientStockMovement> findAll(Pagination pagination, Order order) throws SQLException {
        List<IngredientStockMovement> stockMovements = new ArrayList<>();
        String query = """
           select * from "ingredient_stock_movement"
           order by "movement_datetime" desc limit ? offset ?;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, pagination.getLimit());
        preparedStatement.setInt(2, pagination.getOffset());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            stockMovements.add(resultSetToIngredientStock(rs));
        }
        return stockMovements;
    }

    @Override
    public IngredientStockMovement deleteById(String id) throws SQLException {
        String query = """
            delete from "ingredient_stock_movement" where "id"= ?;
         """;

        IngredientStockMovement toDelete = this.findById((id));
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString (1, toDelete.id());
        prs.executeUpdate();
        return toDelete;
    }

    @Override
    public IngredientStockMovement save(IngredientStockMovement toCreate) throws SQLException {
        String query = """
            insert into "ingredient_stock_movement"("id", "id_ingredient", "quantity", "movement_datetime", "movement_type", "unit")
            values (?, ?, ?, ?, ?, ?);
        """;

        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString (1, toCreate.id());
        prs.setString (2, toCreate.idIngredient());
        prs.setFloat(3, toCreate.quantity());
        prs.setTimestamp(4, Timestamp.valueOf(toCreate.movementDatetime()));
        prs.setObject(5, toCreate.movementType(), Types.OTHER);
        prs.setObject(6, toCreate.unit(), Types.OTHER);
        prs.executeUpdate();
        return this.findById(toCreate.id());
    }

    @Override
    public IngredientStockMovement update(IngredientStockMovement toUpdate) throws SQLException {
        String query =
        """
            update "ingredient_stock_movement"
            set "id_ingredient" = ?, "quantity" = ?, "movement_datetime" = ?, "movement_type" = ?, "unit" = ?
            where "id" = ?
        """;

        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString(1, toUpdate.idIngredient());
        prs.setFloat(2, toUpdate.quantity());
        prs.setTimestamp(3, Timestamp.valueOf(toUpdate.movementDatetime()));
        prs.setObject(4, toUpdate.movementType(), Types.OTHER);
        prs.setObject(5, toUpdate.unit(), Types.OTHER);
        prs.setString(6, toUpdate.id());
        prs.executeUpdate();
        return this.findById(toUpdate.id());
    }

    @Override
    public IngredientStockMovement crupdate(IngredientStockMovement stockMovement) throws SQLException {
        if (findById(stockMovement.id()) != null) {
            return update(stockMovement);
        } else {
            return save(stockMovement);
        }
    }

    @Override
    public List<IngredientStockMovement> saveAll(List<IngredientStockMovement>list) throws SQLException {
        for(IngredientStockMovement priceHistory: list) {
            this.crupdate(priceHistory);
        }
        return list;
    }
}
