## Space MMO Project

### Structure

- **Storage module**: This service responsible for saving all data and sending data by request when others service need
  it. Java with Micronaut, Reactive Streams, PostgreSQL, Kafka.
- **Mechanics module**: This service responsible for game mechanics like calculate characters position, inventory state
  and world physics