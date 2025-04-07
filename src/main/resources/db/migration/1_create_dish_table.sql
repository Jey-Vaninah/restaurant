create table if not exists  "dish"
(
    "id" varchar primary key,
    "name" varchar not null,
    "unit_price" decimal(10,2) check ("unit_price" > 0) not null
);

