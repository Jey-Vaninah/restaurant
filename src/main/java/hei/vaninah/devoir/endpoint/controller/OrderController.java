package hei.vaninah.devoir.endpoint.controller;

import hei.vaninah.devoir.endpoint.mapper.DishOrderMapper;
import hei.vaninah.devoir.endpoint.mapper.DishOrderStatusMapper;
import hei.vaninah.devoir.endpoint.mapper.OrderMapper;
import hei.vaninah.devoir.endpoint.rest.CreateDishOrder;
import hei.vaninah.devoir.endpoint.rest.Order;
import hei.vaninah.devoir.endpoint.rest.DishOrderStatus;
import hei.vaninah.devoir.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final DishOrderMapper dishOrderMapper;
    private final DishOrderStatusMapper dishOrderStatusMapper;

    @GetMapping("/orders/{reference}")
    public ResponseEntity<Order> findByReference(@PathVariable("reference") String reference) {
        hei.vaninah.devoir.entity.Order order = orderService.findOrderByReference(reference);

        if(order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(orderMapper.toRest(order));
    }

    @PutMapping("/orders/{reference}/dishes")
    public Order updateDishes(
            @PathVariable("reference") String reference, @RequestBody List<CreateDishOrder> dishOrders) {
        return orderMapper.toRest(
            orderService.addDishOrder(
                reference,
                dishOrders.stream().map(dishOrder -> dishOrderMapper.createToDomain(reference, dishOrder)).toList()
            )
        );
    }

    @PutMapping("/orders/{reference}/dishes/{dishId}")
    public Order updateDishStatus(
        @PathVariable("reference") String reference,
        @PathVariable("dishId") String dishId,
        @RequestBody DishOrderStatus updateRequest) {

        return orderMapper.toRest(
            orderService.updateDishOrderStatus(
                reference,
                dishOrderStatusMapper.toDomain(reference, dishId, updateRequest)
            )
        );
    }
}
