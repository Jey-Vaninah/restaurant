package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.Ingredient;
import hei.vaninah.devoir.entity.IngredientStockMovement;
import hei.vaninah.devoir.entity.PriceHistory;
import hei.vaninah.devoir.entity.Unit;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;

@Repository
public class IngredientDAO implements RestaurantManagementDAO<Ingredient> {
    private final Connection connection;
    private final PriceHistoryDAO priceHistoryDAO;
    private final IngredientStockMovementDAO ingredientStockMovementDAO;

    public IngredientDAO(Connection connection, PriceHistoryDAO priceHistoryDAO, IngredientStockMovementDAO ingredientStockMovementDAO) {
        this.connection = connection;
        this.priceHistoryDAO = priceHistoryDAO;
        this.ingredientStockMovementDAO = ingredientStockMovementDAO;
    }

    @Override
    public Ingredient findById(String id) {
        return null;
    }

    @Override
    public List<Ingredient> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public Ingredient deleteById(String id) {
        return null;
    }

    @Override
    public Ingredient save(Ingredient id) {
        return null;
    }

    @Override
    public Ingredient update(Ingredient id) {
        return null;
    }

    @Override
    public Ingredient crupdate(Ingredient id) {
        return null;
    }

    @Override
    public List<Ingredient> saveAll(List<Ingredient> list) {
        return List.of();
    }
}
