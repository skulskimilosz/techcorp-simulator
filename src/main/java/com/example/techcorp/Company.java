package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Company {

    private String name;
    private double cash;
    private List<Employee> employees;
    private List<Project> projects;

    public Company(String name, double cash) {
        validateName(name);
        validateCash(cash);

        this.name = name;
        this.cash = cash;
        this.employees = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void hire(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        employees.add(employee);
    }

    public void startProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }
        projects.add(project);
    }

    public void showStatus() {
        System.out.println(" === COMPANY STATUS ===");
        System.out.println(" Name : " + name);
        System.out.println(" Cash : " + cash);
        System.out.println(" Employees : " + employees.size());
        System.out.println(" Projects : " + projects.size());
        System.out.println();

        if (employees.isEmpty()) {
            System.out.println("No employees hired yet.");
        } else {
            System.out.println(" Employees :");
            for (Employee employee : employees) {
                System.out.println("- " + employee.getRoleName()
                    + ": " + employee.getName()
                    + " | skill : " + employee.getSkill()
                    + " | salary : " + employee.getSalary());
            }
        }

        System.out.println();

        if (projects.isEmpty()) {
            System.out.println("No active projects.");
        } else {
            System.out.println(" Projects :");
            for (Project project : projects) {
                System.out.println("- " + project.getName()
                    + " | progress : " + project.getProgress() + "/" + project.getRequiredWork()
                    + " | completion : " + project.getCompletionPercentage() + "%"
                    + " | finished : " + project.isFinished());
            }
        }

        System.out.println(" ====================== ");
    }

    public String getName() {
        return name;
    }

    public double getCash() {
        return cash;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Project> getProjects() {
        return projects;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or blank.");
        }
    }

    private void validateCash(double cash) {
        if (cash < 0) {
            throw new IllegalArgumentException("Company cash cannot be negative.");
        }
    }
}