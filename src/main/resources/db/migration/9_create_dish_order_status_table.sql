create table if not exists "dish_order_status"(
    "id" varchar primary key,
    "id_dish_order" varchar not null references dish_order("id"),
    "status" status_history not null,
    "updated_at" timestamp not null default now(),
    "created_at" timestamp default current_timestamp not null
);