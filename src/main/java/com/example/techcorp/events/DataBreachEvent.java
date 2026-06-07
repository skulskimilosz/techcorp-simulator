package com.example.techcorp.events;

import com.example.techcorp.Company;

/**
 * Zdarzenie: naruszenie ochrony danych osobowych (GDPR Data Breach).
 * <p>
 * Firma ponosi koszty prawne i kary regulacyjne za wyciek danych.
 * Odzwierciedla realne ryzyko cybernetyczne w branży IT. Zdarzenie negatywne.
 * </p>
 */
public class DataBreachEvent implements GameEvent {

    /** Kwota kary GDPR i kosztów prawnych do zapłacenia przez firmę. */
    private final double fineAmount;

    /**
     * @param fineAmount łączna kwota kar i kosztów obsługi prawnej naruszenia danych
     */
    public DataBreachEvent(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    /**
     * Odejmuje karę od gotówki firmy i wyświetla komunikat o naruszeniu.
     *
     * @param company firma ukarana za naruszenie GDPR
     */
    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] GDPR Data Breach Crisis!");
        System.out.println(" Legal fees and regulatory compliance fines applied: -" + fineAmount);
        company.reduceCash(fineAmount);
    }
}
