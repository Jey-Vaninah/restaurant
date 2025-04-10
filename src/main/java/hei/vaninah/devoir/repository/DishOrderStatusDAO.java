package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.DishOrderStatus;
import hei.vaninah.devoir.entity.StatusHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
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

    public List<DishOrderStatus> findByDishOrderId(String dishOrderId) {
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();
        String query = """
            select * from "dish_order_status" where id_dish_order = ? order by "id" asc
        """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dishOrderId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                dishOrderStatuses.add(resultSetToDishOrderStatus(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishOrderStatuses;
    }

    public void createDishOrderStatus(DishOrderStatus dishOrderStatus) throws SQLException {
        String query = "INSERT INTO dish_order_status (id, id_dish_order, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, dishOrderStatus.getId());
        ps.setString(2, dishOrderStatus.getDishOrderId());
        ps.setObject(3, dishOrderStatus.getStatus(), Types.OTHER);
        ps.setTimestamp(4, Timestamp.valueOf(dishOrderStatus.getCreatedAt()));
        ps.setTimestamp(5, Timestamp.valueOf(dishOrderStatus.getUpdatedAt()));
        ps.executeUpdate();
    }

    public void updateDishOrderStatus(String id, StatusHistory newStatus, LocalDateTime updatedAt) throws SQLException {
        String query = "UPDATE dish_order_status SET status = ?, updated_at = ? WHERE id = ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, newStatus.name());
        ps.setTimestamp(2, Timestamp.valueOf(updatedAt));
        ps.setString(3, id);

        ps.executeUpdate();
    }

    @Override
    public DishOrderStatus findById(String id) {
        return null;
    }

    @Override
    public List<DishOrderStatus> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public DishOrderStatus deleteById(String id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public DishOrderStatus save(DishOrderStatus id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public DishOrderStatus update(DishOrderStatus id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public DishOrderStatus crupdate(DishOrderStatus id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public List<DishOrderStatus> saveAll(List<DishOrderStatus> list) {
        return List.of();
    }
}
