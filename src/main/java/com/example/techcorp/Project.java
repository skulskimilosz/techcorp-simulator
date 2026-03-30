package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private int requiredWork;
    private int progress;
    private List<Employee> team;

    public Project(String name, int requiredWork) {
        validateName(name);
        validateRequiredWork(requiredWork);

        this.name = name;
        this.requiredWork = requiredWork;
        this.progress = 0;
        this.team = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        team.add(employee);
    }

    public void workOneTurn() {
        for (Employee employee : team) {
            progress += employee.work();
        }

        if (progress > requiredWork) {
            progress = requiredWork;
        }
    }

    public boolean isFinished() {
        return progress >= requiredWork;
    }

    public int getCompletionPercentage() {
        return (progress * 100) / requiredWork;
    }

    public String getName() {
        return name;
    }

    public int getRequiredWork() {
        return requiredWork;
    }

    public int getProgress() {
        return progress;
    }

    public List<Employee> getTeam() {
        return team;
    }

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