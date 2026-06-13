package com.example.techcorp.service;

import com.example.techcorp.domain.GameState;
import com.example.techcorp.repository.GameRepository;

/**
 * Klasa odpowiedzialna za automatyczny zapis gry – ćwiczenie 5 z lekcji 9.
 * GameEngine nie zapisuje bezpośrednio – deleguje to do SaveManager (SRP).
 */
public class SaveManager {

    private final GameRepository repository;

    public SaveManager(GameRepository repository) {
        this.repository = repository;
    }

    public void autosave(GameState state) {
        repository.save(state);
    }
}