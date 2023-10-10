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

func CreateFuelTank(ctx *gin.Context) {
	var req *models.CreateFuelTankRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": err.Error()})

		return
	}

	var freeSlot int16
	db.Raw(getFreeSlotSelect(), req.CharacterName, req.CharacterName).Scan(&freeSlot)

	equipment := &models.Item{
		CharacterName: req.CharacterName,
		ItemTypeId:    constants.FuelTankItemTypeId,
		UpgradeLevel:  req.UpgradeLevel,
		Cost:          req.Cost,
		NameRu:        req.NameRu,
		DscRu:         req.DscRu,
		SlotId:        freeSlot,
		StorageId:     2,
	}

	newCargoHook := &models.FuelTank{
		Item:           *equipment,
		Capacity:       req.Capacity,
		FuelTankTypeId: req.FuelTankTypeId,
	}

	result := db.Create(&newCargoHook)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusCreated, newCargoHook)
}

func UpdateFuelTank(ctx *gin.Context) {
	var req *models.UpdateFuelTankRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return
	}

	fuelTankId := ctx.Param("fuelTankId")
	var fuelTank models.FuelTank
	result := db.Preload(clause.Associations).First(&fuelTank, fuelTankId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"status": "error",
			"message": fmt.Sprintf("FuelTank with id: %s dosn't exists", fuelTankId)})
		return
	}

	fuelTank.Item.UpgradeLevel = *req.UpgradeLevel
	fuelTank.Item.Cost = *req.Cost
	fuelTank.Item.DscRu = req.DscRu
	fuelTank.Item.NameRu = req.NameRu
	fuelTank.Capacity = *req.Capacity

	result = db.Session(&gorm.Session{FullSaveAssociations: true}).Save(&fuelTank)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusOK, fuelTank)
}

func GetFuelTanks(ctx *gin.Context) {
	var fuelTanks []models.FuelTank
	results := db.Preload(clause.Associations).Find(&fuelTanks)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, fuelTanks)
}

func GetFuelTank(ctx *gin.Context) {
	fuelTankId := ctx.Param("fuelTankId")
	var fuelTank models.FuelTank
	result := db.Preload(clause.Associations).First(&fuelTank, fuelTankId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("FuelTank with id: %s dosn't exists", fuelTankId))
		return
	}

	ctx.JSON(http.StatusOK, fuelTank)
}

func DeleteFuelTank(ctx *gin.Context) {
	fuelTankId := ctx.Param("fuelTankId")
	var fuelTank models.FuelTank
	result := db.Preload(clause.Associations).First(&fuelTank, fuelTankId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("FuelTank with id: %s dosn't exists", fuelTankId))
		return
	}

	result = db.Delete(&fuelTank)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	result = db.Select(clause.Associations).Delete(&fuelTank.Item)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	ctx.JSON(http.StatusNoContent, nil)
}
