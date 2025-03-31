package hei.vaninah.devoir.controller;

import hei.vaninah.devoir.entity.Ingredient;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import hei.vaninah.devoir.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients(
        @RequestParam(name = "priceMinFilter", required = false) BigDecimal priceMinFilter,
        @RequestParam(name = "priceMaxFilter", required = false) BigDecimal priceMaxFilter) {

        if (priceMinFilter != null && priceMinFilter.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (priceMaxFilter != null && priceMaxFilter.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Ingredient> ingredients = ingredientService.getIngredientsByPriceRange(priceMinFilter, priceMaxFilter, new Pagination(1,10) , new Order("name", Order.OrderValue.ASC));
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable String id) {
        return ingredientService.getIngredientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> addIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> savedIngredients = ingredientService.addIngredients(ingredients);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredients);
    }

    @PutMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> updateIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> updatedIngredients = ingredientService.updateIngredients(ingredients);
        return ResponseEntity.ok(updatedIngredients);
    }
}