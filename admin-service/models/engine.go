package models

type Engine struct {
	ItemId int64 `gorm:"primary_key;auto_increment" json:"id"`
	Item   Item  `gorm:"references:ItemId" json:"item"`
	Speed  int16 `json:"speed"`
	Jump   int32 `json:"jump"`
}

type CreateEngineRequest struct {
	SlotId        int16  `json:"slotId" binding:"required"`
	CharacterName string `json:"characterName" binding:"required"`
	ItemTypeId    int16  `json:"itemTypeId" binding:"required"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
	Jump          int32  `json:"jump" binding:"required"`
	Speed         int16  `json:"speed" binding:"required"`
}

type UpdateEngineRequest struct {
	CharacterName string `json:"characterName"`
	ItemTypeId    *int16 `json:"itemTypeId" binding:"required"`
	UpgradeLevel  *int16 `json:"upgradeLevel" binding:"required"`
	Cost          *int32 `json:"cost" binding:"required"`
	Speed         *int16 `json:"speed" binding:"required"`
	Jump          *int32 `json:"jump" binding:"required"`
}
