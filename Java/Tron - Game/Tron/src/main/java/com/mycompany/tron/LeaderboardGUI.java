package com.mycompany.tron;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class LeaderboardGUI {

    private int MAX_ENTRIES = 10;

    /**
     * Mutatja a rangsort, a jelenlegi játékosokkal és a pontjaikkal
     *
     * @param playerName Name of the player to add/update (null if just showing)
     * @param playerScore Score of the player
     */
    public void showLeaderboard() {
        JFrame frame = new JFrame("Leaderboard");
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        try {
            String url = "jdbc:derby:PALLA;create=true";
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate(
                        "CREATE TABLE HIGHSCORES ("
                        + "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + "NAME VARCHAR(50), SCORE INT)"
                );
            } catch (SQLException ignored) {
            }
            stmt.executeUpdate(
                    "DELETE FROM HIGHSCORES WHERE ID NOT IN ("
                    + "SELECT ID FROM HIGHSCORES ORDER BY SCORE DESC FETCH FIRST " + MAX_ENTRIES + " ROWS ONLY)"
            );
            ResultSet rs = stmt.executeQuery(
                    "SELECT NAME, SCORE FROM HIGHSCORES ORDER BY SCORE DESC FETCH FIRST " + MAX_ENTRIES + " ROWS ONLY"
            );
            int rank = 1;
            while (rs.next()) {
                JLabel label = new JLabel(rank + ". " + rs.getString("NAME") + " : " + rs.getInt("SCORE"));
                label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                frame.add(label);
                rank++;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
        frame.setVisible(true);
    }
    /**
     * Visszaadja egy játékosnak a pontszámát
     * @param playerName
     * @return 
     */
    public int getPlayerScore(String playerName) {
        if ("Player 1".equals(playerName) || "Player 2".equals(playerName) || playerName.isEmpty()) {
            return 0;
        }
        int score = 0;
        try {
            String url = "jdbc:derby:PALLA;create=true";
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate(
                        "CREATE TABLE HIGHSCORES ("
                        + "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + "NAME VARCHAR(50), SCORE INT)"
                );
            } catch (SQLException ignored) {
            }
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT SCORE FROM HIGHSCORES WHERE NAME = ?"
            );
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                score = rs.getInt("SCORE");
            }
            rs.close();
            ps.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }
    /**
     * Frissíti a rangsort egy játékossal és egy megadott pontszámmal
     * @param playerName
     * @param playerScore 
     */
    public void updateLeaderboard(String playerName, int playerScore) {
        playerName = playerName.trim();
        if ("Player 1".equals(playerName) || "Player 2".equals(playerName) || playerName.isEmpty()) {
            return;
        }

        try {
            String url = "jdbc:derby:PALLA;create=true";
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate(
                        "CREATE TABLE HIGHSCORES ("
                        + "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + "NAME VARCHAR(50), SCORE INT)"
                );
            } catch (SQLException ignored) {
            }
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT SCORE FROM HIGHSCORES WHERE NAME = ?"
            );
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int existingScore = rs.getInt("SCORE");
                if (playerScore > existingScore) {
                    PreparedStatement update = conn.prepareStatement(
                            "UPDATE HIGHSCORES SET SCORE = ? WHERE NAME = ?"
                    );
                    update.setInt(1, playerScore);
                    update.setString(2, playerName);
                    update.executeUpdate();
                    update.close();
                }
            } else {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO HIGHSCORES (NAME, SCORE) VALUES (?, ?)"
                );
                insert.setString(1, playerName);
                insert.setInt(2, playerScore);
                insert.executeUpdate();
                insert.close();
            }
            rs.close();
            ps.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
