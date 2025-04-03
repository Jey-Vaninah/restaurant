package hei.vaninah.devoir.endpoint.rest;

import hei.vaninah.devoir.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    private String id;
    private String name;
    private LocalDateTime updateDatetime;
    private BigDecimal unitPrice;
    private Unit unit;

}
