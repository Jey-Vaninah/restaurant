package hei.vaninah.devoir.endpoint.rest;

import hei.vaninah.devoir.entity.DishOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishOrder {
    private String id;
    private String orderId;
    private Dish dish;
    private int quantity;
    private DishOrderStatus actualStatus;
}
