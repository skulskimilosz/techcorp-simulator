package com.example.techcorp.events;

import com.example.techcorp.Company;

public class ProjectDeliveryBonusEvent implements GameEvent {
    private double bonusAmount;

    public ProjectDeliveryBonusEvent(double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Project Successfully Delivered!");
        System.out.println(" Company receives bonus: " + bonusAmount);
        company.addCash(bonusAmount);
    }
}
