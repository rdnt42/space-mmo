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

### CMD_UPDATE_MOTION

send update and get updated result

#### response flux

shipTypeId - hull type for rendering

```json
{
  "command": "CMD_UPDATE_MOTION",
  "data": {
    "characterName": "name",
    "x": 100.00,
    "y": 500.00,
    "angle": 90,
    "speed": 10,
    "shipTypeId": 1,
    "hp": 500,
    "evasion": 2,
    "armor": 5
  }
}
```

### CMD_UPDATE_INVENTORY_ITEM

change item slot
slotId - inventory slot in the hold

```json
{
  "command": "CMD_UPDATE_INVENTORY_ITEM",
  "data": {
    "id": 1000000,
    "slotId": 1
  }
}
```