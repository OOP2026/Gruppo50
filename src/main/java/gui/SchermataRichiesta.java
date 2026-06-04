package gui;

import javax.swing.*;
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
        caricaEvents();
        impostaLimiteCaratteri(motivoText, 200);
    }

    private void caricaEvents() {
        indietroButton.addActionListener(e -> {
            frame.setVisible(false);
            frameChiamante.setVisible(true);
            frame.dispose();

        });
        inviaButton.addActionListener(e -> {
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
            JOptionPane.showMessageDialog(frame, "Richiesta inviata con successo!");
        });
    }

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

    //Fare il codice per limitare il motivo a 200 caratteri
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

}
