package hei.vaninah.devoir.entity;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DishIngredient {
    private String idDish;
    private String idIngredient;
    private Float requiredQuantity;
    private Unit unit;
}
