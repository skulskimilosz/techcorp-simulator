package com.example.techcorp.events;

import com.example.techcorp.Company;

/**
 * Zdarzenie: strajk pracowniczy z żądaniami premii strukturalnej.
 * <p>
 * Koszt zdarzenia jest <strong>dynamiczny</strong> – zależny od aktualnego
 * funduszu wynagrodzeń firmy i procentu żądanej premii. Im większy
 * i lepiej płatny zespół, tym dotkliwszy strajk.
 * </p>
 * Jeśli firma nie ma pracowników, zdarzenie nie ma efektu finansowego.
 */
public class EmployeeStrikeEvent implements GameEvent {

    /** Etykieta zdarzenia wyświetlana w konsoli. */
    private final String eventName = "[EVENT] Employee Strike & Bonus Demands!";

    /**
     * Procent łącznego funduszu wynagrodzeń żądany przez pracowników jako premia
     * (np. 0.15 = 15%).
     */
    private final double bonusDemandPercentage;

    /**
     * @param bonusDemandPercentage procent funduszu płac żądany przez strajkujących
     *                              (np. 0.15 dla 15%)
     */
    public EmployeeStrikeEvent(double bonusDemandPercentage) {
        this.bonusDemandPercentage = bonusDemandPercentage;
    }

    /**
     * Oblicza koszt rozwiązania strajku na podstawie aktualnego funduszu płac
     * i odejmuje go od gotówki firmy.
     * Jeśli firma nie ma pracowników – brak efektu finansowego.
     *
     * @param company firma dotknięta strajkiem
     */
    @Override
    public void apply(Company company) {
        System.out.println("\n" + eventName);

        // Weryfikacja: czy jest ktokolwiek do strajkowania?
        int staffCount = company.getEmployees().size();
        if (staffCount == 0) {
            System.out.println("  The office is completely empty. There are no employees left to strike!");
            System.out.println("  Financial impact: 0.0");
            return;
        }

        // Koszt proporcjonalny do aktualnego funduszu wynagrodzeń
        double currentTotalPayroll = company.getTotalPayroll();
        double strikeResolutionCost = currentTotalPayroll * bonusDemandPercentage;

        // Konwersja na procent całkowity dla czytelnego wydruku (np. 15 zamiast 0.15)
        int displayPercentage = (int) Math.round(bonusDemandPercentage * 100);

        System.out.println("  Your staff of " + staffCount + " employees has formed a union strike!");
        System.out.println("  They demand a " + displayPercentage + "% structural bonus to resume operations.");
        System.out.println("  Legal settlements and urgent operational containment cost: -"
                + String.format("%.2f", strikeResolutionCost));

        company.reduceCash(strikeResolutionCost);
    }
}
