package com.example.techcorp.ui;

import com.example.techcorp.Company;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println("╔═════════════════════════════╗");
        System.out.println("║   TECHCORP DECISION GAME    ║");
        System.out.println("╚═════════════════════════════╝");
        System.out.println("\nWelcome to TechCorp Company Management Simulator!");
        System.out.println("Manage your company wisely to avoid bankruptcy and reach prosperity.\n");
    }

    public void showTurn(int turn) {
        System.out.println("\n╔════════════════ TURN " + String.format("%2d", turn) + " ════════════════╗");
    }

    public void showCompanyStatus(Company company) {
        company.showStatus();
    }

    public void showMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Work one turn (pay salaries + advance projects)");
        System.out.println("2. Manual salary payment");
        System.out.println("0. Exit game");
        System.out.print("Choose action: ");
    }

    public int readChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return readChoice();
        }
    }
}
