package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 * Finestra di dialogo (GUI) dedicata alle richieste di spostamento.
 * <p>
 * Questa schermata ai Responsabili fa da pannello di controllo per esaminare
 * e le proposte inoltrate dai docenti.
 * L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Tabella Richieste:</b> Mostra l'elenco completo di tutte le richieste, includendo Docente, Orario attuale, Nuovo orario, Motivo e Stato.</li>
 * <li><b>Pannello Gestione:</b> Un'area che mostra i dettagli della richiesta attualmente selezionata.</li>
 * <li><b>Bottoni di Azione (Approva/Rifiuta):</b> Permettono di validare o respingere la richiesta.</li>
 * <li><b>Bottone Modifica:</b> Consente al responsabile di proporre/forzare un orario alternativo rispetto a quello richiesto dal docente.</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class VisualizzaRichiestaDialog {
    JDialog dialog;

    private JPanel panel1;
    private JPanel pannelloRichieste;
    private JPanel pannelloGestione;
    private JTable table1;
    private JLabel labelTitolo;
    private JLabel labelDocente;
    private JLabel labelOrari;
    private JButton approvaButton;
    private JButton rifiutaButton;
    /**Bottone che permette di modificare l'orario della richiesta selezionata.*/
    private JButton modificaOrarioButton;
    private JLabel labelMotivo;
    private JButton tornaIndietroButton;


    /* --- Stato interno --- */
    private DefaultTableModel tableModel;
    private int rigaSelezionata = -1;

    // ---------------------------------------------------------------

    /**
     * Costruisce la dialog per la gestione delle richieste.
     * Inizializza la tabella, imposta i listener e prepara i controlli UI.
     * @param controller controller dell'applicazione
     * @param frameChiamante frame genitore per il posizionamento della dialog
     */
    public VisualizzaRichiestaDialog(Controller controller, JFrame frameChiamante) {
        dialog = new JDialog(frameChiamante, "Gestione Richieste di Spostamento", true);
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setMinimumSize(new java.awt.Dimension(700, 520));
        dialog.pack();
        dialog.setLocationRelativeTo(frameChiamante);

        inizializzaTabella();
        aggiornaTabella(controller);
        collegaListener(controller);
        if(tornaIndietroButton != null) {
            tornaIndietroButton.addActionListener(e ->
                dialog.dispose() );
        }

    }

    // ---------------------------------------------------------------
    // Inizializzazione tabella
    // ---------------------------------------------------------------

    /**
     * Inizializza il modello della tabella delle richieste e configura le colonne.
     * Metodo chiamato all'avvio della dialog per predisporre la view.
     */
    private void inizializzaTabella() {
        String[] colonne = {
                "#", "Docente", "Orario attuale", "Nuovo orario", "Motivo", "Stato"
        };
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        if(table1!=null) {
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
        }

    // ---------------------------------------------------------------
    // Carica / aggiorna dati dalla lista richieste del controller
    // ---------------------------------------------------------------

    /**
     * Ricarica i dati della tabella prendendoli dal {@code controller} e resetta
     * lo stato dei pannelli di dettaglio e dei bottoni.
     * @param controller il controller da cui leggere i dati
     */
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
        modificaOrarioButton.setEnabled(false);
    }

    // ---------------------------------------------------------------
    // Listener
    // ---------------------------------------------------------------
    /**
     * Collega i listener UI agli elementi della dialog.
     * @param controller il controller usato nei callback
     */
    private void collegaListener(Controller controller) {
        // Ora il metodo fa solo ciò che dice il suo nome: collega i listener
        table1.getSelectionModel().addListSelectionListener(this::gestisciSelezioneTabella);
        approvaButton.addActionListener(e -> gestisciApprovazione(controller));
        rifiutaButton.addActionListener(e -> gestisciRifiuto(controller));
        modificaOrarioButton.addActionListener(e -> gestisciModificaOrario(controller));
    }
    /**
     * Gestisce la selezione di una riga nella tabella delle richieste.
     * Mostra i dettagli della richiesta selezionata e abilita/disabilita
     * i bottoni in base allo stato della richiesta.
     * @param e evento di selezione della tabella
     */
    private void gestisciSelezioneTabella(javax.swing.event.ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;

        int riga = table1.getSelectedRow();

        if (riga < 0) {
            azzeraDettaglio();
            impostaStatoBottoni(false);
            rigaSelezionata = -1;
            return;
        }

        rigaSelezionata = riga;
        mostraDettaglio(riga);

        // I bottoni sono abilitati solo se la richiesta è ancora IN_ATTESA
        String stato = tableModel.getValueAt(riga, 5).toString();
        boolean attesa = "IN_ATTESA".equalsIgnoreCase(stato);
        impostaStatoBottoni(attesa);
    }

    // Un piccolo metodo di supporto per evitare di ripetere l'abilitazione/disabilitazione
    /**
     * Abilita o disabilita i bottoni di azione (approva, rifiuta, modifica).
     * @param abilitati {@code true} per abilitare, {@code false} per disabilitare
     */
    private void impostaStatoBottoni(boolean abilitati) {
        approvaButton.setEnabled(abilitati);
        rifiutaButton.setEnabled(abilitati);
        modificaOrarioButton.setEnabled(abilitati);
    }

    /**
     * Esegue il flusso di approvazione per la richiesta attualmente selezionata:
     * chiede conferma, invoca il controller e aggiorna la tabella.
     * @param controller controller che esegue l'operazione di approvazione
     */
    private void gestisciApprovazione(Controller controller) {
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
            dialogErrore(errore);
        } else {
            JOptionPane.showMessageDialog(dialog, "Richiesta approvata con successo.");
        }
        aggiornaTabella(controller);
    }

    /**
     * Esegue il flusso di rifiuto per la richiesta attualmente selezionata:
     * chiede conferma, invoca il controller e aggiorna la tabella.
     * @param controller controller che esegue l'operazione di rifiuto
     */
    private void gestisciRifiuto(Controller controller) {
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
    }

    /**
     * Richiede all'utente i nuovi valori d'orario e invoca il controller per
     * aggiornare la richiesta selezionata. Gestisce la validazione dei numeri
     * e segnala eventuali errori in una finestra di dialogo.
     * @param controller controller che esegue l'aggiornamento
     */
    private void gestisciModificaOrario(Controller controller) {
        if (rigaSelezionata < 0) return;

        String giorno = JOptionPane.showInputDialog(dialog, "Nuovo giorno (es. Lunedì):");
        if (giorno == null) return; // Previene un'eccezione se l'utente clicca 'Annulla'

        try {
            int oraInizio    = Integer.parseInt(JOptionPane.showInputDialog(dialog, "Ora inizio (08-17):"));
            int minutoInizio = Integer.parseInt(JOptionPane.showInputDialog(dialog, "Minuto inizio:"));
            int oraFine      = Integer.parseInt(JOptionPane.showInputDialog(dialog, "Ora fine (8-17):"));
            int minutoFine   = Integer.parseInt(JOptionPane.showInputDialog(dialog, "Minuto fine:"));

            String errore = controller.modificaOrarioRichiesta(rigaSelezionata, giorno, oraInizio, minutoInizio, oraFine, minutoFine);

            if (errore != null) {
                dialogErrore(errore);
            } else {
                JOptionPane.showMessageDialog(dialog, "Orario della richiesta modificato.");
            }
            aggiornaTabella(controller);

        } catch (NumberFormatException ex) {
            dialogErrore("Orari e minuti devono essere numeri interi.");
        }
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
    /**
     * Mostra un semplice dialog di errore con il messaggio fornito.
     * @param e il messaggio di errore da visualizzare
     */
    private void dialogErrore(String e){
        JOptionPane.showMessageDialog(dialog, e, "Errore", JOptionPane.ERROR_MESSAGE);
    }

}

