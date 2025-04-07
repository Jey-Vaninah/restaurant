package hei.vaninah.devoir.endpoint.rest;

import hei.vaninah.devoir.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishIngredient {
    private Float requiredQuantity;
    private Unit unit;
    private String idIngredient;
}
