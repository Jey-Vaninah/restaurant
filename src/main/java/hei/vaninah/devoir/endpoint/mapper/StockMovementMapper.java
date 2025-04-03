package hei.vaninah.devoir.endpoint.mapper;


import hei.vaninah.devoir.entity.IngredientStockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {
    public IngredientStockMovement toDomain(hei.vaninah.devoir.endpoint.rest.StockMovement stockMovement, String idIngredient) {
        return new IngredientStockMovement(
                stockMovement.getId(),
                idIngredient,
                stockMovement.getQuantity(),
                stockMovement.getMovementDatetime(),
                stockMovement.getMovementType(),
                stockMovement.getUnit()
        );
    }
}
