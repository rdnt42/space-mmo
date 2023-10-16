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

func CreateWeapon(ctx *gin.Context) {
	var req *models.CreateWeaponRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"status": "error", "message": err.Error()})

		return
	}

	var freeSlot int16
	db.Raw(getFreeSlotSelect(), req.CharacterName, req.CharacterName).Scan(&freeSlot)

	equipment := &models.Item{
		CharacterName: req.CharacterName,
		ItemTypeId:    constants.WeaponItemTypeId,
		UpgradeLevel:  req.UpgradeLevel,
		Cost:          req.Cost,
		NameRu:        req.NameRu,
		DscRu:         req.DscRu,
		SlotId:        freeSlot,
		StorageId:     2,
	}

	newWeapon := &models.Weapon{
		Item:         *equipment,
		Damage:       req.Damage,
		Radius:       req.Radius,
		Rate:         req.Rate,
		DamageTypeId: req.DamageTypeId,
		WeaponTypeId: req.WeaponTypeId,
	}

	result := db.Create(&newWeapon)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusCreated, newWeapon)
}

func UpdateWeapon(ctx *gin.Context) {
	var req *models.UpdateWeaponRequest

	if err := ctx.ShouldBindJSON(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return
	}

	weaponId := ctx.Param("weaponId")
	var weapon models.Weapon
	result := db.Preload(clause.Associations).First(&weapon, weaponId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"status": "error",
			"message": fmt.Sprintf("Weapon with id: %s dosn't exists", weaponId)})
		return
	}

	weapon.Item.UpgradeLevel = *req.UpgradeLevel
	weapon.Item.Cost = *req.Cost
	weapon.Item.DscRu = req.DscRu
	weapon.Item.NameRu = req.NameRu
	weapon.Damage = *req.Damage
	weapon.Radius = *req.Radius
	weapon.Rate = *req.Rate

	result = db.Session(&gorm.Session{FullSaveAssociations: true}).Save(&weapon)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error.Error())
		return
	}

	ctx.JSON(http.StatusOK, weapon)
}

func GetWeapons(ctx *gin.Context) {
	var weapons []models.Weapon
	results := db.Preload(clause.Associations).Find(&weapons)
	if results.Error != nil {
		ctx.JSON(http.StatusBadRequest, results.Error)
		return
	}

	ctx.JSON(http.StatusOK, weapons)
}

func GetWeapon(ctx *gin.Context) {
	weaponId := ctx.Param("weaponId")
	var weapon models.Weapon
	result := db.Preload(clause.Associations).First(&weapon, weaponId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("Weapon with id: %s dosn't exists", weaponId))
		return
	}

	ctx.JSON(http.StatusOK, weapon)
}

func DeleteWeapon(ctx *gin.Context) {
	weaponId := ctx.Param("weaponId")
	var weapon models.Weapon
	result := db.Preload(clause.Associations).First(&weapon, weaponId)
	if result.Error != nil {
		ctx.JSON(http.StatusNotFound, fmt.Sprintf("Weapon with id: %s dosn't exists", weaponId))
		return
	}

	result = db.Delete(&weapon)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	result = db.Select(clause.Associations).Delete(&weapon.Item)
	if result.Error != nil {
		ctx.JSON(http.StatusBadRequest, result.Error)
		return
	}

	ctx.JSON(http.StatusNoContent, nil)
}
