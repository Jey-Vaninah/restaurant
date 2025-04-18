package hei.vaninah.devoir.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingTime {
    private Double value;
    ProcessingTimeType unit;
}
