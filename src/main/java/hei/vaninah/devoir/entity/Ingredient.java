package hei.vaninah.devoir.entity;

import java.time.Instant;


public class Ingredient {

        private Long id;
        private String name;
        private Double unitPrice;
        private Instant updatedAt;

    public Ingredient(Long id, String name, Double unitPrice, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
