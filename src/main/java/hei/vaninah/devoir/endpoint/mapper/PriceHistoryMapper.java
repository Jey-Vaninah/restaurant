package hei.vaninah.devoir.endpoint.mapper;

import hei.vaninah.devoir.entity.PriceHistory;
import org.springframework.stereotype.Component;

@Component
public class PriceHistoryMapper {
    public PriceHistory toDomain(hei.vaninah.devoir.endpoint.rest.PriceHistory priceHistory, String idIngredient) {
        return new PriceHistory(
            priceHistory.getId(),
            idIngredient,
            priceHistory.getPriceDatetime(),
            priceHistory.getUnitPrice()
        );
    }

    public hei.vaninah.devoir.endpoint.rest.PriceHistory toRest(PriceHistory priceHistory) {
        return new hei.vaninah.devoir.endpoint.rest.PriceHistory(
            priceHistory.getId(),
            priceHistory.getPriceDatetime(),
            priceHistory.getUnitPrice()
        );
    }
}
