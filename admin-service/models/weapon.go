package models

type Weapon struct {
	ItemId       int64      `gorm:"primary_key;auto_increment" json:"id"`
	Item         Item       `gorm:"references:ItemId" json:"item"`
	Damage       int16      `json:"damage"`
	Radius       int16      `json:"radius"`
	DamageTypeId int32      `json:"damage_type_id"`
	DamageType   DamageType `gorm:"references:DamageType" json:"damageType"`
	WeaponTypeId int32      `json:"weapon_type_id"`
	WeaponType   WeaponType `gorm:"references:WeaponType" json:"weaponType"`
}

type CreateWeaponRequest struct {
	CharacterName string `json:"characterName" binding:"required"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
	Damage        int16  `json:"damage"`
	Radius        int16  `json:"Radius"`
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
	NameRu       string `json:"nameRu"`
	DscRu        string `json:"dscRu"`
}
