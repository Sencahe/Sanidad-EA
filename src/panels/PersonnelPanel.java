package panels;

import dialogs.PersonnelFormulary;
import dialogs.Searcher;
import mytools.MyArrays;
import mytools.Utilities;
import database.DataBase;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import database.Report;
import dialogs.Configurator;
import dialogs.ListGenerator;
import java.util.HashMap;
import mytools.Icons;

public class PersonnelPanel extends JPanel implements ActionListener {

    public static final String TABLE_NAME = "Personal";

    JScrollPane scrollContainer;

    private JButton buttonAdd;
    private JButton buttonSickPanel;
    private JButton buttonReport;
    private JButton buttonReport2;

    private JTabbedPane tabbedPane;
    private JTable[] tables;
    private JScrollPane[] scrolls;
    
    private JLabel[] labelsSummary;

    //FILTROS DE LA TABLA
    private boolean filtered;

    private int filter;
    private int showBySubUnity;
    private int rowOrdering;

    private double IMCfilter;
    private String IMCoperator;
    private String PPSFilter;
    private String aptitudeFilter;
    private String pathologyColumn;

    //OBJETOS PARA LAS VENTANAS
    private MainFrame mainFrame;
    private PersonnelFormulary formulary;
    private Searcher searcher;
    private Configurator config;
    private ListGenerator listGenerator;

    //table model
    private DefaultTableModel[] model;

    public PersonnelPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        components();
        shortcuts();
    }

    private void components() {
        //------------------------------------------
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        //PROPIEDADES DEL PANEL-------------------------------------------------
        setBackground(utility.getColorFondo());
        Dimension dimension = new Dimension(1505, 580);
        setPreferredSize(dimension);
        setLayout(null);
        dimension = null;
        setOpaque(false);
        scrollContainer = new JScrollPane(this);
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //----------------------------------------------------------------------
        //PESTAÑAS DE LAS TABLAS
        UIManager.put("TabbedPane.contentOpaque", false);
        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.setBounds(10, 70, 1485, 460);
        tabbedPane.setFont(utility.getFuentePestañas());
        add(tabbedPane);
        String categories[] = {"   OFICIALES   ", " SUBOFICIALES ", "  SOLDADOS  ", "    CIVILES    "};
        //----------------------------------------------------------------------
        // TABLAS PRINCIPALES 
        scrolls = new JScrollPane[MyArrays.getCategoriasLength()];
        tables = new JTable[MyArrays.getCategoriasLength()];
        model = new DefaultTableModel[MyArrays.getCategoriasLength()];
        //objeto para centrar las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // ciclos para crear las distintas tablas a la vez
        for (int i = 0; i < tables.length; i++) {
            //creacion del table model              
            model[i] = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            // PROPIEDADES de la tabla
            tables[i] = new JTable(model[i]);
            tables[i].setGridColor(Color.black);
            tables[i].setBackground(utility.getColorTabla());
            tables[i].setFont(utility.getFuenteTabla());
            tables[i].setRowHeight(16);
            //eventos al presionar teclas en las tablas
            tables[i].getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            tables[i].getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int categoria = tabbedPane.getSelectedIndex();
                    int puntero = tables[categoria].getSelectedRow();
                    if (puntero != -1) {
                        int id = (int) tables[categoria].getModel().getValueAt(puntero, 20);
                        modifyPersonnel(id, puntero);
                        System.gc();
                    }
                }
            });
            //eventos al clikear la tabla          
            tables[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getClickCount() >= 2) {
                        int categorie = tabbedPane.getSelectedIndex();
                        int pointer = tables[categorie].rowAtPoint(e.getPoint());
                        int id = (int) tables[categorie].getModel().getValueAt(pointer, 20);
                        modifyPersonnel(id, pointer);
                        System.gc();
                    }
                }
            });
            //header de la tabla
            UIManager.put("TableHeader.font", utility.getFuenteHeader());
            JTableHeader header = tables[i].getTableHeader();
            header.setFont(utility.getFuenteHeader());
            header.setBackground(utility.getColorTabla());
            header.setPreferredSize(new Dimension(40, 27));
            ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            //creacion de las columnas
            for (int j = 0; j < MyArrays.getColumnasTablaLength(); j++) {
                model[i].addColumn(MyArrays.getColumnasTabla(j));
            }
            //tamaño de las columnas y filas
            for (int j = 0; j < MyArrays.getColumnasTablaLength(); j++) {
                tables[i].getColumnModel().getColumn(j).setMinWidth(MyArrays.getTamañoColumnas(j));
                tables[i].getColumnModel().getColumn(j).setMaxWidth(MyArrays.getTamañoColumnas(j));
                tables[i].getColumnModel().getColumn(j).setPreferredWidth(MyArrays.getTamañoColumnas(j));

                //centrado del contenido de las columnas
                if (j != 3 && j != 19) {
                    tables[i].getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
                }
                //ocultando la columna con el id
                if (j == 20) {
                    tables[i].getColumnModel().removeColumn(tables[i].getColumnModel().getColumn(j));
                }
            }
            //PROPIEDADES del Scroll y agregarlo al contenedor
            scrolls[i] = new JScrollPane(tables[i]);
            scrolls[i].getViewport().setBackground(utility.getColorFondo().brighter().brighter());
            scrolls[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            tabbedPane.addTab(categories[i], scrolls[i]);
        }

        //----------------------------------------------------------------------
        // BOTONES 
        buttonSickPanel = utility.customButton();
        buttonSickPanel.setText("<html><center>PARTE DE<br>SANIDAD</center></html>");
        buttonSickPanel.setBounds(10, 15, 110, 35);
        buttonSickPanel.setFont(utility.getFuenteBoton());
        add(buttonSickPanel);
        buttonAdd = utility.customButton();
        buttonAdd.setText("<html><center>AGREGAR<br>PERSONAL</center></html>");
        buttonAdd.setBounds(150, 15, 110, 35);
        buttonAdd.addActionListener(this);
        add(buttonAdd);
        //--------------------
        JLabel labelReport = new JLabel("Generar Reportes");
        labelReport.setFont(utility.getFuenteLabelGrande());
        labelReport.setForeground(Color.white);
        labelReport.setBounds(1000, 15, 150, 32);
        add(labelReport);
        JLabel labelPdf = new JLabel();
        labelPdf.setIcon(icons.getIconoPdf());
        labelPdf.setBounds(1150, 15, 32, 32);
        add(labelPdf);
        buttonReport = utility.customButton();
        buttonReport.setText("<html><center>Lista de<br>Personal</center></html>");
        buttonReport.setBounds(1190, 15, 110, 35);
        buttonReport.addActionListener(this);
        add(buttonReport);
        buttonReport2 = utility.customButton();
        buttonReport2.setText("<html><center>Carta de<br>Situacion</center></html>");
        buttonReport2.setBounds(1310, 15, 110, 35);
        buttonReport2.addActionListener(this);
        add(buttonReport2);
        //----------------------------------------------------------------------
        // LABELS CON CANTIDAD DE PERSONAL
        labelsSummary = new JLabel[tables.length + 1];
        int width = 0;
        int contenedorHeight = tabbedPane.getHeight();
        int contenedorY = tabbedPane.getY();
        for (int i = 0; i < labelsSummary.length; i++) {
            labelsSummary[i] = new JLabel();
            labelsSummary[i].setBounds(15 + width, contenedorHeight + contenedorY, 150, 40);
            labelsSummary[i].setForeground(Color.black);
            labelsSummary[i].setFont(utility.getFuenteLabelsResumen());
            width += i == 1 ? 200 : 170;
            add(labelsSummary[i]);
        }
        //----------------------------------------------------------------------
        //FINALIZACION DE LOS COMPONENTES
        utility = null;
        icons = null;

        update(0, 0, 0);
    }

    //---------------------------------------------------------------------------
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
        //---------------------- BOTONES -------------------------
        //BOTON AGREGAR
        if (e.getSource() == buttonAdd) {
            openFormulary(tabbedPane.getSelectedIndex());
        }
        if (e.getSource() == buttonReport) {
            listGenerator.setVisible(true);
        }
        //BOTON REPORTE
        if (e.getSource() == buttonReport2) {
            if (filtered) {
                JOptionPane.showMessageDialog(null, "<html><center>No puede generar la Carta de Situacion habiendo filtros en la tabla"
                        + "<br>Desactivelos con el Menu->Filtrar->Lista Completa</center></html>");
            } else {
                Report reporte = new Report();
                reporte.generarCartaDeSituacion(this);
                reporte = null;
            }
        }
    }

    //--------------------------------------------------------------------------
    //-----------------------METODOS / FUNCIONES--------------------------------
    private void modifyPersonnel(int id, int puntero) {
        formulary.obtenerDatos(id, puntero);
    }

    private void openFormulary(int cat) {
        formulary.setCategoria(cat);
        formulary.nuevoFormulario();
    }

    public void update(int filter, int showBySubUnity, int rowOrdering) {
        this.filter = filter;
        this.showBySubUnity = showBySubUnity;
        this.rowOrdering = rowOrdering;
        DataBase db = new DataBase();
        db.actualizar(this);
        db = null;
        System.gc();
    }

    public void ordenarColumnas() {
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

    //--------------------------------------------------------------------------
    //----------------------------ACCESOS RAPIDOS-------------------------------
    private void shortcuts() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), "abrirBuscador");
        getActionMap().put("abrirBuscador", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searcher.setVisible(true);
            }
        });
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "ordenarColumnas");
        getActionMap().put("ordenarColumnas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarColumnas();
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
    //-------------------------GETTER Y SETTERS---------------------------------
    public PersonnelFormulary getFormulary() {
        return formulary;
    }

    public void setFormulary(PersonnelFormulary formulary) {
        this.formulary = formulary;
    }

    public Searcher getSearcher() {
        return searcher;
    }

    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
    }

    public JScrollPane getScrollContainer() {
        return scrollContainer;
    }

    public DefaultTableModel getTableModel(int cat) {
        return (DefaultTableModel) tables[cat].getModel();
    }

    public JLabel getResumen(int cat) {
        return labelsSummary[cat];
    }

    public JTable getTablas(int cat) {
        return tables[cat];
    }

    public JScrollPane getScroll(int cat) {
        return scrolls[cat];
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JButton getButtonSickPanel() {
        return buttonSickPanel;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getShowBySubUnity() {
        return showBySubUnity;
    }

    public void setShowBySubUnity(int showBySubUnity) {
        this.showBySubUnity = showBySubUnity;
    }

    public int getRowOrdering() {
        return rowOrdering;
    }

    public void setRowOrdering(int rowOrdering) {
        this.rowOrdering = rowOrdering;
    }

    public String getPPSFilter() {
        return PPSFilter;
    }

    public void setPPSFilter(String PPSFilter) {
        this.PPSFilter = PPSFilter;
    }

    public String getAptitudeFilter() {
        return aptitudeFilter;
    }

    public void setAptitudeFilter(String aptitudeFilter) {
        this.aptitudeFilter = aptitudeFilter;
    }

    public String getPathologyColumn() {
        return pathologyColumn;
    }

    public void setPathologyColumn(String pathologyColumn) {
        this.pathologyColumn = pathologyColumn;
    }

    public void setIMCfilter(double IMCfilter) {
        this.IMCfilter = IMCfilter;
    }

    public double getIMCfilter() {
        return IMCfilter;
    }

    public String getIMCoperator() {
        return IMCoperator;
    }

    public void setIMCoperator(String IMCoperator) {
        this.IMCoperator = IMCoperator;
    }

    public void setConfig(Configurator config) {
        this.config = config;
    }

    public void setListGenerator(ListGenerator listGenerator) {
        this.listGenerator = listGenerator;
    }

}
