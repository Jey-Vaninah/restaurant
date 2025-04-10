package hei.vaninah.devoir.endpoint.controller;

import hei.vaninah.devoir.endpoint.mapper.IngredientMapper;
import hei.vaninah.devoir.endpoint.mapper.PriceHistoryMapper;
import hei.vaninah.devoir.endpoint.mapper.StockMovementMapper;
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
    private final PriceHistoryMapper priceHistoryMapper;
    private final StockMovementMapper stockMovementMapper;

    @GetMapping("/ingredients")
    public ResponseEntity<List<hei.vaninah.devoir.endpoint.rest.Ingredient>> getIngredients(
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
    public ResponseEntity<hei.vaninah.devoir.endpoint.rest.Ingredient> getIngredient(@PathVariable String id) {
        return ingredientService.getIngredientById(id)
                .map(ingredient -> ResponseEntity.ok(ingredientMapper.toRest(ingredient)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/ingredients")
    public ResponseEntity<List<hei.vaninah.devoir.endpoint.rest.Ingredient>> addIngredients(@RequestBody List<hei.vaninah.devoir.endpoint.rest.Ingredient> ingredients) {
        List<Ingredient> crupdates = ingredientService.crupdateIngredients(
            ingredients.stream().map(ingredientMapper::createToDomain).toList()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
            crupdates.stream().map(ingredientMapper::toRest).toList()
        );
    }

    @PutMapping("/ingredients")
    public ResponseEntity<List<hei.vaninah.devoir.endpoint.rest.Ingredient>> updateIngredients(@RequestBody List<hei.vaninah.devoir.endpoint.rest.Ingredient> ingredients) {
        List<Ingredient> crupdates = ingredientService.crupdateIngredients(
            ingredients.stream().map(ingredientMapper::createToDomain).toList()
        );

        return ResponseEntity.ok(crupdates.stream().map(ingredientMapper::toRest).toList());
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<hei.vaninah.devoir.endpoint.rest.Ingredient> deleteIngredient(@PathVariable String id) {
        Ingredient deletedIngredient = ingredientService.deleteIngredient(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientMapper.toRest(deletedIngredient));
    }

    @PutMapping("/ingredients/{id}/prices")
    public ResponseEntity<hei.vaninah.devoir.endpoint.rest.Ingredient> addPriceHistories(
            @PathVariable String id, @RequestBody List<hei.vaninah.devoir.endpoint.rest.PriceHistory> priceHistories) {

        Ingredient ingredient = ingredientService.addPriceHistories(
            id,
            priceHistories.stream().map(ph -> priceHistoryMapper.toDomain(ph, id)).toList()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ingredientMapper.toRest(ingredient));
    }

    @PutMapping("/ingredients/{id}/stockMovements")
    public ResponseEntity<hei.vaninah.devoir.endpoint.rest.Ingredient> addStockMovements(
            @PathVariable String id, @RequestBody List<hei.vaninah.devoir.endpoint.rest.StockMovement> stockMovements) {

        Ingredient ingredient = ingredientService.addStockMovements(
            id,
            stockMovements.stream().map(stock -> stockMovementMapper.toDomain(id,stock)).toList()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ingredientMapper.toRest(ingredient));
    }
}