package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import mytools.Arreglos;
import mytools.Utilidades;
import windows.parte.FormularioParte;
import windows.parte.Parte;
import database.BaseDeDatos;
import main.MainFrame;
import mytools.Iconos;

public class Tabla extends JPanel implements ActionListener {

    JScrollPane scrollContainer;
    
    private JButton botonAgregar;
    private JButton botonParte;

    private JTabbedPane contenedor;
    private JTable[] tablas;
    private JScrollPane[] scrolls;

    private JLabel[] resumen;

    //FILTROS DE LA TABLA
    private int filter;
    private int showByDestino;
    private int order;

    private String PPSFilter;
    private String aptitudFilter;
    private String patologiaColumn;

    
    //OBJETOS PARA LAS VENTANAS
    private Formulario formulario;

    
    public Tabla() {
        Componentes();
    }

  

    private void Componentes() {
        //------------------------------------------
        Utilidades utilidad = new Utilidades();
        //PROPIEDADES DEL PANEL
        setBackground(utilidad.getColorFondo());
        Dimension dimension = new Dimension(1505, 580);
        setPreferredSize(dimension);
        setLayout(null);
        dimension = null;

        scrollContainer = new JScrollPane(this);
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //----------------------------------------------------------------------
        //PESTAÑAS DE LAS TABLAS
        UIManager.put("TabbedPane.contentOpaque", false);
        contenedor = new JTabbedPane();
        contenedor.setOpaque(false);
        contenedor.setBounds(10, 10, 1485, 460);
        contenedor.setFont(utilidad.getFuentePestañas());
        add(contenedor);
        String categorias[] = {"   OFICIALES   ", " SUBOFICIALES ", "  SOLDADOS  ", "    CIVILES    "};
        //----------------------------------------------------------------------
        // TABLAS PRINCIPALES 
        scrolls = new JScrollPane[Arreglos.getCategoriasLength()];
        tablas = new JTable[Arreglos.getCategoriasLength()];

        //objeto para centrar las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // ciclos para crear las distintas tablas a la vez
        for (int i = 0; i < tablas.length; i++) {
            //creacion del table model              
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            // PROPIEDADES de la tabla
            tablas[i] = new JTable(model);
            tablas[i].setGridColor(Color.black);
            tablas[i].setBackground(utilidad.getColorTabla());
            tablas[i].setFont(utilidad.getFuenteTabla());
            //eventos al presionar teclas en las tablas
            tablas[i].getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
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
            JTableHeader header = tablas[i].getTableHeader();
            header.setFont(utilidad.getFuenteHeader());
            header.setBackground(utilidad.getColorTabla());
            header.setReorderingAllowed(false);
            ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            //creacion de las columnas
            for (int j = 0; j < Arreglos.getColumnasTablaLength(); j++) {
                model.addColumn(Arreglos.getColumnasTabla(j));
            }
            //tamaño de las columnas
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
            scrolls[i].getViewport().setBackground(utilidad.getColorFondo());
            scrolls[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            contenedor.addTab(categorias[i], scrolls[i]);
            model = null;
        }

        //----------------------------------------------------------------------
        // BOTONES (por ahora solo uno)
        botonAgregar = new JButton("Nuevo +");
        botonAgregar.setBounds(10, 520, 100, 30);
        botonAgregar.setFont(utilidad.getFuenteBoton());
        botonAgregar.setVisible(true);
        botonAgregar.addActionListener(this);
        add(botonAgregar);
        botonParte = new JButton("PARTE");
        botonParte.setBounds(150, 520, 100, 30);
        botonParte.setFont(utilidad.getFuenteBoton());
        botonParte.setVisible(true);
        add(botonParte);
        //----------------------------------------------------------------------
        // LABELS CON CANTIDAD DE PERSONAL
        resumen = new JLabel[tablas.length + 1];
        int width = 0;
        for (int i = 0; i < resumen.length; i++) {
            resumen[i] = new JLabel();
            resumen[i].setBounds(15 + width, contenedor.getHeight() + 10, 150, 40);
            resumen[i].setFont(utilidad.getFuenteLabelsResumen());
            width += i == 1 ? 200 : 170;
            add(resumen[i]);
        }
        //----------------------------------------------------------------------      
        //----------------------------------------------------------------------
        //FINALIZACION DE LOS COMPONENTES
        utilidad = null;

        Actualizar(0, 0, 0);

    }

    //FONDO DEL PANEL
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(50, 500,
                getBackground().brighter().brighter(), 200, 170,
                getBackground().darker().darker());
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    //-------------------------------------------------------------------------------------------------
    //-----------------------------EVENTO BOTONES------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {

        //---------------------- BOTONES ---------------------------------------
        //BOTON AGREGAR
        if (e.getSource() == botonAgregar) {
            abrirFormulario(contenedor.getSelectedIndex());
        }

    }

    //---------------------------------------------------------------------------
    //---------------------------- FORMULARIO-----------------------------------
    private void modificarPersonal(int id, int puntero) {
        formulario.obtenerDatos(id, puntero);
    }

    private void abrirFormulario(int cat) {
        formulario.setCategoria(cat);
        formulario.nuevoFormulario();
    }

    //-------------------------------------------------------------------------------------------------
    //---------------------- LLENAR TABLA--------------------------------------------------------------
    public void Actualizar(int filtro, int destino, int order) {
        this.filter = filtro;
        this.showByDestino = destino;
        this.order = order;
        BaseDeDatos bdd = new BaseDeDatos();
        bdd.actualizar(this);
        bdd = null;
        System.gc();
    }

    //--------GETTER Y SETTERS---------------------------
    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
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

}
