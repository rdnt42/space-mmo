package models

type WeaponType struct {
	WeaponTypeId int32  `gorm:"primary_key" json:"id"`
	Cost         int32  `json:"cost"`
	Damage       int16  `json:"damage"`
	Radius       int16  `json:"radius"`
	Rate         int16  `json:"rate"`
	DamageTypeId int32  `json:"damage_type_id"`
	NameRu       string `json:"nameRu"`
	DscRu        string `json:"dscRu"`
}
