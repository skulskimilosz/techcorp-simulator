package com.example.techcorp;

import com.example.techcorp.engine.GameEngine;
import com.example.techcorp.ui.ConsoleUI;

/**
 * Punkt wejścia do symulatora zarządzania firmą TechCorp.
 * <p>
 * Klasa {@code Main} inicjalizuje wszystkie obiekty potrzebne do rozgrywki:
 * <ol>
 *   <li>Tworzy firmę {@link Company} z kapitałem startowym.</li>
 *   <li>Zatrudnia wstępny zespół pracowników różnych ról.</li>
 *   <li>Uruchamia pierwszy projekt ({@link Project}) i przypisuje do niego zespół.</li>
 *   <li>Tworzy {@link ConsoleUI} (interfejs gracza) i {@link GameEngine} (silnik gry).</li>
 *   <li>Startuje pętlę gry przez {@code engine.run()}.</li>
 * </ol>
 * </p>
 *
 * <p>Jak uruchomić projekt:</p>
 * <pre>
 *   mvn package &amp;&amp; java -cp target/classes com.example.techcorp.Main
 * </pre>
 */
public class Main {

    /**
     * Metoda main – jedyna metoda wejściowa do całej aplikacji.
     *
     * @param args argumenty wiersza poleceń (nieużywane)
     */
    public static void main(String[] args) {

        // --- Inicjalizacja firmy z kapitałem startowym ---
        Company company = new Company("TechCorp", 50_000);

        // --- Zatrudnienie wstępnego zespołu (4 osoby, różne role i umiejętności) ---
        Employee anna  = new Developer("Anna",  9, 4500); // Najsilniejszy developer
        Employee piotr = new Tester("Piotr",    6, 3500); // Tester QA
        Employee ewa   = new Manager("Ewa",     7, 5000); // Menedżer (drogi, mały wkład)
        Employee tomek = new Intern("Tomek",    4, 1500); // Stażysta (tani, mały wkład)

        company.hire(anna);
        company.hire(piotr);
        company.hire(ewa);
        company.hire(tomek);

        // --- Uruchomienie pierwszego projektu i przypisanie zespołu ---
        Project mobileApp = new Project("Mobile App", 40, 30000);
        mobileApp.addEmployee(anna);
        mobileApp.addEmployee(piotr);
        mobileApp.addEmployee(ewa);
        mobileApp.addEmployee(tomek);

        company.startProject(mobileApp);

        // --- Inicjalizacja interfejsu użytkownika i silnika gry ---
        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(company, ui);

        // --- Start głównej pętli rozgrywki ---
        engine.run();

        System.out.println("\nGame ended. Thanks for playing TechCorp Simulator!");
    }
}
