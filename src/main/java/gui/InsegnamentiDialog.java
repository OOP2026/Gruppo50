package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("unused")

/**
 * Finestra di dialogo (GUI) dedicata alla visualizzazione e all'aggiunta di nuovi insegnamenti.
 * <p>
 * Questa schermata che può essere usata solo dal responsabile permette di vedere gli insegnamenti attivi
 * e la possibilità di registrarne di nuovi. L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Tabella Insegnamenti:</b> Una tabella che mostra l'elenco dei corsi (Nome, CFU, Anno corso, Email docente).</li>
 * <li><b>Nome Insegnamento:</b> Campo di testo per inserire il nome del corso.</li>
 * <li><b>CFU:</b> Campo di testo per inserire i crediti formativi.</li>
 * <li><b>Anno Corso:</b> Campo di testo per inserire l'anno di corso.</li>
 * <li><b>Email Docente:</b> Campo di testo per inserire l'email del professore.</li>
 * <li><b>Bottoni di Azione:</b> Aggiungi un nuovo insegnamento o chiudi la finestra.</li>
 * </ul>
 */
public class InsegnamentiDialog {
    JDialog dialog;
    private JPanel panel1;
    private JPanel sottoPannello2;
    private JPanel sottoPannello1;
    private JTable tabellaInsegnamenti;
    private JButton chiudiButton;
    private JLabel labelErrore;
    private JTextField nomeInsField;
    private JTextField cfuField;
    private JTextField annoField;
    private JTextField emailDocField;
    private JButton aggiungiButton;
private final Controller controller;
    private DefaultTableModel tableModel;

    public InsegnamentiDialog(Controller controller, JFrame frameChiamante) {

        dialog = new JDialog(frameChiamante, "Insegnamenti Attivi", true);
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(frameChiamante);
this.controller= controller;
caricaInsegnamenti();
        // Inizializza il DefaultTableModel e assegnalo alla JTable
        String[] colonne = {"Nome", "CFU", "Anno corso", "Email docente"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        // Assicura a SonarQube che il componente non sia null
        java.util.Objects.requireNonNull(tabellaInsegnamenti, "Errore GUI: tabellaInsegnamenti non inizializzata dal designer");

        tabellaInsegnamenti.setModel(tableModel);
        tabellaInsegnamenti.getTableHeader().setReorderingAllowed(false);
        tabellaInsegnamenti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Carica subito gli insegnamenti già presenti
        aggiornaTabella(controller);

        // ── Listener ────────────────────────────────────────────────────────
        chiudiButton.addActionListener(e -> dialog.dispose());

        aggiungiButton.addActionListener(e -> {
            labelErrore.setText("");

            String nome     = nomeInsField.getText().trim();
            String cfuTxt   = cfuField.getText().trim();
            String annoTxt  = annoField.getText().trim();
            String emailDoc = emailDocField.getText().trim();

            if (nome.isEmpty() || cfuTxt.isEmpty() || annoTxt.isEmpty() || emailDoc.isEmpty()) {
                labelErrore.setText("Compila tutti i campi.");
                return;
            }

            try {
                int cfu  = Integer.parseInt(cfuTxt);
                int anno = Integer.parseInt(annoTxt);

                String errore = controller.registraInsegnamento(nome, cfu, anno, emailDoc);
                if (errore != null) {
                    labelErrore.setText(errore);
                    aggiornaTabella(controller);
                    return;
                }

                aggiornaTabella(controller);
                emailDocField.setText("");
                cfuField.setText("");
                annoField.setText("");
                emailDocField.setText("");
                JOptionPane.showMessageDialog(dialog, "Insegnamento aggiunto con successo!");

            } catch (NumberFormatException ex) {
                labelErrore.setText("CFU e anno corso devono essere numeri interi.");
            }
        });


        if(tabellaInsegnamenti != null) {
            tabellaInsegnamenti.getSelectionModel().addListSelectionListener(e -> {
                int riga = tabellaInsegnamenti.getSelectedRow();
                if (riga == -1 || !e.getValueIsAdjusting()) return;
                String nomeInsegnamento = tabellaInsegnamenti.getValueAt(riga, 0).toString();
                String motivo = "Vuoi rimuovere l'insegnamento " + nomeInsegnamento + "? Se rimuovi l'insegnamento verrano rimosse anche le lezioni associate con questo insegnamento";
                JTextArea textArea = new JTextArea(motivo);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(120, 100));
                int risposta = JOptionPane.showConfirmDialog(dialog, scrollPane, "Rimozione Insegnamento", JOptionPane.YES_NO_OPTION);
                rimuoviInsegnamento(risposta, nomeInsegnamento);

             tabellaInsegnamenti.getSelectionModel().clearSelection();
            });
        }
    }

    public void rimuoviInsegnamento(int risposta,String nomeIns){
        if (risposta == JOptionPane.YES_OPTION) {
            String action = controller.removeInsegnamento(nomeIns);
            if (action != null) {
                JOptionPane.showMessageDialog(dialog, action, "Errore nella rimozione", JOptionPane.ERROR_MESSAGE);
            }
            aggiornaTabella(controller);
        }

    }

    private void aggiornaTabella(Controller controller) {
        tableModel.setRowCount(0);
        for (Object[] riga : controller.getInsegnamentiAttivi()) {
            tableModel.addRow(riga);
        }
    }

    private void caricaInsegnamenti(){
        String msg= controller.caricaInsegnamentiDaDB();
        if(msg!=null){
            JOptionPane.showMessageDialog(dialog, msg, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}


