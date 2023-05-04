package models

type Item struct {
	ItemId        int64  `gorm:"primary_key;auto_increment" json:"id"`
	SlotId        int16  `json:"slotId"`
	CharacterName string `json:"characterName"`
	ItemTypeId    int16  `json:"itemTypeId"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
	NameRu        string `json:"nameRu"`
	DscRu         string `json:"dscRu"`
}

func (Item) TableName() string {
	return "items"
}
