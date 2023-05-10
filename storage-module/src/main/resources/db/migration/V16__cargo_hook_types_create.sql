create table if not exists cargo_hook_types
(
    cargo_hook_type_id integer primary key,
    cost               integer,
    load_capacity      smallint not null,
    radius             smallint not null,
    name_ru            text,
    dsc_ru             text
)