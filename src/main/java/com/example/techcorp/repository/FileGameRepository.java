package com.example.techcorp.repository;

import com.example.techcorp.domain.GameState;
import com.example.techcorp.Company;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Implementacja plikowa repozytorium gry – ćwiczenie 1 z lekcji 9.
 * Zapisuje stan gry do pliku tekstowego savegame.txt.
 */
public class FileGameRepository implements GameRepository {

    private static final String SAVE_FILE = "savegame.txt";

    /**
     * Zapisuje stan gry do pliku tekstowego.
     * Ćwiczenie 1 z lekcji 9: SaveGameService rozszerzony o gotówkę i zadłużenie.
     */
    @Override
    public void save(GameState state) {
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            writer.write("company=" + state.getCompanyName() + "\n");
            writer.write("cash=" + state.getCash() + "\n");
            writer.write("loan=" + state.getLoanAmount() + "\n");
            writer.write("turn=" + state.getCurrentTurn() + "\n");
            writer.write("completedProjects=" + state.getCompletedProjects() + "\n");
            System.out.println("[SAVE] Game state saved to " + SAVE_FILE);
        } catch (IOException e) {
            System.out.println("[SAVE] Warning: could not save game state: " + e.getMessage());
        }
    }

    /**
     * Odczytuje ostatnio zapisany stan gry z pliku (do celów informacyjnych).
     */
    @Override
    public GameState load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String companyName = "TechCorp";
            double cash = 0;
            double loan = 0;
            int turn = 1;
            int completed = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length < 2) continue;
                switch (parts[0]) {
                    case "company"          -> companyName = parts[1];
                    case "cash"             -> cash = Double.parseDouble(parts[1]);
                    case "loan"             -> loan = Double.parseDouble(parts[1]);
                    case "turn"             -> turn = Integer.parseInt(parts[1]);
                    case "completedProjects"-> completed = Integer.parseInt(parts[1]);
                }
            }

            Company dummy = new Company(companyName, cash);
            return new GameState(dummy, turn, completed);

        } catch (IOException e) {
            System.out.println("[LOAD] No save file found.");
            return null;
        }
    }
}