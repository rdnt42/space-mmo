package items

import (
	"admin-service/models"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm/clause"
	"net/http"
)

func GetEngineTypes(ctx *gin.Context) {
	var types []models.EngineType
	results := db.Preload(clause.Associations).Find(&types)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, types)
}

func GetCargoHookTypes(ctx *gin.Context) {
	var types []models.CargoHookType
	results := db.Preload(clause.Associations).Find(&types)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, types)
}

func GetFuelTankTypes(ctx *gin.Context) {
	var types []models.FuelTankType
	results := db.Preload(clause.Associations).Find(&types)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, types)
}

func GetWeaponTypes(ctx *gin.Context) {
	var types []models.WeaponType
	results := db.Preload(clause.Associations).Find(&types)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, types)
}

func GetDamageTypes(ctx *gin.Context) {
	var types []models.DamageType
	results := db.Preload(clause.Associations).Find(&types)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, types)
}

func GetHullTypes(ctx *gin.Context) {
	var types []models.HullType
	results := db.Preload(clause.Associations).Find(&types)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, types)
}
