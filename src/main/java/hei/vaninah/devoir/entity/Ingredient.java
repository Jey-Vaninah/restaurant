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
public class Ingredient {
    private String id;
    private String name;
    private LocalDateTime updateDatetime;
    private BigDecimal unitPrice;
    private Unit unit;
    private List<PriceHistory> priceHistories;
    private List<IngredientStockMovement> ingredientStockMovements;

    public Float getAvailableQuantity(LocalDateTime datetime){
        return this.ingredientStockMovements
                .stream()
                .filter(
                        im -> im.movementDatetime().isBefore(datetime.plusSeconds(1))
                )
                .map(im -> {
                    int multiply = im.movementType().equals(IngredientStockMovementType.IN) ? 1 : -1;
                    return im.quantity() * multiply;
                })
                .reduce((float) 0, Float::sum);
    }

    public BigDecimal getCost(LocalDateTime datetime){
        return this.priceHistories
                .stream()
                .filter(priceHistory -> priceHistory.getPriceDatetime().isBefore(datetime.plusSeconds(1)))
                .sorted(
                        (a, b) -> b.getPriceDatetime().compareTo(a.getPriceDatetime())
                )
                .map(PriceHistory::getUnitPrice)
                .findFirst()
                .orElse(this.getUnitPrice());
    }

    public BigDecimal getCost(){
        return this.getCost(LocalDateTime.now());
    }

    public Float getAvailableQuantity(){
        return this.getAvailableQuantity(LocalDateTime.now());
    }

    public Float getCurrentStock(LocalDateTime datetime) {
        return this.ingredientStockMovements
                .stream()
                .filter(ism -> ism.movementDatetime().isBefore(datetime.plusSeconds(1)))
                .map(im -> {
                    int multiply = im.movementType().equals(IngredientStockMovementType.IN) ? 1 : -1;
                    return im.quantity() * multiply;
                })
                .reduce((float) 0, Float::sum);
    }

    public Float getCurrentStock(){
        return this.getCurrentStock(LocalDateTime.now());
    }

    public void addPriceHistories(List<PriceHistory> priceHistories){
        this.priceHistories.addAll(priceHistories);
    }

    public void addStockMovements(List<IngredientStockMovement> stockMovements){
        this.ingredientStockMovements.addAll(stockMovements);
    }
}
