package services

import (
	"admin-service/constants"
	"admin-service/models"
	"fmt"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
	"net/http"
)

func CreateCargoHook(ctx *gin.Context) {
	var req *models.CreateCargoHookRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": err.Error()})

		return
	}

	var freeSlot int16
	db.Raw(getFreeSlotSelect(), req.CharacterName, req.CharacterName).Scan(&freeSlot)

	equipment := &models.Item{
		CharacterName: req.CharacterName,
		ItemTypeId:    constants.CargoHookItemTypeId,
		UpgradeLevel:  req.UpgradeLevel,
		Cost:          req.Cost,
		NameRu:        req.NameRu,
		DscRu:         req.DscRu,
		SlotId:        freeSlot,
		StorageId:     2,
	}

	newCargoHook := &models.CargoHook{
		Item:            *equipment,
		LoadCapacity:    req.LoadCapacity,
		Radius:          req.Radius,
		CargoHookTypeId: req.CargoHookTypeId,
	}

	result := db.Create(&newCargoHook)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusCreated, newCargoHook)
}

func UpdateCargoHook(ctx *gin.Context) {
	var req *models.UpdateCargoHookRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return
	}

	cargoHookId := ctx.Param("cargoHookId")
	var cargoHook models.CargoHook
	result := db.Preload(clause.Associations).First(&cargoHook, cargoHookId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"status": "error",
			"message": fmt.Sprintf("CargoHook with id: %s dosn't exists", cargoHookId)})
		return
	}

	cargoHook.Item.UpgradeLevel = *req.UpgradeLevel
	cargoHook.Item.Cost = *req.Cost
	cargoHook.Item.DscRu = req.DscRu
	cargoHook.Item.NameRu = req.NameRu
	cargoHook.LoadCapacity = *req.LoadCapacity
	cargoHook.Radius = *req.Radius

	result = db.Session(&gorm.Session{FullSaveAssociations: true}).Save(&cargoHook)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusOK, cargoHook)
}

func GetCargoHooks(ctx *gin.Context) {
	var cargoHooks []models.CargoHook
	results := db.Preload(clause.Associations).Find(&cargoHooks)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, cargoHooks)
}

func GetCargoHook(ctx *gin.Context) {
	cargoHookId := ctx.Param("cargoHookId")
	var cargoHook models.CargoHook
	result := db.Preload(clause.Associations).First(&cargoHook, cargoHookId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("CargoHook with id: %s dosn't exists", cargoHookId))
		return
	}

	ctx.JSON(http.StatusOK, cargoHook)
}

func DeleteCargoHook(ctx *gin.Context) {
	cargoHookId := ctx.Param("cargoHookId")
	var cargoHook models.CargoHook
	result := db.Preload(clause.Associations).First(&cargoHook, cargoHookId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("CargoHook with id: %s dosn't exists", cargoHookId))
		return
	}

	result = db.Delete(&cargoHook)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	result = db.Select(clause.Associations).Delete(&cargoHook.Item)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	ctx.JSON(http.StatusNoContent, nil)
}
