package hei.vaninah.devoir.endpoint.controller;

import hei.vaninah.devoir.endpoint.mapper.IngredientMapper;
import hei.vaninah.devoir.endpoint.mapper.PriceHistoryMapper;
import hei.vaninah.devoir.endpoint.mapper.StockMovementMapper;
import hei.vaninah.devoir.entity.Ingredient;
import hei.vaninah.devoir.entity.IngredientStockMovement;
import hei.vaninah.devoir.entity.PriceHistory;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import hei.vaninah.devoir.service.IngredientService;
import hei.vaninah.devoir.service.PriceHistoryService;
import hei.vaninah.devoir.service.StockMovementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class IngredientRestController {
    private final IngredientService ingredientService;
    private final IngredientMapper ingredientMapper;
    private final PriceHistoryService priceHistoryService;
    private final PriceHistoryMapper priceHistoryMapper;
    private final StockMovementMapper stockMovementMapper;
    private final StockMovementService stockMovementService;


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
    public ResponseEntity<List<hei.vaninah.devoir.endpoint.rest.Ingredient>> addIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> savedIngredients = ingredientService.addIngredients(ingredients);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            savedIngredients.stream().map(ingredientMapper::toRest).toList()
        );
    }

    @PutMapping("/ingredients")
    public ResponseEntity<List<hei.vaninah.devoir.endpoint.rest.Ingredient>> updateIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> updatedIngredients = ingredientService.updateIngredients(ingredients);
        return ResponseEntity.ok(updatedIngredients.stream().map(ingredientMapper::toRest).toList());
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<hei.vaninah.devoir.endpoint.rest.Ingredient> deleteIngredient(@PathVariable String id) {
        Ingredient deletedIngredient = ingredientService.deleteIngredient(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientMapper.toRest(deletedIngredient));
    }

    @PutMapping("/ingredients/{id}/prices")
    public ResponseEntity<List<PriceHistory>> addPriceHistoriess(
            @PathVariable String id, @RequestBody List<hei.vaninah.devoir.endpoint.rest.PriceHistory> priceHistories) {

        List<PriceHistory> savedPriceHistories = priceHistoryService.addMultiplePriceHistories(
                priceHistories.stream().map(ph -> priceHistoryMapper.toDomain(ph, id)).toList()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPriceHistories);
    }


//    @PutMapping("/ingredients/{ingredientId}/prices")
//    public ResponseEntity<Object> updateIngredientPrices(@PathVariable Long ingredientId, @RequestBody List<CreateIngredientPrice> ingredientPrices) {
//        List<Price> prices = ingredientPrices.stream()
//                .map(ingredientPrice ->
//                        new Price(ingredientPrice.getAmount(), ingredientPrice.getDateValue()))
//                .toList();
//        Ingredient ingredient = ingredientService.addPrices(ingredientId, prices);
//        IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);
//        return ResponseEntity.ok().body(ingredientRest);
//    }

//    @PostMapping("/ingredients/{id}/prices")
//    public ResponseEntity<List<PriceHistory>> addPriceHistories(
//            @PathVariable String id,
//            @RequestBody List<hei.vaninah.devoir.endpoint.rest.PriceHistory> priceHistories) {
//
//
//        boolean hasNullDateTime = priceHistories.stream()
//                .anyMatch(ph -> ph.getPriceDatetime() == null);
//
//        if (hasNullDateTime) {
//            return ResponseEntity.badRequest().body(null); // Returning 400 Bad Request if dateTime is null
//        }
//
//
//        List<PriceHistory> savedPriceHistories = priceHistoryService.addMultiplePriceHistories(
//                priceHistories.stream()
//                        .map(ph -> priceHistoryMapper.toDomain(ph, id))
//                        .collect(Collectors.toList())
//        );
//
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedPriceHistories);
//    }

    @PostMapping("/ingredients/{id}/prices")
    public ResponseEntity<List<PriceHistory>> addPriceHistories(
            @PathVariable String id,
            @RequestBody List<hei.vaninah.devoir.endpoint.rest.PriceHistory> priceHistories) {

        priceHistories.forEach(ph -> {
            if (ph.getPriceDatetime() == null) {
                System.out.println("PriceDatetime null trouvé pour le PriceHistory avec ID: " + ph.getId());
            }
        });

        boolean hasNullDateTime = priceHistories.stream()
                .anyMatch(ph -> ph.getPriceDatetime() == null);

        if (hasNullDateTime) {
            return ResponseEntity.badRequest().body(null); // Renvoi 400 Bad Request si priceDatetime est null
        }

        List<PriceHistory> savedPriceHistories = priceHistoryService.addMultiplePriceHistories(
                priceHistories.stream()
                        .map(ph -> priceHistoryMapper.toDomain(ph, id))
                        .collect(Collectors.toList())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPriceHistories);
    }


    @PostMapping("/ingredients/{id}/stocks")
    public ResponseEntity<List<IngredientStockMovement>> addStockMovements(
            @PathVariable String id, @RequestBody List<hei.vaninah.devoir.endpoint.rest.StockMovement> stockMovements) {

        List<IngredientStockMovement> savedStockMovements = stockMovementService.addMultipleMovements(
                stockMovements.stream().map(sm -> stockMovementMapper.toDomain(sm, id)).toList()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(savedStockMovements);
    }

}