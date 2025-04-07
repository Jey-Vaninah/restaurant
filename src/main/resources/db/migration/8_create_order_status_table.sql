do
$$
    begin
        if not exists (select from pg_type where typname  = 'status_history') THEN
            create type "status_history" as enum ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'COMPLETED', 'SERVED');
        end if;
    end
$$;

create table if not exists order_status(
    "id" varchar primary key,
    "id_order" varchar not null references "order"("id"),
    "status" status_history not null,
    "updated_at" timestamp not null default now(),
    "created_at" timestamp default current_timestamp not null
);
