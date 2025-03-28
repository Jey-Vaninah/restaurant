package hei.vaninah.devoir.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceHistory {
    private String id;
    private String idIngredient;
    private LocalDateTime priceDatetime;
    private BigDecimal unitPrice;

    public PriceHistory(String id, String idIngredient, LocalDateTime priceDatetime, BigDecimal unitPrice) {
        this.id = id;
        this.idIngredient = idIngredient;
        this.priceDatetime = priceDatetime;
        this.unitPrice = unitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(String idIngredient) {
        this.idIngredient = idIngredient;
    }

    public LocalDateTime getPriceDatetime() {
        return priceDatetime;
    }

    public void setPriceDatetime(LocalDateTime priceDatetime) {
        this.priceDatetime = priceDatetime;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}