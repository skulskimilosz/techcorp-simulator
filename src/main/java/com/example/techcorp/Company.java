package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private final String name;
    private double cash;
    private double loanAmount; 
    private final double interestRate = 0.03; // 3% monthly interest fee
    private final List<Employee> employees;
    private final List<Project> projects;

    public Company(String name, double cash) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException();
        if (cash < 0) throw new IllegalArgumentException();
        this.name = name;
        this.cash = cash;
        this.loanAmount = 0;
        this.employees = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void hire(Employee employee) {
        if (employee == null) throw new IllegalArgumentException();
        employees.add(employee);
    }

    // TUTAJ ZOSTAŁA DODANA TA METODA:
    public void startProject(Project project) {
        if (project == null) throw new IllegalArgumentException();
        projects.add(project);
    }

    // New HR method to fire an employee and remove them from the payroll
    public void fire(Employee employee) {
        if (employee == null) throw new IllegalArgumentException();
        employees.remove(employee);
        
        // Also remove this fired employee from all ongoing project teams to prevent ghost work
        for (Project p : projects) {
            p.removeEmployee(employee);
        }
    }

    public double getTotalPayroll() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getSalary();
        }
        return total;
    }

    public double getMonthlyInterestCost() {
        return this.loanAmount * this.interestRate;
    }

    public void takeLoan(double amount) {
        this.loanAmount += amount;
        this.cash += amount;
    }

    public boolean payEmployeesMonthly() {
        double totalSalaries = getTotalPayroll();
        double interestCost = getMonthlyInterestCost();
        double totalExpenses = totalSalaries + interestCost;

        if (interestCost > 0) {
            System.out.println("Paid monthly loan interest (3%): -" + interestCost);
        }

        this.cash -= totalExpenses;
        
        if (this.cash >= 0) {
            System.out.println("Paid employee salaries: -" + totalSalaries);
            return true;
        } else {
            System.out.println("❌ FINANCIAL COLLAPSE: Not enough cash to cover salaries and interest!");
            return false;
        }
    }

    public void showStatus(double currentLoanLimit) {
        System.out.println("Company: " + name + " | Cash: " + cash);
        System.out.println("Credit Line: " + loanAmount + " / " + currentLoanLimit + " (Interest: " + getMonthlyInterestCost() + "/turn)");
        System.out.println("Employees: " + employees.size() + " | Monthly Payroll: " + getTotalPayroll());
        System.out.println("Projects:");
        for (Project p : projects) {
            String status = p.isFinished() ? "FINISHED" : "ACTIVE";
            System.out.println("  - " + p.getName() + " [" + p.getProgress() + "/" + p.getRequiredWork() + "] (" + status + ")");
        }
    }

    public double getCash() { return cash; }
    public double getLoanAmount() { return loanAmount; }
    public List<Employee> getEmployees() { return employees; }
    public List<Project> getProjects() { return projects; }
    public void addCash(double amount) { this.cash += amount; }
    public void reduceCash(double amount) { this.cash -= amount; }
}