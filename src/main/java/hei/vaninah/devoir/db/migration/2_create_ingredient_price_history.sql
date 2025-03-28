create table if not exists "ingredient_price_history"
(
    "id" varchar primary key,
    "id_ingredient" varchar not null references "ingredient"("id"),
    "price_datetime" timestamp not null,
    "unit_price" decimal(10,2) check ("unit_price" > 0) not null
    );