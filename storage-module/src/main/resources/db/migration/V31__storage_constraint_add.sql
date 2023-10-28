alter table items
    add constraint storage_slot_char_unq unique (character_name, storage_id, slot_id);