package hei.vaninah.devoir.service;

import hei.vaninah.devoir.entity.PriceHistory;
import hei.vaninah.devoir.repository.PriceHistoryDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@AllArgsConstructor
public class PriceHistoryService {
    private final PriceHistoryDAO dao;

    public List<PriceHistory> addMultiplePriceHistories(List<PriceHistory> priceHistories) {
        try {
            return dao.saveAll(priceHistories);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
