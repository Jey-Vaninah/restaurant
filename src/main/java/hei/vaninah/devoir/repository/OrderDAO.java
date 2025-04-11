package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.DishOrder;
import hei.vaninah.devoir.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<hei.vaninah.devoir.entity.Order> findAllInRange(LocalDateTime from, LocalDateTime to) throws SQLException {
        String query = """
            select * FROM "order"
            where "created_at" between ? and ?
        """;
        List<hei.vaninah.devoir.entity.Order> orders = new ArrayList<>();
        PreparedStatement st = connection.prepareStatement(query);
        st.setTimestamp(1, Timestamp.valueOf(from));
        st.setTimestamp(2, Timestamp.valueOf(to));
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            orders.add(resultSetToOrder(rs));
        }
        return orders;
    }

    public hei.vaninah.devoir.entity.Order findByReference(String ref) throws SQLException {
        String query = """
            select * from "order" where reference = ?;
        """;

        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, ref);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return resultSetToOrder(rs);
        }
        return null;
    }

    @Override
    public hei.vaninah.devoir.entity.Order findById(String id) throws SQLException {
        String query = """
            select * from "order" where id = ?
        """;
        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return resultSetToOrder(rs);
        }
        return null;
    }

    @Override
    public List<hei.vaninah.devoir.entity.Order> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public hei.vaninah.devoir.entity.Order deleteById(String id) throws SQLException {
        String query = """
            delete from "order" where "id" = ?;
        """;

        hei.vaninah.devoir.entity.Order toDelete = this.findById((id));
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString (1, toDelete.getId());
        prs.executeUpdate();
        return toDelete;
    }

    @Override
    public hei.vaninah.devoir.entity.Order save(hei.vaninah.devoir.entity.Order order) throws SQLException {
        String query = """
            insert into "order" (id ,reference, updated_at, created_at) values (?, ?, ?, ?);
        """;

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, order.getId());
        stmt.setString(2, order.getReference());
        stmt.setTimestamp(3, Timestamp.valueOf(order.getUpdatedAt()));
        stmt.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt()));
        stmt.executeUpdate();

        orderStatusDAO.saveAll(order.getStatusHistories());
        dishOrderDAO.saveAll(order.getDishOrders());
        return order;
    }

    @Override
    public hei.vaninah.devoir.entity.Order update(hei.vaninah.devoir.entity.Order toUpdate) throws SQLException {
        String query = """
            update "order"
                set "reference" = ?,
                    "created_at" = ?, 
                    "updated_at" = ?
                where "id" = ?
        """;
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString(1, toUpdate.getReference());
        prs.setTimestamp(2, Timestamp.valueOf(toUpdate.getCreatedAt()));
        prs.setTimestamp(3, Timestamp.valueOf(toUpdate.getUpdatedAt()));
        prs.setString(4, toUpdate.getId());
        prs.executeUpdate();

        orderStatusDAO.saveAll(toUpdate.getStatusHistories());
        dishOrderDAO.saveAll(toUpdate.getDishOrders());
        return this.findById(toUpdate.getId());
    }

    @Override
    public hei.vaninah.devoir.entity.Order crupdate(hei.vaninah.devoir.entity.Order crupdateOrder) throws SQLException {
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
}
