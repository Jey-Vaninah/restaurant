package hei.vaninah.devoir.endpoint.rest;

import hei.vaninah.devoir.entity.StatusHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishOrderStatus {
    private StatusHistory actualStatus;
}
