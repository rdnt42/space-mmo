package kafka

import (
	"context"
	"encoding/json"
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
	"time"

	"github.com/segmentio/kafka-go"
)

type CharacterMessage struct {
	Key string `json:"key"`
}

func ProduceRefreshCharacters(ctx *gin.Context) {
	topic := "characters"
	partition := 0

	conn, err := kafka.DialLeader(context.Background(), "tcp", "localhost:9092", topic, partition)
	if err != nil {
		log.Fatal("failed to dial leader:", err)
	}

	_ = conn.SetWriteDeadline(time.Now().Add(10 * time.Second))

	mess := CharacterMessage{
		Key: "CHARACTERS_GET_ALL",
	}
	jsonData, _ := json.Marshal(mess)
	_, err = conn.WriteMessages(
		kafka.Message{Value: jsonData},
	)
	if err != nil {
		log.Fatal("failed to write messages:", err)
	}

	if err := conn.Close(); err != nil {
		log.Fatal("failed to close writer:", err)
	}

	ctx.JSON(http.StatusOK, nil)
}
