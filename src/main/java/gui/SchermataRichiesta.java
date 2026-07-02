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
    private JComboBox giorniBox;
    private JTextField minutiIniziaNuovaText;
    private JTextField minutiFineNuovaText;
    private JTextField oraFineNuovaText;
    private JComboBox giorniNuoviBox;
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

        caricaEvents();
        creaTableRichiesta();
        impostaLimiteCaratteri(motivoText, 200);
    }
    //Controlla i campi
    private boolean checkCampi() {
        JTextField oraIniziaLezione = oraIniziaLezioneText;
        JTextField minutiIniziaLezione = minutiIniziaLezioneText;
        JTextField oraFineLezione = oraFineLezioneText;
        JTextField minutiFineLezione = minutiFineLezioneText;
        JTextField oraIniziaNuova = oraIniziaNuovaText;
        JTextField minutiIniziaNuova = minutiIniziaNuovaText;
        JTextField oraFineNuova = oraFineNuovaText;
        JTextField minutiFineNuova = minutiFineNuovaText;
        JTextArea motivo = motivoText;

        if (oraIniziaLezione == null || minutiIniziaLezione == null || oraFineLezione == null || minutiFineLezione == null
                || oraIniziaNuova == null || minutiIniziaNuova == null || oraFineNuova == null || minutiFineNuova == null
                || motivo == null) {
            return false;
        }

        if(oraIniziaLezione.getText().trim().isEmpty() || minutiIniziaLezione.getText().trim().isEmpty() ||
                oraFineLezione.getText().trim().isEmpty() || minutiFineLezione.getText().trim().isEmpty() ||
                oraIniziaNuova.getText().trim().isEmpty() || minutiIniziaNuova.getText().trim().isEmpty() ||
                oraFineNuova.getText().trim().isEmpty() || minutiFineNuova.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Tutti i campi degli orari devono essere compilati.", "Errore", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            int oraInizioLezione = Integer.parseInt(oraIniziaLezione.getText());
            int minutoInizioLezione = Integer.parseInt(minutiIniziaLezione.getText());
            int oraFineLezioneVal = Integer.parseInt(oraFineLezione.getText());
            int minutoFineLezione = Integer.parseInt(minutiFineLezione.getText());
            int oraInizioNuovo = Integer.parseInt(oraIniziaNuova.getText());
            int minutoInizioNuovo = Integer.parseInt(minutiIniziaNuova.getText());
            int oraFineNuovo = Integer.parseInt(oraFineNuova.getText());
            int minutoFineNuovo = Integer.parseInt(minutiFineNuova.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Inserisci valori numerici validi per orari e minuti.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (motivo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo motivo non può essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
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
            int oraInizioLezione = Integer.parseInt(oraIniziaLezioneText.getText());
            int minutoInizioLezione = Integer.parseInt(minutiIniziaLezioneText.getText());
            int oraFineLezione = Integer.parseInt(oraFineLezioneText.getText());
            int minutoFineLezione = Integer.parseInt(minutiFineLezioneText.getText());
            //Lezione proposta
            int oraInizioNuovo = Integer.parseInt(oraIniziaNuovaText.getText());
            int minutoInizioNuovo = Integer.parseInt(minutiIniziaNuovaText.getText());
            int oraFineNuovo = Integer.parseInt(oraFineNuovaText.getText());
            int minutoFineNuovo = Integer.parseInt(minutiFineNuovaText.getText());
            String giornoLezione = giorniBox.getSelectedItem().toString();
            String giornoNuovo = giorniNuoviBox.getSelectedItem().toString();
            String motivo = motivoText.getText();
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
        //permette di aprire un pop up per vedere il testo nella colonna motivo
        table.getSelectionModel().addListSelectionListener(e->{
            int riga= table.getSelectedRow();
            if(riga==-1|| !e.getValueIsAdjusting()) return;
            String motivo= table.getValueAt(riga,2).toString();
            JTextArea textArea = new JTextArea(motivo);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            //textArea.setBackground(new Color(255, 255, 255, 0) );
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(120, 100));
            JOptionPane.showMessageDialog(frame,scrollPane,"Motivo della richiesta: ",JOptionPane.INFORMATION_MESSAGE);
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
