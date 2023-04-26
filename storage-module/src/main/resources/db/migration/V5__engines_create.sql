create sequence if not exists equipment_seq;

create table if not exists engines
(
    engine_id      bigint default nextval('equipment_seq'),
    slot_id        smallint,
    equipped       bool,
    character_name text references characters (character_name),
    engine_type_id smallint references engine_types (engine_type_id),
    speed          smallint not null,
    upgrade_level  smallint not null,
    cost           integer  not null
)