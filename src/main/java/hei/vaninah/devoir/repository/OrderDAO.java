package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.DishOrder;
import hei.vaninah.devoir.entity.OrderStatus;
import hei.vaninah.devoir.entity.StatusHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hei.vaninah.devoir.entity.StatusHistory.CREATED;

@Repository
@RequiredArgsConstructor
public class OrderDAO implements RestaurantManagementDAO<hei.vaninah.devoir.entity.Order> {
    private final Connection connection;
    private final OrderStatusDAO orderStatusDAO;
    private final DishOrderDAO dishOrderDAO;

    private hei.vaninah.devoir.entity.Order resultSetToOrder(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        List<OrderStatus> orderStatuses = orderStatusDAO.findByOrderId(id);
        List<DishOrder> dishOrders = dishOrderDAO.findByOrderId(id);

        return new hei.vaninah.devoir.entity.Order(
                id,
                rs.getString("reference"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                new ArrayList<>(dishOrders),
                new ArrayList<>(orderStatuses)
        );
    }

    public hei.vaninah.devoir.entity.Order findByReference(String ref) {
        String query = """
            select * from "order" where reference = ?;
        """;

        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, ref);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return resultSetToOrder(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateOrderStatus(String idOrder, StatusHistory newStatus) throws SQLException {
        String query = "UPDATE order SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newStatus.name());
            stmt.setString(2, idOrder);
            stmt.executeUpdate();
        }
    }

    public void addDishToOrder(hei.vaninah.devoir.entity.Order order, DishOrder dishOrder) throws SQLException {
        if (order.isOrderConfirmed()) {
            throw new IllegalStateException("Impossible de modifier une commande confirmée.");
        }
        order.checkIngredientsAvailable();

        dishOrderDAO.save(dishOrder);
    }

    private boolean isOrderConfirmed(String orderId) throws SQLException {
        String query = "select count(*) from order_status where id_order = ? and status = 'confirmed'";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public hei.vaninah.devoir.entity.Order findById(String id) {
        return null;
    }

    @Override
    public List<hei.vaninah.devoir.entity.Order> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public hei.vaninah.devoir.entity.Order deleteById(String id) {
        return null;
    }

    @Override
    public hei.vaninah.devoir.entity.Order save(hei.vaninah.devoir.entity.Order order) {
        String query = """
            insert into "order" (id ,reference, updated_at, created_at) values (?, ?, ?, ?);
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, order.getId());
            stmt.setString(2, order.getReference());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getUpdatedAt()));
            stmt.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt()));
            stmt.executeUpdate();

            OrderStatus createdStatus = new OrderStatus(order.getId(), "O001", CREATED, order.getUpdatedAt(), order.getUpdatedAt());
            orderStatusDAO.save(createdStatus);

            for (DishOrder dishOrder : order.getDishOrders()) {
                dishOrderDAO.save(dishOrder);
            }
            return this.findById(order.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public hei.vaninah.devoir.entity.Order update(hei.vaninah.devoir.entity.Order toUpdate) {
        String query = """
            update "order" 
                set "reference" = ?, 
                    "created_at" = ? , 
                    "updated_at" = ? ,
                where "id" = ?
        """;
        try {
            PreparedStatement prs = connection.prepareStatement(query);
            prs.setString(1, toUpdate.getReference());
            prs.setTimestamp(2, Timestamp.valueOf(toUpdate.getCreatedAt()));
            prs.setTimestamp(3, Timestamp.valueOf(toUpdate.getUpdatedAt()));
            prs.executeUpdate();
            return this.findById(toUpdate.getId());
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }

    @Override
    public hei.vaninah.devoir.entity.Order crupdate(hei.vaninah.devoir.entity.Order crupdateOrder) {
        final boolean isCreate = this.findById(crupdateOrder.getId()) == null;
        if (isCreate) {
            return this.save(crupdateOrder);
        }
        return this.update(crupdateOrder);
    }

    @Override
    public List<hei.vaninah.devoir.entity.Order> saveAll(List<hei.vaninah.devoir.entity.Order> list) {
        return List.of();
    }

    public long getDishProcessingTime(String dishOrderId, LocalDateTime startDate, LocalDateTime endDate, String timeUnit, String durationType) {
        // Vous pouvez ici ajouter la logique de calcul de la durée, en fonction des dates et de l'unité de temps.
        // Exemple simple : retourner la différence en secondes.
        long durationInSeconds = Duration.between(startDate, endDate).getSeconds();

        // Ajuster la durée selon l'unité et le type de durée, par exemple convertir en minutes ou heures selon le cas.
        switch (timeUnit) {
            case "minutes":
                return durationInSeconds / 60;
            case "hours":
                return durationInSeconds / 3600;
            default:
                return durationInSeconds;
        }
    }
}
