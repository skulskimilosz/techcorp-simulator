package com.example.techcorp.events;

import com.example.techcorp.Company;

/**
 * Zdarzenie: globalny boom w sektorze technologicznym.
 * <p>
 * Firma korzysta z nagłego wzrostu popytu na usługi IT –
 * nowi klienci, wyższe kontrakty, krótkoterminowy wzrost wartości.
 * Zdarzenie pozytywne.
 * </p>
 */
public class TechBoomEvent implements GameEvent {

    /** Bonus finansowy wynikający z bumu technologicznego. */
    private final double marketBonus;

    /**
     * @param marketBonus jednorazowy bonus gotówkowy dla firmy za boom rynkowy
     */
    public TechBoomEvent(double marketBonus) {
        this.marketBonus = marketBonus;
    }

    /**
     * Dodaje bonus do gotówki firmy i wyświetla komunikat o zdarzeniu.
     *
     * @param company firma korzystająca z boomu technologicznego
     */
    @Override
    public void apply(Company company) {
        System.out.println(" [EVENT] Global Tech Sector Boom!");
        System.out.println(" Immediate industry demand causes short-term valuation surge: +" + marketBonus);
        company.addCash(marketBonus);
    }
}
