package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.endpoint.rest.DishIngredient;
import org.springframework.stereotype.Component;

@Component
public class DishIngredientMapper {
    public hei.vaninah.devoir.entity.DishIngredient toDomain(String idDish, DishIngredient dishIngredient){
        return new hei.vaninah.devoir.entity.DishIngredient(
            idDish,
            dishIngredient.getIdIngredient(),
            dishIngredient.getRequiredQuantity(),
            dishIngredient.getUnit()
        );
    }
}
