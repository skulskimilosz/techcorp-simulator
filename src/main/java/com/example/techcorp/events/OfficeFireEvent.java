package com.example.techcorp.events;

import com.example.techcorp.Company;

// Student Task: Object-oriented event for physical hazard (office fire)
public class OfficeFireEvent implements GameEvent {
    private final double damageCost;

    public OfficeFireEvent(double damageCost) {
        this.damageCost = damageCost;
    }

    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Office Infrastructure Fire!");
        System.out.println(" Hardware destruction requires urgent replacement costs: -" + damageCost);
        company.reduceCash(damageCost);
    }
}