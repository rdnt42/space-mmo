package items

import (
	"admin-service/constants"
	"admin-service/models"
	"fmt"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
	"net/http"
)

func CreateEngine(ctx *gin.Context) {
	var req *models.CreateEngineRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": err.Error()})

		return
	}

	var freeSlot int16
	db.Raw(getFreeSlotSelect(), req.CharacterName, req.CharacterName).Scan(&freeSlot)

	equipment := &models.Item{
		CharacterName: req.CharacterName,
		ItemTypeId:    constants.EngineItemTypeId,
		UpgradeLevel:  req.UpgradeLevel,
		Cost:          req.Cost,
		NameRu:        req.NameRu,
		DscRu:         req.DscRu,
		SlotId:        freeSlot,
		StorageId:     2,
	}

	newEngine := &models.Engine{
		Item:         *equipment,
		Speed:        req.Speed,
		Jump:         req.Jump,
		EngineTypeId: req.EngineTypeId,
	}

	result := db.Create(&newEngine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusCreated, newEngine)
}

func UpdateEngine(ctx *gin.Context) {
	var req *models.UpdateEngineRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return
	}

	engineId := ctx.Param("engineId")
	var engine models.Engine
	result := db.Preload(clause.Associations).First(&engine, engineId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"status": "error",
			"message": fmt.Sprintf("Engine with id: %s dosn't exists", engineId)})
		return
	}

	engine.Item.UpgradeLevel = *req.UpgradeLevel
	engine.Item.Cost = *req.Cost
	engine.Item.DscRu = req.DscRu
	engine.Item.NameRu = req.NameRu
	engine.Speed = *req.Speed
	engine.Jump = *req.Jump

	result = db.Session(&gorm.Session{FullSaveAssociations: true}).Save(&engine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusOK, engine)
}

func GetEngines(ctx *gin.Context) {
	var engines []models.Engine
	results := db.Preload(clause.Associations).Find(&engines)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, engines)
}

func GetEngine(ctx *gin.Context) {
	engineId := ctx.Param("engineId")
	var engine models.Engine
	result := db.Preload(clause.Associations).First(&engine, engineId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("Engine with id: %s dosn't exists", engineId))
		return
	}

	ctx.JSON(http.StatusOK, engine)
}

func DeleteEngine(ctx *gin.Context) {
	engineId := ctx.Param("engineId")
	var engine models.Engine
	result := db.Preload(clause.Associations).First(&engine, engineId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("Engine with id: %s dosn't exists", engineId))
		return
	}

	result = db.Delete(&engine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	result = db.Select(clause.Associations).Delete(&engine.Item)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	ctx.JSON(http.StatusNoContent, nil)
}
