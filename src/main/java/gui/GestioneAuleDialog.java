package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


/**
 * Dialogo modale per la gestione delle aule.
 * Permette al responsabile di visualizzare le aule esistenti, inserirne di nuove
 * (nome e capienza) e rimuoverle selezionandole dalla tabella.
 */
public class GestioneAuleDialog {
    JDialog dialog;
    private final Controller controller;
    private JPanel panel1;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton salvaButton;
    private JButton indietroButton;

    /**
     * Costruisce e inizializza il dialogo: registra i listener,
     * carica le aule dal database e popola la tabella
     * @param controller il controller dell'applicazione
     * @param frameChiamante il frame che ha aperto il dialogo, usato per centrarlo (può essere {@code null})
     */
    public GestioneAuleDialog(Controller controller, JFrame frameChiamante) {
        dialog = new JDialog(frameChiamante, "Gestisci Aule", true);
        this.controller = controller;
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        java.util.Objects.requireNonNull(table1, "La tabella deve essere inizializzata dal designer");
        //verifico che il frame chiamante non sia null
        if (frameChiamante != null) {
            dialog.setLocationRelativeTo(frameChiamante);
        }

        caricaEvents();
        caricaAule();
        createTableAule();

    }
    /**
     * Registra i listener della GUI: chiusura del dialogo, salvataggio di una nuova aula
     * (con validazione dei campi) e rimozione dell'aula selezionata in tabella
     * previa conferma dell'utente
     */
    private void caricaEvents(){
        if(indietroButton != null) {
            indietroButton.addActionListener(e -> {
                dialog.setVisible(false);
                dialog.dispose();
            });
        }
        if(salvaButton != null) {
            salvaButton.addActionListener(e -> {
                // Logica per salvare i dati dell'aula
                if(!checkCampi()) return;
                String nomeAula = textField1.getText().trim();
                int capienza = Integer.parseInt(textField2.getText().trim());
                String ex= controller.inserisciAula(nomeAula,capienza);
                resetCampi();
                if(ex!=null) {
                    dialogErroreWarning(ex, 0);
                }
                createTableAule();


            });
        }

        if(table1 != null) {
            table1.getSelectionModel().addListSelectionListener(e -> {
                int riga = table1.getSelectedRow();
                if (riga == -1 || !e.getValueIsAdjusting()) return;
                String nomeAula = table1.getValueAt(riga, 0).toString();
                String motivo = "Vuoi rimuovere l'aula " + nomeAula + "? Se rimuovi l'aula verrano rimosse anche le lezioni programmate in quest'aula";
                JTextArea textArea = new JTextArea(motivo);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(120, 100));
                int risposta = JOptionPane.showConfirmDialog(dialog, scrollPane, "Rimozione Aula", JOptionPane.YES_NO_OPTION);
                rimuoviAula(risposta, nomeAula);

                table1.getSelectionModel().clearSelection();
            });
        }

    }

    /**
     * Rimuove l'aula se l'utente ha confermato, mostrando un errore in caso di fallimento,
     * e aggiorna la tabella
     * @param risposta la risposta dell'utente al dialogo di conferma ({@link JOptionPane#YES_OPTION} per procedere)
     * @param nomeAula il nome dell'aula da rimuovere
     */
    private void rimuoviAula(int risposta,String nomeAula){
        if (risposta == JOptionPane.YES_OPTION) {
            String action = controller.rimuoviAula(nomeAula);
            if (action != null) {
                dialogErroreWarning(action,0);
            }
            createTableAule();
        }

    }

    private void resetCampi(){
        textField1.setText("");
        textField2.setText("");
    }

    /**
     * Valida i campi di inserimento: entrambi devono essere compilati e la capienza
     * deve essere un numero intero positivo. In caso contrario mostra un avviso
     * @return {@code true} se i campi sono validi, {@code false} altrimenti
     */
    private boolean checkCampi(){
        String nomeAula = textField1.getText().trim();
        String capienzaStr = textField2.getText().trim();

        if (nomeAula.isEmpty() || capienzaStr.isEmpty()) {
            dialogErroreWarning("Compila tutti i campi.", 1);
            return false;
        }

        try {
            int capienza = Integer.parseInt(capienzaStr);
            if (capienza <= 0) {
                dialogErroreWarning("La capienza deve essere un numero positivo.", 1);
                return false;
            }
        } catch (NumberFormatException e) {
            dialogErroreWarning("La capienza deve essere un numero intero.", 1);
            return false;
        }

        return true;
    }
    /**
     * Popola la tabella con i dati aggiornati delle aule (nome e capienza),
     * rendendola non modificabile
     */
    private void createTableAule(){
        Object[][] data = controller.getAuleData();
        table1.setModel(new DefaultTableModel(data,
                new String[]{"Nome","Capienza"} ) );
        //non rende editable la tabella
        table1.setDefaultEditor(Object.class, null);
        //Quando viene selezionata la riga vien
        table1.setSelectionBackground(Color.LIGHT_GRAY);
    }

    /**
     * Carica le aule dal database tramite il controller,
     * mostrando un dialogo di errore in caso di fallimento
     */
    private void caricaAule(){
        String ex= controller.caricaAuleDaDB();
        if(ex!=null){
            JOptionPane.showMessageDialog(dialog, ex, "Errore", JOptionPane.ERROR_MESSAGE);
        }

    }
    /**
     * Mostra un dialogo di errore o di avviso
     * @param e il messaggio da visualizzare
     * @param tipo {@code 0} per un errore, {@code 1} per un avviso
     */
    private void dialogErroreWarning(String e, int tipo){
        switch (tipo){
            case 0:{
                JOptionPane.showMessageDialog(dialog,e,"Errore",JOptionPane.ERROR_MESSAGE);
                break;
            }
            case 1:{
                JOptionPane.showMessageDialog(dialog,e,"Attenzione",JOptionPane.WARNING_MESSAGE);
                break;
            }
            default:{ break; }
        }

    }
}

