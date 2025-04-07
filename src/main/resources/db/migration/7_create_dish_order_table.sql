create table if not exists "dish_order" (
    "id" varchar primary key not null,
    "id_order" varchar not null references "order"("id"),
    "id_dish" varchar not null references "dish"("id"),
    "quantity" int not null check ( "quantity" > 0)
);
