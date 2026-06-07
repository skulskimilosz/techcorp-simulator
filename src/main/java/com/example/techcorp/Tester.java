package com.example.techcorp;

/**
 * Tester – pracownik odpowiedzialny za zapewnienie jakości oprogramowania (QA).
 * <p>
 * Tester wnosi do projektu połowę swojego poziomu umiejętności.
 * Choć mniej wydajny niż Developer, jest tańszy i niezbędny
 * dla stabilności produktu w rzeczywistym środowisku.
 * </p>
 */
public class Tester extends Employee {

    /**
     * Tworzy nowego testera.
     *
     * @param name   imię pracownika
     * @param skill  poziom umiejętności (np. 1–10)
     * @param salary miesięczne wynagrodzenie
     */
    public Tester(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    /**
     * Tester wnosi do projektu <strong>połowę</strong> swojego poziomu umiejętności.
     *
     * @return {@code skill / 2}
     */
    @Override
    public int work() {
        return getSkill() / 2; // Testers contribute half of their skill to the project
    }

    /**
     * @return stała nazwa roli "Tester"
     */
    @Override
    public String getRoleName() {
        return "Tester";
    }
}
