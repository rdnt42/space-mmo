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
	var payload *models.CreateEngineRequest

	if err := ctx.ShouldBindJSON(&payload); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": err.Error()})

		return
	}

	newEngine := &models.Engine{
		CharacterName: payload.CharacterName,
		EngineTypeId:  payload.EngineTypeId,
		Speed:         payload.Speed,
		UpgradeLevel:  payload.UpgradeLevel,
		Cost:          payload.Cost,
	}

	result := db.Create(newEngine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": result.Error.Error()})
		return
	}

	ctx.JSON(http.StatusCreated, gin.H{"status": "success", "data": newEngine})
}

func UpdateEngine(ctx *gin.Context) {
	var payload *models.UpdateEngineRequest

	if err := ctx.ShouldBindJSON(&payload); err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return
	}

	engineId := ctx.Param("engineId")
	var engine models.Engine
	result := db.First(&engine, engineId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"status": "error",
			"message": fmt.Sprintf("Engine with id: %d dosn't exists", payload.EngineTypeId)})
		return
	}

	engine.CharacterName = payload.CharacterName
	engine.EngineTypeId = payload.EngineTypeId
	engine.Speed = payload.Speed
	engine.UpgradeLevel = payload.UpgradeLevel
	engine.Cost = payload.Cost

	result = db.Save(&engine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": result.Error.Error()})
		return
	}

	ctx.JSON(http.StatusOK, gin.H{"status": "success", "data": engine})
}

func GetEngines(ctx *gin.Context) {
	var engines []models.Engine
	results := db.Find(&engines)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": results.Error})
		return
	}

	ctx.JSON(http.StatusOK, gin.H{"status": "success", "results": len(engines), "data": engines})
}

func DeleteEngine(ctx *gin.Context) {
	engineId := ctx.Param("engineId")
	var engine models.Engine
	result := db.First(&engine, engineId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"status": "error",
			"message": fmt.Sprintf("Engine with id: %s dosn't exists", engineId)})
		return
	}

	result = db.Delete(&engine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": result.Error})
		return
	}

	ctx.JSON(http.StatusNoContent, nil)
}
