package com.example.techcorp.engine;

import com.example.techcorp.Company;
import com.example.techcorp.Employee;
import com.example.techcorp.EmployeeFactory;
import com.example.techcorp.Project;
import com.example.techcorp.ui.ConsoleUI;

import com.example.techcorp.events.EmployeeStrikeEvent;
import com.example.techcorp.events.MarketSlowdownEvent;
import com.example.techcorp.events.GovernmentGrantEvent;
import com.example.techcorp.events.DataBreachEvent;
import com.example.techcorp.events.OfficeFireEvent;
import com.example.techcorp.events.TechBoomEvent;

import java.util.List;

import com.example.techcorp.domain.GameState;
import com.example.techcorp.repository.FileGameRepository;
import com.example.techcorp.service.SaveManager;

/**
 * Silnik gry TechCorp – główna pętla rozgrywki.
 * <p>
 * Klasa {@code GameEngine} orkiestruje każdą turę gry: odbiera decyzje gracza,
 * deleguje wykonanie do odpowiednich klas domenowych ({@link Company}, {@link Project})
 * i sprawdza warunki wygranej/przegranej.
 * </p>
 *
 * <p>Zasady gry:</p>
 * <ul>
 *   <li>Cel: ukończyć {@value #TOTAL_GAME_PROJECTS} projektów.</li>
 *   <li>Przegrana: gotówka firmy spada poniżej 0 (bankructwo).</li>
 *   <li>Co turę wypłacane są wynagrodzenia + odsetki od kredytów.</li>
 *   <li>Po każdej turze pracy istnieje szansa na zdarzenie losowe (48% per turę).</li>
 * </ul>
 */
public class GameEngine {

    /** Firma zarządzana przez gracza – centralny obiekt stanu gry. */
    private final Company company;

    /** Interfejs użytkownika do wyświetlania menu i odczytu inputu. */
    private final ConsoleUI ui;

    /** Numer aktualnej tury rozgrywki (startuje od 1). */
    private int turn = 1;

    private final SaveManager saveManager = new SaveManager(new FileGameRepository());

    /** Flaga kontrolująca główną pętlę gry. False = koniec gry. */
    private boolean running = true;

    /** Unikalny identyfikator gry (8 znaków) – używany do zapisu stanu w bazie danych. */
    private final String gameId = java.util.UUID.randomUUID().toString().substring(0, 8);

    /** Łączna liczba projektów potrzebna do wygrania gry. */
    private static final int TOTAL_GAME_PROJECTS = 5;

    /**
     * Tworzy nowy silnik gry z podaną firmą i interfejsem UI.
     *
     * @param company firma gracza z zainicjowanym stanem
     * @param ui      obiekt interfejsu konsolowego
     */
    public GameEngine(Company company, ConsoleUI ui) {
        this.company = company;
        this.ui = ui;
    }

    // =========================================================================
    // Główna pętla gry
    // =========================================================================

    /**
     * Uruchamia główną pętlę gry.
     * <p>
     * Każda iteracja pętli to jedna tura: wyświetla status firmy,
     * odczytuje decyzję gracza i wykonuje odpowiednią akcję.
     * Pętla kończy się przy bankructwie, wygranej lub rezygnacji gracza.
     * </p>
     */
    public void run() {
        System.out.println("\n=== Welcome to TechCorp Management Simulator ===");
        System.out.println("Goal: Complete all " + TOTAL_GAME_PROJECTS + " projects to win the game!");
        System.out.println();
        System.out.println("--- HOW TO PLAY ---");
        System.out.println("* You can work on ONE project at a time.");
        System.out.println("* Each turn costs salaries — keep an eye on your cash!");
        System.out.println("* After completing a project, pick a new one from the market.");
        System.out.println("* There are 4 roles in the company: Developer, Tester, Manager, and Intern. Each contributes different skill power to projects.");
        System.out.println("* Hire employees to speed up projects, fire them to cut costs.");
        System.out.println("* Take bank loans if you run low on cash (3% interest per turn).");
        System.out.println("* Loan limit increases every 5 turns as your company grows.");
        System.out.println("* Random market events can help or hurt you each turn (48% chance).");
        System.out.println("* Cash drops below 0 = BANKRUPTCY. Game over.");
        System.out.println("-------------------");
        System.out.println();

        while (running && !isGameOver()) {
            // Nagłówek tury z aktualnym statusem firmy
            System.out.println("\n--- TURN " + turn + " ---");
            company.showStatus(getCurrentLoanLimit());
            System.out.println("Completed Projects: " + getCompletedProjectsCount() + " / " + TOTAL_GAME_PROJECTS);

            // Wyświetl menu i odczytaj wybór gracza
            ui.showMainMenu();
            int choice = ui.readChoice();

            boolean turnAdvanced = false; // Czy tura faktycznie minęła (wybór "Work")

            switch (choice) {
                case 1 -> turnAdvanced = handleWorkTurn();   // Praca nad projektem
                case 2 -> handleStartProject();              // Nowy kontrakt
                case 3 -> handleBankLoan();                  // System kredytowy
                case 4 -> handleHRDepartment();              // Dział HR
                case 0 -> running = false;                   // Wyjście z gry
                default -> System.out.println("Invalid choice. Please choose 0-4.");
            }

            // Inkrementacja tury tylko jeśli gracz wybrał "Work" i akcja się powiodła
            if (running && turnAdvanced) {
                turn++;
                ui.readText("\nPress Enter to continue...");
            }
        }

        // Wyświetl podsumowanie końcowe
        displayGameOutcome();
    }

    // =========================================================================
    // Obsługa akcji gracza
    // =========================================================================

    /**
     * Obsługuje turę pracy: wypłaca wynagrodzenia, postępuje projekt
     * i losowo wyzwala zdarzenie rynkowe.
     *
     * @return {@code true} jeśli tura przeszła pomyślnie, {@code false} przy bankructwie
     */
    private boolean handleWorkTurn() {
        // Gracz musi mieć aktywny projekt aby pracować
        if (!hasActiveProject()) {
            System.out.println("❌ No active project! Choose option 2 first.");
            return false;
        }

        System.out.println("\n>> ROUND REPORT:");

        // Krok 1: Wypłata wynagrodzeń i odsetek
        boolean salariesPaid = company.payEmployeesMonthly();
        if (!salariesPaid) {
            running = false;
            return false;
        }

        // Krok 2: Postęp wszystkich aktywnych projektów o jedną turę
        for (Project p : company.getProjects()) {
            if (!p.isFinished()) {
                p.workOneTurn();
                // Jeśli projekt właśnie ukończony – wypłata nagrody
                if (p.isFinished()) {
                    System.out.println("✓ Project '" + p.getName() + "' completed! Payout: +" + p.getReward());
                    company.addCash(p.getReward());
                }
            }
        }

        // Krok 3: Losowe zdarzenie rynkowe (tylko jeśli gra trwa)
        if (!isGameOver()) {
            triggerRandomEvent();
            // Sprawdzenie po zdarzeniu – mogło spowodować bankructwo
            if (company.getCash() < 0) {
                running = false;
            }
        }

        // Autosave po każdej turze – ćwiczenie 5 z lekcji 9
        GameState state = new GameState(gameId, company, turn, getCompletedProjectsCount());
        saveManager.autosave(state);
        
        return true;
    }

    /**
     * Obsługuje wybór nowego projektu przez gracza z listy dostępnych kontraktów.
     * Projekt może być uruchomiony tylko jeśli nie ma aktywnego projektu.
     */
    private void handleStartProject() {
        if (hasActiveProject()) {
            System.out.println("❌ A project is already in progress. Finish it first.");
            return;
        }
        selectNewProject();
    }

    /**
     * Obsługuje system kredytów bankowych.
     * Sprawdza dostępny limit, pobiera kwotę od gracza i zaciąga kredyt.
     */
    private void handleBankLoan() {
        System.out.println("\n>> BANK LOAN SYSTEM:");
        double currentLimit = getCurrentLoanLimit();

        // Weryfikacja limitu kredytowego dla bieżącej fazy gry
        if (company.getLoanAmount() >= currentLimit) {
            System.out.println("❌ Credit denied! Your current stage debt limit (" + currentLimit + ") has been reached.");
            System.out.println("Wait for future rounds to unlock a higher credit line!");
            return;
        }

        // Obliczenie pozostałej zdolności kredytowej i pobranie kwoty od gracza
        double remainingCredit = currentLimit - company.getLoanAmount();
        double amount = ui.readDoubleInRange(
                "Enter loan amount (Max single request " + remainingCredit + "): ",
                1000, remainingCredit);

        company.takeLoan(amount);
        System.out.println("Loan approved! Added +" + amount + " to cash. Total debt accumulates 3% interest fee.");
    }

    /**
     * Obsługuje menu działu HR – rekrutacja lub zwolnienie pracownika.
     */
    private void handleHRDepartment() {
        ui.showHRMenu();
        int subChoice = ui.readChoiceInRange("HR Choice: ", 0, 2);

        if (subChoice == 1) {
            generateAndProposeCandidate(); // Rekrutacja nowego kandydata
        } else if (subChoice == 2) {
            processEmployeeTermination();  // Zwolnienie pracownika
        }
        // subChoice == 0: powrót bez akcji
    }

    // =========================================================================
    // Logika rekrutacji i zwolnień (HR)
    // =========================================================================

    /**
     * Generuje losowego kandydata (przez {@link EmployeeFactory}) i proponuje
     * graczowi decyzję o zatrudnieniu lub odrzuceniu aplikacji.
     * Zatrudniony pracownik automatycznie dołącza do aktywnych projektów.
     */
    private void generateAndProposeCandidate() {
        // EmployeeFactory generuje kandydata (wzorzec Factory Method)
        Employee candidate = EmployeeFactory.generateRandomCandidate();

        // Wyświetlenie profilu kandydata
        System.out.println("\n--- NEW CANDIDATE PROFILE ---");
        System.out.println("Name: "          + candidate.getName());
        System.out.println("Role: "          + candidate.getRoleName());
        System.out.println("Skill Power: "   + candidate.getSkill());
        System.out.println("Salary Demand: " + candidate.getSalary() + " / turn");
        System.out.println("-----------------------------");
        System.out.println("1. Hire this applicant");
        System.out.println("2. Reject applicant");

        int action = ui.readChoiceInRange("Action (1-2): ", 1, 2);
        if (action == 1) {
            // Zatrudnienie: dodaj do firmy i do wszystkich aktywnych projektów
            company.hire(candidate);
            for (Project p : company.getProjects()) {
                if (!p.isFinished()) {
                    p.addEmployee(candidate);
                }
            }
            System.out.println("✓ Success! " + candidate.getName() + " has been added to your production team.");
        } else {
            System.out.println("Application archived.");
        }
    }

    /**
     * Wyświetla listę pracowników i pozwala graczowi wybrać, kogo zwolnić.
     * Zwolniony pracownik jest usuwany z firmy i ze wszystkich projektów.
     */
    private void processEmployeeTermination() {
        List<Employee> list = company.getEmployees();

        // Brak pracowników – nie ma kogo zwalniać
        if (list.isEmpty()) {
            System.out.println("❌ No employees available to lay off.");
            return;
        }

        // Wyświetlenie listy pracowników z numerami
        System.out.println("\nCurrent Staff Directory:");
        for (int i = 0; i < list.size(); i++) {
            Employee e = list.get(i);
            System.out.println((i + 1) + ". " + e.getName()
                    + " [" + e.getRoleName() + "] | Skill: " + e.getSkill() + " | Salary: " + e.getSalary());
        }
        System.out.println("0. Cancel termination");

        int targetIdx = ui.readChoiceInRange("Select staff number to fire (0-" + list.size() + "): ", 0, list.size());

        // 0 = anulowanie akcji zwolnienia
        if (targetIdx == 0) {
            System.out.println("HR action canceled.");
            return;
        }

        // Zwolnienie wybranego pracownika (indeks listy zaczyna się od 0, menu od 1)
        Employee firedTarget = list.get(targetIdx - 1);
        company.fire(firedTarget);
        System.out.println("⚠️ Contract terminated. " + firedTarget.getName()
                + " has been successfully removed from TechCorp.");
    }

    // =========================================================================
    // Logika wyboru projektu
    // =========================================================================

    /**
     * Wyświetla dostępne kontrakty projektowe i pozwala graczowi wybrać następny.
     * Każdy projekt można uruchomić tylko raz (sprawdzane po nazwie).
     * Po wyborze wszyscy zatrudnieni pracownicy są automatycznie przypisywani.
     */
    private void selectNewProject() {
        System.out.println("\nAvailable projects:");

        // Sprawdzenie, które kontrakty nie zostały jeszcze podjęte
        boolean p2Available = isProjectAvailable("E-Commerce Platform");
        boolean p3Available = isProjectAvailable("Cloud Migration");
        boolean p4Available = isProjectAvailable("AI Chatbot Integration");
        boolean p5Available = isProjectAvailable("Cybersecurity Audit");

        // Brak dostępnych kontraktów na rynku
        if (!p2Available && !p3Available && !p4Available && !p5Available) {
            System.out.println("❌ No new contracts left on the market!");
            return;
        }

        // Dynamiczne numerowanie menu (pomijamy już podjęte projekty)
        int validOptionsCount = 0;
        int p2MenuNum = -1, p3MenuNum = -1, p4MenuNum = -1, p5MenuNum = -1;

        if (p2Available) { p2MenuNum = ++validOptionsCount; System.out.println(p2MenuNum + ". E-Commerce Platform [Work: 50 | Reward: 35000]"); }
        if (p3Available) { p3MenuNum = ++validOptionsCount; System.out.println(p3MenuNum + ". Cloud Migration [Work: 75 | Reward: 55000]"); }
        if (p4Available) { p4MenuNum = ++validOptionsCount; System.out.println(p4MenuNum + ". AI Chatbot Integration [Work: 100 | Reward: 80000]"); }
        if (p5Available) { p5MenuNum = ++validOptionsCount; System.out.println(p5MenuNum + ". Cybersecurity Audit [Work: 140 | Reward: 110000]"); }

        int choice = ui.readChoiceInRange("Select (1-" + validOptionsCount + "): ", 1, validOptionsCount);

        // Tworzenie wybranego projektu z danymi kontraktu
        Project newProject = null;
        if (choice == p2MenuNum) newProject = new Project("E-Commerce Platform",  50,  35000);
        else if (choice == p3MenuNum) newProject = new Project("Cloud Migration",       75,  55000);
        else if (choice == p4MenuNum) newProject = new Project("AI Chatbot Integration", 100, 80000);
        else if (choice == p5MenuNum) newProject = new Project("Cybersecurity Audit",   140, 110000);

        if (newProject != null) {
            // Przypisanie wszystkich aktualnych pracowników do nowego projektu
            for (Employee e : company.getEmployees()) {
                newProject.addEmployee(e);
            }
            company.startProject(newProject);
            System.out.println("Started project: " + newProject.getName());
        }
    }

    /**
     * Sprawdza, czy projekt o danej nazwie nie był jeszcze uruchomiony w tej grze.
     *
     * @param name nazwa projektu do sprawdzenia
     * @return {@code true} jeśli projekt jest dostępny (nie był jeszcze uruchomiony)
     */
    private boolean isProjectAvailable(String name) {
        for (Project p : company.getProjects()) {
            if (p.getName().equalsIgnoreCase(name)) return false;
        }
        return true;
    }

    // =========================================================================
    // System losowych zdarzeń
    // =========================================================================

    /**
     * Losuje zdarzenie rynkowe z prawdopodobieństwem 48% per turę.
     * <p>
     * Każde zdarzenie ma równe 8% szansy zajścia. Pozostałe 52% to cisza
     * (brak zdarzenia w danej turze).
     * </p>
     */
    private void triggerRandomEvent() {
        double chance = Math.random();

        // 6 zdarzeń × 8% = 48% łączna szansa zdarzenia per turę
        if      (chance < 0.08) new EmployeeStrikeEvent(0.15).apply(company);
        else if (chance < 0.16) new MarketSlowdownEvent(4000).apply(company);
        else if (chance < 0.24) new GovernmentGrantEvent(5000).apply(company);
        else if (chance < 0.32) new DataBreachEvent(6000).apply(company);
        else if (chance < 0.40) new OfficeFireEvent(8000).apply(company);
        else if (chance < 0.48) new TechBoomEvent(7000).apply(company);
        // >= 0.48: cisza – brak zdarzenia w tej turze
    }

    // =========================================================================
    // Metody pomocnicze – stan gry
    // =========================================================================

    /**
     * Sprawdza, czy firma ma co najmniej jeden aktywny (nieukończony) projekt.
     *
     * @return {@code true} jeśli istnieje projekt ze statusem ACTIVE
     */
    private boolean hasActiveProject() {
        for (Project p : company.getProjects()) {
            if (!p.isFinished()) return true;
        }
        return false;
    }

    /**
     * Zlicza ukończone projekty (z statusem COMPLETED).
     *
     * @return liczba ukończonych projektów
     */
    private int getCompletedProjectsCount() {
        int count = 0;
        for (Project p : company.getProjects()) {
            if (p.isFinished()) count++;
        }
        return count;
    }

    /**
     * Wyznacza aktualny limit kredytowy banku w zależności od numeru tury.
     * Limit rośnie co kilka tur, odzwierciedlając wzrost wiarygodności firmy.
     *
     * @return aktualny maksymalny limit zadłużenia
     */
    private double getCurrentLoanLimit() {
        if      (turn < 5)  return  10_000.0;
        else if (turn < 10) return  30_000.0;
        else if (turn < 15) return  50_000.0;
        else if (turn < 20) return  70_000.0;
        else if (turn < 25) return  90_000.0;
        else if (turn < 30) return 120_000.0;
        else if (turn < 35) return 150_000.0;
        else if (turn < 40) return 180_000.0;
        else                return 210_000.0;
    }

    /**
     * Sprawdza warunek końca gry: bankructwo lub osiągnięcie celu.
     *
     * @return {@code true} jeśli gra powinna się zakończyć
     */
    private boolean isGameOver() {
        return company.getCash() < 0 || getCompletedProjectsCount() >= TOTAL_GAME_PROJECTS;
    }

    /**
     * Wyświetla końcowe podsumowanie rozgrywki z odpowiednim komunikatem
     * zależnym od wyniku: wygrana, przegrana lub rezygnacja.
     */
    private void displayGameOutcome() {
        System.out.println("\n--- GAME OVER ---");
        if (company.getCash() < 0) {
            System.out.println("💸 Liquidation. Bankrupted and lost. Final Account Balance: " + company.getCash());
        } else if (getCompletedProjectsCount() >= TOTAL_GAME_PROJECTS) {
            System.out.println("🏆 VICTORY! Your company successfully delivered all "
                    + TOTAL_GAME_PROJECTS + " projects and dominated the market!");
        } else {
            System.out.println("You voluntarily quit the simulator. Final Progress: "
                    + getCompletedProjectsCount() + "/" + TOTAL_GAME_PROJECTS + " projects.");
        }
    }
}
