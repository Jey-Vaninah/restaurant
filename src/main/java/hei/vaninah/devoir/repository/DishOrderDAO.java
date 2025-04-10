package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.Dish;
import hei.vaninah.devoir.entity.DishOrder;
import hei.vaninah.devoir.entity.DishOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderDAO implements RestaurantManagementDAO<DishOrder> {
    private final Connection connection;
    private final DishOrderStatusDAO dishOrderStatusDAO;
    private final DishDAO dishDAO;

    private DishOrder resultSetToDishOrder(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        Dish dish = dishDAO.findById(rs.getString("id_dish"));
        List<DishOrderStatus> dishOrderStatuses = dishOrderStatusDAO.findByDishOrderId(id);

        return new DishOrder(
            id,
            rs.getString("id_order"),
            dish,
            rs.getInt("quantity"),
            dishOrderStatuses
        );
    }

    public List<DishOrder> findByOrderId(String orderId) throws SQLException {
        List<DishOrder> dishOrders = new ArrayList<>();
        String query = """
            select * from "dish_order" where id_order = ? order by id ;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, orderId);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            dishOrders.add(resultSetToDishOrder(rs));
        }
        return dishOrders;
    }

    @Override
    public DishOrder save(DishOrder dishOrder) throws SQLException {
        String query = "insert into dish_order(id, id_order, id_dish, quantity) values (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, dishOrder.getId());
        stmt.setString(2, dishOrder.getOrderId());
        stmt.setString(3, dishOrder.getDish().getId());
        stmt.setInt(4, dishOrder.getQuantity());
        stmt.executeUpdate();
        this.dishOrderStatusDAO.saveAll(dishOrder.getDishOrderStatus());
        return dishOrder;
    }

    @Override
    public DishOrder findById(String id) throws SQLException {
        String query = """
            select * from dish_order where id = ?;
         """;
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return resultSetToDishOrder(rs);
        }
        return null;
    }

    @Override
    public List<DishOrder> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public DishOrder deleteById(String id) {
        return null;
    }

    @Override
    public DishOrder update(DishOrder toUpdate) throws SQLException {
        String query = """
            update "dish_order"
                set "id_order" = ?,
                    "id_dish" = ? ,
                    "quantity" = ?
                where "id" = ?
        """;
        PreparedStatement prs = connection.prepareStatement(query);
        prs.setString (1, toUpdate.getOrderId());
        prs.setString(2, toUpdate.getDish().getId());
        prs.setInt(3, toUpdate.getQuantity());
        prs.setString(4, toUpdate.getId());
        prs.executeUpdate();
        this.dishOrderStatusDAO.saveAll(toUpdate.getDishOrderStatus());
        return this.findById(toUpdate.getId());
    }

    @Override
    public DishOrder crupdate(DishOrder crupdateDishOrder) throws SQLException {
        final boolean isCreate = this.findById(crupdateDishOrder.getId()) == null;
        if (isCreate) {
            return this.save(crupdateDishOrder);
        }
        return this.update(crupdateDishOrder);
    }

    @Override
    public List<DishOrder> saveAll(List<DishOrder> list) throws SQLException {
        for (DishOrder dishOrder : list) {
            this.crupdate(dishOrder);
        }
        return list;
    }

    public List<DishOrder> findByDishIdAndOrderDateRange(String dishId, LocalDateTime dateDebut, LocalDateTime dateFin) throws SQLException {
        List<DishOrder> dishOrders = new ArrayList<>();
        String query = """
            select dish_order.id, dish_order.id_order, dish_order.id_dish, dish_order.quantity
            from dish_order
            join "order" on dish_order.id_order = "order".id
            where dish_order.id_dish = ? 
            and "order".created_at between ? and ?
            order by dish_order.id;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, dishId);
        preparedStatement.setObject(2, dateDebut);
        preparedStatement.setObject(3, dateFin);

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            dishOrders.add(resultSetToDishOrder(rs));
        }

        return dishOrders;
    }
}
