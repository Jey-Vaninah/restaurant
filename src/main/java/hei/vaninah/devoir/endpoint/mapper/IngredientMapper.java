package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.endpoint.rest.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {
    public Ingredient toRest(hei.vaninah.devoir.entity.Ingredient ingredient) {
        return new Ingredient(
            ingredient.getId() ,
            ingredient.getName(),
            ingredient.getUnitPrice(),
            ingredient.getUpdateDatetime(),
            ingredient.getUnit(),
            ingredient.getCost(),
            ingredient.getCurrentStock()
        );
    }
}


