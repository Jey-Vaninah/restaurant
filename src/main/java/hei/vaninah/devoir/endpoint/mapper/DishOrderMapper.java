package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.endpoint.rest.CreateDishOrder;
import hei.vaninah.devoir.endpoint.rest.DishOrder;
import hei.vaninah.devoir.repository.DishDAO;
import hei.vaninah.devoir.repository.DishOrderStatusDAO;
import hei.vaninah.devoir.repository.OrderDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DishOrderMapper {
    private final DishMapper dishMapper;
    private final DishDAO dishDAO;
    private final DishOrderStatusDAO dishOrderStatusDAO;
    private final OrderDAO orderDAO;

    public DishOrder toRest(hei.vaninah.devoir.entity.DishOrder dishOrder) {
        return new DishOrder(
            dishOrder.getId(),
            dishOrder.getOrderId(),
            dishMapper.toRest(dishOrder.getDish()),
            dishOrder.getQuantity(),
            dishOrder.getActualStatus()
        );
    }

    public hei.vaninah.devoir.entity.DishOrder createToDomain(String reference, CreateDishOrder createDishOrder)
    {
        return new hei.vaninah.devoir.entity.DishOrder(
            createDishOrder.getId(),
            orderDAO.findByReference(reference).getId(),
            dishDAO.findById(createDishOrder.getDishId()),
            createDishOrder.getQuantity(),
            dishOrderStatusDAO.findByDishOrderId(createDishOrder.getId())
        );
    }
}
