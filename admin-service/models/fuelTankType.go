package models

type FuelTankType struct {
	FuelTankTypeId int32  `gorm:"primary_key" json:"id"`
	Cost           int32  `json:"cost"`
	Capacity       int16  `json:"capacity"`
	NameRu         string `json:"nameRu"`
	DscRu          string `json:"dscRu"`
}
