package models

type Weapon struct {
	ItemId       int64      `gorm:"primary_key;auto_increment" json:"id"`
	Item         Item       `gorm:"references:ItemId" json:"item"`
	Damage       int16      `json:"damage"`
	Radius       int16      `json:"radius"`
	Rate         int16      `json:"rate"`
	DamageTypeId int32      `json:"damageTypeId"`
	DamageType   DamageType `gorm:"references:DamageTypeId" json:"damageType"`
	WeaponTypeId int32      `json:"weaponTypeId"`
	WeaponType   WeaponType `gorm:"references:WeaponTypeId" json:"weaponType"`
}

type CreateWeaponRequest struct {
	CharacterName string `json:"characterName" binding:"required"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
	Damage        int16  `json:"damage"`
	Radius        int16  `json:"radius"`
	Rate          int16  `json:"rate"`
	DamageTypeId  int32  `json:"damageTypeId" binding:"required"`
	WeaponTypeId  int32  `json:"weaponTypeId" binding:"required"`
	NameRu        string `json:"nameRu"`
	DscRu         string `json:"dscRu"`
}

type UpdateWeaponRequest struct {
	UpgradeLevel *int16 `json:"upgradeLevel" binding:"required"`
	Cost         *int32 `json:"cost" binding:"required"`
	Damage       *int16 `json:"damage" binding:"required"`
	Radius       *int16 `json:"radius" binding:"required"`
	Rate         *int16 `json:"rate" binding:"required"`
	NameRu       string `json:"nameRu"`
	DscRu        string `json:"dscRu"`
}
