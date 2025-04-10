package hei.vaninah.devoir.repository;

import java.sql.SQLException;
import java.util.List;

public interface RestaurantManagementDAO<T> {
    T save(T id) throws SQLException;
    T update(T id) throws SQLException;
    T crupdate(T id) throws SQLException;
    T findById(String id) throws SQLException;
    T deleteById(String id) throws SQLException;
    List<T> saveAll(List<T> list) throws SQLException;
    List<T> findAll(Pagination pagination, Order order) throws SQLException;
}
