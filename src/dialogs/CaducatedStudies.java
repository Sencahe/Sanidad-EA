/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import main.MainFrame;
import panels.PersonnelPanel;
import mytools.Icons;
import mytools.MyDates;
import mytools.Utilities;

public class CaducatedStudies extends JDialog implements ActionListener {

    PersonnelPanel personnelPanel;
    MainFrame mainFrame;

    ButtonGroup bg;
    JRadioButton radioYearAgo, radioCustomDate;
    JDateChooser dateCustom;
    JButton buttonFilter;

    public CaducatedStudies(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        components();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                empty();               
            }
        });
    }

    private void components() {
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        //PROPIEDADES DEL FRAME        
        setSize(450, 220);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Anexos 27 vencidos o por vencer");
        setIconImage(icons.getIconHealthService().getImage());
        //Fondo del frame
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(50, 500,
                        getBackground().brighter(), 200, 170,
                        getBackground().darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        container.setBackground(utility.getColorBackground());
        Dimension dimension = new Dimension(450, 345);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;

        //----------------------------------------------------------------------
        //COMPONENTES PRINCIPALES-----------------------------------------------
        JLabel labelInfo = new JLabel("Seleccione el tipo de filtro para las"
                + " fechas de los Anexos 27.");
        labelInfo.setBounds(15, 15, 440, 30);
        labelInfo.setForeground(Color.black);
        labelInfo.setFont(utility.getFontLabelBig());
        container.add(labelInfo);

        bg = new ButtonGroup();
        radioYearAgo = new JRadioButton("Anexos 27 con un año de antiguedad.");
        radioYearAgo.setBounds(15, 50, 400, 25);
        radioYearAgo.setOpaque(false);
        radioYearAgo.setForeground(Color.black);
        radioYearAgo.setFont(utility.getFontLabelFormulary());
        radioYearAgo.setFocusPainted(false);
        radioYearAgo.setSelected(true);
        radioYearAgo.addActionListener(this);
        container.add(radioYearAgo);
        radioCustomDate = new JRadioButton("Anexos 27 anteriores a una fecha especifica.");
        radioCustomDate.setBounds(15, 80, 400, 25);
        radioCustomDate.setOpaque(false);
        radioCustomDate.setForeground(Color.black);
        radioCustomDate.setFont(utility.getFontLabelFormulary());
        radioCustomDate.setFocusPainted(false);
        radioCustomDate.addActionListener(this);
        container.add(radioCustomDate);

        bg.add(radioYearAgo);
        bg.add(radioCustomDate);

        //DATE CHOOSER----------------------------------------------------------
        dateCustom = new JDateChooser();
        dateCustom.setBounds(30, 110, 100, 20);
        dateCustom.setForeground(Color.black);
        dateCustom.setFont(utility.getFontTextFields());
        dateCustom.setDateFormatString(MyDates.USER_DATE_FORMAT);
        dateCustom.setVisible(false);
        container.add(dateCustom);

        //BOTONES---------------------------------------------------------------
        buttonFilter = new JButton("Filtrar");
        buttonFilter.setBounds(180, 145, 80, 25);
        buttonFilter.addActionListener(this);
        container.add(buttonFilter);
        
        //FIN
        this.getContentPane().add(container);
        utility = null;
        icons = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonFilter) {
            MyDates mydates = new MyDates(MyDates.USER_DATE_FORMAT);
            
            if (radioCustomDate.isSelected()) {
                //En caso de elegir una fecha para filtrar
                String textDate = ((JTextField) dateCustom.getDateEditor().getUiComponent()).getText();
                if (!textDate.equals("") || dateCustom.getDate() != null) {
                    int customYearAgo = mydates.getCustomYearAgo(dateCustom.getDate());
                    personnelPanel.setStudiesFilter(customYearAgo);
                    personnelPanel.update(PersonnelPanel.FILTER_A27,personnelPanel.getShowBySubUnity(),personnelPanel.getRowOrdering());
                     personnelPanel.setFiltered(true);
                     
                    mainFrame.getItemCaducatedStudies().setIcon(mainFrame.getCheck());
                    empty();
                } else {
                    JOptionPane.showMessageDialog(null, "Fecha invalida");
                }

            } else {
                //En caso de querer filtrar todas las fechas con un año de antiguedad  
                int yearAgo = mydates.getYearAgo();
                personnelPanel.setStudiesFilter(yearAgo);
                personnelPanel.update(PersonnelPanel.FILTER_A27,personnelPanel.getShowBySubUnity(),personnelPanel.getRowOrdering());
                personnelPanel.setFiltered(true);
                
                mainFrame.getItemCaducatedStudies().setIcon(mainFrame.getCheck());
                empty();
            }
        }
        
        if (e.getSource() == radioCustomDate) {
            dateCustom.setVisible(true);
        }
        if ((e.getSource() == radioYearAgo)) {
            dateCustom.setVisible(false);
            dateCustom.setDate(null);
        }
    }

    private void empty() {
        radioYearAgo.setSelected(true);
        dateCustom.setVisible(false);
        dateCustom.setDate(null);
        
        dispose();
        System.gc();
    }

    //----------------------GETTERS Y SETTERS----------------------------
    public void setPersonnelPanel(PersonnelPanel personnelPanel) {
        this.personnelPanel = personnelPanel;
    }

}
