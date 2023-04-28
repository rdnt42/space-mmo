package models

type Engine struct {
	EquipmentId   int64  `gorm:"primary_key;auto_increment;not_null" json:"id"`
	Equipped      bool   `json:"equipped"`
	SlotId        int16  `json:"slotId"`
	CharacterName string `json:"characterName"`
	EngineTypeId  int16  `json:"engineTypeId"`
	Speed         int16  `json:"speed"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
}

type CreateEngineRequest struct {
	SlotId        int16  `json:"slotId"  binding:"required"`
	CharacterName string `json:"characterName"  binding:"required"`
	EngineTypeId  int16  `json:"engineTypeId"  binding:"required"`
	Speed         int16  `json:"speed"  binding:"required"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
}

type UpdateEngineRequest struct {
	CharacterName string `json:"characterName"`
	EngineTypeId  int16  `json:"engineTypeId"`
	Speed         int16  `json:"speed"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
}
