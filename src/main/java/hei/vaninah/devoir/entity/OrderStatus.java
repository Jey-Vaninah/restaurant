package hei.vaninah.devoir.entity;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderStatus {
    private String id;
    private String idOrder;
    private StatusHistory status;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
