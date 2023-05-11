package main

import (
	"admin-service/services"
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
	services.CreateDbLink(DB)
}

func initEngines(rg *gin.RouterGroup) {
	router := rg.Group("engines")
	router.POST("/", services.CreateEngine)
	router.GET("/", services.GetEngines)
	router.GET("/:engineId", services.GetEngine)
	router.PUT("/:engineId", services.UpdateEngine)
	router.DELETE("/:engineId", services.DeleteEngine)
}

func initEngineTypes(rg *gin.RouterGroup) {
	router := rg.Group("engine_types")

	router.GET("/", services.GetEngineTypes)
}

func initCargoHooks(rg *gin.RouterGroup) {
	router := rg.Group("cargoHooks")
	router.POST("/", services.CreateCargoHook)
	router.GET("/", services.GetCargoHooks)
	router.GET("/:cargoHookId", services.GetCargoHook)
	router.PUT("/:cargoHookId", services.UpdateCargoHook)
	router.DELETE("/:cargoHookId", services.DeleteCargoHook)
}

func initCargoHookTypes(rg *gin.RouterGroup) {
	router := rg.Group("cargo_hook_types")

	router.GET("/", services.GetCargoHookTypes)
}

func initFuelTanks(rg *gin.RouterGroup) {
	router := rg.Group("fuelTanks")
	router.POST("/", services.CreateFuelTank)
	router.GET("/", services.GetFuelTanks)
	router.GET("/:fuelTankId", services.GetFuelTank)
	router.PUT("/:fuelTankId", services.UpdateFuelTank)
	router.DELETE("/:fuelTankId", services.DeleteFuelTank)
}

func initFuelTankTypes(rg *gin.RouterGroup) {
	router := rg.Group("fuel_tank_types")

	router.GET("/", services.GetFuelTankTypes)
}
