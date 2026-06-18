package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class VisualizzaRichiestaDialog {
    JDialog dialog;

    private JPanel panel1;
    private JPanel PannelloRichieste;
    private JPanel PannelloGestione;
    private JTable table1;
    private JLabel labelTitolo;
    private JLabel labelDocente;
    private JLabel labelOrari;
    private JButton approvaButton;
    private JButton rifiutaButton;
    private JLabel labelMotivo;
    private JButton tornaIndietroButton;

    /* --- Stato interno --- */
    private DefaultTableModel tableModel;
    private int rigaSelezionata = -1;

    // ---------------------------------------------------------------

    public VisualizzaRichiestaDialog(Controller controller, JFrame frameChiamante) {
        dialog = new JDialog(frameChiamante, "Gestione Richieste di Spostamento", true);
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setMinimumSize(new java.awt.Dimension(700, 520));
        dialog.pack();
        dialog.setLocationRelativeTo(frameChiamante);

        inizializzaTabella();
        aggiornaTabella(controller);
        collegaListener(controller);
        if(tornaIndietroButton != null) {
            tornaIndietroButton.addActionListener(e -> {
                dialog.dispose();
            });
        }

        dialog.setVisible(true);
    }

    // ---------------------------------------------------------------
    // Inizializzazione tabella
    // ---------------------------------------------------------------

    private void inizializzaTabella() {
        String[] colonne = {
                "#", "Docente", "Orario attuale", "Nuovo orario", "Motivo", "Stato"
        };
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table1.setModel(tableModel);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Larghezze colonne
        table1.getColumnModel().getColumn(0).setMaxWidth(30);
        table1.getColumnModel().getColumn(1).setPreferredWidth(130);
        table1.getColumnModel().getColumn(2).setPreferredWidth(150);
        table1.getColumnModel().getColumn(3).setPreferredWidth(150);
        table1.getColumnModel().getColumn(4).setPreferredWidth(180);
        table1.getColumnModel().getColumn(5).setPreferredWidth(80);
    }

    // ---------------------------------------------------------------
    // Carica / aggiorna dati dalla lista richieste del controller
    // ---------------------------------------------------------------

    private void aggiornaTabella(Controller controller) {
        tableModel.setRowCount(0);
        Object[][] richieste = controller.getRichiesteSpostamento();
        for (Object[] riga : richieste) {
            tableModel.addRow(riga);
        }
        azzeraDettaglio();
        rigaSelezionata = -1;
        approvaButton.setEnabled(false);
        rifiutaButton.setEnabled(false);
    }

    // ---------------------------------------------------------------
    // Listener
    // ---------------------------------------------------------------

    private void collegaListener(Controller controller) {

        // Selezione riga → mostra dettaglio e abilita bottoni
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int riga = table1.getSelectedRow();
            if (riga < 0) {
                azzeraDettaglio();
                approvaButton.setEnabled(false);
                rifiutaButton.setEnabled(false);
                rigaSelezionata = -1;
                return;
            }
            rigaSelezionata = riga;
            mostraDettaglio(riga);

            // I bottoni sono abilitati solo se la richiesta è ancora IN_ATTESA
            String stato = tableModel.getValueAt(riga, 5).toString();
            boolean attesa = "IN_ATTESA".equalsIgnoreCase(stato);
            approvaButton.setEnabled(attesa);
            rifiutaButton.setEnabled(attesa);
        });

        // Approva
        approvaButton.addActionListener(e -> {
            if (rigaSelezionata < 0) return;
            int conferma = JOptionPane.showConfirmDialog(
                    dialog,
                    "Approvare la richiesta n° " + rigaSelezionata + "?",
                    "Conferma approvazione",
                    JOptionPane.YES_NO_OPTION
            );
            if (conferma != JOptionPane.YES_OPTION) return;

            String errore = controller.approvaRichiestaSpostamento(rigaSelezionata);
            if (errore != null) {
                JOptionPane.showMessageDialog(dialog, errore, "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Richiesta approvata con successo.");
            }
            aggiornaTabella(controller);
        });

        // Rifiuta
        rifiutaButton.addActionListener(e -> {
            if (rigaSelezionata < 0) return;
            int conferma = JOptionPane.showConfirmDialog(
                    dialog,
                    "Rifiutare la richiesta n° " + rigaSelezionata + "?",
                    "Conferma rifiuto",
                    JOptionPane.YES_NO_OPTION
            );
            if (conferma != JOptionPane.YES_OPTION) return;

            controller.rifiutaRichiestaSpostamento(rigaSelezionata);
            JOptionPane.showMessageDialog(dialog, "Richiesta rifiutata.");
            aggiornaTabella(controller);
        });
    }


    // ---------------------------------------------------------------
    // Helpers UI
    // ---------------------------------------------------------------

    /** Popola il pannello di dettaglio con i dati della riga selezionata. */
    private void mostraDettaglio(int riga) {
        labelDocente.setText(tableModel.getValueAt(riga, 1).toString());
        labelOrari.setText(
                tableModel.getValueAt(riga, 2) + "  →  " + tableModel.getValueAt(riga, 3)
        );
        labelMotivo.setText(tableModel.getValueAt(riga, 4).toString());
    }

    /** Svuota il pannello di dettaglio. */
    private void azzeraDettaglio() {
        if (labelDocente != null) labelDocente.setText("—");
        if (labelOrari   != null) labelOrari.setText("—");
        if (labelMotivo  != null) labelMotivo.setText("—");
    }


}

