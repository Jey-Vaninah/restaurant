package hei.vaninah.devoir.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Criteria {
    private final String column;
    private final Object value;

}

