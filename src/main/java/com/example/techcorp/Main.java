package com.example.techcorp;

import com.example.techcorp.engine.GameEngine;
import com.example.techcorp.ui.ConsoleUI;

public class Main {

    public static void main(String[] args) {
        Company company = new Company("TechCorp", 50_000);

        // Predefined initial workforce using your clean OOP subclasses
        Employee anna = new Developer("Anna", 9, 4500);
        Employee piotr = new Tester("Piotr", 6, 3500);
        Employee ewa = new Manager("Ewa", 7, 5000);
        Employee tomek = new Intern("Tomek", 4, 1500);

        company.hire(anna);
        company.hire(piotr);
        company.hire(ewa);
        company.hire(tomek);

        // Bootstrap the first active project contract
        Project mobileApp = new Project("Mobile App", 40, 30000);
        mobileApp.addEmployee(anna);
        mobileApp.addEmployee(piotr);
        mobileApp.addEmployee(ewa);
        mobileApp.addEmployee(tomek);

        company.startProject(mobileApp);

        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(company, ui);
        engine.run();

        System.out.println("\nGame ended!");
    }
}