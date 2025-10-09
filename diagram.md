```mermaid
graph TB
    subgraph "Client Layer"
        React[React Frontend]
    end

    subgraph "API Gateway/Load Balancer"
        Gateway[API Gateway]
    end

    subgraph "Spring Boot Services - MySQL"
        WorkoutService[Workout Service<br/>Spring Boot]
        DietService[Diet Service<br/>Spring Boot]
        AuthService[User Auth Service<br/>Spring Boot]
        
        WorkoutDB[(MySQL<br/>Workout DB)]
        DietDB[(MySQL<br/>Diet DB)]
        AuthDB[(MySQL<br/>Auth DB)]
    end

    subgraph "Express.js Services - MongoDB"
        UserDetailService[User Detail Service<br/>Express.js]
        UserDetailDB[(MongoDB<br/>User Details)]
    end

    subgraph "Event Streaming Layer"
        Kafka[Apache Kafka<br/>Event Broker]
        
        subgraph "Kafka Topics"
            T1[workout-events]
            T2[diet-events]
            T3[auth-events]
            T4[user-detail-events]
        end
    end

    React -->|HTTP/REST| Gateway
    Gateway --> WorkoutService
    Gateway --> DietService
    Gateway --> AuthService
    Gateway --> UserDetailService

    WorkoutService --> WorkoutDB
    DietService --> DietDB
    AuthService --> AuthDB
    UserDetailService --> UserDetailDB

    WorkoutService -->|Publish Events| Kafka
    DietService -->|Publish Events| Kafka
    AuthService -->|Publish Events| Kafka
    UserDetailService -->|Publish Events| Kafka

    Kafka --> T1
    Kafka --> T2
    Kafka --> T3
    Kafka --> T4

    T1 -->|Subscribe| UserDetailService
    T2 -->|Subscribe| UserDetailService
    T3 -->|Subscribe| WorkoutService
    T3 -->|Subscribe| DietService
    T3 -->|Subscribe| UserDetailService
    T4 -->|Subscribe| WorkoutService
    T4 -->|Subscribe| DietService

    style React fill:#61dafb,stroke:#333,stroke-width:2px
    style Gateway fill:#ff9900,stroke:#333,stroke-width:2px
    style WorkoutService fill:#6db33f,stroke:#333,stroke-width:2px
    style DietService fill:#6db33f,stroke:#333,stroke-width:2px
    style AuthService fill:#6db33f,stroke:#333,stroke-width:2px
    style UserDetailService fill:#68a063,stroke:#333,stroke-width:2px
    style Kafka fill:#231f20,stroke:#333,stroke-width:3px,color:#fff
    style T1 fill:#737373,stroke:#333,stroke-width:1px,color:#fff
    style T2 fill:#737373,stroke:#333,stroke-width:1px,color:#fff
    style T3 fill:#737373,stroke:#333,stroke-width:1px,color:#fff
    style T4 fill:#737373,stroke:#333,stroke-width:1px,color:#fff

    ```