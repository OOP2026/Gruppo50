package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import controller.Controller;

import java.awt.*;

public class SchermataRichiesta {
    JFrame frame;
    private JTextField oraIniziaLezioneText;
    private JTextField minutiIniziaLezioneText;
    private JTextField oraFineLezioneText;
    private JTextField minutiFineLezioneText;
    private JComboBox<String> giorniBox;
    private JTextField minutiIniziaNuovaText;
    private JTextField minutiFineNuovaText;
    private JTextField oraFineNuovaText;
    private JComboBox<String> giorniNuoviBox;
    private JPanel panel1;
    private JPanel panelRichiesta;
    private JPanel panelLezioneDaSpostare;
    private JPanel panelLezioneProposta;
    private JPanel panelButtons;
    private JPanel panelMotivo;
    private JButton inviaButton;
    private JButton indietroButton;
    private JTextArea motivoText;
    private JTextField oraIniziaNuovaText;
    private JScrollPane Scroller;
    private JTable table;
    JFrame frameChiamante;
    Controller controller;

    public SchermataRichiesta(Controller c, JFrame f) {
        frameChiamante = f;
        controller = c;
        frame = new JFrame("Schermata Richiesta");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        //Controllo che il pannello per scrollare le richieste si generi corettamente
        JScrollPane scroller = Scroller;
        if (scroller != null) {
            scroller.setPreferredSize(new Dimension(1000, -1));
        }

        // Diciamo a SonarQube in modo esplicito che i componenti esistono
        java.util.Objects.requireNonNull(Scroller, "Lo Scroller deve essere inizializzato dal designer").setPreferredSize(new Dimension(1000, -1));
        java.util.Objects.requireNonNull(table, "La tabella deve essere inizializzata dal designer");

        caricaEvents();
        creaTableRichiesta();
        impostaLimiteCaratteri(motivoText, 200);
    }
    //Controlla i campi
    private boolean checkCampi() {
        // 1. Controllo che i componenti della GUI siano stati inizializzati
        if (oraIniziaLezioneText == null || minutiIniziaLezioneText == null || oraFineLezioneText == null || minutiFineLezioneText == null
                || oraIniziaNuovaText == null || minutiIniziaNuovaText == null || oraFineNuovaText == null || minutiFineNuovaText == null
                || motivoText == null || giorniBox == null || giorniNuoviBox == null) {
            return false;
        }

        // 2. Controllo sulle ComboBox (FONDAMENTALE per evitare NPE in caricaEvents)
        if (giorniBox.getSelectedItem() == null || giorniNuoviBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frame, "Seleziona i giorni per entrambe le lezioni.", "Errore", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 3. Estrazione sicura dei testi per evitare chiamate a catena pericolose
        String oraInizio = oraIniziaLezioneText.getText();
        String minInizio = minutiIniziaLezioneText.getText();
        String oraFine = oraFineLezioneText.getText();
        String minFine = minutiFineLezioneText.getText();
        String oraInizioN = oraIniziaNuovaText.getText();
        String minInizioN = minutiIniziaNuovaText.getText();
        String oraFineN = oraFineNuovaText.getText();
        String minFineN = minutiFineNuovaText.getText();
        String motivo = motivoText.getText();

        // 4. Controllo campi vuoti (con protezione da eventuali stringhe null)
        if (oraInizio == null || oraInizio.trim().isEmpty() ||
                minInizio == null || minInizio.trim().isEmpty() ||
                oraFine == null || oraFine.trim().isEmpty() ||
                minFine == null || minFine.trim().isEmpty() ||
                oraInizioN == null || oraInizioN.trim().isEmpty() ||
                minInizioN == null || minInizioN.trim().isEmpty() ||
                oraFineN == null || oraFineN.trim().isEmpty() ||
                minFineN == null || minFineN.trim().isEmpty()) {

            JOptionPane.showMessageDialog(frame, "Tutti i campi degli orari devono essere compilati.", "Errore", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (motivo == null || motivo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo motivo non può essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 5. Controllo del formato numerico
        try {
            // Applichiamo il trim() anche qui per evitare eccezioni causate da spazi accidentali (" 12 ")
            Integer.parseInt(oraInizio.trim());
            Integer.parseInt(minInizio.trim());
            Integer.parseInt(oraFine.trim());
            Integer.parseInt(minFine.trim());
            Integer.parseInt(oraInizioN.trim());
            Integer.parseInt(minInizioN.trim());
            Integer.parseInt(oraFineN.trim());
            Integer.parseInt(minFineN.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Inserisci valori numerici validi per orari e minuti.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    /// Carica tutti Action Listener
    private void caricaEvents() {
        indietroButton.addActionListener(e -> {
            frame.setVisible(false);
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        inviaButton.addActionListener(e -> {
            if(!checkCampi()) return;

            // 1. Estraiamo gli oggetti selezionati e facciamo un controllo locale
            // che SonarQube può vedere chiaramente in questo specifico blocco
            Object selGiorno = giorniBox.getSelectedItem();
            Object selGiornoNuovo = giorniNuoviBox.getSelectedItem();

            if (selGiorno == null || selGiornoNuovo == null) {
                return; // Sicurezza extra per SonarQube
            }

            String giornoLezione = selGiorno.toString();
            String giornoNuovo = selGiornoNuovo.toString();

            // 2. Usiamo String.valueOf per il getText() (che protegge da eventuali null interni di Swing)
            int oraInizioLezione = Integer.parseInt(String.valueOf(oraIniziaLezioneText.getText()).trim());
            int minutoInizioLezione = Integer.parseInt(String.valueOf(minutiIniziaLezioneText.getText()).trim());
            int oraFineLezione = Integer.parseInt(String.valueOf(oraFineLezioneText.getText()).trim());
            int minutoFineLezione = Integer.parseInt(String.valueOf(minutiFineLezioneText.getText()).trim());

            int oraInizioNuovo = Integer.parseInt(String.valueOf(oraIniziaNuovaText.getText()).trim());
            int minutoInizioNuovo = Integer.parseInt(String.valueOf(minutiIniziaNuovaText.getText()).trim());
            int oraFineNuovo = Integer.parseInt(String.valueOf(oraFineNuovaText.getText()).trim());
            int minutoFineNuovo = Integer.parseInt(String.valueOf(minutiFineNuovaText.getText()).trim());

            String motivo = String.valueOf(motivoText.getText());

            try {
                controller.richiestaspostamentoLezione(motivo, giornoLezione,
                        oraInizioLezione, minutoInizioLezione, oraFineLezione, minutoFineLezione,
                        giornoNuovo, oraInizioNuovo, minutoInizioNuovo, oraFineNuovo, minutoFineNuovo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            resetCampi();
            creaTableRichiesta();
            JOptionPane.showMessageDialog(frame, "Richiesta inviata con successo!");
        });

        // 3. QUESTO È IL BLOCCO CHE MANCAVA E CHE RISOLVE L'ERRORE SULLA TEXTAREA
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int riga = table.getSelectedRow();
            if (riga == -1) return;

            // Estrazione sicura che previene il NullPointerException
            Object valoreCella = table.getValueAt(riga, 2);
            String motivo = (valoreCella != null) ? valoreCella.toString() : "Nessun motivo specificato";

            JTextArea textArea = new JTextArea(motivo);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(120, 100));
            JOptionPane.showMessageDialog(frame, scrollPane, "Motivo della richiesta: ", JOptionPane.INFORMATION_MESSAGE);

            table.getSelectionModel().clearSelection();
        });
    }
///Reseta i campi
    private void resetCampi() {
        oraIniziaLezioneText.setText("");
        minutiIniziaLezioneText.setText("");
        oraFineLezioneText.setText("");
        minutiFineLezioneText.setText("");
        oraIniziaNuovaText.setText("");
        minutiIniziaNuovaText.setText("");
        oraFineNuovaText.setText("");
        minutiFineNuovaText.setText("");
        motivoText.setText("");
        giorniBox.setSelectedIndex(0);
        giorniNuoviBox.setSelectedIndex(0);
    }

  ///Imposta il limite massimo di caratteri che si possono scrivere nel panel motivo
    private void impostaLimiteCaratteri(JTextArea areaText,int limiteCaratteri) {
        //ottiene il documento di motivoText per applicare il filtro
        AbstractDocument doc = (AbstractDocument) areaText.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                //se il testo è null, consente la sostituzione senza restrizioni
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                int lunghezzaAttuale = fb.getDocument().getLength();
                int lunghezzafutura = lunghezzaAttuale - length + text.length();
                //se la lunghezza futura è entro il limite, consente la sostituzione completa
                if (lunghezzafutura <= limiteCaratteri) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    //calcola lo spazio disponibile e consente solo la parte del testo che rientra nel limite
                    int spazioDisponibile = limiteCaratteri - (lunghezzaAttuale - length);
                    if (spazioDisponibile > 0) {
                        super.replace(fb, offset, length, text.substring(0, spazioDisponibile), attrs);

                    }
                    Toolkit.getDefaultToolkit().beep();
                }
            }

        });
    }
///Genera le colonne della tabella e la riempie con i dati
    private void creaTableRichiesta(){
        Object[][] data=controller.ottieniRichiesteInviate();
        table.setModel(new DefaultTableModel(data,
                new String[]{"Orario Lezione","Orario Nuovo","Motivo","Stato"} ) );
        //non rende editable la tabella
        table.setDefaultEditor(Object.class, null);
        table.setSelectionBackground(Color.LIGHT_GRAY);
    }

}
