package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.Dish;
import hei.vaninah.devoir.entity.DishIngredient;
import hei.vaninah.devoir.entity.DishOrder;
import hei.vaninah.devoir.entity.Order;
import hei.vaninah.devoir.repository.DishIngredientDAO;
import hei.vaninah.devoir.repository.DishOrderDAO;
import hei.vaninah.devoir.repository.OrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO dao;
    private final DishOrderDAO dishOrderDAO;

    public Optional<hei.vaninah.devoir.entity.Order> getDishById(String id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public List<hei.vaninah.devoir.entity.Order> addIngredients(List<hei.vaninah.devoir.entity.Order> orders) {
        return dao.saveAll(orders);
    }

    public List<hei.vaninah.devoir.entity.Order> updateIngredients(List<hei.vaninah.devoir.entity.Order> orders) {
        return dao.saveAll(orders);
    }

    public hei.vaninah.devoir.entity.Order deleteOrder(String id) {
        return dao.deleteById(id);
    }

    public hei.vaninah.devoir.entity.Order findOrderByReference(String reference) {
        return dao.findByReference(reference);
    }

    public Order addDishOrder(String orderReference ,List<DishOrder> dishOrders){
       dishOrderDAO.saveAll(dishOrders);
       return dao.findByReference(orderReference);
    }

    public Order updateDishStatus(String orderReference, DishOrder dishOrder){
         dishOrderDAO.update(dishOrder);
        return dao.findByReference(orderReference);
    }
}
