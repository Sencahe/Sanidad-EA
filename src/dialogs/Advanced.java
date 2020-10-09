package dialogs;

import database.Configurations;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import main.MainFrame;
import mytools.Icons;
import mytools.JTextFieldLimit;
import mytools.MyArrays;
import mytools.Utilities;

public class Advanced extends JDialog implements ActionListener {

    MainFrame mainFrame;
    String[] flagSubUnities;

    JTextField textSubUnities[];
    JComboBox comboSubUnities;
    JButton buttonSave;

    public Advanced(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        components();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                setSavedSubUnities();
                System.gc();
            }
        });
    }

    private void components() {
        // PROPIEDADES DEL FRAME
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        // FRAME DEL BUSCADOR
        super.setLayout(null);
        super.setSize(390, 340);
        super.setResizable(false);
        super.setLocationRelativeTo(null);
        super.setTitle("Configuraciones");
        super.setIconImage(icons.getIconHealthService().getImage());
        super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        super.getContentPane().setBackground(utility.getColorBackground());

        JLabel labelTitle = new JLabel("Modificar Destinos Internos");
        labelTitle.setForeground(Color.black);
        labelTitle.setFont(utility.getFontLabelBig());
        labelTitle.setBounds(15, 10, 250, 30);
        add(labelTitle);

        textSubUnities = new JTextField[20];
        int y = 80;
        int x = 15;
        for (int i = 0; i < textSubUnities.length; i++) {
            textSubUnities[i] = new JTextField();
            textSubUnities[i].setBounds(x, y, 70, 25);
            textSubUnities[i].setDocument(new JTextFieldLimit(7));
            textSubUnities[i].setVisible(false);
            add(textSubUnities[i]);
            y = ((i + 1) % 4 == 0) ? y + 30 : y;
            x = ((i + 1) % 4 == 0) ? 15 : x + 85;
        }

        //----------------------
        JLabel labelCombo = new JLabel("Indicar la cantidad de Destinos");
        labelCombo.setBounds(15,40,150,30);
        labelCombo.setForeground(Color.black);
        add(labelCombo);
        comboSubUnities = new JComboBox();
        comboSubUnities.setBounds(180, 45, 50, 20);
        for (int i = 0; i < 20; i++) {
            comboSubUnities.addItem(i + 1);
        }
        comboSubUnities.addActionListener(this);
        add(comboSubUnities);

        buttonSave = new JButton("Guardar");
        buttonSave.setBounds(140, 250, 80, 30);
        buttonSave.addActionListener(this);
        add(buttonSave);

        //--------------------------------------
        getSavedSubUnities();
        setSavedSubUnities();

    }

    private void getSavedSubUnities() {
        Configurations config = new Configurations();
        this.flagSubUnities = config.getSubUnitiesArray(false);
        config = null;
    }

    private void setSavedSubUnities() {
        comboSubUnities.setSelectedIndex(flagSubUnities.length - 1);
        visibleTextFields();
        for (int i = 0; i < flagSubUnities.length; i++) {
            textSubUnities[i].setText(flagSubUnities[i]);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == comboSubUnities) {
            visibleTextFields();
        }

        if (e.getSource() == buttonSave) {
            String message = "<html><center> Con esta accion no se les aplicaran cambios a los registros actuales, por lo que debera hacerlo manualmente\n" +
                    "<br>¿Esta seguro que desea continuar? </center></html>";
            int option = JOptionPane.showConfirmDialog(null,new JLabel(message, JLabel.CENTER),
                   "Advertencia", 0,2);            
            if (JOptionPane.YES_NO_OPTION == option) {
                if (validation()) {
                    Configurations config = new Configurations();
                    config.setSubUnitiesArray(this);
                    flagSubUnities = config.getSubUnitiesArray(false);
                    config = null;
                    dispose();
                    setSavedSubUnities();
                } else {
                    JOptionPane.showMessageDialog(null, "Debe llenar los campos disponibles  ");
                }
            }
        }
    }

    private boolean validation() {
        int selectedIndex = comboSubUnities.getSelectedIndex() + 1;
        for (int i = 0; i < selectedIndex; i++) {
            if (textSubUnities[i].getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void visibleTextFields() {
        int selectedIndex = comboSubUnities.getSelectedIndex();
        for (int i = 0; i < textSubUnities.length; i++) {
            textSubUnities[i].setVisible(selectedIndex >= i);
            if (!(selectedIndex >= i)) {
                textSubUnities[i].setText("");
            }
        }
    }

    //--------------------GETTER Y SETTERS----------------------------------
    public JTextField[] getTextSubUnities() {
        return textSubUnities;
    }

    public JTextField getTextSubUnities(int index) {
        return textSubUnities[index];
    }

    public void setTextSubUnities(JTextField[] textSubUnities) {
        this.textSubUnities = textSubUnities;
    }

    public JComboBox getComboSubUnities() {
        return comboSubUnities;
    }

    public void setComboSubUnities(JComboBox comboSubUnities) {
        this.comboSubUnities = comboSubUnities;
    }

}
