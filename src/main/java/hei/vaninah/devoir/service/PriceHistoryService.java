package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.PriceHistory;
import hei.vaninah.devoir.repository.PriceHistoryDAO;
import hei.vaninah.devoir.repository.Order;
import hei.vaninah.devoir.repository.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceHistoryService {
    private final PriceHistoryDAO dao;

    public Optional<PriceHistory> getPriceHistoryById(String id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public List<PriceHistory> getAllPriceHistories(Pagination pagination, Order order) {
        return dao.findAll(pagination, order);
    }

    public List<PriceHistory> getPriceHistoriesByIngredientId(String ingredientId) {
        return dao.findByIngredientId(ingredientId);
    }

    public PriceHistory addPriceHistory(PriceHistory priceHistory) {
        return dao.save(priceHistory);
    }

    public List<PriceHistory> addMultiplePriceHistories(List<PriceHistory> priceHistories) {
        return dao.saveAll(priceHistories);
    }

    public PriceHistory updatePriceHistory(PriceHistory priceHistory) {
        return dao.update(priceHistory);
    }

    public PriceHistory deletePriceHistory(String id) {
        return dao.deleteById(id);
    }
}
