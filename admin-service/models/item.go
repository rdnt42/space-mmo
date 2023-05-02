package models

type Item struct {
	ItemId        int64  `gorm:"primary_key;auto_increment" json:"id"`
	Equipped      bool   `json:"equipped"`
	SlotId        int16  `json:"slotId"`
	CharacterName string `json:"characterName"`
	ItemTypeId    int16  `json:"itemTypeId"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
}

func (Item) TableName() string {
	return "items"
}
