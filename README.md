# TechCorp Management Simulator

Projekt zaliczeniowy z programowania obiektowego w Javie.

Gra turowa, w której gracz zarządza firmą technologiczną – zatrudnia pracowników, realizuje projekty IT, zarządza budżetem i reaguje na losowe zdarzenia rynkowe. Celem jest ukończenie 5 kontraktów projektowych zanim firma zbankrutuje.

---

## Jak uruchomić

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
├── Company.java                 # Stan firmy: gotówka, pracownicy, projekty
├── Employee.java                # Abstrakcyjna klasa bazowa pracownika
├── Developer.java               # Podklasa: pełny wkład skilla (×1)
├── Tester.java                  # Podklasa: połowa skilla (×0.5)
├── Manager.java                 # Podklasa: jedna trzecia skilla (×0.33)
├── Intern.java                  # Podklasa: jedna czwarta skilla (×0.25)
├── EmployeeFactory.java         # Wzorzec Factory – losowanie kandydatów
├── Project.java                 # Kontrakt projektowy z postępem i nagrodą
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
    ├── DataBreachEvent.java     # Naruszenie GDPR – kara finansowa
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

## Autor

Miłosz Skulski
