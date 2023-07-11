## Space MMO Project

### Structure

- **Storage module**: This service is responsible for saving all data and sending data by request when other services need
  it. Java with Micronaut, Reactive Streams, PostgreSQL, Kafka.
- **Mechanics module**: This service responsible for game mechanics like calculate characters position, inventory state
  and world physics
- **Client module**: Using JS with PixiJS for rendering objects. All other game logic uses Vanilla JS: Socket connection, keyboard events, calculating
