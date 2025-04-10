package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.entity.IngredientStockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {
    public IngredientStockMovement toDomain(String idIngredient, hei.vaninah.devoir.endpoint.rest.StockMovement stockMovement) {
        return new IngredientStockMovement(
            stockMovement.getId(),
            idIngredient,
            stockMovement.getQuantity(),
            stockMovement.getMovementDatetime(),
            stockMovement.getMovementType(),
            stockMovement.getUnit()
        );
    }

    public hei.vaninah.devoir.endpoint.rest.StockMovement toRest(IngredientStockMovement stockMovement) {
        return new hei.vaninah.devoir.endpoint.rest.StockMovement(
            stockMovement.id(),
            stockMovement.quantity(),
            stockMovement.movementDatetime(),
            stockMovement.movementType(),
            stockMovement.unit()
        );
    }
}
