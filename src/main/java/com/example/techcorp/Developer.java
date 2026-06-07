package com.example.techcorp;

/**
 * Programista – najbardziej produktywny typ pracownika.
 * <p>
 * Developer wnosi do projektu pełną wartość swojego poziomu umiejętności
 * ({@code skill}) bez żadnego dzielnika. Jest to najefektywniejsza rola
 * w kontekście postępu projektu, ale też zazwyczaj najdroższa.
 * </p>
 */
public class Developer extends Employee {

    /**
     * Tworzy nowego programistę.
     *
     * @param name   imię pracownika
     * @param skill  poziom umiejętności (np. 1–10)
     * @param salary miesięczne wynagrodzenie
     */
    public Developer(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    /**
     * Developer wnosi do projektu <strong>pełny</strong> poziom umiejętności.
     *
     * @return wartość {@code skill} bez modyfikatorów
     */
    @Override
    public int work() {
        return getSkill(); // Pełny skill jako wkład w projekt
    }

    /**
     * @return stała nazwa roli "Developer"
     */
    @Override
    public String getRoleName() {
        return "Developer";
    }
}
