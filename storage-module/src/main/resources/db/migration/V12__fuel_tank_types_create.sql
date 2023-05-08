create table if not exists fuel_tank_types
(
    fuel_tank_type_id integer primary key,
    cost              integer,
    capacity          smallint not null,
    name_ru           text,
    dsc_ru            text
)