package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.EmptyBorder;
import mytools.Arreglos;
import mytools.Iconos;
import windows.Buscador;
import windows.Formulario;
import windows.Referencias;
import windows.Tabla;
import windows.parte.FormularioParte;
import windows.parte.Parte;

public class MainFrame extends JFrame implements ActionListener {

    private final static String PARTE = "Parte de Sanidad";
    private final static String TABLA = "Carta de Situacion";
    private JButton botonParte;
    private JButton botonTabla;

    JMenuBar menuBar;

    private JMenu menuFiltrar, menuRef, menuBuscar;
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
    private JMenuItem[] itemsOrdenar;

    private JMenuItem itemRef, itemBuscar;

    //Declaracion de iconos para los menuItems
    private ImageIcon check;

    //Panel y Dialogs
    private Tabla tabla;
    private Parte parte;
    private Formulario formulario;
    private FormularioParte formParte;
    private Buscador buscador;
    private Referencias referencia;

    public MainFrame() {
        //OBJETOS AUXILIARES
        Iconos iconos = new Iconos();
        //PANEL Y JDIALOGS
        tabla = new Tabla();
        parte = new Parte();
        formulario = new Formulario(this, true);
        formParte = new FormularioParte(this, true);
        buscador = new Buscador(this, true);
        referencia = new Referencias(this, true);

        tabla.setFormulario(formulario);
        parte.setFormParte(formParte);
        formulario.setTabla(tabla);
        formulario.setFormParte(formParte);
        formParte.setParte(parte);
        formParte.setFormulario(formulario);
        buscador.setTabla(tabla);
        
        //PROPIEDADES DEL FRAME
        setTitle("Carta de Situacion - Seccion Sanidad RI-1");
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (pantalla.getWidth() < 1525 ? pantalla.getWidth() : 1525);
        int y = (int) (pantalla.getHeight() < 650 ? pantalla.getHeight() : 650);
        pantalla = null;
        setSize(x, y);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(iconos.getIconoSanidad().getImage());
        setLocationRelativeTo(null);
        getContentPane().setLayout(new CardLayout(0, 0));

        componentes(iconos);
        iconos = null;
        
        getContentPane().add(tabla.getScrollContainer(), "Carta de Situacion");
        getContentPane().add(parte.getScrollContainer(), "Parte de Sanidad");

        botonParte = tabla.getBotonParte();
        botonParte.addActionListener(this);
        botonTabla = parte.getBotonTabla();
        botonTabla.addActionListener(this);
    }

    private void componentes(Iconos iconos) {
        check = iconos.getIconoCheck();
        // BARRA MENU----------------------------------------------------------        
        menuBar = new JMenuBar();
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
        itemsPPS = new JMenuItem[Arreglos.getPPSLength()];
        for (int i = 0; i < itemsPPS.length; i++) {
            itemsPPS[i] = new JMenuItem(Arreglos.getPPS(i));
            itemsPPS[i].addActionListener(this);
            menuFiltroPPS.add(itemsPPS[i]);
        }
        itemsPPS[0].setText("Todos");
        //Aptitud
        menuFiltroAptitud = new JMenu("Aptitud");
        menuFiltrar.add(menuFiltroAptitud);
        itemsAptitud = new JMenuItem[Arreglos.getAptitudLength()];
        for (int i = 0; i < itemsAptitud.length; i++) {
            itemsAptitud[i] = new JMenuItem(Arreglos.getAptitud(i));
            itemsAptitud[i].addActionListener(this);
            menuFiltroAptitud.add(itemsAptitud[i]);
        }
        itemsAptitud[0].setText("Todos");
        //Patologias
        menuPatologias = new JMenu("Patologias");
        menuFiltrar.add(menuPatologias);
        itemsPatologias = new JMenuItem[Arreglos.getPatologiasLength()];
        for (int i = 0; i < itemsPatologias.length; i++) {
            if (i == itemsPatologias.length - 1) {
                JPopupMenu.Separator separadorPatologias = new JPopupMenu.Separator();
                menuPatologias.add(separadorPatologias);
                separadorPatologias = null;
            }
            itemsPatologias[i] = new JMenuItem(Arreglos.getPatologias(i));
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
        itemsDestinos = new JMenuItem[Arreglos.getDestinosLength()];
        for (int i = 0; i < itemsDestinos.length; i++) {
            itemsDestinos[i] = new JMenuItem(Arreglos.getDestinos(i));
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
        itemsOrdenar = new JMenuItem[Arreglos.getOrdenTabla().length];
        for (int i = 0; i < itemsOrdenar.length; i++) {
            itemsOrdenar[i] = new JMenuItem(Arreglos.getOrdenTabla(i));
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

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //-----------------------BOTONES----------------------------------
        if (e.getSource() == botonParte) {
            CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
            cl.show(this.getContentPane(), PARTE);
            JMenuBar menu = rootPane.getJMenuBar();
            menu.setVisible(false);
        }
        if (e.getSource() == botonTabla) {
            CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
            cl.show(this.getContentPane(), TABLA);
            JMenuBar menu = rootPane.getJMenuBar();
            menu.setVisible(true);
        }
        //----------------------BARRA MENU--------------------------------------
        //MENU FILTRAR--------------------------------FILTROS-------------------
        //Lista completa
        if (e.getSource() == itemListaCompleta) {
            tabla.Actualizar(0, 0, 0);
            eliminarChecks();
        }
        // anexo vencido
        if (e.getSource() == itemAnexoVencido) {
            tabla.Actualizar(1, tabla.getShowByDestino(), tabla.getOrder());
            eliminarChecksFiltros();
            itemAnexoVencido.setIcon(check);
        }
        // programa peso saludable
        for (int i = 0; i < itemsPPS.length; i++) {
            if (e.getSource() == itemsPPS[i]) {
                if (i != 0) {
                    tabla.setPPSFilter(itemsPPS[i].getText());
                    tabla.Actualizar(2, tabla.getShowByDestino(), tabla.getOrder());
                } else {
                    tabla.Actualizar(0, tabla.getShowByDestino(), tabla.getOrder());
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
                    tabla.setAptitudFilter(itemsAptitud[i].getText());
                    tabla.Actualizar(3, tabla.getShowByDestino(), tabla.getOrder());
                } else {
                    tabla.Actualizar(0, tabla.getShowByDestino(), tabla.getOrder());
                }
                eliminarChecksFiltros();
                menuFiltroAptitud.setIcon(check);
                itemsAptitud[i].setIcon(check);
            }
        }
        // patologias
        for (int i = 0; i < itemsPatologias.length; i++) {
            if (e.getSource() == itemsPatologias[i]) {
                if (i < itemsPatologias.length - 1 && i != 0) {
                    tabla.setPatologiaColumn(Arreglos.getCheckBox(i));
                    tabla.Actualizar(4, tabla.getShowByDestino(), tabla.getOrder());
                } else if (i != 0) {
                    tabla.Actualizar(5, tabla.getShowByDestino(), tabla.getOrder());
                } else {
                    tabla.Actualizar(0, tabla.getShowByDestino(), tabla.getOrder());
                }
                eliminarChecksFiltros();
                menuPatologias.setIcon(check);
                itemsPatologias[i].setIcon(check);
            }
        }
        // observaciones 
        if (e.getSource() == itemObservaciones) {
            tabla.Actualizar(6, tabla.getShowByDestino(), tabla.getOrder());
            eliminarChecksFiltros();
            itemObservaciones.setIcon(check);
        }
        // destinos
        for (int i = 0; i < itemsDestinos.length; i++) {
            if (e.getSource() == itemsDestinos[i]) {
                tabla.Actualizar(tabla.getFilter(), i, tabla.getOrder());
                eliminarChecksDestino();
                menuDestinos.setIcon(check);
                itemsDestinos[i].setIcon(check);
            }
        }
        //ordenar por
        for (int i = 0; i < itemsOrdenar.length; i++) {
            if (e.getSource() == itemsOrdenar[i]) {
                tabla.Actualizar(tabla.getFilter(), tabla.getShowByDestino(), i);
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
            buscador.setVisible(true);
            System.gc();
        }
    }

    private void eliminarChecksFiltros() {
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

    private void eliminarChecksDestino() {
        for (int i = 0; i < itemsDestinos.length; i++) {
            itemsDestinos[i].setIcon(null);
        }
    }

    private void eliminarChecksOrden() {
        for (int i = 0; i < itemsOrdenar.length; i++) {
            itemsOrdenar[i].setIcon(null);
        }
    }

    private void eliminarChecks() {
        //FILTROS
        eliminarChecksFiltros();
        //DESTINOS
        menuDestinos.setIcon(null);
        eliminarChecksDestino();
        //ORDEN
        menuOrdenar.setIcon(null);
        eliminarChecksOrden();
    }

    //----------------------GETTERS Y SETTERS----------------------------------
    public Tabla getTabla() {
        return tabla;
    }

    public Parte getParte() {
        return parte;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public FormularioParte getFormParte() {
        return formParte;
    }

    public Buscador getBuscador() {
        return buscador;
    }

    public Referencias getReferencia() {
        return referencia;
    }

}
