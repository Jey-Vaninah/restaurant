package hei.vaninah.devoir.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderBy;
    private OrderValue orderValue;

    public enum OrderValue {
        DESC,
        ASC
    }

}