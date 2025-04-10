package hei.vaninah.devoir.endpoint.controller;

import hei.vaninah.devoir.endpoint.mapper.DishIngredientMapper;
import hei.vaninah.devoir.endpoint.mapper.DishMapper;
import hei.vaninah.devoir.endpoint.rest.BestSales;
import hei.vaninah.devoir.endpoint.rest.DishIngredient;
import hei.vaninah.devoir.endpoint.rest.Dish;
import hei.vaninah.devoir.service.DishService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class DishRestController {
    private DishService dishService;
    private DishMapper dishMapper;
    private DishIngredientMapper dishIngredientMapper;

    @GetMapping("/dishes")
    public List<Dish> getDishes() {
        return dishService.getAll().stream().map(dishMapper::toRest).toList();
    }

    @PutMapping ( "/dishes/{id}/ingredients")
    public Dish addIngredient(@PathVariable String id, @RequestBody  List<DishIngredient> ingredients) {
        return dishMapper.toRest(dishService.addDishIngredient(
            id,
            ingredients.stream().map(ingredient -> dishIngredientMapper.toDomain(id, ingredient)).toList()
        ));
    }

    @GetMapping("/bestSales")
    public List<BestSales> getBestSales(@RequestParam Integer limit, @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        return dishService.findBestSales(limit, from, to);
    }
}