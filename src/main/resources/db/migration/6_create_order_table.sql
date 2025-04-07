create table if not exists "order" (
    "id" varchar primary key,
    "reference" varchar unique not null,
    "updated_at" timestamp default current_timestamp not null,
    "created_at" timestamp default current_timestamp not null
);

