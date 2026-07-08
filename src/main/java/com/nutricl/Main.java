package com.nutricl;

import java.sql.*; // Pour JDBC [2]
import java.util.Scanner; // Pour l'IHM Console [3]

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Lancement de l'IHM Swing dans le thread de l'interface graphique [12]
        SwingUtilities.invokeLater(() -> {
            new FenetreProduit().setVisible(true);
        });
    }
}