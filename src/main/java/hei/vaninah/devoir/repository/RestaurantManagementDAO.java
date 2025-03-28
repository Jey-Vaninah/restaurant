package hei.vaninah.devoir.repository;

import java.util.List;

public interface RestaurantManagementDAO<T> {
    T findById(String id);
    List<T> findAll(Pagination pagination, Order order);
    T deleteById(String id);
    T save(T id);
    T update(T id);
    T crupdate(T id);
    List<T> saveAll(List<T> list);
}
