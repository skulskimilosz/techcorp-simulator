package com.example.techcorp.events;

import com.example.techcorp.Company;

/**
 * Interfejs reprezentujący zdarzenie losowe wpływające na stan firmy.
 * <p>
 * Wzorzec projektowy: <strong>Strategy / Command</strong>.
 * Każde zdarzenie enkapsuluje własną logikę – może zwiększać lub zmniejszać
 * gotówkę firmy, wpływać na pracowników lub projekty.
 * </p>
 *
 * <p>Zaimplementowane zdarzenia:</p>
 * <ul>
 *   <li>{@link MarketSlowdownEvent}  – straty z powodu spowolnienia rynku</li>
 *   <li>{@link EmployeeStrikeEvent}  – strajk pracowniczy z żądaniami premii</li>
 *   <li>{@link GovernmentGrantEvent} – rządowy grant innowacyjny</li>
 *   <li>{@link DataBreachEvent}      – kara GDPR za naruszenie danych</li>
 *   <li>{@link OfficeFireEvent}      – pożar biura, koszty naprawy</li>
 *   <li>{@link TechBoomEvent}        – boom technologiczny, dodatkowe przychody</li>
 * </ul>
 *
 * Nowe zdarzenia dodaje się tworząc klasę implementującą ten interfejs
 * i dodając ją do puli w {@code GameEngine.triggerRandomEvent()}.
 */
public interface GameEvent {

    /**
     * Aplikuje efekt zdarzenia na stan podanej firmy.
     * Może modyfikować gotówkę, pracowników lub projekty.
     *
     * @param company firma, na którą zdarzenie wywiera efekt
     */
    void apply(Company company);
}
