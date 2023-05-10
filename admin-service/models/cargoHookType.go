package models

type CargoHookType struct {
	CargoHookTypeId int32  `gorm:"primary_key" json:"id"`
	Cost            int32  `json:"cost"`
	LoadCapacity    int16  `json:"loadCapacity"`
	Radius          int16  `json:"radius"`
	NameRu          string `json:"nameRu"`
	DscRu           string `json:"dscRu"`
}
