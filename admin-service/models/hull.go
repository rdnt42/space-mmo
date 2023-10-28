package models

type Hull struct {
	ItemId     int64    `gorm:"primary_key;auto_increment" json:"id"`
	Item       Item     `gorm:"references:ItemId" json:"item"`
	Hp         int16    `json:"hp"`
	Evasion    int16    `json:"evasion"`
	Armor      int16    `json:"armor"`
	HullTypeId int32    `json:"hullTypeId"`
	HullType   HullType `gorm:"references:HullTypeId" json:"hullType"`
}

type CreateHullRequest struct {
	CharacterName string `json:"characterName" binding:"required"`
	Hp            int16  `json:"hp"`
	Evasion       int16  `json:"evasion"`
	Armor         int16  `json:"armor"`
	HullTypeId    int32  `json:"hullTypeId"`
	NameRu        string `json:"nameRu"`
	DscRu         string `json:"dscRu"`
}

type UpdateHullRequest struct {
	Hp      *int16 `json:"hp"`
	Evasion *int16 `json:"evasion"`
	Armor   *int16 `json:"armor"`
	NameRu  string `json:"nameRu"`
	DscRu   string `json:"dscRu"`
}
