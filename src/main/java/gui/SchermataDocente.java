// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;
/**
 * Finestra di interfaccia grafica (GUI) che fa da menu per il docente.
 * <p>
 * Questa schermata accessibile solo ai Docenti, fornisce un pannello di controllo per gestire tutte le attività didattiche personali.
 * L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Insegnamenti:</b> Apre la schermata per visualizzare le proprie materie d'insegnamento.</li>
 * <li><b>Richieste:</b> Permette di accedere alla sezione per le richieste di spostamento delle lezioni.</li>
 * <li><b>Visualizza Orario:</b> Mostra la griglia settimanale con le lezioni assegnate al docente.</li>
 * <li><b>Vincoli:</b> Apre l'interfaccia per inserire o rimuovere i vincoli per i propri orari.</li>
 * <li><b>Logout:</b> Disconnette l'utente in sicurezza e lo riporta alla schermata di accesso iniziale.</li>
 * </ul>
 */
public class SchermataDocente {
    JFrame frame;
    private JPanel panel1;
    private JButton schermataRichiesteButton;
    private JButton visualizzaOrarioButton;
    private JButton gestioneVincoliButton;
    private JButton insegnamentiButton;
    private JButton logoutButton;
    private final Controller controller;
    private final JFrame frameChiamante;
    /**
     * Costruisce la schermata principale del Docente e registra i listener per
     * navigare verso le schermate di richieste, orario, vincoli e insegnamenti.
     * @param c controller dell'applicazione
     * @param f frame chiamante usato per il posizionamento
     */
    public SchermataDocente(Controller c,JFrame f) {
        controller=c;
        frameChiamante=f;
        frame = new JFrame("Schermata Docente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

    caricaEvents();

    }

    /**
     * Registra tutti gli ActionListener per i pulsanti della schermata docente.
     * Incapsula la logica di navigazione verso le altre finestre.
     */
    private void caricaEvents(){
        //Controllo che il bottone invia richieste sia inizializzato correttamente
        if (schermataRichiesteButton != null) {
            schermataRichiesteButton.addActionListener(e -> {
                SchermataRichiesta r = new SchermataRichiesta(controller, frame);
                r.frame.setVisible(true);
                frame.setVisible(false);
            });
        }
        //Controllo che il bottone per gestire i vincoli sia creato
        if (gestioneVincoliButton != null) {
            gestioneVincoliButton.addActionListener(e -> {
                SchermataVincoli v = new SchermataVincoli(controller, frame);
                v.frame.setVisible(true);
                frame.setVisible(false);
            });
        }
        //Controllo che il bottone per visualizzare l'orario sia creato
        if (visualizzaOrarioButton != null) {
            visualizzaOrarioButton.addActionListener(e -> {
                OrarioDocente o = new OrarioDocente(controller, frame);
                o.frame.setVisible(true);
                frame.setVisible(false);
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

        if (insegnamentiButton != null) {
            insegnamentiButton.addActionListener(e -> {
                SchermataInsegnamentiDocente i = new SchermataInsegnamentiDocente(controller, frame);
                i.frame.setVisible(true);
                frame.setVisible(false);
            });
        }


    }
}
