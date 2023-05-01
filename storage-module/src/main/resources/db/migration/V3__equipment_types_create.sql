create table if not exists equipment_types
(
    equipment_type_id smallint primary key,
    name              text    not null,
    name_ru           text    not null,
    dsc_ru            text    not null,
    base_cost         integer not null,
    base_speed        smallint,
    base_jump         smallint
)