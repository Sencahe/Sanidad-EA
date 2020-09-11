package dialogs;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import main.MainFrame;
import panels.PersonnelPanel;
import mytools.Icons;
import mytools.MyDates;
import mytools.Utilities;

public class CaducatedStudies extends JDialog implements ActionListener {

    PersonnelPanel personnelPanel;
    MainFrame mainFrame;

    ButtonGroup bg;
    JRadioButton radioYearAgo, radioCustomDate, radioCustomDate2;
    JDateChooser dateCustom;
    JDateChooser dateCustom2;
    JButton buttonFilter;
    JLabel labelSince, labelUntil;

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
        setSize(450, 280);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Anexos 27 vencidos o por vencer");
        setIconImage(icons.getIconHealthService().getImage());
        //Fondo del frame
        JPanel container = new JPanel();
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
        radioCustomDate2 = new JRadioButton("Anexos 27 entre dos fechas especificas.");
        radioCustomDate2.setBounds(15, 110, 400, 25);
        radioCustomDate2.setOpaque(false);
        radioCustomDate2.setForeground(Color.black);
        radioCustomDate2.setFont(utility.getFontLabelFormulary());
        radioCustomDate2.setFocusPainted(false);
        radioCustomDate2.addActionListener(this);
        container.add(radioCustomDate2);

        bg.add(radioYearAgo);
        bg.add(radioCustomDate);
        bg.add(radioCustomDate2);

        //DATE CHOOSER----------------------------------------------------------
        labelSince = new JLabel("Desde");
        labelSince.setBounds(30, 135, 100, 20);
        labelSince.setForeground(Color.black);
        labelSince.setVisible(false);
        container.add(labelSince);
        dateCustom = new JDateChooser();
        dateCustom.setBounds(30, 155, 100, 20);
        dateCustom.setForeground(Color.black);
        dateCustom.setFont(utility.getFontTextFields());
        dateCustom.setDateFormatString(MyDates.USER_DATE_FORMAT);
        dateCustom.setVisible(false);
        container.add(dateCustom);

        labelUntil = new JLabel("Hasta");
        labelUntil.setBounds(140, 135, 100, 20);
        labelUntil.setForeground(Color.black);
        labelUntil.setVisible(false);
        container.add(labelUntil);
        dateCustom2 = new JDateChooser();
        dateCustom2 = new JDateChooser();
        dateCustom2.setBounds(140, 155, 100, 20);
        dateCustom2.setForeground(Color.black);
        dateCustom2.setFont(utility.getFontTextFields());
        dateCustom2.setDateFormatString(MyDates.USER_DATE_FORMAT);
        dateCustom2.setVisible(false);
        container.add(dateCustom2);

        //BOTONES---------------------------------------------------------------
        buttonFilter = new JButton("Filtrar");
        buttonFilter.setBounds(180, 205, 80, 25);
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
                if (!textDate.equals("") && dateCustom.getDate() != null) {
                    int customYearAgo = mydates.getCustomYearAgo(dateCustom.getDate());
                    personnelPanel.setStudiesFilter(customYearAgo);
                    personnelPanel.update(PersonnelPanel.FILTER_A27, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                    personnelPanel.setBetweenTwoDates(false);

                    mainFrame.getItemCaducatedStudies().setIcon(mainFrame.getCheck());
                    empty();
                } else {
                    JOptionPane.showMessageDialog(null, "Fecha invalida");
                }

            } else if (radioCustomDate2.isSelected()) {
                //En caso de elegir entre dos fechas para filtrar
                String textDate = ((JTextField) dateCustom.getDateEditor().getUiComponent()).getText();
                String textDate2 = ((JTextField) dateCustom2.getDateEditor().getUiComponent()).getText();
                if (!textDate.equals("") && dateCustom.getDate() != null && !textDate2.equals("") && dateCustom2.getDate() != null) {
                    if (mydates.validateBetweenTwoDates(dateCustom.getDate(), dateCustom2.getDate())) {
                        int customYearAgo = mydates.getCustomYearAgo(dateCustom.getDate());
                        int customYearAgo2 = mydates.getCustomYearAgo(dateCustom2.getDate());
                        personnelPanel.setStudiesFilter(customYearAgo2);
                        personnelPanel.setStudiesFilter2(customYearAgo);
                        personnelPanel.setBetweenTwoDates(true);
                        personnelPanel.update(PersonnelPanel.FILTER_A27, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                        personnelPanel.setFiltered(true);
                      
                        mainFrame.getItemCaducatedStudies().setIcon(mainFrame.getCheck());
                        empty();
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Fecha invalida");
                }

            } else {
                //En caso de querer filtrar todas las fechas con un año de antiguedad  
                int yearAgo = mydates.getYearAgo();
                personnelPanel.setStudiesFilter(yearAgo);
                personnelPanel.update(PersonnelPanel.FILTER_A27, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                personnelPanel.setFiltered(true);
                personnelPanel.setBetweenTwoDates(false);

                mainFrame.getItemCaducatedStudies().setIcon(mainFrame.getCheck());
                empty();
            }
        }

        if (e.getSource() == radioCustomDate) {
            dateCustom.setVisible(true);
            dateCustom2.setVisible(false);
            dateCustom2.setDate(null);
            labelSince.setVisible(false);
            labelUntil.setVisible(false);
            
        }
        if (e.getSource() == radioYearAgo) {
            dateCustom.setVisible(false);
            dateCustom.setDate(null);
            dateCustom2.setVisible(false);
            dateCustom2.setDate(null);
            labelSince.setVisible(false);
            labelUntil.setVisible(false);
        }
        if(e.getSource() == radioCustomDate2){
            dateCustom.setVisible(true);
            dateCustom2.setVisible(true);           
            labelSince.setVisible(true);
            labelUntil.setVisible(true);
        }
    }

    private void empty() {
        radioYearAgo.setSelected(true);
        dateCustom.setVisible(false);
        dateCustom.setDate(null);
        dateCustom2.setVisible(false);
        dateCustom2.setDate(null);
        labelUntil.setVisible(false);
        labelSince.setVisible(false);

        dispose();
        System.gc();
    }

    //----------------------GETTERS Y SETTERS----------------------------
    public void setPersonnelPanel(PersonnelPanel personnelPanel) {
        this.personnelPanel = personnelPanel;
    }

}
