package com.example.techcorp.api;

import com.example.techcorp.Company;
import com.example.techcorp.Developer;
import com.example.techcorp.Employee;
import com.example.techcorp.Intern;
import com.example.techcorp.Manager;
import com.example.techcorp.Project;
import com.example.techcorp.Tester;
import com.example.techcorp.repository.GameRepository;
import com.example.techcorp.repository.PostgresGameRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler REST API dla symulatora TechCorp.
 * <p>
 * Udostępnia endpointy HTTP do zarządzania stanem gry.
 * Stan firmy jest przechowywany w pamięci serwera (jeden obiekt Company).
 * </p>
 *
 * Dostępne endpointy:
 *   GET  /game/state  – aktualny status firmy (gotówka, pracownicy, projekty)
 *   POST /game/work   – symuluje jedną turę pracy
 *   POST /game/reset  – resetuje grę do stanu początkowego
 */
@RestController
@RequestMapping("/game")
public class GameController {
    
    private final GameRepository repository = new PostgresGameRepository();
    // Stan gry przechowywany w pamięci serwera
    private Company company = createInitialCompany();
    private int turn = 1;
    
    /**
     * GET /game/state
     * Zwraca aktualny stan firmy jako tekst: turę, gotówkę, liczbę pracowników i projekty.
     */
    @GetMapping("/state")
    public String state() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TechCorp Game State ===\n");
        sb.append("Turn: ").append(turn).append("\n");
        sb.append("Cash: ").append(company.getCash()).append("\n");
        sb.append("Employees: ").append(company.getEmployees().size()).append("\n");
        sb.append("Payroll/turn: ").append(company.getTotalPayroll()).append("\n");
        sb.append("Loan: ").append(company.getLoanAmount()).append("\n");
        sb.append("\nProjects:\n");
        for (Project p : company.getProjects()) {
            String status = p.isFinished() ? "COMPLETED" : "ACTIVE";
            sb.append("  - ").append(p.getName())
              .append(" [").append(p.getProgress()).append("/").append(p.getRequiredWork()).append("] ")
              .append("(").append(status).append(")\n");
        }

        // Sprawdzenie stanu gry
        long completed = company.getProjects().stream().filter(Project::isFinished).count();
        if (company.getCash() < 0) {
            sb.append("\nStatus: BANKRUPT");
        } else if (completed >= 5) {
            sb.append("\nStatus: VICTORY");
        } else {
            sb.append("\nStatus: IN_PROGRESS");
        }

        return sb.toString();
    }

    /**
     * POST /game/work
     * Symuluje jedną turę pracy: wypłaca wynagrodzenia i postępuje projekty.
     */
    @PostMapping("/work")
    public String workOneTurn() {
        if (company.getCash() < 0) {
            return "Game over – company is bankrupt.";
        }

        boolean paid = company.payEmployeesMonthly();
        if (!paid) {
            return "Turn " + turn + ": BANKRUPT – could not pay salaries. Final cash: " + company.getCash();
        }

        StringBuilder result = new StringBuilder("Turn " + turn + " completed.\n");
        for (Project p : company.getProjects()) {
            if (!p.isFinished()) {
                p.workOneTurn();
                if (p.isFinished()) {
                    company.addCash(p.getReward());
                    result.append("Project '").append(p.getName()).append("' completed! Reward: +").append(p.getReward()).append("\n");
                }
            }
        }

        // Autosave to PostgreSQL database after each turn
     long completedCount = company.getProjects().stream().filter(Project::isFinished).count();
     com.example.techcorp.domain.GameState state = new com.example.techcorp.domain.GameState(company, turn, (int) completedCount);
     repository.save(state);

     turn++;
     result.append("Cash after turn: ").append(company.getCash());
     return result.toString();
    }

    /**
     * POST /game/reset
     * Resetuje grę do stanu początkowego (nowa firma, nowy zespół, nowy projekt).
     */
    @PostMapping("/reset")
    public String reset() {
        company = createInitialCompany();
        turn = 1;
        return "Game reset. New game started with 50000 cash and 4 employees.";
    }

    /**
     * Tworzy firmę w stanie początkowym – identycznie jak w oryginalnym Main.java.
     */
    private Company createInitialCompany() {
        Company c = new Company("TechCorp", 50_000);

        Employee anna  = new Developer("Anna",  9, 4500);
        Employee piotr = new Tester("Piotr",    6, 3500);
        Employee ewa   = new Manager("Ewa",     7, 5000);
        Employee tomek = new Intern("Tomek",    4, 1500);

        c.hire(anna);
        c.hire(piotr);
        c.hire(ewa);
        c.hire(tomek);

        Project mobileApp = new Project("Mobile App", 40, 30000);
        mobileApp.addEmployee(anna);
        mobileApp.addEmployee(piotr);
        mobileApp.addEmployee(ewa);
        mobileApp.addEmployee(tomek);
        c.startProject(mobileApp);

        return c;
    }
}
