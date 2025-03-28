package hei.vaninah.devoir.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Ingredient {
    private String id;
    private String name;
    private LocalDateTime updateDatetime;
    private BigDecimal unitPrice;
    private Unit unit;
    private List<PriceHistory> priceHistories;
    private List<IngredientStockMovement> ingredientStockMovements;

}
