package hei.vaninah.devoir.endpoint.rest.mapper;

import hei.vaninah.devoir.endpoint.rest.model.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {
    public Ingredient toRest(hei.vaninah.devoir.entity.Ingredient ingredient) {
        return new Ingredient(
            ingredient.getId() ,
            ingredient.getName(),
            ingredient.getUpdateDatetime(),
            ingredient.getUnitPrice() ,
            ingredient.getUnit()
        );
    }
}
