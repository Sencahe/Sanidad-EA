package dialogs;

import database.DataBase;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import main.MainFrame;
import panels.PersonnelPanel;
import javax.swing.*;
import mytools.Icons;
import mytools.Utilities;

public class Observations extends JDialog implements ActionListener {

    MainFrame mainFrame;
    PersonnelPanel personnelPanel;

    JRadioButton radioAll, radioText;
    JTextField textObs;
    ButtonGroup bg;

    JButton buttonFilter;

    public Observations(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        //Propiedades del frame       
        setSize(230, 205);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Filtrar por observaciones");
        setLayout(null);
        components();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }

    private void components() {
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        this.setIconImage(icons.getIconHealthService().getImage());
        //-----------------------------
        this.getContentPane().setBackground(utility.getColorBackground().brighter());

        JLabel labelInfo = new JLabel("Seleccione como filtrar");
        labelInfo.setBounds(10, 10, 220, 30);
        labelInfo.setForeground(Color.black);
        labelInfo.setFont(utility.getFontLabelBig());
        add(labelInfo);

        radioAll = new JRadioButton("Todos con observaciones");
        radioAll.setBounds(10, 40, 200, 30);
        radioAll.setForeground(Color.black);
        radioAll.setFont(utility.getFontLabelFormulary());
        radioAll.setOpaque(false);
        radioAll.setFocusPainted(false);
        radioAll.addActionListener(this);
        radioAll.setSelected(true);
        add(radioAll);

        radioText = new JRadioButton("Con palabras clave");
        radioText.setBounds(10, 70, 200, 30);
        radioText.setForeground(Color.black);
        radioText.setFont(utility.getFontLabelFormulary());
        radioText.setOpaque(false);
        radioText.setFocusPainted(false);
        radioText.addActionListener(this);
        add(radioText);

        bg = new ButtonGroup();
        bg.add(radioAll);
        bg.add(radioText);

        textObs = new JTextField(" ");
        textObs.setBounds(15, 100, 190, 20);
        textObs.setVisible(false);
        textObs.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filter();
                }
            }
        });
        add(textObs);

        buttonFilter = new JButton("Filtrar");
        buttonFilter.setBounds(70, 130, 60, 25);
        buttonFilter.addActionListener(this);
        add(buttonFilter);

        //--------------------
        icons = null;
        utility = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == radioAll) {
            textObs.setVisible(false);
            textObs.setText(" ");
        }
        if (e.getSource() == radioText) {
            textObs.setVisible(true);
            textObs.setText("");
        }

        if (e.getSource() == buttonFilter) {
            filter();
        }
    }

    //----------------------METODOS/FUNCIONES-----------------------------------
    private void filter() {     
        if(!textObs.getText().equals("")){
            String query = "";
          if (radioAll.isSelected()) {
            query = "Observaciones IS NOT NULL";

        } else if (radioText.isSelected()) {
            query = "Observaciones LIKE '%" + textObs.getText().trim() + "%'";
        }
        personnelPanel.setObsFilter(query);
        personnelPanel.update(PersonnelPanel.FILTER_OBS, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
        personnelPanel.setFiltered(true);
        mainFrame.getItemObs().setIcon(mainFrame.getCheck());
        dispose();  
        } else {
            JOptionPane.showMessageDialog(null,"Debe llenar el campo para filtrar.");
        }     
    }

    //---------------------GETTERS Y SETTERS------------------------------------
    public void setPersonnelPanel(PersonnelPanel personnelPanel) {
        this.personnelPanel = personnelPanel;
    }

}
