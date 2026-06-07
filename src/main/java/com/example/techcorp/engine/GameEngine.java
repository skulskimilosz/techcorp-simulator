package com.example.techcorp.engine;

import com.example.techcorp.Company;
import com.example.techcorp.Employee;
import com.example.techcorp.Developer;
import com.example.techcorp.Tester;
import com.example.techcorp.Manager;
import com.example.techcorp.Intern;
import com.example.techcorp.Project;
import com.example.techcorp.ui.ConsoleUI;

import com.example.techcorp.events.EmployeeStrikeEvent;
import com.example.techcorp.events.MarketSlowdownEvent;
import com.example.techcorp.events.GovernmentGrantEvent;
import com.example.techcorp.events.DataBreachEvent;
import com.example.techcorp.events.OfficeFireEvent;
import com.example.techcorp.events.TechBoomEvent;

import java.util.List;

public class GameEngine {
    private final Company company;
    private final ConsoleUI ui;
    private int turn = 1;
    private boolean running = true;
    
    private static final int TOTAL_GAME_PROJECTS = 5;

    // Predefined pools for generating random unique job candidates
    private static final String[] CANDIDATE_NAMES = {"Adam", "Barbara", "Cezary", "Dorota", "Filip", "Justyna", "Kamil", "Monika", "Marek", "Natalia"};

    public GameEngine(Company company, ConsoleUI ui) {
        this.company = company;
        this.ui = ui;
    }

    public void run() {
        System.out.println("=== Welcome to TechCorp Management Simulator ===");
        System.out.println("Goal: Complete all " + TOTAL_GAME_PROJECTS + " projects to win the game!\n");
        
        while (running && !isGameOver()) {
            System.out.println("\n--- TURN " + turn + " ---");
            
            company.showStatus(getCurrentLoanLimit());
            System.out.println("Completed Projects: " + getCompletedProjectsCount() + " / " + TOTAL_GAME_PROJECTS);
            
            System.out.println("\n1. Work one turn");
            System.out.println("2. Start a new project");
            System.out.println("3. Request a custom bank loan (3% interest)");
            System.out.println("4. HR Department (Hire / Fire Staff)");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            int choice = ui.readChoice();
            
            boolean turnAdvanced = false;
            
            switch (choice) {
                case 1 -> {
                    if (hasActiveProject()) {
                        System.out.println("\n>> ROUND REPORT:");
                        
                        boolean salariesPaid = company.payEmployeesMonthly();
                        
                        if (!salariesPaid || company.getCash() < 0) {
                            running = false;
                        } else {
                            for (Project p : company.getProjects()) {
                                if (!p.isFinished()) {
                                    p.workOneTurn();
                                    if (p.isFinished()) {
                                        System.out.println("✓ Project '" + p.getName() + "' completed! Payout: +" + p.getReward());
                                        company.addCash(p.getReward()); 
                                    }
                                }
                            }
                            
                            if (!isGameOver()) {
                                triggerRandomEvent();
                                if (company.getCash() < 0) {
                                    running = false;
                                }
                            }
                            
                            turnAdvanced = true;
                        }
                    } else {
                        System.out.println("❌ No active project! Choose option 2 first.");
                    }
                }
                case 2 -> {
                    if (!hasActiveProject()) {
                        selectNewProject();
                    } else {
                        System.out.println("❌ A project is already in progress.");
                    }
                }
                case 3 -> {
                    System.out.println("\n>> BANK LOAN SYSTEM:");
                    double currentLimit = getCurrentLoanLimit();
                    
                    if (company.getLoanAmount() >= currentLimit) {
                        System.out.println("❌ Credit denied! Your current stage debt limit (" + currentLimit + ") has been reached.");
                        System.out.println("Wait for future rounds to unlock a higher credit line!");
                    } else {
                        double remainingCreditLeft = currentLimit - company.getLoanAmount();
                        double amount = ui.readDoubleInRange("Enter loan amount (Max single request " + remainingCreditLeft + "): ", 1000, remainingCreditLeft);
                        
                        company.takeLoan(amount);
                        System.out.println("Loan approved! Added +" + amount + " to cash. Total debt accumulates 3% interest fee.");
                    }
                }
                case 4 -> handleHRDepartment(); 
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
            
            if (running && turnAdvanced) {
                turn++;
                ui.readText("\nPress Enter to continue...");
            }
        }
        displayGameOutcome();
    }

    private boolean hasActiveProject() {
        for (Project p : company.getProjects()) {
            if (!p.isFinished()) return true;
        }
        return false;
    }

    private int getCompletedProjectsCount() {
        int count = 0;
        for (Project p : company.getProjects()) {
            if (p.isFinished()) {
                count++;
            }
        }
        return count;
    }

    private double getCurrentLoanLimit() {
        if (turn < 5) {
            return 10000.0;
        } else if (turn < 10) {
            return 30000.0; 
        } else if (turn < 15) {
            return 50000.0;
        } else if (turn < 20) {
            return 70000.0;
        } else if (turn < 25) {
            return 90000.0;
        } else if (turn < 30) {
            return 120000.0;
        } else if (turn < 35) {
            return 150000.0;
        } else if (turn < 40) {
            return 180000.0;
        } else {
            return 210000.0;
        }
    }

    // Student Task: Complete HR management dashboard submodule
    private void handleHRDepartment() {
        System.out.println("\n>> TECHCORP HR DEPARTMENT:");
        System.out.println("1. Recruit new employee candidate");
        System.out.println("2. Terminate employee contract (Fire)");
        System.out.println("0. Back to main dashboard");
        int subChoice = ui.readChoiceInRange("HR Choice: ", 0, 2);

        if (subChoice == 1) {
            generateAndProposeCandidate();
        } else if (subChoice == 2) {
            processEmployeeTermination();
        }
    }

    // Student Task: Factory-pattern random employee generator with realistic balanced salary scales
    private void generateAndProposeCandidate() {
        String randomName = CANDIDATE_NAMES[(int) (Math.random() * CANDIDATE_NAMES.length)];
        int randomRoleIdx = (int) (Math.random() * 4); 
        int skill = (int) (Math.random() * 6) + 3; // Skill from 3 to 8
        
        Employee candidate;
        double salary;

        switch (randomRoleIdx) {
            case 0 -> {
                salary = skill * 600; 
                candidate = new Developer(randomName, skill, salary);
            }
            case 1 -> {
                salary = skill * 500;
                candidate = new Tester(randomName, skill, salary);
            }
            case 2 -> {
                salary = skill * 650;
                candidate = new Manager(randomName, skill, salary);
            }
            default -> {
                salary = skill * 300; 
                candidate = new Intern(randomName, skill, salary);
            }
        }

        System.out.println("\n--- NEW CANDIDATE PROFILE ---");
        System.out.println("Name: " + candidate.getName());
        System.out.println("Role: " + candidate.getRoleName());
        System.out.println("Skill Power: " + candidate.getSkill());
        System.out.println("Salary Demand: " + candidate.getSalary() + " / turn");
        System.out.println("-----------------------------");
        System.out.println("1. Hire this applicant");
        System.out.println("2. Reject applicant");
        
        int action = ui.readChoiceInRange("Action (1-2): ", 1, 2);
        if (action == 1) {
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

    // Student Task: Secure validation mapping for firing staff safely
    private void processEmployeeTermination() {
        List<Employee> list = company.getEmployees();
        if (list.isEmpty()) {
            System.out.println("❌ No employees available to lay off.");
            return;
        }

        System.out.println("\nCurrent Staff Directory:");
        for (int i = 0; i < list.size(); i++) {
            Employee e = list.get(i);
            System.out.println((i + 1) + ". " + e.getName() + " [" + e.getRoleName() + "] - Salary: " + e.getSalary());
        }
        System.out.println("0. Cancel termination");

        int targetIdx = ui.readChoiceInRange("Select staff number to fire (0-" + list.size() + "): ", 0, list.size());
        if (targetIdx == 0) {
            System.out.println("HR action canceled.");
            return;
        }

        Employee firedTarget = list.get(targetIdx - 1);
        company.fire(firedTarget);
        System.out.println("⚠️ Contract terminated. " + firedTarget.getName() + " has been successfully removed from TechCorp.");
    }

    private void selectNewProject() {
        System.out.println("\nAvailable projects:");
        
        boolean p2Available = true;
        boolean p3Available = true;
        boolean p4Available = true;
        boolean p5Available = true;
        
        for (Project p : company.getProjects()) {
            if (p.getName().equalsIgnoreCase("E-Commerce Platform")) p2Available = false;
            if (p.getName().equalsIgnoreCase("Cloud Migration")) p3Available = false;
            if (p.getName().equalsIgnoreCase("AI Chatbot Integration")) p4Available = false;
            if (p.getName().equalsIgnoreCase("Cybersecurity Audit")) p5Available = false;
        }
        
        if (!p2Available && !p3Available && !p4Available && !p5Available) {
            System.out.println("❌ No new contracts left on the market!");
            return;
        }
        
        int validOptionsCount = 0;
        int p2MenuNum = -1;
        int p3MenuNum = -1;
        int p4MenuNum = -1;
        int p5MenuNum = -1;
        
        if (p2Available) {
            validOptionsCount++;
            p2MenuNum = validOptionsCount;
            System.out.println(p2MenuNum + ". E-Commerce Platform [Work: 50 | Reward: 35000]");
        }
        if (p3Available) {
            validOptionsCount++;
            p3MenuNum = validOptionsCount;
            System.out.println(p3MenuNum + ". Cloud Migration [Work: 75 | Reward: 55000]");
        }
        if (p4Available) {
            validOptionsCount++;
            p4MenuNum = validOptionsCount;
            System.out.println(p4MenuNum + ". AI Chatbot Integration [Work: 100 | Reward: 80000]");
        }
        if (p5Available) {
            validOptionsCount++;
            p5MenuNum = validOptionsCount;
            System.out.println(p5MenuNum + ". Cybersecurity Audit [Work: 140 | Reward: 110000]");
        }
        
        int choice = ui.readChoiceInRange("Select (1-" + validOptionsCount + "): ", 1, validOptionsCount);
        
        Project newProject = null;
        
        if (choice == p2MenuNum) {
            newProject = new Project("E-Commerce Platform", 50, 35000);
        } else if (choice == p3MenuNum) {
            newProject = new Project("Cloud Migration", 75, 55000);
        } else if (choice == p4MenuNum) {
            newProject = new Project("AI Chatbot Integration", 100, 80000);
        } else if (choice == p5MenuNum) {
            newProject = new Project("Cybersecurity Audit", 140, 110000);
        }
        
        if (newProject != null) {
            for (Employee e : company.getEmployees()) {
                newProject.addEmployee(e);
            }
            company.startProject(newProject);
            System.out.println("Started project: " + newProject.getName());
        }
    }

    private void triggerRandomEvent() {
        double chance = Math.random();
        
        if (chance < 0.08) {
            new EmployeeStrikeEvent(0.15).apply(company);
        } else if (chance >= 0.08 && chance < 0.16) {
            new MarketSlowdownEvent(4000).apply(company);
        } else if (chance >= 0.16 && chance < 0.24) {
            new GovernmentGrantEvent(5000).apply(company);
        } else if (chance >= 0.24 && chance < 0.32) {
            new DataBreachEvent(6000).apply(company);
        } else if (chance >= 0.32 && chance < 0.40) {
            new OfficeFireEvent(8000).apply(company);
        } else if (chance >= 0.40 && chance < 0.48) {
            new TechBoomEvent(7000).apply(company);
        }
    }

    private boolean isGameOver() {
        return company.getCash() < 0 || getCompletedProjectsCount() >= TOTAL_GAME_PROJECTS;
    }

    private void displayGameOutcome() {
        System.out.println("\n--- GAME OVER ---");
        if (company.getCash() < 0) {
            System.out.println("Liquidation. Bankrupted and lost. Final Account Balance: " + company.getCash());
        } else if (getCompletedProjectsCount() >= TOTAL_GAME_PROJECTS) {
            System.out.println("🏆 VICTORY! Your company successfully delivered all " + TOTAL_GAME_PROJECTS + " projects and dominated the market!");
        } else {
            System.out.println("You voluntarily quit the simulator. Final Progress: " + getCompletedProjectsCount() + "/" + TOTAL_GAME_PROJECTS + " projects.");
        }
    }
}