package hei.vaninah.devoir.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public BigDecimal getGrossMargin(){
        return this.getGrossMargin(LocalDateTime.now());
    }

    public BigDecimal getGrossMargin(LocalDateTime datetime){
        return this.unitPrice.subtract(this.getIngredientCosts(datetime));
    }

    public BigDecimal getIngredientCosts(){
        return this.getIngredientCosts(LocalDateTime.now());
    }

    public BigDecimal getIngredientCosts(LocalDateTime datetime){
        return this.ingredients
                .stream()
                .map(ingredient -> {
                    BigDecimal unitCost = ingredient.getCost(datetime);
                    DishIngredient dishIngredient = this.dishIngredients
                            .stream()
                            .filter(
                                    di -> di.getIdIngredient().equals(ingredient.getId())
                            )
                            .findFirst()
                            .orElseThrow();
                    return unitCost.multiply(BigDecimal.valueOf(dishIngredient.getRequiredQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getMaxPossibleDishes() {
        return this.dishIngredients.stream()
                .mapToInt(dishIngredient -> {
                    Ingredient ingredient = this.ingredients.stream()
                            .filter(i -> i.getId().equals(dishIngredient.getIdIngredient()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Ingrédient introuvable : " + dishIngredient.getIdIngredient()));

                    float availableQuantity = ingredient.getAvailableQuantity();
                    float requiredQuantity = dishIngredient.getRequiredQuantity();

                    return (int) (availableQuantity / requiredQuantity);
                })
                .min()
                .orElse(0);
    }

}
