package com.example.techcorp.events;

import com.example.techcorp.Company;

/**
 * Zdarzenie: pożar infrastruktury biurowej.
 * <p>
 * Firma ponosi koszty wymiany sprzętu i naprawy po pożarze.
 * Odzwierciedla fizyczne ryzyko operacyjne każdej firmy. Zdarzenie negatywne.
 * </p>
 */
public class OfficeFireEvent implements GameEvent {

    /** Koszt zniszczeń i pilnej wymiany sprzętu po pożarze. */
    private final double damageCost;

    /**
     * @param damageCost kwota strat materialnych wymagająca natychmiastowej naprawy
     */
    public OfficeFireEvent(double damageCost) {
        this.damageCost = damageCost;
    }

    /**
     * Odejmuje koszt szkód od gotówki firmy i wyświetla komunikat o zdarzeniu.
     *
     * @param company firma dotknięta pożarem biura
     */
    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Office Infrastructure Fire!");
        System.out.println(" Hardware destruction requires urgent replacement costs: -" + damageCost);
        company.reduceCash(damageCost);
    }
}
