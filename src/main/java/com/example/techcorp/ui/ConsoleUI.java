package com.example.techcorp.ui;

import java.util.Scanner;

/**
 * Warstwa interfejsu użytkownika – obsługuje całą komunikację z konsolą.
 * <p>
 * Klasa {@code ConsoleUI} odpowiada za dwa zadania:
 * <ol>
 *   <li><strong>Wyświetlanie</strong> – menu głównego, podmenu HR, statusów.</li>
 *   <li><strong>Odczyt danych</strong> – bezpieczne parsowanie inputu gracza
 *       z pętlami ponownego pytania przy błędnym wejściu.</li>
 * </ol>
 * Taki podział (UI oddzielnie od logiki) stosuje zasadę SRP
 * (Single Responsibility Principle) z programowania obiektowego.
 * </p>
 */
public class ConsoleUI {

    /** Skaner do odczytu wejścia z konsoli – jeden na cały czas gry. */
    private final Scanner scanner = new Scanner(System.in);

    // =========================================================================
    // Metody wyświetlania menu
    // =========================================================================

    /**
     * Wyświetla menu główne gry z dostępnymi akcjami dla gracza.
     */
    public void showMainMenu() {
        System.out.println("\n1. Work one turn");
        System.out.println("2. Start a new project");
        System.out.println("3. Request a custom bank loan (3% interest)");
        System.out.println("4. HR Department (Hire / Fire Staff)");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    /**
     * Wyświetla podmenu działu HR z opcjami rekrutacji i zwolnień.
     */
    public void showHRMenu() {
        System.out.println("\n>> TECHCORP HR DEPARTMENT:");
        System.out.println("1. Recruit new employee candidate");
        System.out.println("2. Terminate employee contract (Fire)");
        System.out.println("0. Back to main dashboard");
    }

    // =========================================================================
    // Metody odczytu danych wejściowych
    // =========================================================================

    /**
     * Odczytuje liczbę całkowitą z konsoli.
     * W przypadku błędnego formatu wypisuje komunikat i ponawia odczyt.
     *
     * @return wpisana przez użytkownika liczba całkowita
     */
    public int readChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.print("Invalid number. Try again: ");
            return readChoice(); // Rekurencja – ponów odczyt
        }
    }

    /**
     * Wyświetla podany prompt i odczytuje dowolny tekst (np. wciśnięcie Enter).
     *
     * @param prompt tekst zachęty wyświetlany przed oczekiwaniem na input
     * @return wpisany przez użytkownika ciąg znaków
     */
    public String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Wyświetla prompt i odczytuje liczbę całkowitą w podanym zakresie [min, max].
     * Powtarza pytanie, jeśli użytkownik poda wartość spoza zakresu lub tekst.
     *
     * @param prompt tekst zachęty wyświetlany przed odczytem
     * @param min    minimalna dopuszczalna wartość (włącznie)
     * @param max    maksymalna dopuszczalna wartość (włącznie)
     * @return liczba całkowita w zakresie [min, max]
     */
    public int readChoiceInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) return choice;
                System.out.println("Out of range (" + min + "-" + max + "). Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    /**
     * Wyświetla prompt i odczytuje liczbę zmiennoprzecinkową w zakresie [min, max].
     * Stosowane przy wprowadzaniu kwot pożyczek – zapewnia poprawność finansową.
     *
     * @param prompt tekst zachęty wyświetlany przed odczytem
     * @param min    minimalna dopuszczalna wartość (włącznie)
     * @param max    maksymalna dopuszczalna wartość (włącznie)
     * @return liczba zmiennoprzecinkowa w zakresie [min, max]
     */
    public double readDoubleInRange(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= min && value <= max) return value;
                System.out.println("Please enter an amount between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric amount. Try again.");
            }
        }
    }
}
