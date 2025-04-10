package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.OrderStatus;
import hei.vaninah.devoir.entity.StatusHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderStatusDAO implements RestaurantManagementDAO<OrderStatus> {
    private final Connection connection;

    private OrderStatus resultSetToOrderStatus(ResultSet rs) throws SQLException {
        return new OrderStatus(
            rs.getString("id"),
            rs.getString("id_order"),
            StatusHistory.valueOf(rs.getString("status")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }

    public List<OrderStatus> findByOrderId(String orderId) throws SQLException {
        List<OrderStatus> orderStatuses = new ArrayList<>();
        String query = """
            select * from "order_status" where id_order = ? order by id;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, orderId);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            orderStatuses.add(resultSetToOrderStatus(rs));
        }
        return orderStatuses;
    }

    @Override
    public OrderStatus save(OrderStatus orderStatus) throws SQLException {
        String query = "insert into order_status (id, id_order, status, updated_at, created_at) values (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, orderStatus.getId());
        ps.setString(2, orderStatus.getIdOrder());
        ps.setObject(3, orderStatus.getStatus(), Types.OTHER);
        ps.setTimestamp(4, Timestamp.valueOf(orderStatus.getUpdatedAt()));
        ps.setTimestamp(5, Timestamp.valueOf(orderStatus.getUpdatedAt()));
        ps.executeUpdate();
        return orderStatus;
    }

    @Override
    public OrderStatus update(OrderStatus orderStatus) throws SQLException {
        String query = "update order_status set status = ?, updated_at = ? where id = ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setObject(1, orderStatus.getStatus(), Types.OTHER);
        ps.setTimestamp(2, Timestamp.valueOf(orderStatus.getUpdatedAt()));
        ps.setString(3, orderStatus.getId());
        ps.executeUpdate();
        return orderStatus;
    }

    @Override
    public OrderStatus findById(String id) throws SQLException {
        String query = "select * from order_status where id = ?";
        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return resultSetToOrderStatus(rs);
        }
        return null;
    }

    @Override
    public List<OrderStatus> findAll(Pagination pagination, Order order) throws SQLException {
        List<OrderStatus> orderStatuses = new ArrayList<>();
        String query = """
            select * from "dish_order_status"
            order by "updated_at" desc limit ? offset ?
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, pagination.getLimit());
        preparedStatement.setInt(2, pagination.getOffset());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            orderStatuses.add(resultSetToOrderStatus(rs));
        }
        return orderStatuses;
    }

    @Override
    public OrderStatus deleteById(String id) throws SQLException {
        String query = """
            delete from "order_status" where "id" = ?;
        """;

        OrderStatus toDelete = this.findById((id));
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString (1, toDelete.getId());
        prs.executeUpdate();
        return toDelete;
    }

    @Override
    public OrderStatus crupdate(OrderStatus orderStatus) throws SQLException {
        if (findById(orderStatus.getId()) != null) {
            return update(orderStatus);
        } else {
            return save(orderStatus);
        }
    }

    @Override
    public List<OrderStatus> saveAll(List<OrderStatus> list) throws SQLException {
        for(OrderStatus orderStatus: list) {
            this.crupdate(orderStatus);
        }
        return list;
    }
}
