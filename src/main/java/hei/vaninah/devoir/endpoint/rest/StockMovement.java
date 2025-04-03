package hei.vaninah.devoir.endpoint.rest;

import hei.vaninah.devoir.entity.IngredientStockMovementType;
import hei.vaninah.devoir.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockMovement {
    String id;
    Float quantity;
    LocalDateTime movementDatetime;
    IngredientStockMovementType movementType;
    Unit unit;
}
