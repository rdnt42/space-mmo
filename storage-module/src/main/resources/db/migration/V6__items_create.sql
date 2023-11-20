create table if not exists items
(
    item_id        bigserial primary key,
    slot_id        integer not null,
    storage_id     integer not null,
    equipped       bool    not null default false,
    character_name text references characters (character_name),
    item_type_id   integer references item_types (item_type_id),
    upgrade_level  smallint,
    cost           integer,
    name_ru        text    not null,
    dsc_ru         text,
    unique (character_name, slot_id)
)