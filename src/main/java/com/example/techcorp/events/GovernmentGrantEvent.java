package com.example.techcorp.events;

import com.example.techcorp.Company;

public class GovernmentGrantEvent implements GameEvent {
    private final double grantAmount;

    public GovernmentGrantEvent(double grantAmount) {
        this.grantAmount = grantAmount;
    }

    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Government Innovation Grant Approved!");
        System.out.println(" Your company receives a tech subsidy of: " + grantAmount);
        company.addCash(grantAmount);
    }
}