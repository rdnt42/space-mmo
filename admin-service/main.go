package main

import (
	"admin-service/controllers"
	"fmt"
	"github.com/gin-gonic/gin"
	_ "github.com/lib/pq"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
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

	err := server.Run("localhost:8080")
	if err != nil {
		panic(err)
	}
}

func initDb() {
	var err error
	connStr := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)

	DB, err = gorm.Open(postgres.Open(connStr), &gorm.Config{})

	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("DB connected")
}

func initEngines(rg *gin.RouterGroup) {
	controllers.NewEngineController(DB)
	router := rg.Group("engines")
	router.POST("/", controllers.CreateEngine)
}
