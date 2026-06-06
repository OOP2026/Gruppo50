package gui;
import controller.Controller;
import javax.swing.*;

public class CrealezioneDialog {
    JDialog dialog;
    private JPanel panel1;
    private JButton annullaButton;
    private JButton confermaButton;
    private JComboBox giornoCombo;
    private JTextField nomeInsField;
    private JLabel labelErrore;
    private JTextField cfuField;
    private JTextField annoField;
    private JTextField emailDocField;
    private JTextField nomeAulaField;
    private JTextField capienzaField;
    private JTextField oraIField;
    private JTextField minIField;
    private JTextField oraFField;
    private JTextField minFField;

    public CrealezioneDialog(Controller controller, JFrame frameChiamante) {
        dialog = new JDialog(frameChiamante, "Crea Nuova Lezione", true);
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(frameChiamante);
        if(annullaButton != null) dialog.setVisible(false);
        annullaButton.addActionListener(e -> dialog.dispose());

        confermaButton.addActionListener(e -> {
            labelErrore.setText("");

            String nomeIns  = nomeInsField.getText().trim();
            String emailDoc = emailDocField.getText().trim();
            String nomeAula = nomeAulaField.getText().trim();
            String giorno   = (String) giornoCombo.getSelectedItem();

            if (nomeIns.isEmpty() || emailDoc.isEmpty() || nomeAula.isEmpty()
                    || cfuField.getText().trim().isEmpty()
                    || annoField.getText().trim().isEmpty()
                    || capienzaField.getText().trim().isEmpty()
                    || oraIField.getText().trim().isEmpty()
                    || minIField.getText().trim().isEmpty()
                    || oraFField.getText().trim().isEmpty()
                    || minFField.getText().trim().isEmpty()) {
                labelErrore.setText("Compila tutti i campi.");
                return;
            }

            try {
                int cfu      = Integer.parseInt(cfuField.getText().trim());
                int anno     = Integer.parseInt(annoField.getText().trim());
                int capienza = Integer.parseInt(capienzaField.getText().trim());
                int oraI     = Integer.parseInt(oraIField.getText().trim());
                int minI     = Integer.parseInt(minIField.getText().trim());
                int oraF     = Integer.parseInt(oraFField.getText().trim());
                int minF     = Integer.parseInt(minFField.getText().trim());

                String errore = controller.creaLezione(
                        nomeIns, cfu, anno,
                        emailDoc,
                        nomeAula, capienza,
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
