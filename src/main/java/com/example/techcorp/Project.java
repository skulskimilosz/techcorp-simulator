package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private final String name;
    private final int requiredWork;
    private final double reward;
    private int progress;
    private final List<Employee> team;
    private ProjectStatus status;

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

    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        team.add(employee);
    }

    // New helper method to safely remove an employee if they get fired from the company
    public void removeEmployee(Employee employee) {
        team.remove(employee);
    }

    public void workOneTurn() {
        if (status == ProjectStatus.COMPLETED) {
            return;
        }

        for (Employee employee : team) {
            progress += employee.work();
        }

        if (progress > requiredWork) {
            progress = requiredWork;
        }

        if (progress >= requiredWork) {
            status = ProjectStatus.COMPLETED;
        }
    }

    public boolean isFinished() {
        return status == ProjectStatus.COMPLETED;
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

    public double getReward() {
        return reward;
    }

    public List<Employee> getTeam() {
        return team;
    }

    public ProjectStatus getStatus() {
        return status;
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