package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.Ingredient;
import hei.vaninah.devoir.entity.IngredientStockMovement;
import hei.vaninah.devoir.entity.PriceHistory;
import hei.vaninah.devoir.repository.IngredientDAO;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    private final IngredientDAO dao;

    public IngredientService(IngredientDAO dao) {
        this.dao = dao;
    }

    public List<Ingredient> getIngredientsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pagination pagination, Order order) {
        List<Ingredient> allIngredients;
        try {
            allIngredients = dao.findAll(pagination, order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allIngredients.stream()
             .filter(ingredient -> {
                 BigDecimal price = ingredient.getUnitPrice();
                 return (minPrice == null || price.compareTo(minPrice) >= 0) &&
                        (maxPrice == null || price.compareTo(maxPrice) <= 0);
             }   )
             .collect(Collectors.toList());
    }

    public Optional<Ingredient> getIngredientById(String id) {
        try {
            return Optional.ofNullable(dao.findById(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> crupdateIngredients(List<Ingredient> ingredients) {
        try {
            return dao.saveAll(ingredients);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ingredient deleteIngredient(String id) {
        try {
            return dao.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ingredient addPriceHistories(String ingredientId, List<PriceHistory> priceHistories) {
        Ingredient ingredient;
        try {
            ingredient = dao.findById(ingredientId);
            if(ingredient == null) {
                throw new RuntimeException("Ingredient not found");
            }
            ingredient.addPriceHistories(priceHistories);
            return dao.crupdate(ingredient);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ingredient addStockMovements(String ingredientId, List<IngredientStockMovement> stockMovements) {
        Ingredient ingredient;
        try {
            ingredient = dao.findById(ingredientId);
            if(ingredient == null) {
                throw new RuntimeException("Ingredient not found");
            }
            ingredient.addStockMovements(stockMovements);
            return dao.crupdate(ingredient);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
