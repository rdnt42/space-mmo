package services

import "gorm.io/gorm"

var db *gorm.DB

func CreateDbLink(DB *gorm.DB) {
	db = DB
}

func getFreeSlotSelect() string {
	return `select coalesce
           (
               (select slot_id + 1 as gap
                from (select slot_id, lead(slot_id) over (order by slot_id) as next_nr
                      from items
                      where character_name = ?) nr
                where slot_id + 1 <> next_nr),
               (select max(slot_id) + 1
                from items
                where character_name = ?),
               0
           )`
}
