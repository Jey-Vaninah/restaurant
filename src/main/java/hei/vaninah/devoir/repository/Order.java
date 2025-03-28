package hei.vaninah.devoir.repository;

public class Order {
    private String orderBy;
    private OrderValue orderValue;

    public enum OrderValue {
        DESC,
        ASC
    }

    public Order(String orderBy, OrderValue orderValue) {
        this.orderBy = orderBy;
        this.orderValue = orderValue;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public OrderValue getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(OrderValue orderValue) {
        this.orderValue = orderValue;
    }
}