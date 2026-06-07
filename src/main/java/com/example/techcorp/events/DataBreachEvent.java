package com.example.techcorp.events;

import com.example.techcorp.Company;

// Student Task: Object-oriented event for legal/GDPR data breach
public class DataBreachEvent implements GameEvent {
    private final double fineAmount;

    public DataBreachEvent(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] GDPR Data Breach Crisis!");
        System.out.println(" Legal fees and regulatory compliance fines applied: -" + fineAmount);
        company.reduceCash(fineAmount);
    }
}
