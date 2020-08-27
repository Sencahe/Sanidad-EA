package main;

import dialogs.Configurator;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import mytools.MyArrays;
import mytools.Icons;
import mytools.Utilities;
import dialogs.*;
import panels.*;

public class MainFrame extends JFrame implements ActionListener {

    private final static String PARTE = "Parte de Sanidad";
    private final static String TABLA = "Carta de Situacion";
    private final static String RECUENTO = "Recuento de Partes de Enfermo";

    private JButton buttonSickPanel, buttonSickPanel2;
    private JButton buttonPersonnelPanel;
    private JButton buttonReCountPanel;

    private Icons icons;
    private Utilities utility;

    JMenuBar menuBar;

    private JMenu menuFiltrar, menuRef, menuBuscar, menuConfig;
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

    private JMenuItem itemRef, itemBuscar, itemConfig;

    //icono para los menuItems
    private ImageIcon check;

    //Panel y Dialogs
    private PersonnelPanel personnelPanel;
    private SickPanel sickPanel;
    private PersonnelFormulary personnelFormulary;
    private SickFormulary sickFormulary;
    private Searcher searcher;
    private References references;
    private IMC imc;
    private Configurator configurator;
    private ReCountPanel reCountPanel;
    private ListGenerator listGenerator;

    public MainFrame() {
        //OBJETOS AUXILIARES
        icons = new Icons();
        utility = new Utilities();
        //PANEL Y JDIALOGS
        personnelPanel = new PersonnelPanel(this);
        sickPanel = new SickPanel(this);
        reCountPanel = new ReCountPanel(this);
        personnelFormulary = new PersonnelFormulary(this, true);
        sickFormulary = new SickFormulary(this, true);
        searcher = new Searcher(this, false);
        references = new References(this, true);
        imc = new IMC(this, true);
        configurator = new Configurator(this, true);
        listGenerator = new ListGenerator(this, true);

        personnelPanel.setFormulary(personnelFormulary);
        personnelPanel.setSearcher(searcher);
        personnelPanel.setConfig(configurator);
        personnelPanel.setListGenerator(listGenerator);
        sickPanel.setFormularySick(sickFormulary);
        sickPanel.setConfig(configurator);
        reCountPanel.setConfig(configurator);
        personnelFormulary.setTabla(personnelPanel);
        personnelFormulary.setFormParte(sickFormulary);
        sickFormulary.setSickPanel(sickPanel);
        sickFormulary.setFormulario(personnelFormulary);
        searcher.setPersonnelPanel(personnelPanel);
        searcher.setConfigurator(configurator);
        imc.setPersonnelPanel(personnelPanel);
        configurator.setPersonnelPanel(personnelPanel);
        configurator.setSickPanel(sickPanel);
        configurator.setReCountPanel(reCountPanel);
        listGenerator.setTabla(personnelPanel);

        //PROPIEDADES DEL FRAME
        setTitle(TABLA);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screen.getWidth() < 1525 ? screen.getWidth() : 1525);
        int y = (int) (screen.getHeight() < 650 ? screen.getHeight() : 650);
        screen = null;
        setSize(x, y);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(icons.getIconHealthService().getImage());
        setLocationRelativeTo(null);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        getContentPane().setLayout(new CardLayout(0, 0));

        components(icons);

        getContentPane().add(personnelPanel.getScrollContainer(), TABLA);
        getContentPane().add(sickPanel.getScrollContainer(), PARTE);
        getContentPane().add(reCountPanel.getScrollContainer(), RECUENTO);

        buttonSickPanel = personnelPanel.getButtonSickPanel();
        buttonSickPanel.addActionListener(this);

        buttonPersonnelPanel = sickPanel.getButtonPersonnelPanel();
        buttonPersonnelPanel.addActionListener(this);
        buttonReCountPanel = sickPanel.getButtonReCountPanel();
        buttonReCountPanel.addActionListener(this);

        buttonSickPanel2 = reCountPanel.getButtonSickPanel();
        buttonSickPanel2.addActionListener(this);

        //Fin del constructor----------------
        icons = null;
        utility = null;
    }

    private void components(Icons iconos) {
        check = iconos.getIconoCheck();
        // BARRA MENU----------------------------------------------------------        
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // MMENU FILTRAR ----------------------- 
        menuFiltrar = new JMenu("Filtrar");
        menuBar.add(menuFiltrar);
        //ITEM LISTA COMPLETA
        itemListaCompleta = new JMenuItem("Lista Completa");
        itemListaCompleta.setIcon(iconos.getIconRefresh());
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
        itemsPPS = new JMenuItem[MyArrays.getPPSLength()];
        for (int i = 0; i < itemsPPS.length; i++) {
            itemsPPS[i] = new JMenuItem(MyArrays.getPPS(i));
            itemsPPS[i].addActionListener(this);
            menuFiltroPPS.add(itemsPPS[i]);
        }
        itemsPPS[0].setText("Filtrar por IMC...");
        //Aptitud
        menuFiltroAptitud = new JMenu("Aptitud");
        menuFiltrar.add(menuFiltroAptitud);
        itemsAptitud = new JMenuItem[MyArrays.getAptitudeLength()];
        for (int i = 0; i < itemsAptitud.length; i++) {
            itemsAptitud[i] = new JMenuItem(MyArrays.getAptitude(i));
            itemsAptitud[i].addActionListener(this);
            menuFiltroAptitud.add(itemsAptitud[i]);
        }
        itemsAptitud[0].setText("Todos");
        //Patologias
        menuPatologias = new JMenu("Patologias");
        menuFiltrar.add(menuPatologias);
        itemsPatologias = new JMenuItem[MyArrays.getPathologiesLength()];
        for (int i = 0; i < itemsPatologias.length; i++) {
            if (i == itemsPatologias.length - 1) {
                JPopupMenu.Separator separadorPatologias = new JPopupMenu.Separator();
                menuPatologias.add(separadorPatologias);
                separadorPatologias = null;
            }
            itemsPatologias[i] = new JMenuItem(MyArrays.getPathologies(i));
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
        itemsDestinos = new JMenuItem[MyArrays.getSubUnitiesLength()];
        for (int i = 0; i < itemsDestinos.length; i++) {
            itemsDestinos[i] = new JMenuItem(MyArrays.getSubUnities(i));
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
        itemsOrdenar = new JMenuItem[MyArrays.getOrderPersonnelMenu().length];
        for (int i = 0; i < itemsOrdenar.length; i++) {
            itemsOrdenar[i] = new JMenuItem(MyArrays.getOrderPersonnelMenu(i));
            itemsOrdenar[i].addActionListener(this);
            menuOrdenar.add(itemsOrdenar[i]);
        }

        //MENU BUSCAR-------------------------------------- 
        menuBuscar = new JMenu("Buscar");
        menuBar.add(menuBuscar);
        itemBuscar = new JMenuItem("<html>Buscar... Ctrl+G");
        itemBuscar.addActionListener(this);
        itemBuscar.setIcon(iconos.getIconoSearchChico());
        menuBuscar.add(itemBuscar);
        //MENU Config--------------------------------- 
        menuConfig = new JMenu("Configuracion");
        menuBar.add(menuConfig);
        itemConfig = new JMenuItem("Configurar");
        itemConfig.addActionListener(this);
        itemConfig.setIcon(iconos.getIconConfig());
        menuConfig.add(itemConfig);
        //MENU REFERENCIAS--------------------------------- 
        menuRef = new JMenu("Ref.");
        menuBar.add(menuRef);
        itemRef = new JMenuItem("Referencias");
        itemRef.addActionListener(this);
        menuRef.add(itemRef);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //-----------------------BOTONES----------------------------------
        if (e.getSource() == buttonSickPanel || e.getSource() == buttonSickPanel2) {
            CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
            cl.show(this.getContentPane(), PARTE);
            menuFiltrar.setVisible(false);
            menuBuscar.setVisible(false);
            setTitle(PARTE);
        }
        if (e.getSource() == buttonPersonnelPanel) {
            CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
            cl.show(this.getContentPane(), TABLA);
            menuFiltrar.setVisible(true);
            menuBuscar.setVisible(true);
            setTitle(TABLA);
        }
        if (e.getSource() == buttonReCountPanel) {
            CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
            cl.show(this.getContentPane(), RECUENTO);
            menuFiltrar.setVisible(false);
            menuBuscar.setVisible(false);
            setTitle(RECUENTO);
        }

        //----------------------BARRA MENU--------------------------------------
        //MENU FILTRAR--------------------------------FILTROS-------------------
        //Lista completa
        if (e.getSource() == itemListaCompleta) {
            personnelPanel.update(0, 0, 0);
            personnelPanel.setFiltered(false);
            deleteChecks();
        }
        // anexo vencido
        if (e.getSource() == itemAnexoVencido) {
            personnelPanel.update(1, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
            personnelPanel.setFiltered(true);
            deleteChecksFilters();
            itemAnexoVencido.setIcon(check);
        }
        // programa peso saludable
        for (int i = 0; i < itemsPPS.length; i++) {
            if (e.getSource() == itemsPPS[i]) {
                if (i != 0) {
                    personnelPanel.setPPSFilter(itemsPPS[i].getText());
                    personnelPanel.update(2, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                    deleteChecksFilters();
                    menuFiltroPPS.setIcon(check);
                    itemsPPS[i].setIcon(check);
                } else {
                    imc.setVisible(true);
                }
            }
        }
        // aptitudes
        for (int i = 0; i < itemsAptitud.length; i++) {
            if (e.getSource() == itemsAptitud[i]) {
                if (i != 0) {
                    personnelPanel.setAptitudeFilter(itemsAptitud[i].getText());
                    personnelPanel.update(3, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                } else {
                    personnelPanel.update(0, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                }
                deleteChecksFilters();
                menuFiltroAptitud.setIcon(check);
                itemsAptitud[i].setIcon(check);
            }
        }
        // patologias
        for (int i = 0; i < itemsPatologias.length; i++) {
            if (e.getSource() == itemsPatologias[i]) {
                if (i < itemsPatologias.length - 1 && i != 0) {
                    personnelPanel.setPathologyColumn(MyArrays.getCheckBox(i - 1));
                    personnelPanel.update(4, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                } else if (i != 0) {
                    personnelPanel.update(5, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                } else {
                    personnelPanel.update(8, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                }
                deleteChecksFilters();
                menuPatologias.setIcon(check);
                itemsPatologias[i].setIcon(check);
            }
        }
        // observaciones 
        if (e.getSource() == itemObservaciones) {
            personnelPanel.update(6, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
            personnelPanel.setFiltered(true);
            deleteChecksFilters();
            itemObservaciones.setIcon(check);
        }
        // destinos
        for (int i = 0; i < itemsDestinos.length; i++) {
            if (e.getSource() == itemsDestinos[i]) {
                personnelPanel.update(personnelPanel.getFilter(), i, personnelPanel.getRowOrdering());
                personnelPanel.setFiltered(true);
                eliminarChecksShowBy();
                menuDestinos.setIcon(check);
                itemsDestinos[i].setIcon(check);
            }
        }
        //ordenar por
        for (int i = 0; i < itemsOrdenar.length; i++) {
            if (e.getSource() == itemsOrdenar[i]) {
                personnelPanel.update(personnelPanel.getFilter(), personnelPanel.getShowBySubUnity(), i);
                personnelPanel.setFiltered(true);
                deleteChecksOrderBy();
                menuOrdenar.setIcon(check);
                itemsOrdenar[i].setIcon(check);
            }
        }

        //MENU BUSCAR--------------------------------------------
        if (e.getSource() == itemBuscar) {
            searcher.setVisible(true);
            System.gc();
        }
        //MENU REFERENCIAS-----------------------------------------
        if (e.getSource() == itemConfig) {
            configurator.setVisible(true);
            System.gc();
        }
        //MENU REFERENCIAS-----------------------------------------
        if (e.getSource() == itemRef) {
            references.setVisible(true);;
            System.gc();
        }
    }

    public void deleteChecksFilters() {
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

    public void eliminarChecksShowBy() {
        for (int i = 0; i < itemsDestinos.length; i++) {
            itemsDestinos[i].setIcon(null);
        }
    }

    public void deleteChecksOrderBy() {
        for (int i = 0; i < itemsOrdenar.length; i++) {
            itemsOrdenar[i].setIcon(null);
        }
    }

    public void deleteChecks() {
        //FILTROS
        deleteChecksFilters();
        //DESTINOS
        menuDestinos.setIcon(null);
        eliminarChecksShowBy();
        //ORDEN
        menuOrdenar.setIcon(null);
        deleteChecksOrderBy();
    }

    //----------------------GETTERS Y SETTERS----------------------------------
    public PersonnelPanel getPersonnelPanel() {
        return personnelPanel;
    }

    public SickPanel getSickPanel() {
        return sickPanel;
    }

    public PersonnelFormulary getPersonnelFormulary() {
        return personnelFormulary;
    }

    public SickFormulary getSickFormulary() {
        return sickFormulary;
    }

    public Searcher getSearcher() {
        return searcher;
    }

    public References getReferences() {
        return references;
    }

    public JMenu getMenuFiltroPPS() {
        return menuFiltroPPS;
    }

    public JMenuItem getItemIMC() {
        return itemsPPS[0];
    }

    public ImageIcon getCheck() {
        return check;
    }

    public Icons getIcons() {
        return icons;
    }

    public Utilities getUtility() {
        return utility;
    }

}
