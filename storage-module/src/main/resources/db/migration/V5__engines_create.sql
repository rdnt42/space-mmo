create table if not exists engines
(
    engine_id      bigserial,
    is_active      bool     not null,
    character_name text references characters (character_name),
    engine_type_id smallint references engine_types (engine_type_id),
    speed          smallint not null,
    upgrade_level  smallint not null,
    cost           integer  not null
)