package controllers

import (
	"admin-service/models"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"net/http"
)

type EngineController struct {
	DB *gorm.DB
}

func NewEngineController(DB *gorm.DB) EngineController {
	return EngineController{DB}
}

func (ec *EngineController) CreateEngine(ctx *gin.Context) {
	var payload *models.CreateEngineRequest

	if err := ctx.ShouldBindJSON(&payload); err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return
	}

	newEngine := models.Engine{
		CharacterName: payload.CharacterName,
		EngineTypeId:  payload.EngineTypeId,
		Speed:         payload.Speed,
		UpgradeLevel:  payload.UpgradeLevel,
		Cost:          payload.Cost,
	}

	result := ec.DB.Create(newEngine)
	if result.Error != nil {
		ctx.JSON(http.StatusBadGateway, gin.H{"status": "error", "message": result.Error.Error()})
		return
	}

	ctx.JSON(http.StatusCreated, gin.H{"status": "success", "data": newEngine})
}
