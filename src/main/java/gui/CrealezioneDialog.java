package gui;
import controller.Controller;
import javax.swing.*;
import javax.swing.JComboBox;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;

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
    private JTextField nomeAulaField;
    private JTextField oraIField;
    private JTextField minIField;
    private JTextField oraFField;
    private JTextField minFField;
    final private JTextField textComboBox;
    final private Controller controller;

    public CrealezioneDialog(Controller controller, JFrame frameChiamante) {
        dialog = new JDialog(frameChiamante, "Crea Nuova Lezione", true);
        this.controller = controller;
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(frameChiamante);
        textComboBox=textFieldComboBox();
        caricaInsegnamentiBox("");
        caricaEvents();
        //Controllo che il bottone si crei correttamente.
        if(annullaButton != null) {
            annullaButton.addActionListener(e -> dialog.dispose());
        }
        if(confermaButton != null) {
            confermaButton.addActionListener(e -> {
                labelErrore.setText("");

                String nomeIns = Objects.requireNonNull(nomeInsField.getSelectedItem()).toString();
                String emailDoc = emailDocField.getText().trim();
                String nomeAula = nomeAulaField.getText().trim();
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
        nomeInsField.removeAllItems();
        List<String> data= controller.getInsegnamentiRegistrati(materia.toLowerCase());
        for(String insegnamento:data){
            nomeInsField.addItem(insegnamento);
        }

        nomeInsField.setMaximumRowCount(4);
        nomeInsField.setSelectedIndex(-1);
        nomeInsField.revalidate();
        nomeInsField.repaint();
        if(nomeInsField.isPopupVisible()){
            nomeInsField.setPopupVisible(false);
            System.out.println(nomeInsField.getItemCount());
            if(nomeInsField.getItemCount()>0)
                nomeInsField.setPopupVisible(true);
        }else{
        if(nomeInsField.getItemCount()>0 && !materia.equalsIgnoreCase(""))
            nomeInsField.setPopupVisible(true);
        }
        textComboBox.setText(materia);
    }
    public JTextField textFieldComboBox() {
        Component editor = nomeInsField.getEditor().getEditorComponent();

        if (editor instanceof JTextField) {
            return (JTextField) editor;
        }

        // Se non è un JTextField, interrompiamo con un messaggio d'errore chiaro
        // invece di passare un null che causerebbe danni più avanti.
        throw new IllegalStateException("L'editor della JComboBox 'nomeInsField' non è un JTextField. Assicurati che sia editabile.");
    }


    private void caricaEvents() {
            textComboBox.addKeyListener(new KeyAdapter(){
                @Override
                public void keyReleased(KeyEvent e){
                    String testoAttuale= textComboBox.getText();
                    caricaInsegnamentiBox(testoAttuale);
                }

            });


    }
}
