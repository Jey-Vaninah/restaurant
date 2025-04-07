package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.endpoint.rest.DishOrder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DishOrderMapper {
    private final DishMapper dishMapper;

    public DishOrder toRest(hei.vaninah.devoir.entity.DishOrder dishOrder) {
        return new DishOrder(
            dishOrder.getId(),
            dishOrder.getOrderId(),
            dishMapper.toRest(dishOrder.getDish()),
            dishOrder.getQuantity(),
            dishOrder.getActualStatus()
        );
    }
}
