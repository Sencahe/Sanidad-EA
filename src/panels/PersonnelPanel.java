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
import dialogs.CaducatedStudies;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import mytools.Icons;
import mytools.mycomponents.MyJScrollPane;

public class PersonnelPanel extends JPanel implements ActionListener {

    public static final String TABLE_NAME = "Personal";

    public static final int NAME_COLUMN = 3;
    public static final int DNI_COLUMN = 5;
    public static final int OBS_COLUMN = 19;
    public static final int ID_COLUMN = 20;

    JScrollPane scrollContainer;

    private JButton buttonAdd;
    private JButton buttonSickPanel;
    private JButton buttonReport;
    private JButton buttonReport2;
    private JButton buttonReport3;

    private JTabbedPane tabbedPane;
    private JTable[] tables;
    private JScrollPane[] scrolls;

    private JLabel[] labelsSummary;
    
    
    //FILTROS DE LA TABLA
    private ArrayList<Integer> filterList;
    
    public static final int FILTER_A27 = 1,
                            FILTER_PPS = 2,
                            FILTER_APTITUDE = 3,
                            FILTER_PATHOLOGY = 4,
                            FILTER_AJM = 5,
                            FILTER_OBS = 6,
                            FILTER_IMC = 7,
                            FILTER_ALL_PATHOLOGIES = 8,
                            FILTER_GENRE = 9;
    
    
    private boolean filtered;

    private int showBySubUnity;
    private int rowOrdering;

    private double IMCfilter;
    private int studiesFilter;
    private int studiesFilter2;
    private boolean betweenTwoDates;
    private String IMCoperator;
    private String PPSFilter;
    private String aptitudeFilter;
    private String pathologyColumn;
    private char genreFilter;

    //OBJETOS PARA LAS VENTANAS
    private MainFrame mainFrame;
    private PersonnelFormulary formulary;
    private Searcher searcher;
    private Configurator config;
    private ListGenerator listGenerator;
    private CaducatedStudies caducatedStudies;

    
    public PersonnelPanel(MainFrame mainFrame) {       
        this.mainFrame = mainFrame;
        this.betweenTwoDates = false;
        filterList = new ArrayList<Integer>();
        
        components();
        shortcuts();
    }

    private void components() {
        //------------------------------------------
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        //PROPIEDADES DEL PANEL-------------------------------------------------
        setBackground(utility.getColorBackground());
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
        tabbedPane.setFont(utility.getFontTabbedPane());
        add(tabbedPane);
        String categories[] = {"   OFICIALES   ", " SUBOFICIALES ", "  SOLDADOS  ", "    CIVILES    "};
        //----------------------------------------------------------------------
        // TABLAS PRINCIPALES 
        scrolls = new JScrollPane[MyArrays.getCategoriesLength()];
        tables = new JTable[MyArrays.getCategoriesLength()];
        //objeto para centrar las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // ciclos para crear las distintas tablas a la vez
        for (int i = 0; i < tables.length; i++) {
            // PROPIEDADES de la tabla
            tables[i] = utility.customTable(this, NAME_COLUMN, OBS_COLUMN);
            //eventos al presionar teclas en lastablas
            tables[i].getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            tables[i].getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int categorie = tabbedPane.getSelectedIndex();
                    int pointer = tables[categorie].getSelectedRow();
                    if (pointer != -1) {
                        int id = (int) tables[categorie].getModel().getValueAt(pointer, 20);
                        modifyPersonnel(id, pointer);
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

            //PROPIEDADES del Scroll y agregarlo al tabbedPane------------------
            scrolls[i] = new MyJScrollPane(tables[i]);
            scrolls[i].getViewport().setBackground(utility.getColorBackground());
            scrolls[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            tabbedPane.addTab(categories[i], scrolls[i]);
        }
        //----------------------------------------------------------------------
        // BOTONES 
        buttonSickPanel = utility.customButton();
        buttonSickPanel.setText("<html><center>PARTE DE<br>SANIDAD</center></html>");
        buttonSickPanel.setBounds(10, 15, 110, 35);
        buttonSickPanel.setFont(utility.getFontButton());
        add(buttonSickPanel);
        buttonAdd = utility.customButton();
        buttonAdd.setText("<html><center>Agregar<br>Personal</center></html>");
        buttonAdd.setBounds(150, 15, 110, 35);
        buttonAdd.addActionListener(this);
        add(buttonAdd);
        //--------------------
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
        buttonReport.setText("<html><center>Lista de<br>Personal</center></html>");
        buttonReport.setBounds(1090, 15, 110, 35);
        buttonReport.addActionListener(this);
        add(buttonReport);
        buttonReport2 = utility.customButton();
        buttonReport2.setText("<html><center>Carta de<br>Situacion</center></html>");
        buttonReport2.setBounds(1210, 15, 110, 35);
        buttonReport2.addActionListener(this);
        add(buttonReport2);
        buttonReport3 = utility.customButton();
        buttonReport3.setText("<html><center>Control de<br>IMC</center></html>");
        buttonReport3.setBounds(1330, 15, 110, 35);
        buttonReport3.addActionListener(this);
        add(buttonReport3);

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
            labelsSummary[i].setFont(utility.getFontLabelSummary());
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
                reporte.createSituationChart(this);
                reporte = null;
            }
        }
        if (e.getSource() == buttonReport3) {
            Report reporte = new Report();
            reporte.createOverWeightControlList();
            reporte = null;
        }
    }

    //--------------------------------------------------------------------------
    //-----------------------METODOS / FUNCIONES--------------------------------
    private void modifyPersonnel(int id, int puntero) {
        formulary.obtainData(id, puntero);
    }

    private void openFormulary(int cat) {
        formulary.setCategorie(cat);
        formulary.newPersonnel();
    }

    public void update(int filter, int showBySubUnity, int rowOrdering) {
        if(filter == 0){
            filterList.clear();
        } else if(filter > 0){
            if(!filterList.contains(filter)){
                filterList.add(filter);
            } 
        }
        this.showBySubUnity = showBySubUnity;
        this.rowOrdering = rowOrdering;
        DataBase db = new DataBase();
        db.update(this);
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

    public JLabel getSummary(int cat) {
        return labelsSummary[cat];
    }

    public JTable getTables(int cat) {
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

    public ArrayList<Integer> getFilterList() {
        return filterList;
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

    public int getStudiesFilter() {
        return studiesFilter;
    }

    public void setStudiesFilter(int studiesFilter) {
        this.studiesFilter = studiesFilter;
    }
    
    public void setConfig(Configurator config) {
        this.config = config;
    }

    public void setListGenerator(ListGenerator listGenerator) {
        this.listGenerator = listGenerator;
    }

    public void setCaducatedStudies(CaducatedStudies caducatedStudies) {
        this.caducatedStudies = caducatedStudies;
    }

    public int getStudiesFilter2() {
        return studiesFilter2;
    }

    public void setStudiesFilter2(int studiesFilter2) {
        this.studiesFilter2 = studiesFilter2;
    }

    public boolean isBetweenTwoDates() {
        return betweenTwoDates;
    }

    public void setBetweenTwoDates(boolean betweenTwoDates) {
        this.betweenTwoDates = betweenTwoDates;
    }

    public char getGenreFilter() {
        return genreFilter;
    }

    public void setGenreFilter(char genreFilter) {
        this.genreFilter = genreFilter;
    }
       
    
}
