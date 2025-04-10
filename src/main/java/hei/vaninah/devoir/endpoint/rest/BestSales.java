package hei.vaninah.devoir.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestSales {
    private String dish;
    private Integer salesCount;
    private Double salesPrice;
}
