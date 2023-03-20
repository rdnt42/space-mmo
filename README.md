## Kafka keys contract

### Character change state

- CHARACTER_CREATE - create new character
- CHARACTER_MOTION_UPDATE - update character x, y, angle
- CHARACTER_STATE_UPDATE - update is_online sign

---

### Get Character

- CHARACTERS_GET_ONE - get one character (connected new player)
- CHARACTERS_GET_ALL - get all online players (init all players)


## Backend to frontend contract

### CMD_UPDATE_CURRENT_PLAYER

#### response:

```json
{
  "command": "CMD_UPDATE_CURRENT_PLAYER",
  "data": {
    "playerMotion": {
      "playerName": "name",
      "x": 100,
      "y": 500,
      "angle": 90,
      "speed": 10
    },
    
    "playersMotions": []
  }
}
```