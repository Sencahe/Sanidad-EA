package dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import main.MainFrame;
import mytools.Icons;
import mytools.Utilities;
import panels.PersonnelPanel;

public class IMC extends JDialog implements ActionListener {

    private ButtonGroup bg;
    private JRadioButton radioHigher;
    private JRadioButton radioLesser;

    private JTextField imc;
    private JButton filter;

    private PersonnelPanel personnelPanel;
    private MainFrame mainFrame;

    public IMC(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        //Propiedades del frame       
        setSize(205, 190);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Filtrar por IMC");
        components();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }

    private void components() {
        //------------------------------
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        this.setIconImage(icons.getIconHealthService().getImage());
        //Fondo del frame
        JPanel container = new JPanel();
        container.setBackground(utility.getColorBackground().brighter());
        Dimension dimension = new Dimension(250, 140);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        //Label
        JLabel label = new JLabel("Filtrar por IMC que sea: ");
        label.setFont(utility.getFontLabelFormulary());
        label.setBounds(30, 15, 160, 30);
        container.add(label);
        //RadioButtons
        bg = new ButtonGroup();
        radioHigher = new JRadioButton("Mayor a");
        radioHigher.setBounds(30, 45, 70, 30);
        radioHigher.addActionListener(this);
        radioHigher.setOpaque(false);
        radioHigher.setFocusPainted(false);
        radioHigher.setSelected(true);
        bg.add(radioHigher);
        container.add(radioHigher);
        radioLesser = new JRadioButton("Menor a");
        radioLesser.setBounds(95, 45, 90, 30);
        radioLesser.addActionListener(this);
        radioLesser.setOpaque(false);
        radioLesser.setFocusPainted(false);
        bg.add(radioLesser);
        container.add(radioLesser);
        //TextField
        imc = new JTextField();
        imc.setBounds(45, 80, 95, 20);
        imc.addKeyListener(utility.bloquearLetras);
        imc.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        container.add(imc);
        imc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filter();
                }
            }
        });
        filter = new JButton("Filtrar");
        filter.setBounds(60, 110, 60, 25);
        filter.addActionListener(this);
        container.add(filter);
        //--------------------------
        this.getContentPane().add(container);
        utility = null;
        icons = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == filter) {
           filter();

        }
    }

    private void filter() {
        try {
            double inputIMC = Double.parseDouble(imc.getText());

            personnelPanel.setIMCfilter(inputIMC);
            personnelPanel.setIMCoperator(radioHigher.isSelected() ? ">=" : "<=");
            

            personnelPanel.update(7, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());

            personnelPanel.setFiltered(true); 
            mainFrame.deleteChecksPPS();
            mainFrame.getMenuFilterPPS().setIcon(mainFrame.getCheck());
            mainFrame.getItemIMC().setIcon(mainFrame.getCheck());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Numero ingresado incorrecto.");
        }
    }

    public void setPersonnelPanel(PersonnelPanel personnelPanel) {
        this.personnelPanel = personnelPanel;
    }
}
