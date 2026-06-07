package com.example.techcorp;

/**
 * Menedżer – pracownik zarządzający zespołem i koordynujący prace projektowe.
 * <p>
 * Manager wnosi do projektu jedną trzecią swojego poziomu umiejętności —
 * jego wkład techniczny jest niższy niż Developer czy Tester, ale jego
 * wynagrodzenie jest zazwyczaj najwyższe ze względu na rolę strategiczną.
 * </p>
 */
public class Manager extends Employee {

    /**
     * Tworzy nowego menedżera.
     *
     * @param name   imię pracownika
     * @param skill  poziom umiejętności (np. 1–10)
     * @param salary miesięczne wynagrodzenie
     */
    public Manager(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    /**
     * Manager wnosi do projektu <strong>jedną trzecią</strong> swojego poziomu umiejętności.
     * Odzwierciedla fakt, że menedżer skupia się bardziej na organizacji niż kodowaniu.
     *
     * @return {@code skill / 3}
     */
    @Override
    public int work() {
        return getSkill() / 3; // Managers contribute a third of their skill to the project
    }

    /**
     * @return stała nazwa roli "Manager"
     */
    @Override
    public String getRoleName() {
        return "Manager";
    }
}
