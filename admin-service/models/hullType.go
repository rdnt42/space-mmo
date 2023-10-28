package models

type HullType struct {
	HullTypeId int32  `gorm:"primary_key" json:"id"`
	Cost       int32  `json:"cost"`
	Hp         int16  `json:"hp"`
	Evasion    int16  `json:"evasion"`
	Armor      int16  `json:"armor"`
	NameRu     string `json:"nameRu"`
	DscRu      string `json:"dscRu"`
}
