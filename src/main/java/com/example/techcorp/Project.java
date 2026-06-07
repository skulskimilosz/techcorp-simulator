package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje kontrakt projektowy realizowany przez firmę TechCorp.
 * <p>
 * Projekt posiada wymaganą ilość pracy do wykonania ({@code requiredWork})
 * oraz nagrodę pieniężną ({@code reward}) wypłacaną po ukończeniu.
 * Co turę metoda {@link #workOneTurn()} sumuje wkład wszystkich przypisanych
 * pracowników i aktualizuje postęp projektu.
 * </p>
 *
 * <p>Cykl życia projektu:</p>
 * <ol>
 *   <li>Projekt tworzony ze statusem {@link ProjectStatus#ACTIVE}.</li>
 *   <li>Pracownicy dodawani przez {@link #addEmployee(Employee)}.</li>
 *   <li>Co turę wywoływana jest {@link #workOneTurn()} — postęp rośnie.</li>
 *   <li>Gdy {@code progress >= requiredWork}, status zmienia się na {@link ProjectStatus#COMPLETED}.</li>
 * </ol>
 */
public class Project {

    /** Nazwa projektu (kontrakt), np. "Mobile App", "AI Chatbot Integration". */
    private final String name;

    /** Łączna liczba punktów pracy potrzebna do ukończenia projektu. */
    private final int requiredWork;

    /** Nagroda finansowa (w PLN) wypłacana firmie po ukończeniu projektu. */
    private final double reward;

    /** Aktualny skumulowany postęp prac nad projektem. */
    private int progress;

    /** Lista pracowników przypisanych do tego projektu. */
    private final List<Employee> team;

    /** Aktualny status projektu: ACTIVE lub COMPLETED. */
    private ProjectStatus status;

    /**
     * Tworzy nowy projekt z podaną nazwą, wymaganą ilością pracy i nagrodą.
     *
     * @param name         nazwa projektu (nie może być pusta)
     * @param requiredWork wymagana praca do ukończenia (musi być > 0)
     * @param reward       nagroda finansowa po ukończeniu (musi być >= 0)
     * @throws IllegalArgumentException jeśli któryś z parametrów jest niepoprawny
     */
    public Project(String name, int requiredWork, double reward) {
        validateName(name);
        validateRequiredWork(requiredWork);
        if (reward < 0) throw new IllegalArgumentException("Reward cannot be negative.");

        this.name = name;
        this.requiredWork = requiredWork;
        this.reward = reward;
        this.progress = 0;
        this.team = new ArrayList<>();
        this.status = ProjectStatus.ACTIVE;
    }

    /**
     * Dodaje pracownika do zespołu projektowego.
     * Pracownik będzie brał udział w pracach od następnej tury.
     *
     * @param employee pracownik do dodania (nie może być null)
     * @throws IllegalArgumentException jeśli pracownik jest null
     */
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        team.add(employee);
    }

    /**
     * Usuwa pracownika z zespołu projektowego (np. po zwolnieniu z firmy).
     * Zapobiega tzw. "ghost work" — pracy nieistniejącego już pracownika.
     *
     * @param employee pracownik do usunięcia
     */
    public void removeEmployee(Employee employee) {
        team.remove(employee);
    }

    /**
     * Symuluje jedną turę pracy nad projektem.
     * <p>
     * Każdy pracownik z zespołu wywołuje {@link Employee#work()} i dodaje
     * swój wkład do postępu. Jeśli postęp przekroczy {@code requiredWork},
     * zostaje ograniczony do tej wartości, a projekt oznaczany jako ukończony.
     * </p>
     * Metoda jest idempotentna — nie działa, jeśli projekt jest już COMPLETED.
     */
    public void workOneTurn() {
        // Projekt ukończony – nic nie robimy
        if (status == ProjectStatus.COMPLETED) {
            return;
        }

        // Każdy pracownik wnosi swój wkład do postępu projektu
        for (Employee employee : team) {
            progress += employee.work();
        }

        // Ograniczenie postępu do dokładnej wartości requiredWork (bez przekroczenia)
        if (progress > requiredWork) {
            progress = requiredWork;
        }

        // Sprawdzenie warunку ukończenia projektu
        if (progress >= requiredWork) {
            status = ProjectStatus.COMPLETED;
        }
    }

    /**
     * @return {@code true}, jeśli projekt ma status {@link ProjectStatus#COMPLETED}
     */
    public boolean isFinished() {
        return status == ProjectStatus.COMPLETED;
    }

    /** @return nazwa projektu */
    public String getName()        { return name; }

    /** @return wymagana łączna praca do ukończenia projektu */
    public int getRequiredWork()   { return requiredWork; }

    /** @return aktualny postęp prac (0 do requiredWork) */
    public int getProgress()       { return progress; }

    /** @return nagroda finansowa za ukończenie projektu */
    public double getReward()      { return reward; }

    /** @return lista pracowników przypisanych do projektu */
    public List<Employee> getTeam() { return team; }

    /** @return aktualny status projektu (ACTIVE / COMPLETED) */
    public ProjectStatus getStatus() { return status; }

    // -------------------------------------------------------------------------
    // Walidacja parametrów konstruktora
    // -------------------------------------------------------------------------

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank.");
        }
    }

    private void validateRequiredWork(int requiredWork) {
        if (requiredWork <= 0) {
            throw new IllegalArgumentException("Required work must be greater than 0.");
        }
    }
}
