package hei.vaninah.devoir.service;


import hei.vaninah.devoir.entity.Dish;
import hei.vaninah.devoir.entity.DishIngredient;
import hei.vaninah.devoir.repository.DishDAO;
import hei.vaninah.devoir.repository.DishIngredientDAO;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Data
public class DishService {
    private final DishDAO dao;
    private final DishIngredientDAO dishIngredientDAO;

    public List<Dish> getAll(){
        try {
            return dao.findAll(new Pagination(1, 500), new Order("name", Order.OrderValue.ASC));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish addDishIngredient(String id, List<DishIngredient> dishIngredients) {
        try {
            Dish dish = dao.findById(id);
            dish.addDishIngredients(dishIngredients);
            return dao.crupdate(dish);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
