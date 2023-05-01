package models

type Engine struct {
	EquipmentId int64     `gorm:"primary_key;auto_increment" json:"id"`
	Equipment   Equipment `gorm:"references:EquipmentId" json:"equipment"`
	Speed       int16     `json:"speed"`
	Cost        int32     `json:"cost"`
}

type CreateEngineRequest struct {
	SlotId          int16  `json:"slotId"  binding:"required"`
	CharacterName   string `json:"characterName"  binding:"required"`
	EquipmentTypeId int16  `json:"equipmentTypeId"  binding:"required"`
	Speed           int16  `json:"speed"  binding:"required"`
	UpgradeLevel    int16  `json:"upgradeLevel"`
	Cost            int32  `json:"cost"`
}

type UpdateEngineRequest struct {
	CharacterName   string `json:"characterName"`
	EquipmentTypeId int16  `json:"equipmentTypeId"`
	Speed           int16  `json:"speed"`
	UpgradeLevel    int16  `json:"upgradeLevel"`
	Cost            int32  `json:"cost"`
}
