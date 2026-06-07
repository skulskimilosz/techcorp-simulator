package com.example.techcorp.events;

import com.example.techcorp.Company;

public class EmployeeStrikeEvent implements GameEvent { // Changed extends to implements

    private final String name = "[EVENT] Employee Strike & Bonus Demands!";
    private final double bonusDemandPercentage;

    // The constructor now takes only the percentage and doesn't call super()
    public EmployeeStrikeEvent(double bonusDemandPercentage) {
        this.bonusDemandPercentage = bonusDemandPercentage;
    }

    @Override
    public void apply(Company company) {
        System.out.println("\n" + name);
        
        // Check if there are actually any employees present to form a strike
        int staffCount = company.getEmployees().size();
        
        if (staffCount == 0) {
            System.out.println("  The office is completely empty. There are no employees left to strike!");
            System.out.println("  Financial impact: 0.0");
            return;
        }

        // Calculate dynamic strike cost based directly on real-time headcount and total payroll expenditure
        double currentTotalPayroll = company.getTotalPayroll();
        double strikeResolutionCost = currentTotalPayroll * bonusDemandPercentage;

        // Convert percentage to integer for clean text output (e.g., 15 instead of 15.0000000002)
        int displayPercentage = (int) Math.round(bonusDemandPercentage * 100);

        System.out.println("  Your staff of " + staffCount + " employees has formed a union strike!");
        System.out.println("  They demand a " + displayPercentage + "% structural bonus to resume operations.");
        
        // Using String.format to guarantee professional financial decimal outputs (.2f)
        System.out.println("  Legal settlements and urgent operational containment cost: -" + String.format("%.2f", strikeResolutionCost));
        
        company.reduceCash(strikeResolutionCost);
    }
}