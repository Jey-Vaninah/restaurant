package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.endpoint.rest.DishOrder;
import hei.vaninah.devoir.endpoint.rest.Order;
import hei.vaninah.devoir.repository.DishDAO;
import hei.vaninah.devoir.repository.DishOrderStatusDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderMapper {
    private final DishOrderMapper dishOrderMapper;
    private final DishDAO dishDAO;
    private final DishOrderStatusDAO dishOrderStatusDAO;

    public Order toRest(hei.vaninah.devoir.entity.Order order) {
        return new Order(
            order.getId(),
            order.getReference(),
            order.getCreatedAt(),
            order.getUpdatedAt(),
            order.getActualStatus(),
            order.getDishOrders().stream().map(dishOrderMapper::toRest).toList()
        );
    }
}
