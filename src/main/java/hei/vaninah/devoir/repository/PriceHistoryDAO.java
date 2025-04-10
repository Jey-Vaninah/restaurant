package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.PriceHistory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class PriceHistoryDAO implements RestaurantManagementDAO<PriceHistory> {
    final private Connection connection;

    private PriceHistory resultSetToPriceHistory(ResultSet rs) throws SQLException {
        return new PriceHistory(
            rs.getString("id"),
            rs.getString("id_ingredient"),
            rs.getTimestamp("price_datetime").toLocalDateTime(),
            rs.getBigDecimal("unit_price")
        );
    }

    @Override
    public PriceHistory findById(String id) throws SQLException {
        String query = "select * from ingredient_price_history where id = ?";
        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return resultSetToPriceHistory(rs);
        }
        return null;
    }

    @Override
    public List<PriceHistory> findAll(Pagination pagination, Order order) throws SQLException {
        List<PriceHistory> priceHistories = new ArrayList<>();
        String query = """
            select * from "dish_order_status"
            order by "price_datetime" desc limit ? offset ?
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, pagination.getLimit());
        preparedStatement.setInt(2, pagination.getOffset());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            priceHistories.add(resultSetToPriceHistory(rs));
        }
        return priceHistories;
    }

    @Override
    public PriceHistory deleteById(String id) throws SQLException {
        String query = """
            delete from "ingredient_price_history" where "id" = ?;
        """;

        PriceHistory toDelete = this.findById((id));
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString (1, toDelete.getId());
        prs.executeUpdate();
        return toDelete;
    }

    @Override
    public PriceHistory save(PriceHistory toCreate) throws SQLException {
        String query = """
            insert into "ingredient_price_history"("id", "id_ingredient", "price_datetime", "unit_price")
            values (?, ?, ?, ?);
        """;

        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString (1, toCreate.getId());
        prs.setString (2, toCreate.getIdIngredient());
        prs.setTimestamp(3, Timestamp.valueOf(toCreate.getPriceDatetime()));
        prs.setBigDecimal (4, toCreate.getUnitPrice());
        prs.executeUpdate();
        return this.findById(toCreate.getId());
    }

    @Override
    public PriceHistory update(PriceHistory toUpdate) throws SQLException {
        String query = """
            update "ingredient_price_history"
            set "id_ingredient" = ?,
                "price_datetime" = ?,
                "unit_price" = ?
            where "id" = ?
        """;

        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString(1, toUpdate.getIdIngredient());
        prs.setTimestamp(2, Timestamp.valueOf(toUpdate.getPriceDatetime()));
        prs.setBigDecimal(3, toUpdate.getUnitPrice());
        prs.setString(4, toUpdate.getId());
        prs.executeUpdate();
        return this.findById(toUpdate.getId());
    }

    @Override
    public PriceHistory crupdate(PriceHistory priceHistory) throws SQLException {
        if (findById(priceHistory.getId()) != null) {
            return update(priceHistory);
        } else {
            return save(priceHistory);
        }
    }

    @Override
    public List<PriceHistory> saveAll(List<PriceHistory> list) throws SQLException {
        for(PriceHistory priceHistory: list) {
            this.crupdate(priceHistory);
        }
        return list;
    }

    public List<PriceHistory> findByIngredientId(String id) throws SQLException {
        List<PriceHistory> priceHistories = new ArrayList<>();
        String query = """
            select * from "ingredient_price_history"
                where "id_ingredient" = ?
                order by "price_datetime" desc
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            priceHistories.add(resultSetToPriceHistory(rs));
        }
        return priceHistories;
    }
}
