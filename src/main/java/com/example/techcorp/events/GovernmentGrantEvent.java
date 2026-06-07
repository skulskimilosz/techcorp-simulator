package com.example.techcorp.events;

import com.example.techcorp.Company;

/**
 * Zdarzenie: rządowy grant innowacyjny dla sektora IT.
 * <p>
 * Firma otrzymuje jednorazowe wsparcie finansowe od państwa,
 * odzwierciedlające programy dotacji technologicznych. Zdarzenie pozytywne.
 * </p>
 */
public class GovernmentGrantEvent implements GameEvent {

    /** Kwota grantu dodawana do gotówki firmy. */
    private final double grantAmount;

    /**
     * @param grantAmount wartość rządowego grantu innowacyjnego
     */
    public GovernmentGrantEvent(double grantAmount) {
        this.grantAmount = grantAmount;
    }

    /**
     * Dodaje wartość grantu do gotówki firmy i wyświetla komunikat.
     *
     * @param company firma otrzymująca grant
     */
    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Government Innovation Grant Approved!");
        System.out.println(" Your company receives a tech subsidy of: " + grantAmount);
        company.addCash(grantAmount);
    }
}
