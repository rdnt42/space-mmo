insert into weapon_types (weapon_type_id, cost, damage, radius, rate, damage_type_id, name_ru, dsc_ru)
values (1, 10000, 10, 270, 100, 1, 'Промышленный лазер',
        'Основное назначение промышленного лазера - работы, связанные с глубоким бурением, добычей полезных минералов и дроблением породы');

insert into damage_types (damage_type_id, name)
values (1, 'Термическое');