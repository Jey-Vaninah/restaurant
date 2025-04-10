package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.endpoint.rest.Ingredient;
import hei.vaninah.devoir.repository.IngredientDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IngredientMapper {
    private final IngredientDAO ingredientDAO;

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

    public hei.vaninah.devoir.entity.Ingredient createToDomain(Ingredient ingredient) {
        return new hei.vaninah.devoir.entity.Ingredient(
            ingredient.getId(),
            ingredient.getName(),
            ingredient.getUpdateDatetime(),
            ingredient.getUnitPrice(),
            ingredient.getUnit(),
            List.of(),
            List.of()
        );
    }
}


