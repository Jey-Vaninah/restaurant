package hei.vaninah.devoir.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private String id;
    private String name;
    private BigDecimal unitPrice;
    private List<Ingredient> ingredients;
    private List<DishIngredient> dishIngredients;
}
