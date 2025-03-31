package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.PriceHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
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
    public PriceHistory findById(String id){
        String query = """
            select * from "ingredient_price_history" where "id" = ?
        """;
        try{
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                return resultSetToPriceHistory(rs);
            }
            return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PriceHistory> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public PriceHistory deleteById(String id) {
        String query = """
            delete from "ingredient_price_history" where "id" = ?;
        """;

        try{
            final PriceHistory toDelete = this.findById((id));
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString (1, toDelete.getId());
            prs.executeUpdate();
            return toDelete;
        }catch (SQLException error){
            throw new RuntimeException(error);
        }
    }

    @Override
    public PriceHistory save(PriceHistory toCreate) {
        String query = """
            insert into "ingredient_price_history"("id", "id_ingredient", "price_datetime", "unit_price")
            values (?, ?, ?, ?);
        """;

        try{
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString (1, toCreate.getId());
            prs.setString (2, toCreate.getIdIngredient());
            prs.setTimestamp(3, Timestamp.valueOf(toCreate.getPriceDatetime()));
            prs.setBigDecimal (4, toCreate.getUnitPrice());
            prs.executeUpdate();
            return this.findById(toCreate.getId());
        }catch (SQLException error){
            throw new RuntimeException(error);
        }
    }


    @Override
    public PriceHistory update(PriceHistory id) {
        throw new RuntimeException("no implemented");
    }

    @Override
    public PriceHistory crupdate(PriceHistory id) {
        throw new RuntimeException("no implemented");
    }

    @Override
    public List<PriceHistory> saveAll(List<PriceHistory> list) {
        return List.of();
    }

    public List<PriceHistory> findByIngredientId(String id){
        List<PriceHistory> priceHistories = new ArrayList<>();
        String query = """
            select *
                from "ingredient_price_history"
                where "id_ingredient" = ?
                order by "price_datetime" desc
        """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                priceHistories.add(resultSetToPriceHistory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return priceHistories;
    }
}
