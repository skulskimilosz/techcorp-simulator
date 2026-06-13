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

        String sql = "INSERT INTO game_state (game_id, company_name, cash, loan_amount, current_turn, completed_projects) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, state.getGameId());
            pstmt.setString(2, state.getCompanyName());    
            pstmt.setDouble(3, state.getCash());
            pstmt.setDouble(4, state.getLoanAmount());
            pstmt.setInt(5, state.getCurrentTurn());
            pstmt.setInt(6, state.getCompletedProjects());

            pstmt.executeUpdate();
            System.out.println("[POSTGRES] Game state saved successfully for Game ID: " + state.getGameId());

        } catch (Exception e) {
            System.out.println("[POSTGRES] Database error during autosave: " + e.getMessage());
        }
    }

    @Override
    public GameState load() {
        return null;
    }
}