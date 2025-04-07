package hei.vaninah.devoir.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private String id;
    private String name;
    private Integer availableQuantity;
    private List<Ingredient> ingredients;
}


