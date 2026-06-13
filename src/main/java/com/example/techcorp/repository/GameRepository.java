package com.example.techcorp.repository;

import com.example.techcorp.domain.GameState;

/**
 * Interfejs warstwy persystencji – wzorzec Repository z lekcji 9.
 * Nie określa JAK dane są przechowywane, tylko CO można z nimi zrobić.
 */
public interface GameRepository {
    void save(GameState state);
    GameState load();
}