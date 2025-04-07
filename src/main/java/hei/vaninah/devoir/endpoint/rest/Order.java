package hei.vaninah.devoir.endpoint.rest;

import hei.vaninah.devoir.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Order {
    private String id;
    private String reference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderStatus actualStatus;
    private List<DishOrder> dishOrders;
}