package dialogs;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.*;

public class About extends JDialog {

    public About(Frame parent, boolean modal) {
        super(parent, modal);

        setSize(500, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Acerca del software");

        JTextArea area = new JTextArea();
        area.setBounds(0, 0, 500, 200);
        area.setBackground(Color.white);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        add(area);

        area.setText("Software desarrolado sin fines de lucro por Francisco Cahe "
                + "para facilitar el manejo de la informacion medica utilizada "
                + "frecuentemente en la Seccion Sanidad del Regimiento de Infanteria 1 \"Patricios\""
                + "\n\n"
                + "Ante cualquier duda, consulta, o problema relacionado con el Software"
                + " contactese con Francisco Cahe:\n"
                + "\nfranciscocahe@gmail.com"
                + "\nsencahe@gmail.com");

    }

}
