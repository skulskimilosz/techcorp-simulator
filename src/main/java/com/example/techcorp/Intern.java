package com.example.techcorp;

/**
 * Stażysta – najtańszy i najmniej wydajny typ pracownika.
 * <p>
 * Intern wnosi do projektu jedną czwartą swojego poziomu umiejętności.
 * Mimo niskiej produktywności jest opłacalny przy ograniczonym budżecie,
 * gdy firma potrzebuje powiększyć zespół tanim kosztem.
 * </p>
 */
public class Intern extends Employee {

    /**
     * Tworzy nowego stażystę.
     *
     * @param name   imię pracownika
     * @param skill  poziom umiejętności (np. 1–10)
     * @param salary miesięczne wynagrodzenie
     */
    public Intern(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    /**
     * Intern wnosi do projektu <strong>jedną czwartą</strong> swojego poziomu umiejętności.
     * Odzwierciedla brak doświadczenia i konieczność wdrożenia.
     *
     * @return {@code skill / 4}
     */
    @Override
    public int work() {
        return getSkill() / 4; // Interns contribute a fourth of their skill to the project
    }

    /**
     * @return stała nazwa roli "Intern"
     */
    @Override
    public String getRoleName() {
        return "Intern";
    }
}
