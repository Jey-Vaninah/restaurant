package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.endpoint.rest.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderMapper {
    private final DishOrderMapper dishOrderMapper;

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

