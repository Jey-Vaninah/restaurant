package hei.vaninah.devoir.service;

import hei.vaninah.devoir.endpoint.rest.BestSales;
import hei.vaninah.devoir.endpoint.rest.ProcessingTime;
import hei.vaninah.devoir.endpoint.rest.ProcessingTimeType;
import hei.vaninah.devoir.endpoint.rest.ProcessingValueType;
import hei.vaninah.devoir.entity.Dish;
import hei.vaninah.devoir.entity.DishIngredient;
import hei.vaninah.devoir.entity.DishOrder;
import hei.vaninah.devoir.entity.StatusHistory;
import hei.vaninah.devoir.repository.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hei.vaninah.devoir.entity.StatusHistory.*;

@Service
@Data
public class DishService {
    private final OrderDAO orderDAO;
    private final DishDAO dao;
    private final DishOrderDAO dishOrderDAO;
    private final DishIngredientDAO dishIngredientDAO;

    public List<Dish> getAll(){
        try {
            return dao.findAll(new Pagination(1, 500), new Order("name", Order.OrderValue.ASC));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish addDishIngredient(String id, List<DishIngredient> dishIngredients) {
        try {
            Dish dish = dao.findById(id);
            dish.addDishIngredients(dishIngredients);
            return dao.crupdate(dish);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BestSales> findBestSales(Integer limit, LocalDateTime from, LocalDateTime to) {
        try {
            List<hei.vaninah.devoir.entity.Order> orders = orderDAO.findAllInRange(from, to);
            Map<String, BestSales> bestSales = new HashMap<>();

            orders.stream().filter(order -> order.getActualStatus().getStatus().equals(COMPLETED)).forEach(order -> {
                order.getDishOrders().forEach(dishOrder -> {
                    BestSales before = bestSales.getOrDefault(
                        dishOrder.getDish().getId(),
                        new BestSales(
                            dishOrder.getDish().getName(),
                            0,
                            0d
                        )
                    );

                   bestSales.put(dishOrder.getDish().getId(), new BestSales(
                       before.getDish(),
                       before.getSalesCount() + dishOrder.getQuantity(),
                       before.getSalesPrice() + dishOrder.getCost().doubleValue()
                   ));
                });
            });

            return bestSales.values().stream()
                    .sorted(Comparator.comparingInt(BestSales::getSalesCount).reversed())
                    .limit(limit)
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProcessingTime getProcessingTime(String dishId, ProcessingValueType valueType, ProcessingTimeType timeType){
        try {
            List<Duration> durations = this.getDurationsByDishId(dishId);
            if (durations.isEmpty()) {
                return new ProcessingTime(0D, timeType);
            }

            double processingTime = this.computeAggregateDuration(durations, valueType);
            double convertedProcessingTime = this.convertDuration(processingTime, timeType);

            return new ProcessingTime(convertedProcessingTime, timeType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Duration> getDurationsByDishId(String dishId) throws SQLException {
        List<DishOrder> dishOrders = this.dishOrderDAO.findByDishId(dishId);

        return dishOrders
            .stream()
            .filter(dishOrder -> {
                StatusHistory status = dishOrder.getActualStatus().getStatus();
                return !status.equals(CREATED) && !status.equals(IN_PREPARATION);
            })
            .map(dishOrder ->{
                Duration duration = dishOrder.getStatusDuration(IN_PREPARATION, COMPLETED);
                return duration.dividedBy(dishOrder.getQuantity());
            })
            .toList();
    }


    private double computeAggregateDuration(List<Duration> durations, ProcessingValueType type) {
        return switch (type) {
            case MINIMUM -> durations.stream()
                .mapToLong(Duration::getSeconds)
                .min()
                .orElse(0);
            case MAXIMUM -> durations.stream()
                .mapToLong(Duration::getSeconds)
                .max()
                .orElse(0);
            case AVERAGE -> (double) durations.stream()
                .mapToLong(Duration::getSeconds)
                .average()
                .orElse(0);
        };
    }

    private double convertDuration(double durationTime, ProcessingTimeType unit) {
        return switch (unit) {
            case SECONDS -> durationTime;
            case MINUTES -> durationTime / 60;
            case HOURS   -> durationTime / 3600;
        };
    }
}
