package com.example.techcorp.events;

import com.example.techcorp.Company;

/**
 * Zdarzenie: spowolnienie rynku technologicznego.
 * <p>
 * Firma traci stałą kwotę gotówki, odzwierciedlając spadek
 * popytu na usługi IT w danym kwartale. Zdarzenie negatywne.
 * </p>
 */
public class MarketSlowdownEvent implements GameEvent {

    /** Kwota gotówki, którą firma traci w wyniku spowolnienia rynku. */
    private final double cashReduction;

    /**
     * @param cashReduction kwota strat dla firmy (dodatnia wartość = uszczuplenie budżetu)
     */
    public MarketSlowdownEvent(double cashReduction) {
        this.cashReduction = cashReduction;
    }

    /**
     * Odejmuje {@code cashReduction} od gotówki firmy i wyświetla komunikat zdarzenia.
     *
     * @param company firma dotknięta zdarzeniem
     */
    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Market Slowdown Detected!");
        System.out.println(" Company loses: " + cashReduction);
        company.reduceCash(cashReduction);
    }
}
