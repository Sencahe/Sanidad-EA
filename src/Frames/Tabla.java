package Frames;

import Tools.Arreglos;
import Tools.BaseDeDatos;
import Tools.Filtros;
import Tools.Iconos;
import Tools.Utilidades;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class Tabla extends JFrame implements ActionListener {

    private JButton Agregar;

    public static JTabbedPane contenedor;
    public static JTable tablas[];
    public static JScrollPane scrolls[];
    public static int ID[][];

    public static JLabel resumen[];

    public static JMenu menuFiltrar, menuRef, menuBuscar;
    public static JMenu menuFiltroPPS;
    public static JMenu menuFiltroAptitud;
    public static JMenu menuPatologias;
    //
    public static JMenu menuDestinos;
    //
    public static JMenu menuOrdenar;

    public static JMenuItem itemListaCompleta;
    public static JMenuItem itemAnexoVencido;
    public static JMenuItem[] itemsPPS;
    public static JMenuItem[] itemsAptitud;
    public static JMenuItem[] itemsPatologias;
    public static JMenuItem itemObservaciones;
    //
    public static JMenuItem[] itemsDestinos;
    //
    public static JMenuItem[] itemsOrdenar;

    private JMenuItem itemRef, itemBuscar;

    //Declaracion de iconos para los menuItems
    private Iconos iconos;

    //DECLARACION DE LOS OBJETOS PARA LOS JFRAMES
    private Formulario formulario;
    private Buscador buscador;
    private Referencias referencia;

    public Tabla() {
        this.iconos = new Iconos();
        this.formulario = new Formulario(this, true);
        this.buscador = new Buscador(this, false);
        this.referencia = new Referencias(this, true);
        Componentes();      
    }

    private void Componentes() {
        //OBJETOS---------------------------------------------------------------
        Utilidades utilidad = new Utilidades();
        Arreglos arreglo = new Arreglos();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();       
        //PROPIEDADES DEL FRAME PRINCIPAL        
        setTitle("Carta de Situacion - Seccion Sanidad RI-1");
        int x = (int) (pantalla.getWidth() < 1525 ? pantalla.getWidth(): 1525);
        int y = (int) (pantalla.getHeight()< 650 ? pantalla.getHeight(): 650);
        setSize(x, y);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(iconos.getIconoSanidad().getImage());
        
        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(1505, 580));
        container.setLayout(null);
        JScrollPane scrollContainer = new JScrollPane(container);       
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //----------------------------------------------------------------------
        //PESTAÑAS DE LAS TABLAS
        UIManager.put("TabbedPane.selected", new Color(50, 205, 50));
        contenedor = new JTabbedPane();
        contenedor.setBounds(10, 10, 1485, 460);
        contenedor.setFont(utilidad.fuentePestañas());
        container.add(contenedor);
        String categorias[] = {"   OFICIALES   ", " SUBOFICIALES ", "  SOLDADOS  ", "    CIVILES    "};
        //----------------------------------------------------------------------
        // TABLAS PRINCIPALES 
        String nombreColumnas[] = arreglo.columnasTabla();
        int tamañoColumnas[] = arreglo.tamañoColumnas();
        scrolls = new JScrollPane[arreglo.getCategoriasLength()];
        tablas = new JTable[arreglo.getCategoriasLength()];
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
            tablas[i].setBackground(utilidad.colorTabla());
            tablas[i].setFont(utilidad.fuenteTabla());
            //eventos al presionar teclas en las tablas
            tablas[i].getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            tablas[i].getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int categoria = contenedor.getSelectedIndex();
                    int puntero = tablas[categoria].getSelectedRow();
                    if (puntero != -1) {
                        int id = ID[categoria][puntero];
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
                        int id = ID[categoria][puntero];
                        modificarFormulario(id, puntero);
                        System.gc();
                    }
                }
            });
            //header de la tabla
            JTableHeader header = tablas[i].getTableHeader();
            header.setFont(new Font("Tahoma", 1, 12));
            header.setBackground(utilidad.colorTabla());
            header.setReorderingAllowed(false);
            ((DefaultTableCellRenderer) tablas[i].getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            //creacion de las columnas
            for (String j : nombreColumnas) {
                model.addColumn(j);
            }
            //tamaño de las columnas
            for (int j = 0; j < tamañoColumnas.length; j++) {
                TableColumn column = tablas[i].getColumnModel().getColumn(j);
                column.setMinWidth(tamañoColumnas[j]);
                column.setMaxWidth(tamañoColumnas[j]);
                column.setPreferredWidth(tamañoColumnas[j]);

                //centrado del contenido de las columnas
                if (j != 3 && j != 19) {
                    column.setCellRenderer(centerRenderer);
                }
            }
            //PROPIEDADES del Scroll y agregarlo al contenedor
            scrolls[i] = new JScrollPane(tablas[i]);
            scrolls[i].getViewport().setBackground(utilidad.colorTabla());
            contenedor.addTab(categorias[i], scrolls[i]);

        }
        // BOTONES (por ahora solo uno)
        Agregar = new JButton("Nuevo +");
        Agregar.setBounds(10, 510, 100, 30);
        Agregar.setFont(utilidad.fuenteBoton());
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
        String[] PPSs = arreglo.PPS();
        PPSs[0] = "Todos";
        itemsPPS = new JMenuItem[PPSs.length];
        for (int i = 0; i < itemsPPS.length; i++) {
            itemsPPS[i] = new JMenuItem(PPSs[i]);
            itemsPPS[i].addActionListener(this);
            menuFiltroPPS.add(itemsPPS[i]);
        }
        //Aptitud
        menuFiltroAptitud = new JMenu("Aptitud");
        menuFiltrar.add(menuFiltroAptitud);
        String[] aptitudes = arreglo.Aptitud();
        aptitudes[0] = "Todos";
        itemsAptitud = new JMenuItem[aptitudes.length];
        for (int i = 0; i < itemsAptitud.length; i++) {
            itemsAptitud[i] = new JMenuItem(aptitudes[i]);
            itemsAptitud[i].addActionListener(this);
            menuFiltroAptitud.add(itemsAptitud[i]);
        }
        //Patologias
        menuPatologias = new JMenu("Patologias");
        menuFiltrar.add(menuPatologias);
        String[] patologias = arreglo.patologias();
        itemsPatologias = new JMenuItem[patologias.length];
        for (int i = 0; i < itemsPatologias.length; i++) {
            if (i == itemsPatologias.length - 1) {
                JPopupMenu.Separator separadorPatologias = new JPopupMenu.Separator();
                menuPatologias.add(separadorPatologias);
            }
            itemsPatologias[i] = new JMenuItem(patologias[i]);
            itemsPatologias[i].addActionListener(this);
            menuPatologias.add(itemsPatologias[i]);
        }
        //Observaciones
        itemObservaciones = new JMenuItem("Observaciones");
        itemObservaciones.addActionListener(this);
        menuFiltrar.add(itemObservaciones);
        JPopupMenu.Separator separador2 = new JPopupMenu.Separator();
        menuFiltrar.add(separador2);
        //Destinos
        menuDestinos = new JMenu("Mostrar por destino");
        menuFiltrar.add(menuDestinos);
        String[] destinos = arreglo.Destinos();
        destinos[0] = "Todos";
        itemsDestinos = new JMenuItem[destinos.length];
        for (int i = 0; i < itemsDestinos.length; i++) {
            itemsDestinos[i] = new JMenuItem(destinos[i]);
            itemsDestinos[i].addActionListener(this);
            menuDestinos.add(itemsDestinos[i]);
        }
        JPopupMenu.Separator separador3 = new JPopupMenu.Separator();
        menuFiltrar.add(separador3);
        //ordenamiento de la tabla
        menuOrdenar = new JMenu("Ordenar por...");
        menuFiltrar.add(menuOrdenar);
        String[] ordenamiento = arreglo.ordenTabla();
        itemsOrdenar = new JMenuItem[ordenamiento.length];
        for (int i = 0; i < itemsOrdenar.length; i++) {
            itemsOrdenar[i] = new JMenuItem(ordenamiento[i]);
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
        itemBuscar.setIcon(new Iconos().getIconoSearchChico());
        menuBuscar.add(itemBuscar);
        //----------------------------------------------------------------------
        // LABELS CON CANTIDAD DE PERSONAL
        resumen = new JLabel[tablas.length + 1];
        int width = 0;
        for (int i = 0; i < resumen.length; i++) {
            resumen[i] = new JLabel("");
            resumen[i].setBounds(15 + width, contenedor.getHeight() + 10, 150, 40);
            resumen[i].setFont(utilidad.fuenteLabelsResumen());
            width += 170;
            container.add(resumen[i]);
        }
        //----------------------------------------------------------------------
        // WALLPAPER DE LA APP
        JLabel fondo = new JLabel();
        fondo.setBounds(0, 0, 1525, 650);
        fondo.setVisible(true);
        container.add(fondo);
        ImageIcon imagen = iconos.getWallpaper();
        Icon wallpaper = new ImageIcon(imagen.getImage().getScaledInstance(fondo.getWidth(),
                 fondo.getHeight(), Image.SCALE_DEFAULT));
        fondo.setIcon(wallpaper);
        //----------------------------------------------------------------------
        //FINALIZACION DE LOS COMPONENTES
        this.getContentPane().add(scrollContainer);
        
        Actualizar(0, 0, 0);

        utilidad = null;
        pantalla = null;
        arreglo = null;
        System.gc();
    }

    //-------------------------------------------------------------------------------------------------
    //-----------------------------EVENTO BOTONES------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {

        //BOTON AGREGAR
        if (e.getSource() == Agregar) {
            formulario.comboBox[0].setSelectedIndex(contenedor.getSelectedIndex());
            formulario.setVisible(true);
        }
        //----------------------BARRA MENU--------------------------------------
        //MENU FILTRAR--------------------------------
        //Lista completa
        if (e.getSource() == itemListaCompleta) {
            Actualizar(0, 0, 0);
            iconos.eliminarChecks();
        }
        // anexo vencido
        if (e.getSource() == itemAnexoVencido) {
            Actualizar(1, Filtros.filtroDestinos, Filtros.ordenamiento);
            iconos.eliminarChecksFiltros();
            itemAnexoVencido.setIcon(iconos.getIconoCheck());
        }
        // programa peso saludable
        for (int i = 0; i < itemsPPS.length; i++) {
            if (e.getSource() == itemsPPS[i]) {
                if (i != 0) {
                    Filtros.FiltroPPS = itemsPPS[i].getText();
                    Actualizar(2, Filtros.filtroDestinos, Filtros.ordenamiento);
                } else {
                    Actualizar(0, Filtros.filtroDestinos, Filtros.ordenamiento);
                }
                iconos.eliminarChecksFiltros();
                menuFiltroPPS.setIcon(iconos.getIconoCheck());
                itemsPPS[i].setIcon(iconos.getIconoCheck());
            }
        }
        // aptitudes
        for (int i = 0; i < itemsAptitud.length; i++) {
            if (e.getSource() == itemsAptitud[i]) {
                if (i != 0) {
                    Filtros.FiltroAptitud = itemsAptitud[i].getText();
                    Actualizar(3, Filtros.filtroDestinos, Filtros.ordenamiento);
                } else {
                    Actualizar(0, Filtros.filtroDestinos, Filtros.ordenamiento);
                }
                iconos.eliminarChecksFiltros();
                menuFiltroAptitud.setIcon(iconos.getIconoCheck());
                itemsAptitud[i].setIcon(iconos.getIconoCheck());
            }
        }
        // patologias
        for (int i = 0; i < itemsPatologias.length; i++) {
            if (e.getSource() == itemsPatologias[i]) {
                if (i != itemsPatologias.length - 1) {
                    String[] patologias = new Arreglos().checkBox();
                    Filtros.columPatologia = patologias[i];
                    Actualizar(4, Filtros.filtroDestinos, Filtros.ordenamiento);
                } else {
                    Actualizar(5, Filtros.filtroDestinos, Filtros.ordenamiento);
                }
                iconos.eliminarChecksFiltros();
                menuPatologias.setIcon(iconos.getIconoCheck());
                itemsPatologias[i].setIcon(iconos.getIconoCheck());
            }
        }
        // observaciones 
        if (e.getSource() == itemObservaciones) {
            Actualizar(6, Filtros.filtroDestinos, Filtros.ordenamiento);
            iconos.eliminarChecksFiltros();
            itemObservaciones.setIcon(iconos.getIconoCheck());
        }
        // destinos
        for (int i = 0; i < itemsDestinos.length; i++) {
            if (e.getSource() == itemsDestinos[i]) {
                Actualizar(Filtros.filtro, i, Filtros.ordenamiento);
                iconos.eliminarChecksDestino();
                menuDestinos.setIcon(iconos.getIconoCheck());
                itemsDestinos[i].setIcon(iconos.getIconoCheck());
            }
        }
        //ordenar por
        for (int i = 0; i < itemsOrdenar.length; i++) {
            if (e.getSource() == itemsOrdenar[i]) {
                Actualizar(Filtros.filtro, Filtros.filtroDestinos, i);
                iconos.eliminarChecksOrden();
                menuOrdenar.setIcon(iconos.getIconoCheck());
                itemsOrdenar[i].setIcon(iconos.getIconoCheck());
            }
        }
        //MENU REFERENCIAS-----------------------------
        if (e.getSource() == itemRef) {
            referencia.setVisible(true);;
        }

        //MENU BUSCAR---------------------------------
        if (e.getSource() == itemBuscar) {
            menuFiltrar.setEnabled(false);
            menuRef.setEnabled(false);
            menuBuscar.setEnabled(false);
            buscador.setVisible(true);
        }
        System.gc();
    }

    //----------------------------------------------------------------------------------------------------
    //------------------ MODIFICAR FORMULARIO--------------------------------------------------------------
    private void modificarFormulario(int id, int puntero) {
        formulario.obtenerDatos(id, puntero);
        formulario.setVisible(true);
        System.gc();
    }

    //-------------------------------------------------------------------------------------------------
    //---------------------- LLENAR TABLA--------------------------------------------------------------
    private void Actualizar(int filtro, int destino, int orden) {
        Filtros.filtro = filtro;
        Filtros.filtroDestinos = destino;
        Filtros.ordenamiento = orden;
        BaseDeDatos bdd = new BaseDeDatos();
        bdd.Actualizar();
        bdd = null;
        System.gc();
    }

}
