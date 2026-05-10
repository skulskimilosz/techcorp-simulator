package com.example.techcorp.events;

import com.example.techcorp.Company;

public class MarketSlowdownEvent implements GameEvent {
    private double cashReduction;

    public MarketSlowdownEvent(double cashReduction) {
        this.cashReduction = cashReduction;
    }

    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Market Slowdown Detected!");
        System.out.println(" Company loses: " + cashReduction);
        company.reduceCash(cashReduction);
    }
}
