create table if not exists weapon_types
(
    weapon_type_id integer primary key,
    cost           integer,
    damage         smallint not null,
    radius         smallint not null,
    rate           smallint not null,
    damage_type_id smallint not null references damage_types,
    name_ru        text,
    dsc_ru         text
)