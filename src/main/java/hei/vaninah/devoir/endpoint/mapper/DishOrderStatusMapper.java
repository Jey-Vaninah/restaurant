package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.entity.DishOrder;
import hei.vaninah.devoir.entity.DishOrderStatus;
import hei.vaninah.devoir.entity.Order;
import hei.vaninah.devoir.repository.OrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Component
@RequiredArgsConstructor
public class DishOrderStatusMapper {
    private final OrderDAO orderDAO;

    public DishOrderStatus toDomain(String orderReference, String dishId, hei.vaninah.devoir.endpoint.rest.DishOrderStatus status){
        Order order;
        try {
            order = orderDAO.findByReference(orderReference);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DishOrder dishOrder = order.getDishOrders().stream().filter(orderDish -> orderDish.getDish().getId().equals(dishId)).findFirst().orElseThrow();

        return new DishOrderStatus(
            randomUUID().toString(),
            dishOrder.getId(),
            status.getActualStatus(),
            now(),
            now()
        );
    }
}
