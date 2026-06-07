package com.example.techcorp;

/**
 * Enum reprezentujący aktualny status projektu w systemie TechCorp.
 * <p>
 * Używany wewnątrz klasy {@link Project} do śledzenia, czy projekt
 * jest jeszcze w toku, czy został już zakończony i nagroda wypłacona.
 * </p>
 */
public enum ProjectStatus {

    /** Projekt jest aktywny – trwają prace, postęp rośnie co turę. */
    ACTIVE,

    /** Projekt został ukończony – wymagana praca osiągnięta, nagroda wypłacona. */
    COMPLETED
}
