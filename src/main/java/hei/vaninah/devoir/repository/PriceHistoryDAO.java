package hei.vaninah.devoir.repository;

import hei.vaninah.devoir.entity.PriceHistory;

import java.sql.Connection;
import java.util.List;

public class PriceHistoryDAO implements RestaurantManagementDAO<PriceHistory> {
    final private Connection connection;

    public PriceHistoryDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PriceHistory findById(String id) {
        return null;
    }

    @Override
    public List<PriceHistory> findAll(Pagination pagination, Order order) {
        return List.of();
    }

    @Override
    public PriceHistory deleteById(String id) {
        return null;
    }

    @Override
    public PriceHistory save(PriceHistory id) {
        return null;
    }

    @Override
    public PriceHistory update(PriceHistory id) {
        return null;
    }

    @Override
    public PriceHistory crupdate(PriceHistory id) {
        return null;
    }

    @Override
    public List<PriceHistory> saveAll(List<PriceHistory> list) {
        return List.of();
    }
}
