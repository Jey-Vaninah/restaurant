package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.endpoint.rest.Dish;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DishMapper {
    private IngredientMapper ingredientMapper;

    public Dish toRest(hei.vaninah.devoir.entity.Dish dish){
        return new Dish(
            dish.getId(),
            dish.getName(),
            dish.getMaxPossibleDishes(),
            dish.getIngredients().stream().map(ingredientMapper::toRest).toList(),
            dish.getUnitPrice()
        );
    }
}