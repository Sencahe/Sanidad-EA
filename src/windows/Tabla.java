package windows;

import mytools.database.BaseDeDatos;
import mytools.Filtros;
import mytools.Iconos;
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

public class Tabla extends JFrame implements ActionListener {

    private JButton Agregar;

    private static JTabbedPane contenedor;
    private static JTable[] tablas;
    private static JScrollPane[] scrolls;

    private static JLabel[] resumen;

    private static JMenu menuFiltrar, menuRef, menuBuscar;
    private JMenu menuFiltroPPS;
    private JMenu menuFiltroAptitud;
    private JMenu menuPatologias;
    //
    private JMenu menuDestinos;
    //
    private JMenu menuOrdenar;

    private JMenuItem itemListaCompleta;
    private JMenuItem itemAnexoVencido;
    private JMenuItem[] itemsPPS;
    private JMenuItem[] itemsAptitud;
    private JMenuItem[] itemsPatologias;
    private JMenuItem itemObservaciones;
    //
    private JMenuItem[] itemsDestinos;
    //
    private static JMenuItem[] itemsOrdenar;

    private JMenuItem itemRef, itemBuscar;

    //Declaracion de iconos para los menuItems
    private ImageIcon check;

    //DECLARACION DE LOS OBJETOS PARA LOS JFRAMES
    private Formulario formulario;
    private Buscador buscador;
    private Referencias referencia;

    public Tabla() {
        Componentes();
        this.formulario = new Formulario(this, true);
        this.buscador = new Buscador(this, false);
        this.referencia = new Referencias(this, true);
    }

    private void Componentes() {
        //------------------------------------------
        Utilidades utilidad = new Utilidades();
        Iconos iconos = new Iconos();
        Arreglos arreglo = new Arreglos();
        check = iconos.getIconoCheck();
        //PROPIEDADES DEL FRAME PRINCIPAL        
        setTitle("Carta de Situacion - Seccion Sanidad RI-1");
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (pantalla.getWidth() < 1525 ? pantalla.getWidth() : 1525);
        int y = (int) (pantalla.getHeight() < 650 ? pantalla.getHeight() : 650);
        setSize(x, y);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(iconos.getIconoSanidad().getImage());
        pantalla = null;
        //jpanel para el fondo del frame
        JPanel container = new JPanel() {
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
        };
        container.setBackground(utilidad.getColorFondo());
        Dimension dimension = new Dimension(1505, 580);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        //scrollpane para el frame
        JScrollPane scrollContainer = new JScrollPane(container);
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //----------------------------------------------------------------------
        //PESTAÑAS DE LAS TABLAS
        UIManager.put("TabbedPane.selected", utilidad.getColorTabla());
        contenedor = new JTabbedPane();
        contenedor.setBounds(10, 10, 1485, 460);
        contenedor.setFont(utilidad.getFuentePestañas());
        container.add(contenedor);
        String categorias[] = {"   OFICIALES   ", " SUBOFICIALES ", "  SOLDADOS  ", "    CIVILES    "};
        //----------------------------------------------------------------------
        // TABLAS PRINCIPALES 
        scrolls = new JScrollPane[arreglo.getCategoriasLength()];
        tablas = new JTable[arreglo.getCategoriasLength()];
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
                        modificarFormulario(id, puntero);
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
                        modificarFormulario(id, puntero);
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
            for (int j = 0; j < arreglo.getColumnasTabla().length; j++) {
                model.addColumn(arreglo.getColumnasTabla()[j]);
            }
            //tamaño de las columnas
            for (int j = 0; j < arreglo.getColumnasTablaLength(); j++) {
                tablas[i].getColumnModel().getColumn(j).setMinWidth(arreglo.getTamañoColumnas()[j]);
                tablas[i].getColumnModel().getColumn(j).setMaxWidth(arreglo.getTamañoColumnas()[j]);
                tablas[i].getColumnModel().getColumn(j).setPreferredWidth(arreglo.getTamañoColumnas()[j]);
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
            scrolls[i].getViewport().setBackground(utilidad.getColorTabla());
            scrolls[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            contenedor.addTab(categorias[i], scrolls[i]);
            model = null;
        }

        //----------------------------------------------------------------------
        // BOTONES (por ahora solo uno)
        Agregar = new JButton("Nuevo +");
        Agregar.setBounds(10, 510, 100, 30);
        Agregar.setFont(utilidad.getFuenteBoton());
        Agregar.setVisible(true);
        Agregar.addActionListener(this);
        container.add(Agregar);
        //----------------------------------------------------------------------

        // BARRA MENU----------------------------------------------------------        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // MMENU FILTRAR ----------------------- 
        menuFiltrar = new JMenu("Filtrar");
        menuBar.add(menuFiltrar);
        //ITEM LISTA COMPLETA
        itemListaCompleta = new JMenuItem("Lista Completa");
        itemListaCompleta.setIcon(iconos.getIconoRefrescar());
        itemListaCompleta.addActionListener(this);
        menuFiltrar.add(itemListaCompleta);
        JPopupMenu.Separator separador1 = new JPopupMenu.Separator();
        menuFiltrar.add(separador1);
        //ITEMS FILTROS
        // Anexo vencido
        itemAnexoVencido = new JMenuItem("Anexos Vencidos");
        itemAnexoVencido.addActionListener(this);
        menuFiltrar.add(itemAnexoVencido);
        // Programa Peso Saludable
        menuFiltroPPS = new JMenu("Programa Peso Saludable");
        menuFiltrar.add(menuFiltroPPS);
        itemsPPS = new JMenuItem[arreglo.getPPSLength()];
        for (int i = 0; i < itemsPPS.length; i++) {
            itemsPPS[i] = new JMenuItem(arreglo.getPPS()[i]);
            itemsPPS[i].addActionListener(this);
            menuFiltroPPS.add(itemsPPS[i]);
        }
        itemsPPS[0].setText("Todos");
        //Aptitud
        menuFiltroAptitud = new JMenu("Aptitud");
        menuFiltrar.add(menuFiltroAptitud);
        itemsAptitud = new JMenuItem[arreglo.getAptitudLength()];
        for (int i = 0; i < itemsAptitud.length; i++) {
            itemsAptitud[i] = new JMenuItem(arreglo.getAptitud()[i]);
            itemsAptitud[i].addActionListener(this);
            menuFiltroAptitud.add(itemsAptitud[i]);
        }
        itemsAptitud[0].setText("Todos");
        //Patologias
        menuPatologias = new JMenu("Patologias");
        menuFiltrar.add(menuPatologias);
        itemsPatologias = new JMenuItem[arreglo.getPatologias().length];
        for (int i = 0; i < itemsPatologias.length; i++) {
            if (i == itemsPatologias.length - 1) {
                JPopupMenu.Separator separadorPatologias = new JPopupMenu.Separator();
                menuPatologias.add(separadorPatologias);
                separadorPatologias = null;
            }
            itemsPatologias[i] = new JMenuItem(arreglo.getPatologias()[i]);
            itemsPatologias[i].addActionListener(this);
            menuPatologias.add(itemsPatologias[i]);
        }
        //Observaciones
        itemObservaciones = new JMenuItem("Observaciones");
        itemObservaciones.addActionListener(this);
        menuFiltrar.add(itemObservaciones);
        JPopupMenu.Separator separador2 = new JPopupMenu.Separator();
        menuFiltrar.add(separador2);
        separador2 = null;
        //Destinos
        menuDestinos = new JMenu("Mostrar por destino");
        menuFiltrar.add(menuDestinos);
        itemsDestinos = new JMenuItem[arreglo.getDestinosLength()];
        for (int i = 0; i < itemsDestinos.length; i++) {
            itemsDestinos[i] = new JMenuItem(arreglo.getDestinos()[i]);
            itemsDestinos[i].addActionListener(this);
            menuDestinos.add(itemsDestinos[i]);
        }
        itemsDestinos[0].setText("Todos");
        JPopupMenu.Separator separador3 = new JPopupMenu.Separator();
        menuFiltrar.add(separador3);
        separador3 = null;
        //ordenamiento de la tabla
        menuOrdenar = new JMenu("Ordenar por...");
        menuFiltrar.add(menuOrdenar);
        itemsOrdenar = new JMenuItem[arreglo.getOrdenTabla().length];
        for (int i = 0; i < itemsOrdenar.length; i++) {
            itemsOrdenar[i] = new JMenuItem(arreglo.getOrdenTabla()[i]);
            itemsOrdenar[i].addActionListener(this);
            menuOrdenar.add(itemsOrdenar[i]);
        }
        //MENU REFERENCIAS--------------------------------- 
        menuRef = new JMenu("Ref.");
        menuBar.add(menuRef);
        itemRef = new JMenuItem("Referencias");
        itemRef.addActionListener(this);
        menuRef.add(itemRef);
        //MENU BUSCAR-------------------------------------- 
        menuBuscar = new JMenu("Buscar");
        menuBar.add(menuBuscar);
        itemBuscar = new JMenuItem("Buscar...");
        itemBuscar.addActionListener(this);
        itemBuscar.setIcon(iconos.getIconoSearchChico());
        menuBuscar.add(itemBuscar);
        menuBar = null;
        //----------------------------------------------------------------------
        // LABELS CON CANTIDAD DE PERSONAL
        resumen = new JLabel[tablas.length + 1];
        int width = 0;
        for (int i = 0; i < resumen.length; i++) {
            resumen[i] = new JLabel();
            resumen[i].setBounds(15 + width, contenedor.getHeight() + 10, 150, 40);
            resumen[i].setFont(utilidad.getFuenteLabelsResumen());
            width += i == 1 ? 200 : 170;
            container.add(resumen[i]);
        }
        //----------------------------------------------------------------------      
        //----------------------------------------------------------------------
        //FINALIZACION DE LOS COMPONENTES
        this.getContentPane().add(scrollContainer);
        scrollContainer = null;
        container = null;
        utilidad = null;
        arreglo = null;
        iconos = null;
        Actualizar(0, 0, 0);

    }

    //-------------------------------------------------------------------------------------------------
    //-----------------------------EVENTO BOTONES------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {

        //BOTON AGREGAR
        if (e.getSource() == Agregar) {
            abrirFormulario(contenedor.getSelectedIndex());

        }
        //----------------------BARRA MENU--------------------------------------
        //MENU FILTRAR--------------------------------FILTROS-----------------
        //Lista completa
        if (e.getSource() == itemListaCompleta) {
            Actualizar(0, 0, 0);
            eliminarChecks();
        }
        // anexo vencido
        if (e.getSource() == itemAnexoVencido) {
            Actualizar(1, Filtros.getShowByDestino(), Filtros.getShowByDestino());
            eliminarChecksFiltros();
            itemAnexoVencido.setIcon(check);
        }
        // programa peso saludable
        for (int i = 0; i < itemsPPS.length; i++) {
            if (e.getSource() == itemsPPS[i]) {
                if (i != 0) {
                    Filtros.setPPSColumn(itemsPPS[i].getText());
                    Actualizar(2, Filtros.getShowByDestino(), Filtros.getOrder());
                } else {
                    Actualizar(0, Filtros.getShowByDestino(), Filtros.getOrder());
                }
                eliminarChecksFiltros();
                menuFiltroPPS.setIcon(check);
                itemsPPS[i].setIcon(check);
            }
        }
        // aptitudes
        for (int i = 0; i < itemsAptitud.length; i++) {
            if (e.getSource() == itemsAptitud[i]) {
                if (i != 0) {
                    Filtros.setAptitudColumn(itemsAptitud[i].getText());
                    Actualizar(3, Filtros.getShowByDestino(), Filtros.getOrder());
                } else {
                    Actualizar(0, Filtros.getShowByDestino(), Filtros.getOrder());
                }
                eliminarChecksFiltros();
                menuFiltroAptitud.setIcon(check);
                itemsAptitud[i].setIcon(check);
            }
        }
        // patologias
        for (int i = 0; i < itemsPatologias.length; i++) {
            if (e.getSource() == itemsPatologias[i]) {
                if (i != itemsPatologias.length - 1) {
                    Filtros.setPatologiaColumn(itemsPatologias[i].getText());
                    Actualizar(4, Filtros.getShowByDestino(), Filtros.getOrder());
                } else {
                    Actualizar(5, Filtros.getShowByDestino(), Filtros.getOrder());
                }
                eliminarChecksFiltros();
                menuPatologias.setIcon(check);
                itemsPatologias[i].setIcon(check);
            }
        }
        // observaciones 
        if (e.getSource() == itemObservaciones) {
            Actualizar(6, Filtros.getShowByDestino(), Filtros.getOrder());
            eliminarChecksFiltros();
            itemObservaciones.setIcon(check);
        }
        // destinos
        for (int i = 0; i < itemsDestinos.length; i++) {
            if (e.getSource() == itemsDestinos[i]) {
                Actualizar(Filtros.getFilter(), i, Filtros.getOrder());
                eliminarChecksDestino();
                menuDestinos.setIcon(check);
                itemsDestinos[i].setIcon(check);
            }
        }
        //ordenar por
        for (int i = 0; i < itemsOrdenar.length; i++) {
            if (e.getSource() == itemsOrdenar[i]) {
                Actualizar(Filtros.getFilter(), Filtros.getShowByDestino(), i);
                eliminarChecksOrden();
                menuOrdenar.setIcon(check);
                itemsOrdenar[i].setIcon(check);
            }
        }
        //MENU REFERENCIAS-----------------------------------------
        if (e.getSource() == itemRef) {
            referencia.setVisible(true);;
            System.gc();
        }

        //MENU BUSCAR--------------------------------------------
        if (e.getSource() == itemBuscar) {
            menuFiltrar.setEnabled(false);
            menuRef.setEnabled(false);
            menuBuscar.setEnabled(false);
            buscador.setVisible(true);
            System.gc();
        }
    }

    //----------------------------------------------------------------------------------------------------
    //------------------ MODIFICAR FORMULARIO--------------------------------------------------------------
    private void modificarFormulario(int id, int puntero) {
        formulario.obtenerDatos(id, puntero);
        formulario.setVisible(true);
    }

    private void abrirFormulario(int cat) {
        formulario.setCategoria(cat);
        formulario.setVisible(true);
    }

    //-------------------------------------------------------------------------------------------------
    //---------------------- LLENAR TABLA--------------------------------------------------------------
    private void Actualizar(int filtro, int destino, int order) {
        Filtros.setFilter(filtro);
        Filtros.setShowByDestino(destino);
        Filtros.setOrder(order);
        BaseDeDatos bdd = new BaseDeDatos();
        bdd.Actualizar();
        bdd = null;
        System.gc();
    }
    //--------------------------------------------------------------------------------------------------
    //---------------------- CHECK ICONS ---------------------------------------------------------------

    public void eliminarChecksFiltros() {
        itemAnexoVencido.setIcon(null);

        menuFiltroPPS.setIcon(null);
        for (int i = 0; i < itemsPPS.length; i++) {
            itemsPPS[i].setIcon(null);
        }
        menuFiltroAptitud.setIcon(null);
        for (int i = 0; i < itemsAptitud.length; i++) {
            itemsAptitud[i].setIcon(null);
        }
        menuPatologias.setIcon(null);
        for (int i = 0; i < itemsPatologias.length; i++) {
            itemsPatologias[i].setIcon(null);
        }
        itemObservaciones.setIcon(null);
    }

    public void eliminarChecksDestino() {
        for (int i = 0; i < itemsDestinos.length; i++) {
            itemsDestinos[i].setIcon(null);
        }
    }

    public void eliminarChecksOrden() {
        for (int i = 0; i < itemsOrdenar.length; i++) {
            itemsOrdenar[i].setIcon(null);
        }
    }

    public void eliminarChecks() {
        //FILTROS
        eliminarChecksFiltros();
        //DESTINOS
        menuDestinos.setIcon(null);
        eliminarChecksDestino();
        //ORDEN
        menuOrdenar.setIcon(null);
        eliminarChecksOrden();
    }

    //--------GETTER Y SETTERS---------------------------
    public static DefaultTableModel getTableModel(int cat) {
        return (DefaultTableModel) tablas[cat].getModel();
    }

    public static JLabel getResumen(int cat) {
        return resumen[cat];
    }

    public static JTable getTablas(int cat) {
        return tablas[cat];
    }

    public static JScrollPane getScroll(int cat) {
        return scrolls[cat];
    }

    public static JTabbedPane getContenedor() {
        return contenedor;
    }

    public static void habilitarMenuFiltrar() {
        menuFiltrar.setEnabled(true);
    }

    public static void habilitarMenuBuscar() {
        menuBuscar.setEnabled(true);
    }

    public static void habilitarMenuRef() {
        menuRef.setEnabled(true);
    }

}
