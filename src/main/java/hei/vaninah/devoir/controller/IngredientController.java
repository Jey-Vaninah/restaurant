package hei.vaninah.devoir.controller;

import hei.vaninah.devoir.entity.Ingredient;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import hei.vaninah.devoir.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * Récupérer tous les ingrédients avec un filtre optionnel sur le prix
     */
    @GetMapping
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

    /**
     * Récupérer un ingrédient par son ID
     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getIngredient(@PathVariable String id) {
//        Optional<Ingredient> optionalIngredient = ingredientService.getIngredientById(id);
//
//        return optionalIngredient.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient=" + id + " not found"));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable String id) {
        return ingredientService.getIngredientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Ajouter une liste d'ingrédients
     */
    @PostMapping
    public ResponseEntity<List<Ingredient>> addIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> savedIngredients = ingredientService.addIngredients(ingredients);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredients);
    }

    /**
     * Mettre à jour une liste d'ingrédients
     */
    @PutMapping
    public ResponseEntity<List<Ingredient>> updateIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> updatedIngredients = ingredientService.updateIngredients(ingredients);
        return ResponseEntity.ok(updatedIngredients);
    }
}
