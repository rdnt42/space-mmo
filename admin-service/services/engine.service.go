package services

import (
	"admin-service/models"
	"fmt"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"net/http"
)

var db *gorm.DB

func NewEngineController(DB *gorm.DB) {
	db = DB
}

func CreateEngine(ctx *gin.Context) {
	var req *models.CreateEngineRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": err.Error()})

		return
	}

	equipment := &models.Equipment{
		Equipped:        false,
		SlotId:          req.SlotId,
		CharacterName:   req.CharacterName,
		EquipmentTypeId: req.EquipmentTypeId,
		UpgradeLevel:    req.UpgradeLevel,
	}

	newEngine := &models.Engine{
		Equipment: *equipment,
		Speed:     req.Speed,
		Cost:      req.Cost,
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
	result := db.First(&engine, engineId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"status": "error",
			"message": fmt.Sprintf("Engine with id: %s dosn't exists", engineId)})
		return
	}

	engine.Equipment.CharacterName = req.CharacterName
	engine.Equipment.EquipmentTypeId = req.EquipmentTypeId
	engine.Equipment.UpgradeLevel = req.UpgradeLevel
	engine.Speed = req.Speed
	engine.Cost = req.Cost

	result = db.Save(&engine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusOK, engine)
}

func GetEngines(ctx *gin.Context) {
	var engines []models.Engine
	results := db.Find(&engines)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, engines)
}

func GetEngine(ctx *gin.Context) {
	engineId := ctx.Param("engineId")
	var engine models.Engine
	result := db.First(&engine, engineId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("Engine with id: %s dosn't exists", engineId))
		return
	}

	ctx.JSON(http.StatusOK, engine)
}

func DeleteEngine(ctx *gin.Context) {
	engineId := ctx.Param("engineId")
	var engine models.Engine
	result := db.First(&engine, engineId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("Engine with id: %s dosn't exists", engineId))
		return
	}

	result = db.Delete(&engine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	ctx.JSON(http.StatusNoContent, nil)
}
