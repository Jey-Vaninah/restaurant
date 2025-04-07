package hei.vaninah.devoir.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DishOrderStatus {
    private String id;
    private String dishOrderId;
    private StatusHistory status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
