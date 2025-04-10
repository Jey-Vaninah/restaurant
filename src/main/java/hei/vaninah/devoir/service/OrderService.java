package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.*;
import hei.vaninah.devoir.repository.DishOrderDAO;
import hei.vaninah.devoir.repository.DishOrderStatusDAO;
import hei.vaninah.devoir.repository.OrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static hei.vaninah.devoir.entity.StatusHistory.IN_PREPARATION;
import static hei.vaninah.devoir.entity.StatusHistory.SERVED;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO dao;
    private final DishOrderDAO dishOrderDAO;
    private final DishOrderStatusDAO dishOrderStatusDAO;

    public Optional<hei.vaninah.devoir.entity.Order> getDishById(String id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public List<hei.vaninah.devoir.entity.Order> addIngredients(List<hei.vaninah.devoir.entity.Order> orders) {
        return dao.saveAll(orders);
    }

    public List<hei.vaninah.devoir.entity.Order> updateIngredients(List<hei.vaninah.devoir.entity.Order> orders) {
        return dao.saveAll(orders);
    }

    public hei.vaninah.devoir.entity.Order deleteOrder(String id) {
        return dao.deleteById(id);
    }

    public hei.vaninah.devoir.entity.Order findOrderByReference(String reference) {
        return dao.findByReference(reference);
    }

    public Order addDishOrder(String orderReference ,List<DishOrder> dishOrders){
       dishOrderDAO.saveAll(dishOrders);
       return dao.findByReference(orderReference);
    }

    public Order updateDishStatus(String orderReference, DishOrder dishOrder){
        dishOrderDAO.update(dishOrder);
        return dao.findByReference(orderReference);
    }

    public Order updateDishOrderStatus(String orderReference, DishOrderStatus dishOrderStatus){
        dishOrderStatusDAO.save(dishOrderStatus);
        return dao.findByReference(orderReference);
    }

    public long getDishProcessingTime(String orderId, String dishOrderId, LocalDateTime startDate, LocalDateTime endDate, String timeUnit, String durationType) {
        dao.findById(orderId);
        return dao.getDishProcessingTime(dishOrderId, startDate, endDate, timeUnit, durationType);
    }

    public long getProcessingTime(String dishId, LocalDateTime dateDebut, LocalDateTime dateFin,
                                  String timeUnit, String aggregationType) {

        List<DishOrder> dishOrders = dishOrderDAO.findByDishIdAndOrderDateRange(dishId, dateDebut, dateFin);

        List<Duration> durations = new ArrayList<>();

        for (DishOrder dishOrder : dishOrders) {
            List<OrderStatus> statusHistories = List.of();

            LocalDateTime inProgressTime = statusHistories.stream()
                    .filter(status -> status.getStatus() == IN_PREPARATION)
                    .filter(status -> !status.getCreatedAt().isBefore(dateDebut) && !status.getCreatedAt().isAfter(dateFin))
                    .map(OrderStatus::getCreatedAt)
                    .findFirst()
                    .orElse(null);

            LocalDateTime finishedTime = statusHistories.stream()
                    .filter(status -> status.getStatus() == SERVED)
                    .filter(status -> !status.getCreatedAt().isBefore(dateDebut) && !status.getCreatedAt().isAfter(dateFin))
                    .map(OrderStatus::getCreatedAt)
                    .findFirst()
                    .orElse(null);

            if (inProgressTime != null && finishedTime != null) {
                durations.add(Duration.between(inProgressTime, finishedTime));
            }
        }

        if (durations.isEmpty()) {
            return 0;
        }

        long durationInSeconds = calculateDurationByType(durations, aggregationType);
        return convertDuration(Duration.ofSeconds(durationInSeconds), timeUnit);
    }


    private long calculateDurationByType(List<Duration> durations, String aggregationType) {
        if ("SUM".equalsIgnoreCase(aggregationType)) {
            return durations.stream().mapToLong(Duration::getSeconds).sum();
        } else if ("AVERAGE".equalsIgnoreCase(aggregationType)) {
            return (long) durations.stream().mapToLong(Duration::getSeconds).average().orElse(0);
        }
        return 0;
    }


    private long convertDuration(Duration duration, String timeUnit) {
        return switch (timeUnit) {
            case "minutes" -> duration.toMinutes();
            case "hours" -> duration.toHours();
            default -> duration.getSeconds();
        };
    }
}
