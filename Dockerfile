# Krok 1: Budowanie aplikacji przy użyciu Mavena i Javy 17
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Krok 2: Uruchamianie aplikacji
FROM eclipse-temurin:17-jre
COPY --from=build /target/techcorp-web-game-1.0.0.jar techcorp-web-game.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "techcorp-web-game.jar"]