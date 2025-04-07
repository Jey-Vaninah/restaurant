package hei.vaninah.devoir.service;


import hei.vaninah.devoir.entity.Dish;
import hei.vaninah.devoir.entity.DishIngredient;
import hei.vaninah.devoir.repository.DishDAO;
import hei.vaninah.devoir.repository.DishIngredientDAO;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class DishService {
    private final DishDAO dao;
    private final DishIngredientDAO dishIngredientDAO;

    public List<Dish> getAll(){
        return dao.findAll(new Pagination(1, 500), new Order("name", Order.OrderValue.ASC));
    }

    public Optional<Dish> getDishById(String id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public List<Dish> addIngredients(List<Dish> dishes) {
        return dao.saveAll(dishes);
    }

    public List<Dish> updateIngredients(List<Dish> dishes) {
        return dao.saveAll(dishes);
    }

    public Dish deleteDish(String id) {
        return dao.deleteById(id);
    }

    public Dish addDishIngredient(String id, List<DishIngredient> dishIngredients) {
        dishIngredientDAO.saveAll(dishIngredients);
        return dao.findById(id);
    }
}
