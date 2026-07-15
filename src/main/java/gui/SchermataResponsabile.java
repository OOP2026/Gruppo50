// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;
/**
 * Finestra di interfaccia grafica (GUI) che fa da menu principale per il responsabile.
 * <p>
 * Questa schermata accessibile solo ai Responsabili fornisce un pannello di controllo per la gestione dell'orario,
 * dei corsi e delle richieste dei docenti.
 * L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Visualizza Richieste:</b> Apre la finestra per controllare, approvare o rifiutare le richieste di spostamento delle lezioni mandate dai docenti.</li>
 * <li><b>Crea Lezione:</b> Apre il modulo dedicato alla programmazione e all'inserimento di una nuova lezione nell'orario.</li>
 * <li><b>Insegnamenti Attivi:</b> Permette di accedere alla schermata per visualizzare e registrare nuovi insegnamenti.</li>
 * <li><b>Logout:</b> Disconnette l'utente in sicurezza e lo riporta alla schermata di accesso iniziale.</li>
 * </ul>
 */

public class SchermataResponsabile {
    JFrame frame;
    private JPanel panel1;
    private JButton visualizzaRichiesteButton;
    private JButton creaLezioneButton;
    private JButton insegnamentiAttiviButton;
    private JButton logoutButton;
    private JButton gestioneAuleButton;

    public SchermataResponsabile(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Responsabile");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);


        /* Controllo che il bottone si inizializzato corettamente*/
        if(creaLezioneButton != null) {
            creaLezioneButton.addActionListener(e -> {
                CrealezioneDialog d = new CrealezioneDialog(controller, frame);
                d.dialog.setVisible(true);
            });
        }
        if(gestioneAuleButton!=null){
            gestioneAuleButton.addActionListener(e->{
                GestioneAuleDialog g= new GestioneAuleDialog(controller,frame);
                g.dialog.setVisible(true);
            });

        }


        if(insegnamentiAttiviButton != null) {
            insegnamentiAttiviButton.addActionListener(e -> {
                InsegnamentiDialog d = new InsegnamentiDialog(controller, frame);
                d.dialog.setVisible(true);
            });

        }
        if(logoutButton != null) {
            logoutButton.addActionListener(e -> {
                controller.logout();
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }
        if(visualizzaRichiesteButton != null) {
            visualizzaRichiesteButton.addActionListener(e -> {
               VisualizzaRichiestaDialog d= new VisualizzaRichiestaDialog(controller,frame);
               d.dialog.setVisible(true);
            });
        }
    }

}