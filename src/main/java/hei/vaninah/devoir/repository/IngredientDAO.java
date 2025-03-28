package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.Ingredient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IngredientDAO implements RestaurantManagementDAO<Ingredient> {

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
