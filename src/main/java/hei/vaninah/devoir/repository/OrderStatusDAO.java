package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.OrderStatus;
import hei.vaninah.devoir.entity.StatusHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
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

    public List<OrderStatus> findByOrderId(String orderId) {
        List<OrderStatus> orderStatuses = new ArrayList<>();
        String query = """
            select * from "order_status" where id_order = ? order by id asc
        """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, orderId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                orderStatuses.add(resultSetToOrderStatus(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderStatuses;
    }

    public void insertOrderStatus(OrderStatus orderStatus) throws SQLException {
        String query = "INSERT INTO order_status (id, id_order, status, updated_at, created_date) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, orderStatus.getId());
        ps.setString(2, orderStatus.getIdOrder());
        ps.setObject(3, orderStatus.getStatus(), Types.OTHER); // Convertir en chaîne
        ps.setTimestamp(4, Timestamp.valueOf(orderStatus.getUpdatedAt()));
        ps.setTimestamp(5, Timestamp.valueOf(orderStatus.getUpdatedAt()));
        ps.executeUpdate();
    }

    public void updateOrderStatus(String id, StatusHistory newStatus, LocalDateTime updatedAt) throws SQLException {
        String query = "UPDATE order_status SET status = ?, updated_at = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, newStatus.name());
            ps.setTimestamp(2, Timestamp.valueOf(updatedAt));
            ps.setString(3, id);

            ps.executeUpdate();
        }
    }

    @Override
    public OrderStatus findById(String id) {
        return null;
    }

    @Override
    public List<OrderStatus> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public OrderStatus deleteById(String id) {
        return null;
    }

    @Override
    public OrderStatus save(OrderStatus id) {
        return null;
    }

    @Override
    public OrderStatus update(OrderStatus id) {
        return null;
    }

    @Override
    public OrderStatus crupdate(OrderStatus id) {
        return null;
    }

    @Override
    public List<OrderStatus> saveAll(List<OrderStatus> list) {
        return List.of();
    }
}
