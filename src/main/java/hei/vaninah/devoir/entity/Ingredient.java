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

    public Ingredient(String id, String name, LocalDateTime updateDatetime, BigDecimal unitPrice, Unit unit, List<PriceHistory> priceHistories, List<IngredientStockMovement> ingredientStockMovements) {
        this.id = id;
        this.name = name;
        this.updateDatetime = updateDatetime;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.priceHistories = priceHistories;
        this.ingredientStockMovements = ingredientStockMovements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(LocalDateTime updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public List<PriceHistory> getPriceHistories() {
        return priceHistories;
    }

    public void setPriceHistories(List<PriceHistory> priceHistories) {
        this.priceHistories = priceHistories;
    }

    public List<IngredientStockMovement> getIngredientStockMovements() {
        return ingredientStockMovements;
    }

    public void setIngredientStockMovements(List<IngredientStockMovement> ingredientStockMovements) {
        this.ingredientStockMovements = ingredientStockMovements;
    }

}
