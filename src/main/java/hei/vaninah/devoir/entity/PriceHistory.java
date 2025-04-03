package hei.vaninah.devoir.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistory {
    private String id;
    private String idIngredient;
    private LocalDateTime priceDatetime;
    private BigDecimal unitPrice;

}