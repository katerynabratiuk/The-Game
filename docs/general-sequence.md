```mermaid
sequenceDiagram
    participant Client
    participant Server
    participant DB as Database
    participant Game as GameThread

    Server->>Server: Start server & socket
    Client->>Client: Start client & socket
    Client->>Server: Login/Register request
    Server->>DB: Validate credentials / Register user
    DB-->>Server: User info + playable items
    Server-->>Client: Playable items list

    Client->>Client: Player chooses items
    Client->>Server: Join game request
    Server->>Server: Create player actor
    Server->>Game: Add player to the map

    loop Each frame
        Game->>Server: Update game state (Collisions, movements, etc.)
        Server->>Client: Send game state updates
        Client->>Client: Update local state (replication)
    end