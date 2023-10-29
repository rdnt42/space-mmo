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

func CreateHull(ctx *gin.Context) {
	var req *models.CreateHullRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": err.Error()})

		return
	}

	equipment := &models.Item{
		CharacterName: req.CharacterName,
		ItemTypeId:    constants.HullItemTypeId,
		UpgradeLevel:  0,
		Cost:          100000,
		NameRu:        req.NameRu,
		DscRu:         req.DscRu,
		SlotId:        8,
		StorageId:     1,
	}

	newHull := &models.Hull{
		Item:       *equipment,
		Hp:         req.Hp,
		Evasion:    req.Evasion,
		Armor:      req.Armor,
		HullTypeId: req.HullTypeId,
	}

	result := db.Create(&newHull)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusCreated, newHull)
}

func UpdateHull(ctx *gin.Context) {
	var req *models.UpdateHullRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return
	}

	hullId := ctx.Param("hullId")
	var hull models.Hull
	result := db.Preload(clause.Associations).First(&hull, hullId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"status": "error",
			"message": fmt.Sprintf("Hull with id: %s dosn't exists", hullId)})
		return
	}

	hull.Item.DscRu = req.DscRu
	hull.Item.NameRu = req.NameRu
	hull.Hp = *req.Hp
	hull.Evasion = *req.Evasion
	hull.Armor = *req.Armor

	result = db.Session(&gorm.Session{FullSaveAssociations: true}).Save(&hull)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusOK, hull)
}

func GetHulls(ctx *gin.Context) {
	var hulls []models.Hull
	results := db.Preload(clause.Associations).Find(&hulls)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, hulls)
}

func GetHull(ctx *gin.Context) {
	hullId := ctx.Param("hullId")
	var hull models.Hull
	result := db.Preload(clause.Associations).First(&hull, hullId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("Hull with id: %s dosn't exists", hullId))
		return
	}

	ctx.JSON(http.StatusOK, hull)
}

func DeleteHull(ctx *gin.Context) {
	hullId := ctx.Param("hullId")
	var hull models.Hull
	result := db.Preload(clause.Associations).First(&hull, hullId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("Hull with id: %s dosn't exists", hullId))
		return
	}

	result = db.Delete(&hull)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	result = db.Select(clause.Associations).Delete(&hull.Item)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	ctx.JSON(http.StatusNoContent, nil)
}
