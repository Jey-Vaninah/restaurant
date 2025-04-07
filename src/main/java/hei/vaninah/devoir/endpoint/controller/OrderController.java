package hei.vaninah.devoir.endpoint.controller;

import hei.vaninah.devoir.endpoint.mapper.OrderMapper;
import hei.vaninah.devoir.endpoint.rest.Order;
import hei.vaninah.devoir.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/orders/{reference}")
    public ResponseEntity<Order> findByReference(@PathVariable("reference") String reference) {
        hei.vaninah.devoir.entity.Order order = orderService.findOrderByReference(reference);

        if(order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(orderMapper.toRest(order));
    }
}
