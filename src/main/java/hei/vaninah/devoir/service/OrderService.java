package hei.vaninah.devoir.service;

import hei.vaninah.devoir.repository.OrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO dao;

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
}