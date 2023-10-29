package main

import (
	"admin-service/services/items"
	"admin-service/services/kafka"
	"fmt"
	"github.com/gin-gonic/gin"
	_ "github.com/lib/pq"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
	"log"
)

const (
	host     = "localhost"
	port     = 5432
	user     = "manager"
	password = "manager"
	dbname   = "space_mmo_storage"
)

var DB *gorm.DB

func main() {
	initDb()

	server := gin.Default()
	group := server.Group("/api")

	initEngines(group)
	initEngineTypes(group)
	initCargoHooks(group)
	initCargoHookTypes(group)
	initFuelTanks(group)
	initFuelTankTypes(group)
	initWeapons(group)
	initWeaponTypes(group)
	initDamageTypes(group)
	initHulls(group)
	initHullTypes(group)
	initCharactersRefresh(group)

	err := server.Run("localhost:8080")
	if err != nil {
		panic(err)
	}
}

func initDb() {
	var err error
	connStr := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)

	DB, err = gorm.Open(postgres.Open(connStr), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Info),
	})

	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("DB connected")
	items.CreateDbLink(DB)
}

func initCharactersRefresh(rg *gin.RouterGroup) {
	router := rg.Group("characters")
	router.POST("/refresh", kafka.ProduceRefreshCharacters)
}

func initEngines(rg *gin.RouterGroup) {
	router := rg.Group("engines")
	router.POST("/", items.CreateEngine)
	router.GET("/", items.GetEngines)
	router.GET("/:engineId", items.GetEngine)
	router.PUT("/:engineId", items.UpdateEngine)
	router.DELETE("/:engineId", items.DeleteEngine)
}

func initEngineTypes(rg *gin.RouterGroup) {
	router := rg.Group("engine_types")
	router.GET("/", items.GetEngineTypes)
}

func initCargoHooks(rg *gin.RouterGroup) {
	router := rg.Group("cargoHooks")
	router.POST("/", items.CreateCargoHook)
	router.GET("/", items.GetCargoHooks)
	router.GET("/:cargoHookId", items.GetCargoHook)
	router.PUT("/:cargoHookId", items.UpdateCargoHook)
	router.DELETE("/:cargoHookId", items.DeleteCargoHook)
}

func initCargoHookTypes(rg *gin.RouterGroup) {
	router := rg.Group("cargo_hook_types")
	router.GET("/", items.GetCargoHookTypes)
}

func initFuelTanks(rg *gin.RouterGroup) {
	router := rg.Group("fuelTanks")
	router.POST("/", items.CreateFuelTank)
	router.GET("/", items.GetFuelTanks)
	router.GET("/:fuelTankId", items.GetFuelTank)
	router.PUT("/:fuelTankId", items.UpdateFuelTank)
	router.DELETE("/:fuelTankId", items.DeleteFuelTank)
}

func initFuelTankTypes(rg *gin.RouterGroup) {
	router := rg.Group("fuel_tank_types")
	router.GET("/", items.GetFuelTankTypes)
}

func initWeapons(rg *gin.RouterGroup) {
	router := rg.Group("weapons")
	router.POST("/", items.CreateWeapon)
	router.GET("/", items.GetWeapons)
	router.GET("/:weaponId", items.GetWeapon)
	router.PUT("/:weaponId", items.UpdateWeapon)
	router.DELETE("/:weaponId", items.DeleteWeapon)
}

func initWeaponTypes(rg *gin.RouterGroup) {
	router := rg.Group("weapon_types")
	router.GET("/", items.GetWeaponTypes)
}

func initDamageTypes(rg *gin.RouterGroup) {
	router := rg.Group("damage_types")
	router.GET("/", items.GetDamageTypes)
}

func initHulls(rg *gin.RouterGroup) {
	router := rg.Group("hulls")
	router.POST("/", items.CreateHull)
	router.GET("/", items.GetHulls)
	router.GET("/:hullId", items.GetHull)
	router.PUT("/:hullId", items.UpdateHull)
	router.DELETE("/:hullId", items.DeleteHull)
}

func initHullTypes(rg *gin.RouterGroup) {
	router := rg.Group("hull_types")
	router.GET("/", items.GetHullTypes)
}
