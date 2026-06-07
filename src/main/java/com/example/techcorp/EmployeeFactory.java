package com.example.techcorp;

/**
 * Fabryka pracowników – implementacja wzorca projektowego Factory Method.
 * <p>
 * Odpowiada za tworzenie losowych kandydatów do pracy w firmie TechCorp.
 * Centralizuje logikę generowania pracowników, odciążając {@code GameEngine}
 * i zapewniając spójność parametrów (nazwy, skill, wynagrodzenia).
 * </p>
 *
 * <p>Wzorzec Factory jest tu zastosowany, aby:</p>
 * <ul>
 *   <li>oddzielić tworzenie obiektów od logiki gry (Single Responsibility),</li>
 *   <li>ułatwić dodawanie nowych typów pracowników w przyszłości,</li>
 *   <li>skupić wszystkie współczynniki wynagrodzeń w jednym miejscu.</li>
 * </ul>
 */
public class EmployeeFactory {

    /** Pula imion kandydatów losowanych podczas rekrutacji. */
    private static final String[] CANDIDATE_NAMES = {
        "Adam", "Barbara", "Cezary", "Dorota", "Filip",
        "Justyna", "Kamil", "Monika", "Marek", "Natalia"
    };

    /**
     * Generuje losowego kandydata do pracy.
     * <p>
     * Losuje imię, typ roli (Developer / Tester / Manager / Intern)
     * oraz poziom umiejętności w przedziale 3–8.
     * Wynagrodzenie obliczane jest jako iloczyn skill × stały mnożnik roli.
     * </p>
     *
     * @return nowy obiekt {@link Employee} gotowy do zatrudnienia lub odrzucenia
     */
    public static Employee generateRandomCandidate() {
        // Losowanie imienia z predefiniowanej puli
        String randomName = CANDIDATE_NAMES[(int) (Math.random() * CANDIDATE_NAMES.length)];

        // Losowanie roli: 0=Developer, 1=Tester, 2=Manager, 3=Intern
        int randomRoleIdx = (int) (Math.random() * 4);

        // Losowanie poziomu umiejętności w zakresie 3–8
        int skill = (int) (Math.random() * 6) + 3;

        // Tworzenie kandydata zgodnie z wylosowaną rolą i wynagrodzeniem proporcjonalnym do skilla
        return switch (randomRoleIdx) {
            case 0 -> new Developer(randomName, skill, skill * 600.0); // Najdroższy – pełny wkład
            case 1 -> new Tester(randomName, skill, skill * 500.0);    // Średni koszt
            case 2 -> new Manager(randomName, skill, skill * 650.0);   // Najdroższy per skill (prestiż)
            default -> new Intern(randomName, skill, skill * 300.0);   // Najtańszy – minimalny wkład
        };
    }
}
