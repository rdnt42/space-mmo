package models

type DamageType struct {
	DamageTypeId int32  `gorm:"primary_key" json:"id"`
	Name         string `json:"name"`
}
