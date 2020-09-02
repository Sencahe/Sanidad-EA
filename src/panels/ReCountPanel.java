package panels;

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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import dialogs.Configurator;
import main.MainFrame;
import mytools.MyArrays;
import mytools.Utilities;
import database.Report;
import mytools.Icons;

public class ReCountPanel extends JPanel implements ActionListener {

    public static final String TABLE_NAME = "RecuentoParte";
    
    public static final int NAME_COLUMN = 2;
    public static final int DIAG_COLUMN = 5;

    private JButton buttonSickPanel;
    private JButton buttonGetByDNI, buttonGetAll, buttonClean, buttonSearchByName;
    private JButton buttonReport;
    
    private JLabel labelGetByDNI, labelSearchByName, labelInfoSearchByName;
    private JTextField textGetByDNI, textSearchByName;
    private JTable table;
    private JScrollPane scrollTable;
    private DefaultTableModel model;
    private Dimension dimension;

    //Flags
    int pointer;
    boolean foud;

    private JScrollPane scrollContainer;

    private MainFrame mainFrame;
    private Configurator config;

    public ReCountPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        pointer = 0;
        foud = false;
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
        textSearchByName.setVisible(false);
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
        //LABELS----------------------------------------------------------------
        labelGetByDNI = new JLabel("Ingrese el DNI para buscar en el recuento:");
        labelGetByDNI.setBounds(40, 60, 300, 30);
        labelGetByDNI.setFont(utility.getFontLabelBig());
        labelGetByDNI.setForeground(Color.black);
        add(labelGetByDNI);
        labelSearchByName = new JLabel("Buscar por nombre:");
        labelSearchByName.setBounds(690, 60, 170, 30);
        labelSearchByName.setFont(utility.getFontLabelBig());
        labelSearchByName.setForeground(Color.black);
        labelSearchByName.setVisible(false);
        add(labelSearchByName);
        labelInfoSearchByName = new JLabel("<html>La lista completa del recuento esta "
                + "ordenada en funcion al momento que se les dio de alta medica en el sistema.</html>");
        labelInfoSearchByName.setBounds(690, 15, 210, 45);
        labelInfoSearchByName.setForeground(Color.black);
        labelInfoSearchByName.setVisible(false);
        add(labelInfoSearchByName);

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
        buttonSearchByName.setBounds(1005, 61, 70, 25);
        buttonSearchByName.addActionListener(this);
        buttonSearchByName.setVisible(false);
        add(buttonSearchByName);
        buttonSickPanel = utility.customButton();
        buttonSickPanel.setText("<html><center>Volver al Parte</center></html>");
        buttonSickPanel.setBounds(10, 15, 110, 35);
        buttonSickPanel.addActionListener(this);
        add(buttonSickPanel);
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
        if (e.getSource() == buttonGetByDNI) {
            getByDNI();
        }

        if (e.getSource() == buttonGetAll) {
            Receiver receiver = new Receiver();
            receiver.obtainInformation(this, 0, true);
            if (table.getRowCount() > 0) {
                labelInfoSearchByName.setVisible(true);
                labelSearchByName.setVisible(true);
                textSearchByName.setVisible(true);
                buttonSearchByName.setVisible(true);
            }
            receiver = null;
        }
        if (e.getSource() == buttonClean) {
            ((DefaultTableModel) table.getModel()).setRowCount(0);
            updateWindow();
            buttonSearchByName.setVisible(false);
            labelSearchByName.setVisible(false);
            labelInfoSearchByName.setVisible(false);
            textSearchByName.setVisible(false);
        }
        if (e.getSource() == buttonSickPanel) {
            pointer = 0;
            foud = false;
            ((DefaultTableModel) table.getModel()).setRowCount(0);
            updateWindow();
            buttonSearchByName.setVisible(false);
            labelSearchByName.setVisible(false);
            labelInfoSearchByName.setVisible(false);
            textSearchByName.setVisible(false);
        }
        if (e.getSource() == buttonSearchByName) {
            searchByName();
        }
        if (e.getSource() == buttonReport) {
            if (table.getRowCount() < 1) {
                JOptionPane.showMessageDialog(null, "No hay informacion mostrada, busque por DNI o presione el boton \"Todos\"");
            } else {
                Report report = new Report();
                report.createReCountReport(this);
                report = null;
            }

        }
    }

    //--------------------------------------------------------------------------
    //-----------------------METODOS / FUNCIONES--------------------------------
    private void getByDNI() {
        try {
            long dni = Long.parseLong(textGetByDNI.getText());
            labelSearchByName.setVisible(false);
            textSearchByName.setVisible(false);
            buttonSearchByName.setVisible(false);
            labelInfoSearchByName.setVisible(false);
            Receiver receptor = new Receiver();
            receptor.obtainInformation(this, dni, false);
            receptor = null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "El numero ingresado es incorrecto.");
        }
    }

    private void searchByName() {
        String nombre = "";
        String buscar = textSearchByName.getText().toLowerCase().trim();
        boolean searchNext = true;
        
        while (searchNext) {
            if (pointer == table.getRowCount()) {
                pointer = 0;
                if (!foud) {
                    searchNext = false;
                    JOptionPane.showMessageDialog(null, new JLabel("No se ha encontrado.", JLabel.CENTER));
                }
            } else {
                nombre = ((String) table.getValueAt(pointer, 2)).toLowerCase();
                if (nombre.contains(buscar)) {
                    scrollTable.getVerticalScrollBar().setValue(pointer * 16);
                    table.setRowSelectionInterval(pointer, pointer);
                    searchNext = false;
                    foud = true;
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
        model = null;
        tcm = null;
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

}
