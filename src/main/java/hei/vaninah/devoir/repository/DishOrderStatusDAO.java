package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.DishOrderStatus;
import hei.vaninah.devoir.entity.StatusHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderStatusDAO implements RestaurantManagementDAO<DishOrderStatus> {
    private final Connection connection;

    private DishOrderStatus resultSetToDishOrderStatus(ResultSet rs) throws SQLException {
        return new DishOrderStatus(
            rs.getString("id"),
            rs.getString("id_dish_order"),
            StatusHistory.valueOf(rs.getString("status")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }

    public List<DishOrderStatus> findByDishOrderId(String dishOrderId) throws SQLException {
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();
        String query = """
            select * from "dish_order_status" where id_dish_order = ? order by "updated_at" desc
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, dishOrderId);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            dishOrderStatuses.add(resultSetToDishOrderStatus(rs));
        }
        return dishOrderStatuses;
    }

    @Override
    public DishOrderStatus save(DishOrderStatus dishOrderStatus) throws SQLException {
        String query = "insert into dish_order_status (id, id_dish_order, status, created_at, updated_at) values (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, dishOrderStatus.getId());
        ps.setString(2, dishOrderStatus.getDishOrderId());
        ps.setObject(3, dishOrderStatus.getStatus(), Types.OTHER);
        ps.setTimestamp(4, Timestamp.valueOf(dishOrderStatus.getCreatedAt()));
        ps.setTimestamp(5, Timestamp.valueOf(dishOrderStatus.getUpdatedAt()));
        ps.executeUpdate();
        return dishOrderStatus;
    }

    @Override
    public DishOrderStatus update(DishOrderStatus orderStatus) throws SQLException {
        String query = "update dish_order_status set status = ?, updated_at = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setObject(1, orderStatus.getStatus(), Types.OTHER);
        ps.setTimestamp(2, Timestamp.valueOf(orderStatus.getUpdatedAt()));
        ps.setString(3, orderStatus.getId());
        ps.executeUpdate();
        return orderStatus;
    }

    @Override
    public DishOrderStatus findById(String id) throws SQLException {
        String query = "select * from dish_order_status where id = ?";
        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return resultSetToDishOrderStatus(rs);
        }
        return null;
    }

    @Override
    public List<DishOrderStatus> findAll(Pagination pagination, Order order) throws SQLException {
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();
        String query = """
            select * from "dish_order_status"
            order by "updated_at" desc limit ? offset ?
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, pagination.getLimit());
        preparedStatement.setInt(2, pagination.getOffset());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            dishOrderStatuses.add(resultSetToDishOrderStatus(rs));
        }
        return dishOrderStatuses;
    }

    @Override
    public DishOrderStatus deleteById(String id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public DishOrderStatus crupdate(DishOrderStatus dishOrderStatus) throws SQLException {
        if (findById(dishOrderStatus.getId()) != null) {
            return update(dishOrderStatus);
        } else {
            return save(dishOrderStatus);
        }
    }

    @Override
    public List<DishOrderStatus> saveAll(List<DishOrderStatus> list) throws SQLException {
        for(DishOrderStatus dishOrderStatus: list) {
            this.crupdate(dishOrderStatus);
        }
        return list;
    }
}
