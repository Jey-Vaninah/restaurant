package hei.vaninah.devoir.repository;

public class Criteria {
    private final String column;
    private final Object value;

    public Criteria(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}

