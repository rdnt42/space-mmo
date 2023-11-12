insert into characters (character_name, account_name, experience, x, y, angle, is_online)
values ('p1', 'user1', 0, 0, 0, 0, false);

insert into characters (character_name, account_name, experience, x, y, angle, is_online)
values ('sd', 'user2', 0, 200, 200, 0, true);

-- P1
insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (8, 1, 'p1', 8, 0, 100000, 'Пират', 'Пиратский корпус');

insert into hulls (item_id, hp, evasion, armor, hull_type_id, config)
values (1, 800, 2, 2, 2, 3969);

insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (9, 1, 'p1', 9, 0, 10000, 'вооружение 1', 'описание');

insert into weapons (item_id, damage, radius, rate, damage_type_id, weapon_type_id)
values (2, 15, 250, 300, 3, 1);

insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (10, 1, 'p1', 9, 0, 10000, 'вооружение 2', 'описание');

insert into weapons (item_id, damage, radius, rate, damage_type_id, weapon_type_id)
values (3, 15, 250, 300, 2, 2);

insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (1, 1, 'p1', 1, 0, 10000, 'двигатель', 'описание');

insert into engines (item_id, speed, jump, engine_type_id)
values (4, 500, 20, 1);

-- SD
insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (8, 1, 'sd', 8, 0, 100000, 'Рейнджер', 'Рейнджерский корпус');

insert into hulls (item_id, hp, evasion, armor, hull_type_id, config)
values (5, 800, 2, 2, 1, 3969);

insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (9, 1, 'sd', 9, 0, 10000, 'вооружение 1', 'описание');

insert into weapons (item_id, damage, radius, rate, damage_type_id, weapon_type_id)
values (6, 15, 250, 300, 3, 1);

insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (10, 1, 'sd', 9, 0, 10000, 'вооружение 2', 'описание');

insert into weapons (item_id, damage, radius, rate, damage_type_id, weapon_type_id)
values (7, 15, 250, 300, 2, 2);

insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (1, 1, 'sd', 1, 0, 10000, 'двигатель', 'описание');

insert into engines (item_id, speed, jump, engine_type_id)
values (8, 500, 20, 1);