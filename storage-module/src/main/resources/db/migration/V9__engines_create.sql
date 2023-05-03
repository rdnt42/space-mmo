create table if not exists engines
(
    item_id        bigint primary key references items,
    speed          smallint not null,
    jump           smallint not null,
    engine_type_id integer references engine_types
)