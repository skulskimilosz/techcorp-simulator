package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje firmę technologiczną zarządzaną przez gracza.
 * <p>
 * Klasa {@code Company} przechowuje stan finansowy firmy (gotówka, kredyty),
 * listę pracowników oraz listę aktywnych i ukończonych projektów.
 * Udostępnia metody do zarządzania zasobami ludzkimi (hire/fire),
 * finansami (takeLoan, payEmployeesMonthly) oraz projektami (startProject).
 * </p>
 *
 * <p>Główne zasady finansowe:</p>
 * <ul>
 *   <li>Gotówka nie może spaść poniżej 0 bez konsekwencji (bankructwo).</li>
 *   <li>Kredyt bankowy generuje 3% miesięcznego oprocentowania.</li>
 *   <li>Wynagrodzenia + odsetki są płacone raz na turę.</li>
 * </ul>
 */
public class Company {

    /** Nazwa firmy (niezmienna po założeniu). */
    private final String name;

    /** Aktualny stan gotówki firmy. Bankructwo następuje gdy cash < 0. */
    private double cash;

    /** Łączna kwota zaciągniętych kredytów bankowych. */
    private double loanAmount;

    /** Stałe oprocentowanie kredytu miesięcznie (3%). */
    private final double interestRate = 0.03;

    /** Lista wszystkich zatrudnionych pracowników firmy. */
    private final List<Employee> employees;

    /** Lista wszystkich projektów (aktywnych i ukończonych). */
    private final List<Project> projects;

    /**
     * Tworzy nową firmę z podaną nazwą i kapitałem startowym.
     *
     * @param name nazwa firmy (nie może być pusta ani null)
     * @param cash kapitał startowy (musi być >= 0)
     * @throws IllegalArgumentException jeśli parametry są niepoprawne
     */
    public Company(String name, double cash) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Company name cannot be blank.");
        if (cash < 0) throw new IllegalArgumentException("Starting cash cannot be negative.");
        this.name = name;
        this.cash = cash;
        this.loanAmount = 0;
        this.employees = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    /**
     * Zatrudnia nowego pracownika i dodaje go do listy płac.
     *
     * @param employee pracownik do zatrudnienia (nie może być null)
     * @throws IllegalArgumentException jeśli pracownik jest null
     */
    public void hire(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("Cannot hire null employee.");
        employees.add(employee);
    }

    /**
     * Zwalnia pracownika z firmy: usuwa go z listy płac
     * oraz ze wszystkich aktywnych projektów (zapobiega "ghost work").
     *
     * @param employee pracownik do zwolnienia (nie może być null)
     * @throws IllegalArgumentException jeśli pracownik jest null
     */
    public void fire(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("Cannot fire null employee.");
        employees.remove(employee);

        // Usuwamy zwolnionego z każdego projektu, aby nie pracował po odejściu
        for (Project p : projects) {
            p.removeEmployee(employee);
        }
    }

    /**
     * Rejestruje nowy projekt w firmie (dodaje do listy projektów).
     *
     * @param project projekt do uruchomienia (nie może być null)
     * @throws IllegalArgumentException jeśli projekt jest null
     */
    public void startProject(Project project) {
        if (project == null) throw new IllegalArgumentException("Cannot start null project.");
        projects.add(project);
    }

    /**
     * Oblicza łączny miesięczny koszt wynagrodzeń wszystkich pracowników.
     *
     * @return suma wynagrodzeń wszystkich pracowników
     */
    public double getTotalPayroll() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getSalary();
        }
        return total;
    }

    /**
     * Oblicza miesięczny koszt odsetek od zaciągniętych kredytów (3% × loanAmount).
     *
     * @return kwota odsetek za bieżącą turę
     */
    public double getMonthlyInterestCost() {
        return this.loanAmount * this.interestRate;
    }

    /**
     * Zaciąga kredyt bankowy: zwiększa gotówkę i łączne zadłużenie.
     * Odsetki od nowej kwoty będą naliczane od następnej tury.
     *
     * @param amount kwota kredytu do zaciągnięcia (musi być > 0)
     */
    public void takeLoan(double amount) {
        this.loanAmount += amount;
        this.cash += amount;
    }

    /**
     * Wypłaca miesięczne wynagrodzenia oraz odsetki od kredytów.
     * <p>
     * Wydatki (wynagrodzenia + odsetki) odejmowane są od gotówki.
     * Jeśli po wypłacie gotówka spadnie poniżej 0, firma bankrutuje
     * i metoda zwraca {@code false}.
     * </p>
     *
     * @return {@code true} jeśli firma ma wystarczające środki, {@code false} przy bankructwie
     */
    public boolean payEmployeesMonthly() {
        double totalSalaries = getTotalPayroll();
        double interestCost = getMonthlyInterestCost();
        double totalExpenses = totalSalaries + interestCost;

        // Informujemy o kosztach odsetek tylko jeśli firma ma kredyt
        if (interestCost > 0) {
            System.out.println("Paid monthly loan interest (3%): -" + interestCost);
        }

        // Sprawdzamy wypłacalność PRZED odjęciem kosztów, aby pokazać ujemne saldo w przypadku bankructwa
        if (this.cash < totalExpenses) {
            System.out.println("❌ FINANCIAL COLLAPSE: Not enough cash to cover salaries and interest!");
            this.cash -= totalExpenses; // Odejmujemy aby pokazać ujemne saldo
            return false;
        }

        // Firma jest wypłacalna – odejmujemy koszty i potwierdzamy
        this.cash -= totalExpenses;
        System.out.println("Paid employee salaries: -" + totalSalaries);
        return true;
    }

    /**
     * Wyświetla aktualny status firmy: gotówkę, kredyty, pracowników i projekty.
     *
     * @param currentLoanLimit aktualny limit kredytowy dla bieżącej tury (z GameEngine)
     */
    public void showStatus(double currentLoanLimit) {
        System.out.println("Company: " + name + " | Cash: " + cash);
        System.out.println("Credit Line: " + loanAmount + " / " + currentLoanLimit
                + " (Interest: " + getMonthlyInterestCost() + "/turn)");
        System.out.println("Employees: " + employees.size() + " | Monthly Payroll: " + getTotalPayroll());
        System.out.println("Projects:");
        for (Project p : projects) {
            String status = p.isFinished() ? "FINISHED" : "ACTIVE";
            System.out.println("  - " + p.getName()
                    + " [" + p.getProgress() + "/" + p.getRequiredWork() + "] (" + status + ")");
        }
    }

    // -------------------------------------------------------------------------
    // Gettery i metody pomocnicze do zarządzania gotówką
    // -------------------------------------------------------------------------

    /** @return aktualny stan gotówki firmy */
    public double getCash()               { return cash; }

    /** @return łączna kwota zaciągniętych kredytów */
    public double getLoanAmount()         { return loanAmount; }

    /** @return lista zatrudnionych pracowników (modyfikowalna referencja) */
    public List<Employee> getEmployees()  { return employees; }

    /** @return lista wszystkich projektów firmy */
    public List<Project> getProjects()    { return projects; }

    /**
     * Zwiększa gotówkę firmy o podaną kwotę (np. nagroda za projekt, grant rządowy).
     * @param amount kwota do dodania
     */
    public void addCash(double amount)    { this.cash += amount; }

    /**
     * Zmniejsza gotówkę firmy o podaną kwotę (np. skutek zdarzenia losowego, kara).
     * @param amount kwota do odjęcia
     */
    public void reduceCash(double amount) { this.cash -= amount; }
}
