package hei.vaninah.devoir.endpoint.rest.controller;

import hei.vaninah.devoir.endpoint.rest.mapper.IngredientMapper;
import hei.vaninah.devoir.entity.Ingredient;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import hei.vaninah.devoir.service.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
public class IngredientRestController {
    private final IngredientService ingredientService;
    private final IngredientMapper ingredientMapper;


    @GetMapping("/ingredients")
    public ResponseEntity<List<hei.vaninah.devoir.endpoint.rest.model.Ingredient>> getIngredients(
        @RequestParam(name = "priceMinFilter", required = false) BigDecimal priceMinFilter,
        @RequestParam(name = "priceMaxFilter", required = false) BigDecimal priceMaxFilter) {

        if (priceMinFilter != null && priceMinFilter.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (priceMaxFilter != null && priceMaxFilter.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Ingredient> ingredients = ingredientService.getIngredientsByPriceRange(priceMinFilter, priceMaxFilter, new Pagination(1,10) , new Order("name", Order.OrderValue.ASC));
        return ResponseEntity.ok(ingredients.stream().map(ingredientMapper::toRest).toList());
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<hei.vaninah.devoir.endpoint.rest.model.Ingredient> getIngredient(@PathVariable String id) {
        return ingredientService.getIngredientById(id)
                .map(ingredient -> ResponseEntity.ok(ingredientMapper.toRest(ingredient)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/ingredients")
    public ResponseEntity<List<hei.vaninah.devoir.endpoint.rest.model.Ingredient>> addIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> savedIngredients = ingredientService.addIngredients(ingredients);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            savedIngredients.stream().map(ingredientMapper::toRest).toList()
        );
    }

    @PutMapping("/ingredients")
    public ResponseEntity<List<hei.vaninah.devoir.endpoint.rest.model.Ingredient>> updateIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> updatedIngredients = ingredientService.updateIngredients(ingredients);
        return ResponseEntity.ok(updatedIngredients.stream().map(ingredientMapper::toRest).toList());
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<hei.vaninah.devoir.endpoint.rest.model.Ingredient> deleteIngredient(@PathVariable String id) {
        Ingredient deletedIngredient = ingredientService.deleteIngredient(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientMapper.toRest(deletedIngredient));
    }
}