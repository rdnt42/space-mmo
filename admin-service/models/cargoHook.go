package models

type CargoHook struct {
	ItemId          int64         `gorm:"primary_key;auto_increment" json:"id"`
	Item            Item          `gorm:"references:ItemId" json:"item"`
	LoadCapacity    int16         `json:"loadCapacity"`
	Radius          int16         `json:"radius"`
	CargoHookTypeId int32         `json:"cargoHookTypeId"`
	CargoHookType   CargoHookType `gorm:"references:CargoHookTypeId" json:"cargoHookType"`
}

type CreateCargoHookRequest struct {
	CharacterName   string `json:"characterName" binding:"required"`
	UpgradeLevel    int16  `json:"upgradeLevel"`
	Cost            int32  `json:"cost"`
	LoadCapacity    int16  `json:"loadCapacity" binding:"required"`
	Radius          int16  `json:"radius" binding:"required"`
	CargoHookTypeId int32  `json:"cargoHookTypeId"`
	NameRu          string `json:"nameRu"`
	DscRu           string `json:"dscRu"`
}

type UpdateCargoHookRequest struct {
	UpgradeLevel *int16 `json:"upgradeLevel" binding:"required"`
	Cost         *int32 `json:"cost" binding:"required"`
	LoadCapacity *int16 `json:"loadCapacity" binding:"required"`
	Radius       *int16 `json:"radius" binding:"required"`
	NameRu       string `json:"nameRu"`
	DscRu        string `json:"dscRu"`
}
