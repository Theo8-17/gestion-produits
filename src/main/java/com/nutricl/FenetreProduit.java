package com.nutricl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FenetreProduit extends JFrame {
    // Composants de l'interface
    private JTextField txtNom = new JTextField(20);
    private JTextField txtPrix = new JTextField(20);
    private JButton btnAjouter = new JButton("Ajouter");
    private JButton btnLister = new JButton("Lister");
    private JTextArea areaAffichage = new JTextArea(10, 30);

    public FenetreProduit() {
        // Configuration de la fenêtre [3]
        setTitle("Gestion de Produits - Mission 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout()); // Gestionnaire de mise en page simple [4]

        // Ajout des composants [5]
        add(new JLabel("Nom du produit :"));
        add(txtNom);
        add(new JLabel("Prix :"));
        add(txtPrix);
        add(btnAjouter);
        add(btnLister);
        add(new JScrollPane(areaAffichage)); // Ajout de barres de défilement [6]

        // Gestion des événements (Listeners) [7]
        btnAjouter.addActionListener(e -> ajouterProduit());
        btnLister.addActionListener(e -> listerProduits());

        pack(); // Ajuste la taille automatiquement [8]
    }

    private void ajouterProduit() {
        String nom = txtNom.getText();
        double prix = Double.parseDouble(txtPrix.getText());

        // Logique Métier intégrée (validation simple) [1]
        if (prix <= 0) {
            JOptionPane.showMessageDialog(this, "Le prix doit être positif.");
            return;
        }

        // Connexion JDBC directe (Architecture 2-Tiers) [1]
        String url = "jdbc:postgresql://localhost:5432/gestion_produits;";
        try (Connection conn = DriverManager.getConnection(url, "postgres", "27061710")) {
            String sql = "INSERT INTO produits (nom, prix) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nom);
                pstmt.setDouble(2, prix);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès !");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void listerProduits() {
        areaAffichage.setText("");
        String url = "jdbc:postgresql://localhost:5432/gestion_produits;";
        try (Connection conn = DriverManager.getConnection(url, "postgres", "27061710")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM produits");
            while (rs.next()) {
                areaAffichage.append(rs.getString("nom") + " - " + rs.getDouble("prix") + "€\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}