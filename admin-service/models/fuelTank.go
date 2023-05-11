package models

type FuelTank struct {
	ItemId         int64        `gorm:"primary_key;auto_increment" json:"id"`
	Item           Item         `gorm:"references:ItemId" json:"item"`
	Capacity       int16        `json:"capacity"`
	FuelTankTypeId int32        `json:"fuelTankTypeId"`
	FuelTankType   FuelTankType `gorm:"references:FuelTankTypeId" json:"fuelTankType"`
}

type CreateFuelTankRequest struct {
	CharacterName  string `json:"characterName" binding:"required"`
	UpgradeLevel   int16  `json:"upgradeLevel"`
	Cost           int32  `json:"cost"`
	Capacity       int16  `json:"capacity" binding:"required"`
	FuelTankTypeId int32  `json:"fuelTankTypeId"`
	NameRu         string `json:"nameRu"`
	DscRu          string `json:"dscRu"`
}

type UpdateFuelTankRequest struct {
	UpgradeLevel *int16 `json:"upgradeLevel" binding:"required"`
	Cost         *int32 `json:"cost" binding:"required"`
	Capacity     *int16 `json:"capacity" binding:"required"`
	NameRu       string `json:"nameRu"`
	DscRu        string `json:"dscRu"`
}
