package hei.vaninah.devoir.endpoint.controller;

import hei.vaninah.devoir.endpoint.mapper.DishMapper;
import hei.vaninah.devoir.endpoint.rest.Dish;
import hei.vaninah.devoir.service.DishService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class DishRestController {
    private DishService dishService;
    private DishMapper dishMapper;

    @GetMapping("/dishes")
    public List<Dish> getDishes() {
        return dishService.getAll().stream().map(dishMapper::toRest).toList();
    }
}
