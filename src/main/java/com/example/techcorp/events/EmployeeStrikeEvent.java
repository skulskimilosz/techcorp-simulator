package com.example.techcorp.events;

import com.example.techcorp.Company;

public class EmployeeStrikeEvent implements GameEvent {
    private double wageDemand;

    public EmployeeStrikeEvent(double wageDemand) {
        this.wageDemand = wageDemand;
    }

    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Employee Strike!");
        System.out.println(" Employees demand additional wages: " + wageDemand);
        company.reduceCash(wageDemand);
    }
}
