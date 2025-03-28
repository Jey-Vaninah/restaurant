package hei.vaninah.devoir.entity;

import java.time.LocalDateTime;

public record IngredientStockMovement(
        String id,
        String idIngredient,
        Float quantity,
        LocalDateTime movementDatetime,
        IngredientStockMovementType movementType,
        Unit unit
) { }