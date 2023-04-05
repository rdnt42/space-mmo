package models

type Engine struct {
	EngineId      int64 `gorm:"primary_key;auto_increment;not_null"`
	IsActive      bool
	CharacterName string
	EngineTypeId  int16
	Speed         int16
	UpgradeLevel  int16
	Cost          int32
}

type CreateEngineRequest struct {
	CharacterName string `json:"character_name"  binding:"required"`
	EngineTypeId  int16  `json:"engine_type_id"  binding:"required"`
	Speed         int16  `json:"speed"  binding:"required"`
	UpgradeLevel  int16  `json:"upgrade_level"`
	Cost          int32  `json:"cost"`
}

type UpdateEngineRequest struct {
	EngineId      uint64 `json:"engine_id"  binding:"required"`
	CharacterName string `json:"character_name"`
	EngineTypeId  int16  `json:"engine_type_id"`
	Speed         int16  `json:"speed"`
	UpgradeLevel  int16  `json:"upgrade_level"`
	Cost          int32  `json:"cost"`
}
