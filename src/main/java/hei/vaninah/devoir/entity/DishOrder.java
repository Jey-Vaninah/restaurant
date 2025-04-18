package hei.vaninah.devoir.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DishOrder {
    private String id;
    private String orderId;
    private Dish dish;
    private int quantity;
    private List<DishOrderStatus> dishOrderStatus;

    public void addStatus(DishOrderStatus dishOrderStatus) {
        this.dishOrderStatus.add(dishOrderStatus);
    }

    public DishOrderStatus getActualStatus() {
        return this.dishOrderStatus.stream()
                .max(Comparator.comparing(DishOrderStatus::getCreatedAt))
                .orElse(new DishOrderStatus(randomUUID().toString(), this.getId(), StatusHistory.CREATED, now(), now()));
    }

    public BigDecimal getCost() {
        return this.dish.getUnitPrice().multiply(new BigDecimal(this.quantity));
    }

    public DishOrderStatus updateStatus(StatusHistory statusHistory) {
        DishOrderStatus status = new DishOrderStatus(
                randomUUID().toString(),
                this.getId(),
                statusHistory,
                now(),
                now()
        );

        this.dishOrderStatus.add(status);
        return status;
    }

    public String getOrder() {
        return this.orderId;
    }

    public Duration getStatusDuration(StatusHistory statusFrom, StatusHistory statusTo) {
        LocalDateTime statusFromTime = this.dishOrderStatus.stream()
                .filter(status -> status.getStatus().equals(statusFrom))
                .map(DishOrderStatus::getCreatedAt)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status " + statusFrom + " not found"));

        LocalDateTime statusToTime = this.dishOrderStatus.stream()
                .filter(status -> status.getStatus().equals(statusTo))
                .map(DishOrderStatus::getCreatedAt)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status " + statusTo + " not found"));

        return Duration.between(statusFromTime, statusToTime);
    }
}
