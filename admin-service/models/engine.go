package models

type Engine struct {
	ItemId       int64      `gorm:"primary_key;auto_increment" json:"id"`
	Item         Item       `gorm:"references:ItemId" json:"item"`
	Speed        int16      `json:"speed"`
	Jump         int32      `json:"jump"`
	EngineTypeId int32      `json:"engineTypeId"`
	EngineType   EngineType `gorm:"references:EngineTypeId" json:"engineType"`
}

type CreateEngineRequest struct {
	CharacterName string `json:"characterName" binding:"required"`
	UpgradeLevel  int16  `json:"upgradeLevel"`
	Cost          int32  `json:"cost"`
	Jump          int32  `json:"jump" binding:"required"`
	Speed         int16  `json:"speed" binding:"required"`
	EngineTypeId  int32  `json:"engineTypeId"`
	NameRu        string `json:"nameRu"`
	DscRu         string `json:"dscRu"`
}

type UpdateEngineRequest struct {
	UpgradeLevel *int16 `json:"upgradeLevel" binding:"required"`
	Cost         *int32 `json:"cost" binding:"required"`
	Speed        *int16 `json:"speed" binding:"required"`
	Jump         *int32 `json:"jump" binding:"required"`
	NameRu       string `json:"nameRu"`
	DscRu        string `json:"dscRu"`
}
