## Space MMO Project

### Structure

- **Storage module**: This service is responsible for saving all data and sending data by request when other services need
  it. Java with Micronaut, Reactive Streams, PostgreSQL, Kafka.
- **Mechanics module**: This service responsible for game mechanics like calculate characters position, inventory state
  and world physics
- **Client module**: Using JS with PixiJS for rendering objects. All other game logic uses Vanilla JS: Socket connection, keyboard events, calculating
- **Admin service**: Backend for admin panel. Supports CRUD operations for storage entities

### How to run
1) docker compose up -d
2) start storage-module. It has db migration scripts for creating tables and data
3) start mechanics module. After start it can receive socket connections
4) open http://localhost:63342/space-mmo/client-module/index.html

