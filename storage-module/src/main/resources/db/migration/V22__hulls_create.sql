create table if not exists hulls
(
    item_id      bigint primary key references items,
    hp           smallint not null,
    evasion      smallint not null,
    armor        smallint not null,
    hull_type_id integer references hull_types
)