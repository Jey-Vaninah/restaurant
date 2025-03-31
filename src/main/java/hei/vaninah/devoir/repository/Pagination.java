package hei.vaninah.devoir.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pagination {
    private int page;
    private int pageSize;
}