create table if not exists weapons
(
    item_id        bigint primary key references items,
    damage         smallint not null,
    radius         smallint not null,
    rate           smallint not null,
    damage_type_id smallint not null references damage_types,
    weapon_type_id integer references weapon_types
)