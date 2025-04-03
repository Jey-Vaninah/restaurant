package hei.vaninah.devoir.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistory{
    private String id;
    private LocalDateTime priceDatetime;
    private BigDecimal unitPrice;

}
