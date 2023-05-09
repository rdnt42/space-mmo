package models

type EngineType struct {
	EngineTypeId int32  `gorm:"primary_key" json:"id"`
	Cost         int32  `json:"cost"`
	Speed        int32  `json:"speed"`
	Jump         int16  `json:"jump"`
	NameRu       string `json:"nameRu"`
	DscRu        string `json:"dscRu"`
}
