do
$$
    begin
        if not exists(select from pg_type where typname = 'unit') then
            create type "unit" as enum ('G', 'L', 'U');
        end if;
    end
$$;

create table if not exists "ingredient"
(
    "id" varchar primary key,
    "name" varchar not null,
    "update_datetime" timestamp default current_timestamp not null,
    "unit_price" DECIMAL(10,2) check ("unit_price" > 0) not null,
    "unit" unit not null
);


