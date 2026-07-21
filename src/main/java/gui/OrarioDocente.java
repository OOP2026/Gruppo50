package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
/**
 * Finestra di interfaccia grafica (GUI) dedicata all'orario settimanale.
 * <p>
 * Questa schermata accessibile solo ai docenti fornisce una griglia in cui sono
 * elencate tutte le lezioni che gli sono state assegnate. L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Tabella Orario:</b> Una tabella strutturata in cinque colonne che vanno dal Lunedì al Venerdì.</li>
 * <li><b>Bottone Indietro:</b> Un pulsante per chiudere l'orario e tornare alla schermata precedente.</li>
 * </ul>
 */
public class OrarioDocente {
    JFrame frame;
    private final JFrame frameChiamante;
    private JPanel panel1;
    private JTable tabellaOrario;
    private JButton indietroButton;

    /**
     * Costruisce la schermata che mostra l'orario del docente.
     * Crea la tabella e registra l'evento di ritorno alla schermata chiamante.
     * @param c controller usato per ottenere le lezioni del docente
     * @param f frame chiamante per il posizionamento
     */
    public OrarioDocente(Controller c, JFrame f){
        frameChiamante=f;
        frame = new JFrame("Schermata Orario Docente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        if (indietroButton != null) {
            caricaEvents();
        }

        //Passa la tabella solo se è stata inizializzata
        if (tabellaOrario != null) {
            creaTable(tabellaOrario, c.getLezioniDocente());
        }
    }


    /**
     * Registra i listener dei pulsanti presenti nella schermata (ad es. Indietro).
     */
    private void caricaEvents(){
        if(indietroButton != null) {
            indietroButton.addActionListener(e -> {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();

            });
        }
    }
    /**
     * Costruisce il modello della tabella orario e la popola con i dati forniti.
     * @param table la JTable da configurare
     * @param data dati delle lezioni da inserire (righe)
     */
    private void creaTable(JTable table, Object[][] data){
       DefaultTableModel model=new DefaultTableModel(null,
               new String[]{"Lunedi","Martedi","Mercoledi","Giovedi","Venerdi"} );
        table.setModel(model);
        //non rende editable la tabella
        table.setDefaultEditor(Object.class, null);
      configuraTable(table);
      caricaLezioniTable(table,data);
        //inseriamo manualmente i dati per un test
    }
    /**
     * Applica un renderer personalizzato per mostrare il testo delle celle
     * su più righe e imposta l'altezza delle righe.
     * @param table la JTable da configurare
     */
    private void configuraTable(JTable table){
        //TableCellRenderer gestisce la creazione delle celle della tabella
        TableCellRenderer orarioRender= new TableCellRenderer() {
            final JTextArea area= creaTextArea();
            @Override
            //questo metodo viene chiamto ogni volta che la tabella crea una cella, e permette di personalizzare il modo in cui viene renderizzata la cella
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    area.setText(value.toString());
                    area.setFont(table.getFont());
                    area.setSize(table.getColumnModel().getColumn(column).getWidth(),table.getRowHeight());
                    area.setPreferredSize(new Dimension(table.getColumnModel().getColumn(column).getWidth(),table.getRowHeight()));
                    //ritorna una textare, cosi da poter vedere il testo su piu righe
                    return area;
            }
        };
        //ogni colonna della tabella viene configurata con il render creato in precedenza
        //cosi ogni cella avrà una text are
        for(int i=0;i<table.getColumnCount();i++){
            table.getColumnModel().getColumn(i).setCellRenderer(orarioRender);
        }
        table.setRowHeight(50);
    }
    /**
     * Crea e restituisce una JTextArea pronta per essere usata come renderer
     * nelle celle della tabella (non editabile, con word-wrap abilitato).
     * @return una JTextArea configurata
     */
    private JTextArea creaTextArea() {
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setMaximumSize(new Dimension(70, 90));
        return area;
    }
    /**
     * Inserisce le righe delle lezioni nel modello della tabella.
     * Mostra un messaggio se non sono presenti lezioni.
     * @param table la JTable da popolare
     * @param data i dati delle lezioni (array di righe)
     */
    private void caricaLezioniTable(JTable table,Object[][] data){
        DefaultTableModel model= (DefaultTableModel) table.getModel();
        if(data.length==0){
            JOptionPane.showMessageDialog(this.frame,"Non hai lezioni!");
        return;
        }
        for(Object[] riga:data){
            model.addRow(riga);
        }
    }

}

