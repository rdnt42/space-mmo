create table if not exists cargo_hooks
(
    item_id            bigint primary key references items,
    load_capacity      smallint not null,
    radius             smallint not null,
    cargo_hook_type_id integer references cargo_hook_types
)