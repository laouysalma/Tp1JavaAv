/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpjdbc;

/**
 *
 * @author X1 YOGA
 */

import java.sql.*;

public class ExoJDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/atelier";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            Class.forName("com.mysql.jdbc.Driver");

            // Suppression/création de la table
            stmt.executeUpdate("DROP TABLE IF EXISTS DevData");
            stmt.executeUpdate(
                "CREATE TABLE DevData (" +
                "Developpeurs VARCHAR(32), " +
                "Jour CHAR(11), " +
                "NbScripts INTEGER)"
            );

            // Insertion des données
            stmt.executeUpdate("INSERT INTO DevData VALUES ('ALAMI', 'Lundi', 1)");
            stmt.executeUpdate("INSERT INTO DevData VALUES ('WAFI', 'Lundi', 2)");
            stmt.executeUpdate("INSERT INTO DevData VALUES ('SLAMI', 'Mardi', 9)");

            // a) Max scripts par jour
            ResultSet rs1 = stmt.executeQuery(
                "SELECT Developpeurs, Jour, MAX(NbScripts) FROM DevData GROUP BY Jour");
            System.out.println("Max scripts par jour :");
            while (rs1.next()) {
                System.out.println(rs1.getString(1) + " | " + rs1.getString(2) + " | " + rs1.getInt(3));
            }

            // b) Classement
            ResultSet rs2 = stmt.executeQuery(
                "SELECT Developpeurs, SUM(NbScripts) AS Total " +
                "FROM DevData GROUP BY Developpeurs ORDER BY Total DESC");
            System.out.println("\nClassement des développeurs :");
            while (rs2.next()) {
                System.out.println(rs2.getString(1) + " | " + rs2.getInt(2));
            }

            // c) Total semaine
            ResultSet rs3 = stmt.executeQuery("SELECT SUM(NbScripts) FROM DevData");
            if (rs3.next()) {
                System.out.println("\nTotal scripts semaine : " + rs3.getInt(1));
            }

            // d) Total pour un développeur donné
            PreparedStatement ps = conn.prepareStatement(
                "SELECT SUM(NbScripts) FROM DevData WHERE Developpeurs = ?");
            ps.setString(1, "ALAMI");
            ResultSet rs4 = ps.executeQuery();
            if (rs4.next()) {
                System.out.println("\nTotal scripts pour ALAMI : " + rs4.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}