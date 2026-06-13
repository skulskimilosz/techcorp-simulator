package com.example.techcorp.domain;

import com.example.techcorp.Company;

/**
 * Reprezentuje kompletny stan gry do zapisania na dysk.
 * Wzorzec z lekcji 9 – obiekt persystencji.
 */
public class GameState {
    private String companyName;
    private double cash;
    private double loanAmount;
    private int currentTurn;
    private int completedProjects;

    public GameState(Company company, int currentTurn, int completedProjects) {
        this.companyName = company.getName() != null ? company.getName() : "TechCorp";
        this.cash = company.getCash();
        this.loanAmount = company.getLoanAmount();
        this.currentTurn = currentTurn;
        this.completedProjects = completedProjects;
    }

    public String getCompanyName()    { return companyName; }
    public double getCash()           { return cash; }
    public double getLoanAmount()     { return loanAmount; }
    public int getCurrentTurn()       { return currentTurn; }
    public int getCompletedProjects() { return completedProjects; }
}