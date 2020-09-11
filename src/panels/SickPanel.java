package panels;


import dialogs.SickFormulary;
import database.DataBase;
import database.Report;
import mytools.MyArrays;
import mytools.Utilities;
import main.MainFrame;
import dialogs.Configurator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import mytools.Icons;


public class SickPanel extends JPanel implements ActionListener {
    
    
    public static final String TABLE_NAME = "Parte";
    public static final int EXPIRED_TABLE = 3;
    
    public static final int NAME_COLUMN = 3;
    public static final int DIAG_COLUMN = 5;
    public static final int ID_COLUMN = 11;

    private JScrollPane scrollContainer;

    private JButton buttonPersonnelPanel, buttonReCountPanel, buttonReport, buttonReport2;

    private JLabel[] titles;

    private JTable[] tables;

    private JScrollPane[] scrolls;
    private Dimension dimension;

    //Objetos para las ventanas
    private MainFrame mainFrame;
    private SickFormulary formularySick;
    private Configurator config;

    public SickPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        components();
        shortcut();
    }

    private void components() {
        //OBJETOS AUXILIARES       
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        //-------------PROPIEDADES DEL PANEL------------------------------------
        dimension = new Dimension();
        setBackground(utility.getColorBackground());
        setOpaque(false);
        setLayout(null);
        scrollContainer = new JScrollPane(this);
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.getVerticalScrollBar().setUnitIncrement(30);

        //TABLAS DEL PARTE------------------------------------------------------       
        tables = new JTable[MyArrays.getSickTypesLength()];
        scrolls = new JScrollPane[MyArrays.getSickTypesLength()];
        titles = new JLabel[MyArrays.getSickTypesLength()];       
        for (int i = 0; i < 4; i++) {
            //creacion del table model y de la tabla
            tables[i] = utility.customTable(this, 3, 5);          
            tables[i].setDefaultRenderer(Object.class, utility.sickCellRenderer());
            tables[i].setSelectionForeground(new Color(187,187,187));
            //eventos al presionar teclas en las tablas
            tables[i].getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            tables[i].getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    for (int i = 0; i < 4; i++) {
                        if (ae.getSource() == tables[i]) {
                            modifySick(i);
                        }
                    }
                }
            });
            //eventos al clikear la tabla          
            tables[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    for (int i = 0; i < 4; i++) {
                        if (e.getClickCount() >= 2) {
                            modifySick(i);
                        }
                    }
                }
            });
            //seleccionar una tabla a la vez
            tables[i].addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    super.focusGained(e);
                    for (int i = 0; i < 4; i++) {
                        if (e.getSource() == tables[i]) {

                        } else {
                            int count = tables[i].getRowCount();
                            if (count >= 1) {
                                tables[i].removeRowSelectionInterval(0, count - 1);
                            }
                        }
                    }
                }
            });
            //Titulo, Scrolls          
            titles[i] = new JLabel(MyArrays.getSickTypes(i));
            titles[i].setFont(utility.getFontLabelTitle());
            titles[i].setForeground(Color.black);
            scrolls[i] = new JScrollPane(tables[i]);
            scrolls[i].setOpaque(false);
            scrolls[i].getViewport().setOpaque(false);
            scrolls[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            add(scrolls[i]);
            add(titles[i]);
        }

        //BOTONES---------------------------------------------------------------
        buttonPersonnelPanel = utility.customButton();
        buttonPersonnelPanel.setText("Volver");
        buttonPersonnelPanel.setBounds(10, 15, 110, 35);
        add(buttonPersonnelPanel);
        buttonReCountPanel = utility.customButton();
        buttonReCountPanel.setText("<html><center>Ver Recuento</center></html>");
        buttonReCountPanel.setBounds(150, 15, 110, 35);
        add(buttonReCountPanel);
        //---------
        JLabel labelReport = new JLabel("Generar Reportes");
        labelReport.setFont(utility.getFontLabelBig());
        labelReport.setForeground(Color.white);
        labelReport.setBounds(900, 15, 150, 32);
        add(labelReport);
        JLabel labelPdf = new JLabel();
        labelPdf.setIcon(icons.getIconPdf());
        labelPdf.setBounds(1050, 15, 32, 32);
        add(labelPdf);
        buttonReport = utility.customButton();
        buttonReport.setText("<html><center>Parte con<br>Diagnosticos</center></html>");
        buttonReport.setBounds(1090, 15, 110, 35);
        buttonReport.addActionListener(this);
        add(buttonReport);
        buttonReport2 = utility.customButton();
        buttonReport2.setText("<html><center>Parte sin<br>Diagnosticos</center></html>");
        buttonReport2.setBounds(1210, 15, 110, 35);
        buttonReport2.addActionListener(this);
        add(buttonReport2);
        //----------------------------------------------------------------------
        //FINALIZACION DE LOS COMPONENTES        
        DataBase db = new DataBase();
        db.update(this);
        db = null;

        utility = null;
        icons = null;

        updateWindow();
    }
    //--------------------------------------------------------------------------
    //---------------------FONDO DEL PANEL--------------------------------------

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(100, 500,
                getBackground().brighter(), 500, 500,
                getBackground().darker().darker());
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    //--------------------------------------------------------------------------
    //--------------------------EVENTOS-----------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonReport) {
            Report reporte = new Report();
            reporte.createSickReport(this, true);
        }
        if (e.getSource() == buttonReport2) {
            Report reporte = new Report();
            reporte.createSickReport(this, false);
        }
    }

    //--------------------------------------------------------------------------
    //-----------------------METODOS / FUNCIONES--------------------------------
    public void restoreColumns() {
        for (int i = 0; i < 4; i++) {
            TableModel model = tables[i].getModel();
            TableColumnModel tcm = tables[i].getColumnModel();
            for (int j = 0; j < model.getColumnCount() - 1; j++) {
                int location = tcm.getColumnIndex(model.getColumnName(j));
                tcm.moveColumn(location, j);
            }
            model = null;
            tcm = null;
        }

    }

    public void updateWindow() {
        int y = 90;
        for (int i = 0; i < 4; i++) {
            int heigth = 27 + tables[i].getRowCount() * 16;
            titles[i].setBounds(145, y - 40, 400, 40);
            scrolls[i].setBounds(100, y, 1136, heigth);
            y += heigth + 55;
        }
        dimension.setSize(1505, y);
        this.setPreferredSize(dimension);
    }

    public void modifySick(int i) {
        int pointer = tables[i].getSelectedRow();
        if (pointer != -1) {
            int idSick = (int) tables[i].getModel().getValueAt(pointer, ID_COLUMN);
            formularySick.obtainData(idSick);
        }
    }
    

    //--------------------------------------------------------------------------
    //---------------------ACCESOS RAPIDOS--------------------------------------
    private void shortcut() {
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
    //-----------------------SETTERS Y GETTERS---------------------------------
    public DefaultTableModel getTableModel(int index) {
        return (DefaultTableModel) tables[index].getModel();
    }

    public JTable[] getTables() {
        return tables;
    }

    public JTable getTablas(int index) {
        return tables[index];
    }

    public void setFormularySick(SickFormulary formularySick) {
        this.formularySick = formularySick;
    }

    public JScrollPane getScrollContainer() {
        return scrollContainer;
    }

    public JButton getButtonPersonnelPanel() {
        return buttonPersonnelPanel;
    }

    public void setConfig(Configurator config) {
        this.config = config;
    }

    public JButton getButtonReCountPanel() {
        return buttonReCountPanel;
    }


}
