package com.example.techcorp.events;

import com.example.techcorp.Company;

// Student Task: Object-oriented positive event for a market tech boom
public class TechBoomEvent implements GameEvent {
    private final double marketBonus;

    public TechBoomEvent(double marketBonus) {
        this.marketBonus = marketBonus;
    }

    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Global Tech Sector Boom!");
        System.out.println(" Immediate industry demand causes short-term valuation surge: +" + marketBonus);
        company.addCash(marketBonus);
    }
}