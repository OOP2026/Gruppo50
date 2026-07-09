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

    private static final String[] GIORNI = {
            "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"
    };

    public SchermataStudente(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Studente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        // Slot per giorno, in ordine: prima, seconda, terza lezione
        JTextArea[][] slotPerGiorno = {
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

        // Recupera le lezioni dal Controller, ordinate per orario
        Map<String, List<String>> lezioniPerGiorno = controller.getLezioniStudentePerGiorno();
        for (String g : GIORNI) {
            lezioniPerGiorno.computeIfAbsent(g, k -> new ArrayList<>())
                    .sort(String::compareTo);
        }

        // Popola ogni slot: lezione se presente, "—" se assente
        for (int i = 0; i < GIORNI.length; i++) {
            List<String> lezioni = lezioniPerGiorno.getOrDefault(GIORNI[i], new ArrayList<>());
            JTextArea[]  slots   = slotPerGiorno[i];
            for (int s = 0; s < slots.length; s++) {
                if (slots[s] == null) continue;
                slots[s].setText(s < lezioni.size() ? lezioni.get(s) : "—");
            }
        }

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
    
}