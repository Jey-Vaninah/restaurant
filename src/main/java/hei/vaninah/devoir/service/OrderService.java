package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.*;
import hei.vaninah.devoir.repository.DishOrderDAO;
import hei.vaninah.devoir.repository.DishOrderStatusDAO;
import hei.vaninah.devoir.repository.OrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO dao;
    private final DishOrderDAO dishOrderDAO;
    private final DishOrderStatusDAO dishOrderStatusDAO;

    public Order findOrderByReference(String reference) {
        try {
            return dao.findByReference(reference);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order addDishOrder(String orderReference ,List<DishOrder> dishOrders){
        try {
            Order order = dao.findByReference(orderReference);
            order.addDishOrders(dishOrders);
            return dao.update(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order updateDishOrderStatus(String orderReference, DishOrderStatus dishOrderStatus) {
        try {
            dishOrderStatusDAO.save(dishOrderStatus);
            return dao.findByReference(orderReference);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
