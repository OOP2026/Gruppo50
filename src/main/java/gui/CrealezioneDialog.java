package gui;
import controller.Controller;
import javax.swing.*;
import javax.swing.JComboBox;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class CrealezioneDialog {
    JDialog dialog;
    private JPanel panel1;
    private JButton annullaButton;
    private JButton confermaButton;
    private JComboBox<String> giornoCombo;
    private JComboBox<String> nomeInsField;
    private JLabel labelErrore;
    private JTextField cfuField;
    private JTextField annoField;
    private JTextField emailDocField;
    private JComboBox<String> nomeAulaField;
    private JTextField oraIField;
    private JTextField minIField;
    private JTextField oraFField;
    private JTextField minFField;
    private final  JTextField textComboBoxIns;
    private final JTextField textComboBoxAula;
    private final  Controller controller;
    private static final Logger logger = Logger.getLogger(CrealezioneDialog.class.getName());

    public CrealezioneDialog(Controller controller, JFrame frameChiamante) {
        dialog = new JDialog(frameChiamante, "Crea Nuova Lezione", true);
        this.controller = controller;
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();

        //verifico che il frame chiamante non sia null
        if (frameChiamante != null) {
            dialog.setLocationRelativeTo(frameChiamante);
        }

        textComboBoxIns=  textFieldComboBoxIns();
        textComboBoxAula=textFieldComboBoxAula();
        caricaInsegnamentiBox("");
        caricaAuleBox("");
        caricaEvents();
        //Controllo che il bottone si crei correttamente.
        JButton annulla = annullaButton;
        if (annulla != null) {
            annulla.addActionListener(e -> dialog.dispose());
        }
        JButton conferma = confermaButton;
        if(conferma != null) {
            conferma.addActionListener(e -> {
                labelErrore.setText("");

                String nomeIns = Objects.requireNonNull(nomeInsField.getSelectedItem()).toString();
                String emailDoc = emailDocField.getText().trim();
                String nomeAula = Objects.requireNonNull(nomeAulaField.getSelectedItem()).toString();
                String giorno = (String) giornoCombo.getSelectedItem();

                if (nomeIns.isEmpty() || emailDoc.isEmpty() || nomeAula.isEmpty()
                        || cfuField.getText().trim().isEmpty()
                        || annoField.getText().trim().isEmpty()
                        || oraIField.getText().trim().isEmpty()
                        || minIField.getText().trim().isEmpty()
                        || oraFField.getText().trim().isEmpty()
                        || minFField.getText().trim().isEmpty()) {
                    labelErrore.setText("Compila tutti i campi.");
                    return;
                }

                try {
                    int cfu = Integer.parseInt(cfuField.getText().trim());
                    int anno = Integer.parseInt(annoField.getText().trim());
                    int oraI = Integer.parseInt(oraIField.getText().trim());
                    int minI = Integer.parseInt(minIField.getText().trim());
                    int oraF = Integer.parseInt(oraFField.getText().trim());
                    int minF = Integer.parseInt(minFField.getText().trim());

                    String errore = controller.creaLezione(
                            nomeIns, cfu, anno,
                            emailDoc,
                            nomeAula,
                            giorno, oraI, minI, oraF, minF);

                    if (errore != null) {
                        labelErrore.setText(errore);
                        return;
                    }

                    JOptionPane.showMessageDialog(dialog, "Lezione creata con successo!");
                    dialog.dispose();

                } catch (NumberFormatException ex) {
                    labelErrore.setText("CFU, anno, capienza e orari devono essere numeri interi.");
                }
            });
        }
    }

    private void caricaInsegnamentiBox(String materia){
        //Rimuove tutti gli items precedenti
        nomeInsField.removeAllItems();
        //richiede gli insegnamenti in base alla string materia
        List<String> data= controller.getInsegnamentiRegistrati(materia.toLowerCase());
        for(String insegnamento:data){
            nomeInsField.addItem(insegnamento);
        }

        nomeInsField.setMaximumRowCount(4);
        nomeInsField.setSelectedIndex(-1);
        // revalidate e repaint servono per ricalibrare la JComboBox dopo aver rimosso e aggiunto items
        nomeInsField.revalidate();
        nomeInsField.repaint();
        //Questo if ha lo scopo di chiudere il popup della JComboBox se è aperto
        // e poi riaprirlo solo se ci sono items da mostrare. Questo evita che il popup rimanga aperto con una lista vuota.
            if(nomeInsField.isPopupVisible()){
            nomeInsField.setPopupVisible(false);
            if(nomeInsField.getItemCount()>0)
                nomeInsField.setPopupVisible(true);
        }else if(nomeInsField.getItemCount()>0 && !materia.equalsIgnoreCase(""))
        {
            nomeInsField.setPopupVisible(true);
        }
        //rimette il testo scritto dall'utente nella textfield della combobox
        textComboBoxIns.setText(materia);
    }
    /**
     * Restituisce la JTextField dell'editor della JComboBox 'nomeInsField'.
     * @throws IllegalStateException se l'editor non è un JTextField, indicando che la JComboBox non è editabile.
     */
    public JTextField textFieldComboBoxIns() {
        if(nomeInsField == null) {
            throw new NullPointerException("nomeInsField è null");
        }
        Component editor = nomeInsField.getEditor().getEditorComponent();



        if (editor instanceof JTextField) {
            return (JTextField) editor;
        }
        // Se non è un JTextField, interrompiamo con un messaggio d'errore chiaro
        // invece di passare un null che causerebbe danni più avanti.
        throw new IllegalStateException("L'editor della JComboBox 'nomeInsField' non è un JTextField. Assicurati che sia editabile.");
    }
    private void caricaAuleBox(String nomeAula){
        //rimuove tutti gli items precedenti
        nomeAulaField.removeAllItems();
        //richiede le aule in base alla stringa nomeAula
        List<String> data= controller.getAule(nomeAula.toLowerCase());
        for(String aula:data){
            nomeAulaField.addItem(aula);
        }

        nomeAulaField.setMaximumRowCount(4);
        nomeAulaField.setSelectedIndex(-1);
        //revalidate e repaint servono per ricalibrare la JComboBox dopo aver rimosso e aggiunto items
        nomeAulaField.revalidate();
        nomeAulaField.repaint();
        //Questo if ha lo scopo di chiudere il popup della JComboBox se è aperto
        // e poi riaprirlo solo se ci sono items da mostrare.
        // Questo evita che il popup rimanga aperto con una lista vuota.
        if(nomeAulaField.isPopupVisible()){
            nomeAulaField.setPopupVisible(false);

            if(nomeAulaField.getItemCount()>0)
                nomeAulaField.setPopupVisible(true);
        }else{
            if(nomeAulaField.getItemCount()>0 && !nomeAula.equalsIgnoreCase(""))
                nomeAulaField.setPopupVisible(true);
        }
        //Rimette il testo digitato dall'utente nella textfield della ComboBox
        textComboBoxAula.setText(nomeAula);
    }
    /**
     * Restituisce la JTextField dell'editor della JComboBox 'nomeAulaField'.
     * @throws IllegalStateException se l'editor non è un JTextField, indicando che la JComboBox non è editabile.
     */
    public JTextField textFieldComboBoxAula() {
        if(nomeAulaField == null) {
            throw new NullPointerException("nomeAulaField è null");
        }
        Component editor = nomeAulaField.getEditor().getEditorComponent();

        if (editor instanceof JTextField) {
            return (JTextField) editor;
        }
        // Se non è un JTextField, interrompiamo con un messaggio d'errore chiaro
        // invece di passare un null che causerebbe danni più avanti.
        throw new IllegalStateException("L'editor della JComboBox 'nomeAulaField' non è un JTextField. Assicurati che sia editabile.");

    }


    private void caricaEvents() {
            textComboBoxIns.addKeyListener(new KeyAdapter(){
                @Override
                public void keyReleased(KeyEvent e){
                    String testoAttuale= textComboBoxIns.getText();
                    caricaInsegnamentiBox(testoAttuale);
                }

            });
            textComboBoxAula.addKeyListener(new KeyAdapter(){
                @Override
                public void keyReleased(KeyEvent e){
                    String testoAttuale= textComboBoxAula.getText();
                    caricaAuleBox(testoAttuale);
                }

            });


    }
}
