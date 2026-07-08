package com.nutricl;

import java.sql.*; // Pour JDBC [2]
import java.util.Scanner; // Pour l'IHM Console [3]

public class Main {
    public static void main(String[] args) {
        // Configuration PostgreSQL (Logique Données intégrée) [4]
        String url = "jdbc:postgresql://localhost:5432/gestion_produits;";
        String user = "postgres";
        String password = "27061710";

        // Établissement de la connexion [5]
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connecté à la base de données PostgreSQL.");

            Scanner scanner = new Scanner(System.in);
            boolean enCours = true;

            // Boucle de répétition pour le menu [6, 7]
            while (enCours) {
                // IHM Console : Affichage du menu
                System.out.println("\n--- GESTION DE PRODUITS (MONOLITHE) ---");
                System.out.println("1. Ajouter un produit");
                System.out.println("2. Afficher tous les produits");
                System.out.println("3. Quitter");
                System.out.print("Votre choix : ");

                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le saut de ligne

                // Structure de décision pour les actions [8]
                switch (choix) {
                    case 1:
                        // IHM : Saisie des données
                        System.out.print("Nom du produit : ");
                        String nom = scanner.nextLine();
                        System.out.print("Prix du produit : ");
                        double prix = scanner.nextDouble();

                        // --- LOGIQUE MÉTIER --- [9]
                        // Vérification simple : le prix ne doit pas être négatif
                        if (prix <= 0) {
                            System.out.println("ERREUR MÉTIER : Le prix doit être supérieur à zéro.");
                        } else {
                            // --- SQL : Insertion via JDBC --- [10]
                            String sqlInsert = "INSERT INTO produits (nom, prix) VALUES (?, ?)";
                            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                                pstmt.setString(1, nom);
                                pstmt.setDouble(2, prix);
                                pstmt.executeUpdate();
                                System.out.println("Produit ajouté avec succès.");
                            }
                        }
                        break;

                    case 2:
                        // --- SQL : Lecture via JDBC --- [11, 12]
                        String sqlSelect = "SELECT * FROM produits";
                        try (Statement stmt = conn.createStatement();
                                ResultSet rs = stmt.executeQuery(sqlSelect)) { // [13]

                            System.out.println("\n--- LISTE DES PRODUITS ---");
                            // Navigation dans le jeu de résultats [14]
                            while (rs.next()) {
                                // Extraction des données par nom de colonne [15]
                                int id = rs.getInt("id");
                                String n = rs.getString("nom");
                                double p = rs.getDouble("prix");
                                System.out.println("ID: " + id + " | Nom: " + n + " | Prix: " + p + "€");
                            }
                        }
                        break;

                    case 3:
                        enCours = false;
                        System.out.println("Fermeture de l'application.");
                        break;

                    default:
                        System.out.println("Choix invalide.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur de connexion SQL : " + e.getMessage());
        }
    }
}