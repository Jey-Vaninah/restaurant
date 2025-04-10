package hei.vaninah.devoir.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private int page;
    private int pageSize;

    int getLimit(){
        return pageSize;
    }

    int getOffset(){
        return (page - 1) * pageSize;
    }
}