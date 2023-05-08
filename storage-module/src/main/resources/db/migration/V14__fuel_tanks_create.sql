create table if not exists fuel_tanks
(
    item_id           bigint primary key references items,
    capacity          smallint not null,
    fuel_tank_type_id integer references fuel_tank_types
)