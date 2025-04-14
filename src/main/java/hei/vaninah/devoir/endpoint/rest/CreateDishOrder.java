package hei.vaninah.devoir.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDishOrder {
    private String id;
    private String dishId;
    private int quantity;
}
