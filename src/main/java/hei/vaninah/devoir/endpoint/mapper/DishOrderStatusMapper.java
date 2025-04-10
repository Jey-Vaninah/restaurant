package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.entity.DishOrder;
import hei.vaninah.devoir.entity.DishOrderStatus;
import hei.vaninah.devoir.entity.Order;
import hei.vaninah.devoir.repository.OrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Component
@RequiredArgsConstructor
public class DishOrderStatusMapper {
    private final OrderDAO orderDAO;

    public DishOrderStatus toDomain(String orderReference, String dishId, hei.vaninah.devoir.endpoint.rest.DishOrderStatus status){
        Order order = orderDAO.findByReference(orderReference);
        DishOrder dishOrder = order.getDishOrders().stream().filter(orderDish -> orderDish.getDish().getId().equals(dishId)).findFirst().orElseThrow();

        return new DishOrderStatus(
            randomUUID().toString(),
            dishOrder.getOrderId(),
            status.getActualStatus(),
            now(),
            now()
        );
    }
}
