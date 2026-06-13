package com.example.techcorp.repository;

import com.example.techcorp.domain.GameState;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Production repository implementation using PostgreSQL database.
 * Handles game state persistence via JDBC.
 */
public class PostgresGameRepository implements GameRepository {

    // URL will be read from Render's Environment Variables during runtime
    private final String url = System.getenv("DB_URL");

    @Override
    public void save(GameState state) {
        if (url == null) {
            System.out.println("[POSTGRES] Error: DB_URL environment variable is not set!");
            return;
        }

        String sql = "INSERT INTO game_state (company_name, cash, loan_amount, current_turn, completed_projects) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, state.getCompanyName());
            pstmt.setDouble(2, state.getCash());
            pstmt.setDouble(3, state.getLoanAmount());
            pstmt.setInt(4, state.getCurrentTurn());
            pstmt.setInt(5, state.getCompletedProjects());

            pstmt.executeUpdate();
            System.out.println("[POSTGRES] Game state saved successfully to Render Database!");

        } catch (Exception e) {
            System.out.println("[POSTGRES] Database error during autosave: " + e.getMessage());
        }
    }

    @Override
    public GameState load() {
        return null;
    }
}