package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.IngredientStockMovement;
import hei.vaninah.devoir.repository.IngredientStockMovementDAO;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StockMovementService {

    private final IngredientStockMovementDAO ingredientStockMovementDAO;

    public List<IngredientStockMovement> getAllMovements(Pagination pagination, Order order) {
        return ingredientStockMovementDAO.findAll(pagination, order);
    }

    public IngredientStockMovement getMovementById(String id) {
        return ingredientStockMovementDAO.findById(id);
    }

    public List<IngredientStockMovement> getMovementsByIngredient(String ingredientId) {
        return ingredientStockMovementDAO.findByIngredientId(ingredientId);
    }

    public IngredientStockMovement addMovement(IngredientStockMovement movement) {
        return ingredientStockMovementDAO.save(movement);
    }

    public IngredientStockMovement updateMovement(IngredientStockMovement movement) {
        return ingredientStockMovementDAO.update(movement);
    }

    public IngredientStockMovement deleteMovement(String id) {
        return ingredientStockMovementDAO.deleteById(id);
    }

    public List<IngredientStockMovement> addMultipleMovements(List<IngredientStockMovement> movements) {
        return ingredientStockMovementDAO.saveAll(movements);
    }
}
