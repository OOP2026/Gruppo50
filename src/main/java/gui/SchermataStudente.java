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
    private JTextArea PrimaLezioneLunedi;
    private JTextArea SecondaLezioneLunedi;
    private JTextArea TerzaLezioneLunedi;

    // — Martedì —
    private JTextArea PrimaLezioneMartedi;
    private JTextArea SecondaLezioneMartedi;
    private JTextArea TerzaLezioneMartedi;

    // — Mercoledì —
    private JTextArea PrimaLezioneMercoledi;
    private JTextArea SecondaLezioneMercoledi;
    private JTextArea TerzaLezioneMercoledi;

    // — Giovedì —
    private JTextArea PrimaLezioneGiovedi;
    private JTextArea SecondaLezioneGiovedi;
    private JTextArea TerzaLezioneGiovedi;

    // — Venerdì —
    private JTextArea PrimaLezioneVenerdi;
    private JTextArea SecondaLezioneVenerdi;
    private JTextArea TerzaLezioneVenerdi;

    private JLabel matricola;
    private JButton indietroButton;

    private static final String[] GIORNI = {
            "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"
    };

    public SchermataStudente(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Studente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        // Slot per giorno, in ordine: prima, seconda, terza lezione
        JTextArea[][] slotPerGiorno = {
                { PrimaLezioneLunedi,    SecondaLezioneLunedi,    TerzaLezioneLunedi    },
                { PrimaLezioneMartedi,   SecondaLezioneMartedi,   TerzaLezioneMartedi   },
                { PrimaLezioneMercoledi, SecondaLezioneMercoledi, TerzaLezioneMercoledi },
                { PrimaLezioneGiovedi,   SecondaLezioneGiovedi,   TerzaLezioneGiovedi   },
                { PrimaLezioneVenerdi,   SecondaLezioneVenerdi,   TerzaLezioneVenerdi   }
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
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }
    }

    public void mostra() {
        frame.setVisible(true);
    }
}