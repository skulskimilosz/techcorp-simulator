package com.example.techcorp.engine;

import com.example.techcorp.Company;
import com.example.techcorp.Project;
import com.example.techcorp.ui.ConsoleUI;

public class GameEngine {
    private Company company;
    private ConsoleUI ui;
    private int turn = 1;
    private boolean running = true;
    private static final double BANKRUPTCY_THRESHOLD = 5000;
    private static final double WIN_CASH_AMOUNT = 100000;

    public GameEngine(Company company, ConsoleUI ui) {
        this.company = company;
        this.ui = ui;
    }

    public void run() {
        ui.showWelcome();
        
        while (running && !isGameOver()) {
            ui.showTurn(turn);
            ui.showCompanyStatus(company);
            ui.showMenu();
            int choice = ui.readChoice();
            
            switch (choice) {
                case 1 -> workOneTurn();
                case 2 -> paySalaries();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
            
            turn++;
            
            if (isGameOver()) {
                displayGameOutcome();
            }
        }
    }

    private void workOneTurn() {
        System.out.println("\n--- Working one turn ---");
        company.payEmployeesMonthly();
        
        for (Project p : company.getProjects()) {
            p.workOneTurn();
            if (p.isFinished()) {
                System.out.println("✓ Project '" + p.getName() + "' completed!");
                company.addCash(10000); // Bonus for completing project
            }
        }
    }

    private void paySalaries() {
        System.out.println("\n--- Manual Salary Payment ---");
        company.payEmployeesMonthly();
    }

    private boolean isGameOver() {
        // Lose condition: bankruptcy
        if (company.getCash() <= 0) {
            return true;
        }
        
        // Win condition: reach target cash
        if (company.getCash() >= WIN_CASH_AMOUNT) {
            return true;
        }
        
        return false;
    }

    private void displayGameOutcome() {
        System.out.println("\n========== GAME OVER ==========");
        
        if (company.getCash() <= 0) {
            System.out.println("LOST: Company bankrupt!");
            System.out.println("Your company ran out of money.");
        } else if (company.getCash() >= WIN_CASH_AMOUNT) {
            System.out.println("WON: Company thriving!");
            System.out.println("Congratulations! Your company reached " + WIN_CASH_AMOUNT + " cash!");
        }
        
        System.out.println("Final Status:");
        company.showStatus();
        System.out.println("================================");
        
        running = false;
    }
}
