/**Scheramta dello studente in cui può visualizzare
 * il proprio orario in base all'anno di corso.
 */
package gui;

import controller.Controller;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchermataStudente {

    JFrame frame;
    private JPanel panel1;

    // — Lunedì —
    private JTextArea primaLezioneLunedi;
    private JTextArea secondaLezioneLunedi;
    private JTextArea terzaLezioneLunedi;

    // — Martedì —
    private JTextArea primaLezioneMartedi;
    private JTextArea secondaLezioneMartedi;
    private JTextArea terzaLezioneMartedi;

    // — Mercoledì —
    private JTextArea primaLezioneMercoledi;
    private JTextArea secondaLezioneMercoledi;
    private JTextArea terzaLezioneMercoledi;

    // — Giovedì —
    private JTextArea primaLezioneGiovedi;
    private JTextArea secondaLezioneGiovedi;
    private JTextArea terzaLezioneGiovedi;

    // — Venerdì —
    private JTextArea primaLezioneVenerdi;
    private JTextArea secondaLezioneVenerdi;
    private JTextArea terzaLezioneVenerdi;

    private JLabel matricola;
    private JButton indietroButton;
    private JButton logoutButton;

    /** Slot per giorno, in ordine: prima, seconda, terza lezione */
    private JTextArea[][] slotPerGiorno;

    private static final String[] GIORNI = {
            "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"
    };

    public SchermataStudente(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Studente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        slotPerGiorno = new JTextArea[][]{
                {primaLezioneLunedi, secondaLezioneLunedi, terzaLezioneLunedi},
                {primaLezioneMartedi, secondaLezioneMartedi, terzaLezioneMartedi},
                {primaLezioneMercoledi, secondaLezioneMercoledi, terzaLezioneMercoledi},
                {primaLezioneGiovedi, secondaLezioneGiovedi, terzaLezioneGiovedi},
                {primaLezioneVenerdi, secondaLezioneVenerdi, terzaLezioneVenerdi}
        };

        // Rendi non editabili e pulisci i placeholder statici del form
        for (JTextArea[] slots : slotPerGiorno) {
            for (JTextArea ta : slots) {
                if (ta != null) {
                    ta.setEditable(false);
                    ta.setText("");
                }
            }
        }
        if(logoutButton != null) {
            logoutButton.addActionListener(e -> {
                controller.logout();
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }

        // Popola il tabellone con le lezioni esistenti (caricate dal DB al login)
        // e con quelle già aggiunte in memoria durante l'esecuzione.
        aggiornaOrario(controller);

        // Ripopola il tabellone ogni volta che la finestra torna in primo piano,
        // così le lezioni aggiunte durante l'esecuzione compaiono senza riavviare.
        frame.addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                aggiornaOrario(controller);
            }
        });

        matricola.setText("Matricola: " + controller.getMatricola());
        if (indietroButton != null) {
            indietroButton.addActionListener(e -> {
                controller.logout();
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }
    }

    /**
     * Recupera dal {@link Controller} le lezioni dello studente (esistenti e
     * aggiunte durante l'esecuzione) e popola gli slot del tabellone:
     * lezione se presente, "—" se assente.
     */
    private void aggiornaOrario(Controller controller) {
        // Recupera le lezioni dal Controller, ordinate per orario
        Map<String, List<String>> lezioniPerGiorno = controller.getLezioniStudentePerGiorno();
        for (String g : GIORNI) {
            lezioniPerGiorno.computeIfAbsent(g, k -> new ArrayList<>())
                    .sort(String::compareTo);
        }

        for (int i = 0; i < GIORNI.length; i++) {
            List<String> lezioni = lezioniPerGiorno.getOrDefault(GIORNI[i], new ArrayList<>());
            JTextArea[]  slots   = slotPerGiorno[i];
            for (int s = 0; s < slots.length; s++) {
                if (slots[s] == null) continue;
                slots[s].setText(s < lezioni.size() ? lezioni.get(s) : "—");
            }
        }
    }

}