insert into characters (character_name, account_name, experience, x, y, angle, is_online)
values ('p1', 'user1', 0, 0, 0, 0, false);

insert into characters (character_name, account_name, experience, x, y, angle, is_online)
values ('sd', 'user2', 0, 200, 200, 0, false);

insert into items (slot_id, storage_id, character_name, item_type_id, upgrade_level, cost, name_ru, dsc_ru)
values (8, 2, 'p1', 8, 0, 100000, 'Пират', 'Рейнджерский корпус');

insert into hulls (item_id, hp, evasion, armor, hull_type_id)
values (5, 800, 2, 2, 2);