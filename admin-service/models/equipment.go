package models

type Equipment struct {
	EquipmentId     int64  `gorm:"primary_key;auto_increment" json:"id"`
	Equipped        bool   `json:"equipped"`
	SlotId          int16  `json:"slotId"`
	CharacterName   string `json:"characterName"`
	EquipmentTypeId int16  `json:"equipmentTypeId"`
	UpgradeLevel    int16  `json:"upgradeLevel"`
}

func (Equipment) TableName() string {
	return "equipments"
}
