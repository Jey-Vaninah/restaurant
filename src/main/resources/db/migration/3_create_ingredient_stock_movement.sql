do
$$
begin
        if not exists(select from pg_type where typname = 'ingredient_stock_movement_type') then
create type "ingredient_stock_movement_type" as enum ('IN', 'OUT');
end if;
end
$$;

create table if not exists "ingredient_stock_movement"
(
    "id" varchar primary key,
    "id_ingredient" varchar not null references "ingredient"("id"),
    "movement_datetime" timestamp default current_timestamp not null,
    "movement_type" "ingredient_stock_movement_type" not null,
    "quantity" float check ("quantity" > 0) not null,
    "unit" unit not null
);