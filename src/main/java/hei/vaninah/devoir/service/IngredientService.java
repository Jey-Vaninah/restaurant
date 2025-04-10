package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.Ingredient;
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

    public List<Ingredient> addIngredients(List<Ingredient> ingredients) {
        try {
            return dao.saveAll(ingredients);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> updateIngredients(List<Ingredient> ingredients) {
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
}
