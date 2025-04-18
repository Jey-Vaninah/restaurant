package hei.vaninah.devoir.entity;

import hei.vaninah.devoir.exception.BadRequestException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static hei.vaninah.devoir.entity.StatusHistory.*;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Order {
    private String id;
    private String reference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ArrayList<DishOrder> dishOrders;
    private ArrayList<OrderStatus> statusHistories;

    public void confirm() throws BadRequestException {
        this.checkIngredientsAvailable();
        this.dishOrders.forEach(di -> {
            di.updateStatus(CONFIRMED);
        });
        this.statusHistories.add(new OrderStatus(randomUUID().toString(), this.id, CONFIRMED, now(), now()));
    }

    public void prepare(){
        this.dishOrders.forEach(di -> {
            di.updateStatus(IN_PREPARATION);
        });
        this.statusHistories.add(new OrderStatus(randomUUID().toString(), this.id, IN_PREPARATION, now(), now()));
    }

    public void serve(){
        boolean isAllFinished = this.dishOrders.stream().allMatch(di -> di.getActualStatus().getStatus().equals(COMPLETED));
        if(!isAllFinished){
            throw new RuntimeException("All dish orders have not been completed");
        }

        this.dishOrders.forEach(di -> {
            di.updateStatus(SERVED);
        });

        this.statusHistories.add(new OrderStatus(randomUUID().toString(), this.id, SERVED, now(), now()));
    }

    public void compeleteDishOrder(String dishOrderId) {
        DishOrder dishOrder = this.dishOrders.stream().filter(di -> di.getOrderId().equals(dishOrderId)).findFirst().orElse(null);
        if(dishOrder == null){
            throw new RuntimeException("Dish order not found");
        }

        boolean isAllFinished = this.dishOrders.stream().allMatch(di -> di.getActualStatus().getStatus().equals(COMPLETED));
        if(!isAllFinished){
            return;
        }
        this.dishOrders.forEach(di -> {
            di.updateStatus(COMPLETED);
        });
        this.statusHistories.add(new OrderStatus(randomUUID().toString(), this.id, COMPLETED, now(), now()));
    }

    public Duration getOrderDuration() {
        LocalDateTime created = this.createdAt;
        LocalDateTime servedAt = this.getStatusHistories().stream()
                .filter(status -> status.getStatus().equals(SERVED))
                .map(OrderStatus::getCreatedAt)
                .findFirst()
                .orElseThrow();

        return Duration.between(created, servedAt);
    }

    public Duration getStatusDuration(StatusHistory statusFrom, StatusHistory statusTo) {
        LocalDateTime statusFromTime = this.getStatusHistories().stream()
                .filter(status -> status.getStatus().equals(statusFrom))
                .map(OrderStatus::getCreatedAt)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status " + statusFrom + " not found"));

        LocalDateTime statusToTime = this.getStatusHistories().stream()
                .filter(status -> status.getStatus().equals(statusTo))
                .map(OrderStatus::getCreatedAt)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status " + statusTo + " not found"));

        return Duration.between(statusFromTime, statusToTime);
    }

    public BigDecimal getTotalAmount(){
        return this.getDishOrders()
                .stream()
                .map(DishOrder::getCost)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public OrderStatus getActualStatus() {
        return this.statusHistories.stream()
                .max(Comparator.comparing(OrderStatus::getCreatedAt))
                .orElse(new OrderStatus(randomUUID().toString(), this.getId(), CREATED, now(), now()));
    }

    public void addDishOrders(List<DishOrder> dishOrders) throws BadRequestException {
        OrderStatus actualStatus = this.getActualStatus();
        if (!CREATED.equals(actualStatus.getStatus())) {
            throw new BadRequestException("Only CREATE status can be updated");
        }

        for(DishOrder dishOrder : dishOrders){
            if(!CREATED.equals(actualStatus.getStatus())) {
                throw new BadRequestException("Only CREATE status can be updated");
            }

            DishOrder fromOrder = this.dishOrders
                    .stream()
                    .filter(dishO -> dishO.getId().equals(dishOrder.getId()))
                    .findFirst()
                    .orElse(null);

            if(fromOrder == null){
                this.dishOrders.add(dishOrder);
                continue;
            }

            if(!CREATED.equals(fromOrder.getActualStatus().getStatus())){
                throw new BadRequestException("Only CREATE status can be updated");
            }

            fromOrder.setQuantity(dishOrder.getQuantity());
        }

        this.checkIngredientsAvailable();
    }

    public void checkIngredientsAvailable() throws BadRequestException {
        for(DishOrder dishOrder : this.getDishOrders()) {
            List<Ingredient> ingredients = dishOrder.getDish().getIngredients();
            for (Ingredient ingredient : ingredients) {
                float availableQuantity = ingredient.getAvailableQuantity(ingredient.getUpdateDatetime());
                DishIngredient dishIngredient = dishOrder
                    .getDish()
                    .getDishIngredients()
                    .stream()
                    .filter(di -> di.getIdIngredient().equals(ingredient.getId()))
                    .findFirst()
                    .orElseThrow();

                if (availableQuantity < dishIngredient.getRequiredQuantity()) {
                    throw new BadRequestException("Ingredient " + ingredient.getName() + " is too low");
                }
            }
        }
    }

    public boolean isConfirmed(){
        return CONFIRMED.equals(this.getActualStatus().getStatus());
    }
}
