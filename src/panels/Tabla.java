package panels;

import dialogs.Formulario;
import dialogs.Buscador;
import mytools.Arreglos;
import mytools.Utilidades;
import database.BaseDeDatos;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import database.Reportes;
import main.Configuracion;

public class Tabla extends JPanel implements ActionListener {

    public static final String TABLA = "Personal";

    JScrollPane scrollContainer;

    private JButton botonAgregar;
    private JButton botonParte;
    private JButton botonReporte;

    private JTabbedPane contenedor;
    private JTable[] tablas;
    private JScrollPane[] scrolls;

    private JLabel[] resumen;

    //FILTROS DE LA TABLA
    private int filter;
    private int showByDestino;
    private int order;

    private double IMCfilter;
    private String IMCoperator;
    private String PPSFilter;
    private String aptitudFilter;
    private String patologiaColumn;

    //OBJETOS PARA LAS VENTANAS
    private MainFrame mainFrame;
    private Formulario formulario;
    private Buscador buscador;
    private Configuracion config;

    //table model
    private DefaultTableModel[] model;

    public Tabla(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        componentes();
        shortcut();
    }

    private void componentes() {
        //------------------------------------------
        Utilidades utilidad = mainFrame.getUtilidad();
        //PROPIEDADES DEL PANEL-------------------------------------------------
        setBackground(utilidad.getColorFondo());
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
        contenedor = new JTabbedPane();
        contenedor.setOpaque(false);
        contenedor.setBounds(10, 70, 1485, 460);
        contenedor.setFont(utilidad.getFuentePestañas());
        add(contenedor);
        String categorias[] = {"   OFICIALES   ", " SUBOFICIALES ", "  SOLDADOS  ", "    CIVILES    "};
        //----------------------------------------------------------------------
        // TABLAS PRINCIPALES 
        scrolls = new JScrollPane[Arreglos.getCategoriasLength()];
        tablas = new JTable[Arreglos.getCategoriasLength()];
        model = new DefaultTableModel[Arreglos.getCategoriasLength()];
        //objeto para centrar las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // ciclos para crear las distintas tablas a la vez
        for (int i = 0; i < tablas.length; i++) {
            //creacion del table model              
            model[i] = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            // PROPIEDADES de la tabla
            tablas[i] = new JTable(model[i]);
            tablas[i].setGridColor(Color.black);
            tablas[i].setBackground(utilidad.getColorTabla());
            tablas[i].setFont(utilidad.getFuenteTabla());
            tablas[i].setRowHeight(16);
            //eventos al presionar teclas en las tablas
            tablas[i].getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            tablas[i].getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int categoria = contenedor.getSelectedIndex();
                    int puntero = tablas[categoria].getSelectedRow();
                    if (puntero != -1) {
                        int id = (int) tablas[categoria].getModel().getValueAt(puntero, 20);
                        modificarPersonal(id, puntero);
                        System.gc();
                    }
                }
            });
            //eventos al clikear la tabla          
            tablas[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getClickCount() >= 2) {
                        int categoria = contenedor.getSelectedIndex();
                        int puntero = tablas[categoria].rowAtPoint(e.getPoint());
                        int id = (int) tablas[categoria].getModel().getValueAt(puntero, 20);
                        modificarPersonal(id, puntero);
                        System.gc();
                    }
                }
            });
            //header de la tabla
            UIManager.put("TableHeader.font", utilidad.getFuenteHeader());
            JTableHeader header = tablas[i].getTableHeader();
            header.setFont(utilidad.getFuenteHeader());
            header.setBackground(utilidad.getColorTabla());
            header.setPreferredSize(new Dimension(40, 27));
            ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            //creacion de las columnas
            for (int j = 0; j < Arreglos.getColumnasTablaLength(); j++) {
                model[i].addColumn(Arreglos.getColumnasTabla(j));
            }
            //tamaño de las columnas y filas
            for (int j = 0; j < Arreglos.getColumnasTablaLength(); j++) {
                tablas[i].getColumnModel().getColumn(j).setMinWidth(Arreglos.getTamañoColumnas(j));
                tablas[i].getColumnModel().getColumn(j).setMaxWidth(Arreglos.getTamañoColumnas(j));
                tablas[i].getColumnModel().getColumn(j).setPreferredWidth(Arreglos.getTamañoColumnas(j));

                //centrado del contenido de las columnas
                if (j != 3 && j != 19) {
                    tablas[i].getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
                }
                //ocultando la columna con el id
                if (j == 20) {
                    tablas[i].getColumnModel().removeColumn(tablas[i].getColumnModel().getColumn(j));
                }
            }
            //PROPIEDADES del Scroll y agregarlo al contenedor
            scrolls[i] = new JScrollPane(tablas[i]);
            scrolls[i].getViewport().setBackground(utilidad.getColorFondo().brighter().brighter());
            scrolls[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            contenedor.addTab(categorias[i], scrolls[i]);
        }

        //----------------------------------------------------------------------
        // BOTONES 
        botonParte = utilidad.customButton();
        botonParte.setText("<html><center>PARTE DE<br>SANIDAD</center></html>");
        botonParte.setBounds(10, 15, 110, 35);
        botonParte.setFont(utilidad.getFuenteBoton());
        add(botonParte);
        botonAgregar = utilidad.customButton();
        botonAgregar.setText("<html><center>AGREGAR<br>PERSONAL</center></html>");
        botonAgregar.setBounds(150, 15, 110, 35);
        botonAgregar.addActionListener(this);
        add(botonAgregar);
        botonReporte = utilidad.customButton();
        botonReporte.setText("<html><center>GENERAR<br>REPORTE</center></html>");
        botonReporte.setBounds(350, 15, 110, 35);
        botonReporte.addActionListener(this);
        add(botonReporte);
        //----------------------------------------------------------------------
        // LABELS CON CANTIDAD DE PERSONAL
        resumen = new JLabel[tablas.length + 1];
        int width = 0;
        int contenedorHeight = contenedor.getHeight();
        int contenedorY = contenedor.getY();
        for (int i = 0; i < resumen.length; i++) {
            resumen[i] = new JLabel();
            resumen[i].setBounds(15 + width, contenedorHeight + contenedorY, 150, 40);
            resumen[i].setForeground(Color.black);
            resumen[i].setFont(utilidad.getFuenteLabelsResumen());
            width += i == 1 ? 200 : 170;
            add(resumen[i]);
        }
        //----------------------------------------------------------------------
        //FINALIZACION DE LOS COMPONENTES
        utilidad = null;
        
        Actualizar(0, 0, 0);
    }

    //---------------------------------------------------------------------------
    //----------------------------ACCESOS RAPIDOS-------------------------------
    private void shortcut() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), "abrirBuscador");
        getActionMap().put("abrirBuscador", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscador.setVisible(true);
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
        if (e.getSource() == botonAgregar) {
            abrirFormulario(contenedor.getSelectedIndex());
        }
        //BOTON REPORTE
        if(e.getSource() == botonReporte){
            Reportes reporte = new Reportes();
            reporte.generarReportePersonal(this);
        }

    }

    //--------------------------------------------------------------------------
    //---------------------------- FORMULARIO-----------------------------------
    private void modificarPersonal(int id, int puntero) {
        formulario.obtenerDatos(id, puntero);
    }

    private void abrirFormulario(int cat) {
        formulario.setCategoria(cat);
        formulario.nuevoFormulario();
    }

    //--------------------------------------------------------------------------
    //---------------------- LLENAR TABLA---------------------------------------
    public void Actualizar(int filtro, int destino, int order) {
        this.filter = filtro;
        this.showByDestino = destino;
        this.order = order;
        BaseDeDatos bdd = new BaseDeDatos();
        bdd.actualizar(this);
        bdd = null;
        System.gc();
    }

    //--------------------------------------------------------------------------
    //---------------------------ORDENAR COLUMNAS-------------------------------
    public void ordenarColumnas() {
        for (int i = 0; i < 4; i++) {
            TableModel model = tablas[i].getModel();
            TableColumnModel tcm = tablas[i].getColumnModel();
            for (int j = 0; j < model.getColumnCount() - 1; j++) {
                int location = tcm.getColumnIndex(model.getColumnName(j));
                tcm.moveColumn(location, j);
            }
            model = null;
            tcm = null;
        }

    }

    //--------GETTER Y SETTERS---------------------------
    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public Buscador getBuscador() {
        return buscador;
    }

    public void setBuscador(Buscador buscador) {
        this.buscador = buscador;
    }

    public JScrollPane getScrollContainer() {
        return scrollContainer;
    }

    public DefaultTableModel getTableModel(int cat) {
        return (DefaultTableModel) tablas[cat].getModel();
    }

    public JLabel getResumen(int cat) {
        return resumen[cat];
    }

    public JTable getTablas(int cat) {
        return tablas[cat];
    }

    public JScrollPane getScroll(int cat) {
        return scrolls[cat];
    }

    public JTabbedPane getContenedor() {
        return contenedor;
    }

    public JButton getBotonParte() {
        return botonParte;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getShowByDestino() {
        return showByDestino;
    }

    public void setShowByDestino(int showByDestino) {
        this.showByDestino = showByDestino;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPPSFilter() {
        return PPSFilter;
    }

    public void setPPSFilter(String PPSFilter) {
        this.PPSFilter = PPSFilter;
    }

    public String getAptitudFilter() {
        return aptitudFilter;
    }

    public void setAptitudFilter(String aptitudFilter) {
        this.aptitudFilter = aptitudFilter;
    }

    public String getPatologiaColumn() {
        return patologiaColumn;
    }

    public void setPatologiaColumn(String patologiaColumn) {
        this.patologiaColumn = patologiaColumn;
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

    public void setConfig(Configuracion config) {
        this.config = config;
    }

}
