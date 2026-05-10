package com.example.techcorp;

import com.example.techcorp.engine.GameEngine;
import com.example.techcorp.ui.ConsoleUI;

public class Main {

    public static void main(String[] args) {
        Company company = new Company("TechCorp", 50_000);

        company.hire(new Developer("Anna", 9, 8_000));
        company.hire(new Tester("Piotr", 6, 6_500));
        company.hire(new Manager("Ewa", 7, 9_000));
        company.hire(new Intern("Tomek", 4, 2_000));

        Project mobileApp = new Project("Mobile App", 40);
        mobileApp.addEmployee(company.getEmployees().get(0));
        mobileApp.addEmployee(company.getEmployees().get(1));
        mobileApp.addEmployee(company.getEmployees().get(2));
        mobileApp.addEmployee(company.getEmployees().get(3));

        company.startProject(mobileApp);

        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(company, ui);
        engine.run();

        System.out.println("\nGame ended!");
    }
}