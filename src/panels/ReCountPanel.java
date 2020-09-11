package panels;

import com.toedter.calendar.JDateChooser;
import database.Receiver;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import dialogs.Configurator;
import main.MainFrame;
import mytools.Utilities;
import mytools.MyDates;
import database.Report;
import mytools.Icons;

public class ReCountPanel extends JPanel implements ActionListener {

    public static final String TABLE_NAME = "RecuentoParte";

    public static final int NAME_COLUMN = 2;
    public static final int DIAG_COLUMN = 5;

    private JButton buttonSickPanel;
    private JButton buttonGetByDNI, buttonGetAll, buttonClean, buttonSearchByName;
    private JButton buttonReport;

    private JLabel labelGetByDNI, labelGetByDate, labelGetByDates, labelSearchByName;
    private JDateChooser dateOne, dateTwo;
    private JTextField textGetByDNI, textSearchByName;
    private JTable table;
    private JScrollPane scrollTable;
    private Dimension dimension;

    private ButtonGroup bg;
    private JRadioButton radioDNI, radioSingleDate, radioBetweenDates;
    private JRadioButton radioBefore, radioAfter;
    private JRadioButton radioAsc, radioDesc;
    //Flags
    int pointer;
    boolean found;

    private JScrollPane scrollContainer;

    private MainFrame mainFrame;
    private Configurator config;

    public ReCountPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        pointer = 0;
        found = false;
        components();
        shortcuts();
    }

    public void components() {
        //------------------------------------------
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();

        //PROPIEDADES DEL PANEL-------------------------------------------------
        setBackground(utility.getColorBackground());
        dimension = new Dimension(1505, 580);
        setPreferredSize(dimension);
        setLayout(null);
        setOpaque(false);
        scrollContainer = new JScrollPane(this);
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //TABLA CON RECUENTO----------------------------------------------------
        table = utility.customTable(this, NAME_COLUMN, DIAG_COLUMN);

        //scroll
        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(40, 100, 1367, 27);
        scrollTable.setPreferredSize(new Dimension(1350, 1300));
        scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollTable);

        //TEXT FIELDS-----------------------------------------------------------
        textGetByDNI = new JTextField();
        textGetByDNI.setBounds(345, 65, 70, 20);
        textGetByDNI.addKeyListener(utility.soloNumeros);
        textGetByDNI.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    getByDNI();
                }
            }
        });
        add(textGetByDNI);
        textSearchByName = new JTextField();
        textSearchByName.setBounds(840, 65, 150, 20);
        textSearchByName.setVisible(true);
        textSearchByName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchByName();
                }
            }
        });
        add(textSearchByName);
        //DATE CHOOSER
        dateOne = new JDateChooser();
        dateOne.setDateFormatString(MyDates.USER_DATE_FORMAT);
        dateOne.setBounds(180, 65, 95, 20);
        dateOne.setForeground(Color.black);
        dateOne.setFont(utility.getFontTextFields());
        dateOne.setVisible(false);
        add(dateOne);
        dateTwo = new JDateChooser();
        dateTwo.setDateFormatString(MyDates.USER_DATE_FORMAT);
        dateTwo.setBounds(285, 65, 95, 20);
        dateTwo.setForeground(Color.black);
        dateTwo.setFont(utility.getFontTextFields());
        dateTwo.setVisible(false);
        add(dateTwo);
        //LABELS----------------------------------------------------------------
        labelGetByDNI = new JLabel("Ingrese el DNI para buscar en el recuento:");
        labelGetByDNI.setBounds(40, 60, 300, 30);
        labelGetByDNI.setFont(utility.getFontLabelBig());
        labelGetByDNI.setForeground(Color.black);
        add(labelGetByDNI);

        labelGetByDate = new JLabel("Ingrese fecha");
        labelGetByDate.setBounds(40, 60, 150, 30);
        labelGetByDate.setFont(utility.getFontLabelBig());
        labelGetByDate.setForeground(Color.black);
        labelGetByDate.setVisible(false);
        add(labelGetByDate);

        labelGetByDates = new JLabel("Ingrese las fechas");
        labelGetByDates.setBounds(40, 60, 300, 30);
        labelGetByDates.setFont(utility.getFontLabelBig());
        labelGetByDates.setForeground(Color.black);
        labelGetByDates.setVisible(false);
        add(labelGetByDates);

        labelSearchByName = new JLabel("Buscar por nombre:");
        labelSearchByName.setBounds(690, 60, 170, 30);
        labelSearchByName.setFont(utility.getFontLabelBig());
        labelSearchByName.setForeground(Color.black);
        labelSearchByName.setVisible(true);
        add(labelSearchByName);

        //BOTONES---------------------------------------------------------------
        buttonGetByDNI = utility.customButton();
        buttonGetByDNI.setText("<html>Buscar</html>");
        buttonGetByDNI.setBounds(420, 61, 70, 25);
        buttonGetByDNI.addActionListener(this);
        add(buttonGetByDNI);
        buttonGetAll = utility.customButton();
        buttonGetAll.setText("<html>Todos</html>");
        buttonGetAll.setBounds(510, 61, 70, 25);
        buttonGetAll.addActionListener(this);
        add(buttonGetAll);
        buttonClean = utility.customButton();
        buttonClean.setText("<html>Limpiar</html>");
        buttonClean.setBounds(600, 61, 70, 25);
        buttonClean.addActionListener(this);
        add(buttonClean);
        buttonSearchByName = utility.customButton();
        buttonSearchByName.setText("<html>Buscar</html>");
        buttonSearchByName.setBounds(1005, 61, 80, 25);
        buttonSearchByName.addActionListener(this);
        buttonSearchByName.setVisible(true);
        add(buttonSearchByName);

        buttonSickPanel = utility.customButton();
        buttonSickPanel.setText("<html><center>Volver al Parte</center></html>");
        buttonSickPanel.setBounds(10, 15, 110, 35);
        buttonSickPanel.addActionListener(this);
        add(buttonSickPanel);
        //Radio Buttons--------------------------------------------------------      
        radioDNI = new JRadioButton("Buscar por DNI");
        radioDNI.setBounds(130, 10, 150, 20);
        radioDNI.setOpaque(false);
        radioDNI.setFocusPainted(false);
        radioDNI.setFont(utility.getFontLabelFormulary());
        radioDNI.setForeground(Color.black);
        radioDNI.addActionListener(this);
        radioDNI.setSelected(true);
        add(radioDNI);

        radioSingleDate = new JRadioButton("Buscar por fecha");
        radioSingleDate.setBounds(130, 25, 150, 20);
        radioSingleDate.setOpaque(false);
        radioSingleDate.setFocusPainted(false);
        radioSingleDate.setFont(utility.getFontLabelFormulary());
        radioSingleDate.setForeground(Color.black);
        radioSingleDate.addActionListener(this);
        add(radioSingleDate);

        radioBetweenDates = new JRadioButton("Buscar entre dos fechas");
        radioBetweenDates.setBounds(130, 40, 180, 20);
        radioBetweenDates.setOpaque(false);
        radioBetweenDates.setFocusPainted(false);
        radioBetweenDates.setFont(utility.getFontLabelFormulary());
        radioBetweenDates.setForeground(Color.black);
        radioBetweenDates.addActionListener(this);
        add(radioBetweenDates);

        radioAfter = new JRadioButton("Posterior a la ingresada");
        radioAfter.setBounds(310, 25, 200, 20);
        radioAfter.setOpaque(false);
        radioAfter.setFocusPainted(false);
        radioAfter.setFont(utility.getFontLabelFormulary());
        radioAfter.setForeground(Color.black);
        radioAfter.addActionListener(this);
        radioAfter.setVisible(false);
        add(radioAfter);

        radioBefore = new JRadioButton("Anterior a la ingresada");
        radioBefore.setBounds(310, 10, 200, 20);
        radioBefore.setOpaque(false);
        radioBefore.setFocusPainted(false);
        radioBefore.setFont(utility.getFontLabelFormulary());
        radioBefore.setForeground(Color.black);
        radioBefore.addActionListener(this);
        radioBefore.setVisible(false);
        radioBefore.setSelected(true);
        add(radioBefore);

        radioAsc = new JRadioButton("Fechas Ascendiente");
        radioAsc.setBounds(510, 10, 200, 20);
        radioAsc.setOpaque(false);
        radioAsc.setFocusPainted(false);
        radioAsc.setFont(utility.getFontLabelFormulary());
        radioAsc.setForeground(Color.black);
        radioAsc.setSelected(true);
        add(radioAsc);

        radioDesc = new JRadioButton("Fechas Descendiente");
        radioDesc.setBounds(510, 25, 200, 20);
        radioDesc.setOpaque(false);
        radioDesc.setFocusPainted(false);
        radioDesc.setFont(utility.getFontLabelFormulary());
        radioDesc.setForeground(Color.black);
        add(radioDesc);

        bg = new ButtonGroup();
        bg.add(radioDNI);
        bg.add(radioSingleDate);
        bg.add(radioBetweenDates);

        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(radioBefore);
        bg2.add(radioAfter);

        ButtonGroup bg3 = new ButtonGroup();
        bg3.add(radioAsc);
        bg3.add(radioDesc);
        //----------
        JLabel reporte = new JLabel("Generar Reportes");
        reporte.setFont(utility.getFontLabelBig());
        reporte.setForeground(Color.white);
        reporte.setBounds(900, 15, 150, 32);
        add(reporte);
        JLabel pdf = new JLabel();
        pdf.setIcon(icons.getIconPdf());
        pdf.setBounds(1050, 15, 32, 32);
        add(pdf);
        buttonReport = utility.customButton();
        buttonReport.setText("<html><center>Recuento<br>Partes</center></html>");
        buttonReport.setBounds(1090, 15, 110, 35);
        buttonReport.addActionListener(this);
        add(buttonReport);

        //----------------------------------------------------------------------
        //FINALIZACION DE LOS COMPONENTES
        icons = null;
        utility = null;
    }

    //--------------------------------------------------------------------------
    //----------------------------FONDO DEL PANEL-------------------------------
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(200, 500,
                getBackground().brighter().brighter(), 1000, 500,
                getBackground().darker().darker());
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    //--------------------------------------------------------------------------
    //-----------------------------EVENTOS--------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        //RADIO BUTTTONS--------------------------------------------------------
        if (e.getSource() == radioDNI) {
            labelGetByDNI.setVisible(true);
            labelGetByDate.setVisible(false);
            labelGetByDates.setVisible(false);

            textGetByDNI.setVisible(true);
            dateOne.setVisible(false);
            dateTwo.setVisible(false);

            radioAfter.setVisible(false);
            radioBefore.setVisible(false);

        }
        if (e.getSource() == radioSingleDate) {
            labelGetByDNI.setVisible(false);
            labelGetByDate.setVisible(true);
            labelGetByDates.setVisible(false);

            textGetByDNI.setVisible(false);
            dateOne.setVisible(true);
            dateTwo.setVisible(false);

            radioAfter.setVisible(true);
            radioBefore.setVisible(true);
        }
        if (e.getSource() == radioBetweenDates) {
            labelGetByDNI.setVisible(false);
            labelGetByDate.setVisible(false);
            labelGetByDates.setVisible(true);

            textGetByDNI.setVisible(false);
            dateOne.setVisible(true);
            dateTwo.setVisible(true);

            radioAfter.setVisible(false);
            radioBefore.setVisible(false);
        }

        //JBUTTONS--------------------------------------------------------------
        //Buscadores con Base de Datos------------------------------------------
        if (e.getSource() == buttonGetByDNI) {
            if (radioDNI.isSelected()) {
                getByDNI();
            } else if (radioSingleDate.isSelected()) {
                getBySingleDate();
            } else if (radioBetweenDates.isSelected()) {
                getByTwoDates();
            }

        }

        if (e.getSource() == buttonGetAll) {
            Receiver receiver = new Receiver();
            String statement = "SELECT * FROM RecuentoParte";
            receiver.obtainInformation(this, statement);
            if (table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No hay informacion cargada del recuento en la base de datos");

            } 
            receiver = null;
        }

        //Borra base de datos---------------------------------------------------
        if (e.getSource() == buttonClean) {
            ((DefaultTableModel) table.getModel()).setRowCount(0);
            updateWindow();

        }

        // RESTABLECE LA PANTALLA ----------------------------------------------
        if (e.getSource() == buttonSickPanel) {
            pointer = 0;
            found = false;
            ((DefaultTableModel) table.getModel()).setRowCount(0);
            updateWindow();

        }
        if (e.getSource() == buttonSearchByName) {
            searchByName();
        }
        if (e.getSource() == buttonReport) {
            if (table.getRowCount() < 1) {
                JOptionPane.showMessageDialog(null, "No hay informacion mostrada, busque por DNI o presione el boton \"Todos\"");
            } else {
                boolean keepAsking = true;
                
                while (keepAsking) {
                    String title = JOptionPane.showInputDialog(null, "Ingrese un titulo", "Ingrese un titulo para el recuento", 1);
                    
                    if (title != null && !title.equals("")) {
                        
                        int opcion = JOptionPane.showConfirmDialog(null, "¿Imprimir con Diagnosticos?", "Opcion", 1);
                        
                        if (opcion == JOptionPane.YES_NO_OPTION || opcion == JOptionPane.NO_OPTION) {
                            
                            boolean diag = JOptionPane.NO_OPTION != opcion;
                            Report report = new Report();
                            report.createReCountReport(this, title, diag);
                            report = null;
                            keepAsking = false;
                            
                        } else if (opcion == JOptionPane.CANCEL_OPTION){
                            keepAsking = false;
                        }

                    } else if (title == null) {
                        keepAsking = false;
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe elegir un titulo.");
                    }
                }

            }

        }
    }

    //--------------------------------------------------------------------------
    //-----------------------METODOS / FUNCIONES--------------------------------
    private void getByDNI() {
        try {
            
            long dni = Long.parseLong(textGetByDNI.getText());
            String stm = "SELECT * FROM RecuentoParte  WHERE DNI LIKE '%" + dni + "%'";
            

            Receiver receptor = new Receiver();
            boolean result = receptor.obtainInformation(this, stm);
            receptor = null;
            if (!result) {
                JOptionPane.showMessageDialog(null, "No han habido resultados con ese numero de DNI.");
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "El numero ingresado es incorrecto.");
        }
    }

    private void getBySingleDate() {
        String date = ((JTextField) dateOne.getDateEditor().getUiComponent()).getText();
        
        if (!date.equals("") && dateOne.getDate() != null) {
            
            MyDates mydates = new MyDates(MyDates.USER_DATE_FORMAT);
            int sendDate = mydates.getCustomYearAgo(dateOne.getDate());
            String stm = "SELECT * FROM RecuentoParte WHERE (SUBSTR(Alta,1,4)||SUBSTR(Alta,6,2)||SUBSTR(Alta,9,2)) ";
            stm += radioBefore.isSelected() ? " <= \"" + sendDate + "\"" : " >= \"" + sendDate + "\"";
            mydates = null;
     

            Receiver receptor = new Receiver();
            boolean result = receptor.obtainInformation(this, stm);
            receptor = null;
            
            if (!result) {
                JOptionPane.showMessageDialog(null, "No hubieron resultados. Revise la fecha ingresadas");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Fecha incorrecta");
        }
    }

    private void getByTwoDates() {
        String date = ((JTextField) dateOne.getDateEditor().getUiComponent()).getText();
        String date2 = ((JTextField) dateTwo.getDateEditor().getUiComponent()).getText();
        
        if (!date.equals("") && dateOne.getDate() != null && !date2.equals("") && dateTwo.getDate() != null) {
            
            MyDates mydates = new MyDates(MyDates.USER_DATE_FORMAT);
            int sendDate = mydates.getCustomYearAgo(dateOne.getDate());
            int sendDate2 = mydates.getCustomYearAgo(dateTwo.getDate());
            mydates = null;
            String stm = "SELECT * FROM RecuentoParte WHERE ((SUBSTR(Alta,1,4)||SUBSTR(Alta,6,2)||SUBSTR(Alta,9,2))"
                    + " <= \"" + sendDate2 + "\") AND ((SUBSTR(Alta,1,4)||SUBSTR(Alta,6,2)||SUBSTR(Alta,9,2)) >= \"" + sendDate + "\")";
            
           

            Receiver receptor = new Receiver();
            boolean result = receptor.obtainInformation(this, stm);
            receptor = null;
            
            if (!result) {
                JOptionPane.showMessageDialog(null, "No hubieron resultados. Revise la fecha ingresadas");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Fechas incorrectas");
        }
    }

 

    private void searchByName() {
        String nombre = "";
        String buscar = textSearchByName.getText().toLowerCase().trim();
        boolean searchNext = true;

        while (searchNext) {
            if (pointer == table.getRowCount()) {
                pointer = 0;
                if (!found) {
                    searchNext = false;
                    JOptionPane.showMessageDialog(null, new JLabel("No se ha encontrado.", JLabel.CENTER));
                }
            } else {
                nombre = ((String) table.getValueAt(pointer, 2)).toLowerCase();
                if (nombre.contains(buscar)) {
                    scrollTable.getVerticalScrollBar().setValue(pointer * 16);
                    table.setRowSelectionInterval(pointer, pointer);
                    searchNext = false;
                    found = true;
                    pointer++;
                } else {
                    pointer++;
                }
            }
        }
    }

    public void restoreColumns() {
        TableModel model = table.getModel();
        TableColumnModel tcm = table.getColumnModel();
        for (int j = 0; j < model.getColumnCount() - 1; j++) {
            int location = tcm.getColumnIndex(model.getColumnName(j));
            tcm.moveColumn(location, j);
        }
    }

    public void updateWindow() {
        int y = 100;

        int height = 28 + table.getRowCount() * 16;

        if (height > 460) {
            scrollTable.setBounds(40, y, 1367, 460);
        } else {
            scrollTable.setBounds(40, y, 1367, height);
        }
        y += height + 30;
    }

    //--------------------------------------------------------------------------
    //----------------------------ACCESOS RAPIDOS-------------------------------
    private void shortcuts() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "ordenarColumnas");
        getActionMap().put("ordenarColumnas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreColumns();
            }
        });
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), "bloquearTablas");
        getActionMap().put("bloquearTablas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (config.configColumns.isSelected()) {
                    config.configColumns.setSelected(false);
                } else {
                    config.configColumns.setSelected(true);
                }
            }
        });
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "seleccionarCeldas");
        getActionMap().put("seleccionarCeldas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (config.configRow.isSelected()) {
                    config.configRow.setSelected(false);

                } else {
                    config.configRow.setSelected(true);
                }
            }
        });
    }

    //--------------------------------------------------------------------------
    //----------------------------GETTER Y SETTER-------------------------------
    public JScrollPane getScrollContainer() {
        return scrollContainer;
    }

    public JButton getButtonSickPanel() {
        return buttonSickPanel;
    }

    public void setConfig(Configurator config) {
        this.config = config;
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) table.getModel();
    }

    public JRadioButton getRadioAsc() {
        return radioAsc;
    }

}
