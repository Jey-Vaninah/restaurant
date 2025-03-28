package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.IngredientStockMovement;

import java.sql.Connection;
import java.util.List;

public class IngredientStockMovementDAO implements RestaurantManagementDAO<IngredientStockMovement> {
    private final Connection connection;

    public IngredientStockMovementDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public IngredientStockMovement findById(String id) {
        return null;
    }

    @Override
    public List<IngredientStockMovement> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public IngredientStockMovement deleteById(String id) {
        return null;
    }

    @Override
    public IngredientStockMovement save(IngredientStockMovement id) {
        return null;
    }

    @Override
    public IngredientStockMovement update(IngredientStockMovement id) {
        return null;
    }

    @Override
    public IngredientStockMovement crupdate(IngredientStockMovement id) {
        return null;
    }

    @Override
    public List<IngredientStockMovement> saveAll(List<IngredientStockMovement> list) {
        return List.of();
    }
}
