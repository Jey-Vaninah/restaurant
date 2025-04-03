package hei.vaninah.devoir.service;


import hei.vaninah.devoir.entity.Dish;
import hei.vaninah.devoir.repository.DishDAO;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class DishService {
    private final DishDAO dao;

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
}
