create table if not exists "dish_ingredient"
(
    "id_dish" varchar not null references "dish"("id"),
    "id_ingredient" varchar not null references "ingredient"("id"),
    "required_quantity" float check ("required_quantity" > 0) not null,
    "unit" unit not null,
    primary key ("id_dish", "id_ingredient")
);
