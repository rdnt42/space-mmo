create table if not exists equipments
(
    equipment_id      bigserial primary key,
    slot_id           smallint,
    equipped          bool,
    character_name    text references characters (character_name),
    equipment_type_id smallint references equipment_types (equipment_type_id),
    upgrade_level     smallint not null
)