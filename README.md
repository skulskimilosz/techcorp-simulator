# TechCorp Management Simulator

Projekt zaliczeniowy z programowania obiektowego w Javie.

Gra turowa, w której gracz zarządza firmą technologiczną – zatrudnia pracowników, realizuje projekty IT, zarządza budżetem i reaguje na losowe zdarzenia rynkowe. Celem jest ukończenie 5 kontraktów projektowych zanim firma zbankrutuje.

---

## Jak uruchomić lokalnie

**Wymagania:** Java 17+, Maven 3.6+

```bash
# Kompilacja i uruchomienie
mvn package && java -cp target/classes com.example.techcorp.Main

# Tylko kompilacja
mvn compile

# Uruchomienie przez Maven
mvn exec:java
```

---

## Struktura projektu

```
src/main/java/com/example/techcorp/
│
├── Main.java                    # Punkt wejścia – inicjalizacja i start gry
├── Company.java                 # Stan firmy: gotówka/pożyczki, pracownicy, projekty
├── Employee.java                # Abstrakcyjna klasa bazowa pracownika
├── Developer.java               # Podklasa: pełny wkład skilla (×1)
├── Tester.java                  # Podklasa: połowa skilla (×0.5)
├── Manager.java                 # Podklasa: jedna trzecia skilla (×0.33)
├── Intern.java                  # Podklasa: jedna czwarta skilla (×0.25)
├── EmployeeFactory.java         # Wzorzec Factory – losowanie kandydatów
├── Project.java                 # Kontrakt projektowy z postępem i nagrodą, metody z dodawaniem pracowników do projektu, zwalnianiem ich czyli usuwanie z projektu
├── ProjectStatus.java           # Enum: ACTIVE / COMPLETED
│
├── engine/
│   └── GameEngine.java          # Silnik gry – główna pętla, logika tur
│
├── ui/
│   └── ConsoleUI.java           # Interfejs konsolowy – wyświetlanie i odczyt inputu
│
└── events/
    ├── GameEvent.java           # Interfejs zdarzeń losowych
    ├── MarketSlowdownEvent.java # Spowolnienie rynku – straty finansowe
    ├── EmployeeStrikeEvent.java # Strajk pracowniczy – koszt proporcjonalny do płac
    ├── GovernmentGrantEvent.java# Grant rządowy – dodatkowe środki
    ├── DataBreachEvent.java     # Naruszenie GDPR (RODO) – kara finansowa
    ├── OfficeFireEvent.java     # Pożar biura – koszty naprawy
    └── TechBoomEvent.java       # Boom technologiczny – dodatkowe przychody
```

---

## Zasady gry

**Cel:** ukończyć 5 kontraktów projektowych.

**Przegrana:** gotówka firmy spada poniżej 0 zł (bankructwo).

### Typy pracowników

| Rola      | Wkład w projekt | Wynagrodzenie (za skill) |
|-----------|-----------------|--------------------------|
| Developer | skill × 1       | skill × 600              |
| Tester    | skill / 2       | skill × 500              |
| Manager   | skill / 3       | skill × 650              |
| Intern    | skill / 4       | skill × 300              |

### Dostępne projekty

| Projekt                 | Wymagana praca | Nagroda   |
|-------------------------|----------------|-----------|
| Mobile App              | 40             | 30 000 zł |
| E-Commerce Platform     | 50             | 35 000 zł |
| Cloud Migration         | 75             | 55 000 zł |
| AI Chatbot Integration  | 100            | 80 000 zł |
| Cybersecurity Audit     | 140            | 110 000 zł|

### Zdarzenia losowe (48% szansa per tura)

| Zdarzenie              | Efekt                                            |
|------------------------|--------------------------------------------------|
| Market Slowdown        | -4 000 zł                                        |
| Employee Strike        | -15% łącznego funduszu płac                      |
| Government Grant       | +5 000 zł                                        |
| Data Breach (GDPR)     | -6 000 zł                                        |
| Office Fire            | -8 000 zł                                        |
| Tech Boom              | +7 000 zł                                        |

### System kredytowy

Bank oferuje kredyty z 3% miesięcznym oprocentowaniem. Limit kredytowy rośnie co kilka tur:

| Tury   | Limit     |
|--------|-----------|
| 1–4    | 10 000 zł |
| 5–9    | 30 000 zł |
| 10–14  | 50 000 zł |
| ...    | ...       |
| 40+    | 210 000 zł|

---

## Wzorce projektowe zastosowane w projekcie

- **Inheritance + Polymorphism** – hierarchia `Employee` z 4 podklasami, każda nadpisuje `work()`
- **Strategy (Interface)** – `GameEvent` jako interfejs; każde zdarzenie to osobna strategia
- **Factory Method** – `EmployeeFactory.generateRandomCandidate()` centralizuje tworzenie pracowników
- **Single Responsibility** – `ConsoleUI` tylko UI, `GameEngine` tylko logika gry, `Company` tylko stan firmy

---

## Deployment na Render

Projekt TechCorp Simulator został rozszerzony o warstwę webową opartą na Spring Boot i wdrożony na platformie Render.

**Co zostało zmienione**

- **pom.xml** — dodano zależności `spring-boot-starter-web` oraz `spring-boot-maven-plugin`.
- **TechCorpApplication.java** — klasa startowa z adnotacją `@SpringBootApplication`.
- **api/HealthController.java** — endpoint `GET /health` potwierdzający działanie serwera.
- **api/GameController.java** — endpointy REST do zarządzania stanem gry.
- **src/main/resources/application.properties** — konfiguracja portu: `${PORT:8080}`.
- **Dockerfile** — plik konfiguracyjny do konteneryzacji (wymagany przez Render).

### Dockerfile

Dockerfile wykorzystuje multi-stage build:

1. Etap budowania — obraz `maven:3.8.5-openjdk-17` kompiluje projekt i tworzy JAR.
2. Etap uruchamiania — obraz `eclipse-temurin:17-jre` uruchamia gotowy JAR.

### Dostępne endpointy API

| Metoda | Endpoint | Opis |
|--------:|:--------:|:----|
| GET | `/health` | Sprawdzenie czy serwer działa |
| GET | `/game/state` | Aktualny stan firmy (tura, gotówka, projekty) |
| POST | `/game/work` | Symulacja jednej tury pracy |
| POST | `/game/reset` | Reset gry do stanu początkowego |

**Publiczny URL:** https://techcorp-game-ms-api.onrender.com

Przykład endpointu: https://techcorp-game-ms-api.onrender.com/game/state

### Jak uruchomić lokalnie (Spring Boot)

Gra konsolowa (bez zmian):

```bash
mvn compile
java -cp target/classes com.example.techcorp.Main
```

Serwer Spring Boot (lokalnie):

```bash
mvn spring-boot:run
# lub
mvn clean package -DskipTests
java -jar target/techcorp-web-game-1.0.0.jar
```
# Warstwa Persystencji – PostgreSQL i wzorzec Repository

## Cel

Dane gry (gotówka, tury, projekty) istnieją domyślnie tylko w pamięci serwera.
Po restarcie aplikacji wszystko znika. Aby temu zapobiec, projekt implementuje
**warstwę persystencji** opartą na bazie danych PostgreSQL.

---

## Architektura

Projekt stosuje **wzorzec Repository** — kluczową zasadę inżynierii oprogramowania:

```
GameController (logika gry)
        |
        v
  GameRepository (interfejs)
        |
        v
PostgresGameRepository (implementacja JDBC)
        |
        v
  Baza PostgreSQL na Render
```

`GameController` nie wie jak dane są przechowywane — zna tylko interfejs `GameRepository`.
Dzięki temu można w każdej chwili zamienić PostgreSQL na pliki tekstowe bez zmiany logiki gry.
Jest to bezpośrednie zastosowanie zasady **Single Responsibility Principle (SRP)**.

---

## Klasy warstwy persystencji

| Klasa | Odpowiedzialność |
|---|---|
| `GameRepository` | Interfejs — definiuje `save()` i `load()` |
| `PostgresGameRepository` | Implementacja JDBC — komunikacja z bazą danych |
| `GameState` | Obiekt reprezentujący stan gry do zapisania |

---

## Co jest zapisywane po każdej turze

Po każdym wywołaniu `POST /game/work` aplikacja automatycznie zapisuje do bazy:

| Kolumna | Opis |
|---|---|
| `game_id` | Unikalny identyfikator sesji gry |
| `company_name` | Nazwa firmy |
| `cash` | Aktualna gotówka |
| `loan_amount` | Zadłużenie |
| `current_turn` | Numer tury |
| `completed_projects` | Liczba ukończonych projektów |
| `saved_at` | Znacznik czasu zapisu (automatyczny) |

---

## Tabela w PostgreSQL

```sql
CREATE TABLE game_state (
    id SERIAL PRIMARY KEY,
    game_id VARCHAR(20),
    company_name VARCHAR(100),
    cash DOUBLE PRECISION,
    loan_amount DOUBLE PRECISION,
    current_turn INT,
    completed_projects INT,
    saved_at TIMESTAMP DEFAULT NOW()
);
```

---

## Konfiguracja na Render

Połączenie z bazą odbywa się przez zmienną środowiskową `DB_URL` ustawioną na Render:

```
DB_URL=jdbc:postgresql://host:port/dbname?user=xxx&password=yyy
```

Aplikacja odczytuje ją w runtime:

```java
private final String url = System.getenv("DB_URL");
```

Dzięki temu **dane połączenia nigdy nie trafiają do repozytorium GitHub** —
to standard bezpieczeństwa w aplikacjach produkcyjnych.

---

## Przepływ danych

```
Użytkownik wywołuje POST /game/work
        |
        v
GameController przetwarza turę
        |
        v
Tworzy obiekt GameState ze stanem gry
        |
        v
Wywołuje repository.save(state)
        |
        v
PostgresGameRepository wykonuje SQL INSERT
        |
        v
Stan gry zapisany w bazie PostgreSQL na Render
```
# Uruchomienie i obsługa projektu (z bazą danych)

## Kompilacja i uruchomienie lokalne

```bash
mvn clean compile && java -cp target/classes com.example.techcorp.Main
```

---

## PostgreSQL – konfiguracja

### Instalacja klienta PostgreSQL w GitHub Codespaces

```bash
sudo apt-get update && sudo apt-get install -y postgresql-client
```

### Połączenie z bazą danych na Render

```bash
psql "postgresql://techcorp_db_user:[hasło]@dpg-d8mnfa1kh4rs738iptpg-a.frankfurt-postgres.render.com/techcorp_db"
```

Struktura adresu bazy danych:
- `dpg-d8mnfa1kh4rs738iptpg-a` — unikalny identyfikator bazy na Render
- `.frankfurt-postgres.render.com` — serwer Rendera w regionie Frankfurt
- `/techcorp_db` — nazwa bazy danych

### Odczyt zapisanych stanów gry

```sql
SELECT * FROM game_state;
```

---

## REST API – endpointy

Aplikacja działa na Render pod adresem:
```
https://techcorp-game-ms-api.onrender.com
```

### Reset gry

```bash
curl -X POST https://techcorp-game-ms-api.onrender.com/game/reset
```

Resetuje grę do stanu początkowego — nowa firma, nowy `game_id`, tura wraca do 1.

### Nowa tura

```bash
curl -X POST https://techcorp-game-ms-api.onrender.com/game/work
```

Symuluje jedną turę pracy — wypłaca wynagrodzenia, postępuje projekty, zapisuje stan do bazy PostgreSQL.

### Stan gry

```bash
curl https://techcorp-game-ms-api.onrender.com/game/state
```

Zwraca aktualny stan firmy: turę, gotówkę, pracowników i projekty.
---
## Autor
Miłosz Skulski
