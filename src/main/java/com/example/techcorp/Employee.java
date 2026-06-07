package com.example.techcorp;

/**
 * Abstrakcyjna klasa bazowa reprezentująca pracownika firmy TechCorp.
 * <p>
 * Każdy pracownik posiada imię, poziom umiejętności oraz miesięczne wynagrodzenie.
 * Konkretne podklasy (Developer, Tester, Manager, Intern) definiują,
 * ile punktów pracy wnoszą do projektu przez metodę {@link #work()}.
 * </p>
 *
 * Hierarchia dziedziczenia:
 * Employee (abstract)
 *   ├── Developer   – pełny skill jako wkład w projekt
 *   ├── Tester      – połowa skilla
 *   ├── Manager     – jedna trzecia skilla
 *   └── Intern      – jedna czwarta skilla
 */
public abstract class Employee {

    /** Imię pracownika. */
    private String name;

    /** Poziom umiejętności pracownika (musi być > 0). */
    private int skill;

    /** Miesięczne wynagrodzenie pracownika (nie może być ujemne). */
    private double salary;

    /**
     * Tworzy nowego pracownika po walidacji wszystkich pól.
     *
     * @param name   imię pracownika (nie może być puste ani null)
     * @param skill  poziom umiejętności (musi być > 0)
     * @param salary miesięczne wynagrodzenie (musi być >= 0)
     * @throws IllegalArgumentException jeśli któryś z parametrów jest niepoprawny
     */
    public Employee(String name, int skill, double salary) {
        validateName(name);
        validateSkill(skill);
        validateSalary(salary);

        this.name = name;
        this.skill = skill;
        this.salary = salary;
    }

    /**
     * Wykonuje pracę przez jedną turę i zwraca ilość punktów pracy
     * wniesionych do aktywnego projektu. Każda podklasa implementuje
     * własną logikę na podstawie poziomu umiejętności.
     *
     * @return liczba punktów postępu projektu za tę turę
     */
    public abstract int work();

    /**
     * Zwraca czytelną nazwę roli pracownika (np. "Developer", "Manager").
     *
     * @return nazwa roli jako String
     */
    public abstract String getRoleName();

    /** @return imię pracownika */
    public String getName()   { return name; }

    /** @return poziom umiejętności pracownika */
    public int getSkill()     { return skill; }

    /** @return miesięczne wynagrodzenie pracownika */
    public double getSalary() { return salary; }

    // -------------------------------------------------------------------------
    // Metody walidacji – prywatne, wywoływane tylko w konstruktorze
    // -------------------------------------------------------------------------

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Employee name cannot be null or blank.");
        }
    }

    private void validateSkill(int skill) {
        if (skill <= 0) {
            throw new IllegalArgumentException("Employee skill must be greater than 0.");
        }
    }

    private void validateSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Employee salary cannot be negative.");
        }
    }
}
