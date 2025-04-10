package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.IngredientStockMovement;
import hei.vaninah.devoir.repository.IngredientStockMovementDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@AllArgsConstructor
public class StockMovementService {
    private final IngredientStockMovementDAO ingredientStockMovementDAO;

    public List<IngredientStockMovement> addMultipleMovements(List<IngredientStockMovement> movements) {
        try {
            return ingredientStockMovementDAO.saveAll(movements);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
