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

    private final static String SICK = "Parte de Sanidad";
    private final static String PERSONNEL = "Carta de Situacion";
    private final static String RECOUNT = "Recuento de Partes de Enfermo";

    private JButton buttonSickPanel, buttonSickPanel2;
    private JButton buttonPersonnelPanel;
    private JButton buttonReCountPanel;

    private Icons icons;
    private Utilities utility;

    JMenuBar menuBar;

    private JMenu menuFilter, menuRef, menuSearch, menuConfig, menuAbout;
    private JMenu menuFilterPPS;
    private JMenu menuFilterAptitude;
    private JMenu menuPathologies;
    //
    private JMenu menuSubUnities;
    //
    private JMenu menuOrderBy;

    private JMenuItem itemRefreshList;
    private JMenuItem ItemCaducatedStudies;
    private JMenuItem[] itemsPPS;
    private JMenuItem[] itemsAptitude;
    private JMenuItem[] itemsPathologies;
    private JMenuItem itemObs;
    //
    private JMenuItem[] itemSubUnities;
    //
    private JMenuItem[] itemsOrderBy;

    private JMenuItem itemRef, itemSearch, itemConfig, itemAbout;

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
    private About about;

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
        about = new About(this,true);

        personnelPanel.setFormulary(personnelFormulary);
        personnelPanel.setSearcher(searcher);
        personnelPanel.setConfig(configurator);
        personnelPanel.setListGenerator(listGenerator);
        sickPanel.setFormularySick(sickFormulary);
        sickPanel.setConfig(configurator);
        reCountPanel.setConfig(configurator);
        personnelFormulary.setPersonnelPanel(personnelPanel);
        personnelFormulary.setSickFormulary(sickFormulary);
        sickFormulary.setSickPanel(sickPanel);
        sickFormulary.setPersonnelFormulary(personnelFormulary);
        searcher.setPersonnelPanel(personnelPanel);
        searcher.setConfigurator(configurator);
        imc.setPersonnelPanel(personnelPanel);
        configurator.setPersonnelPanel(personnelPanel);
        configurator.setSickPanel(sickPanel);
        configurator.setReCountPanel(reCountPanel);
        listGenerator.setTabla(personnelPanel);

        //PROPIEDADES DEL FRAME
        setTitle(PERSONNEL);
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

        getContentPane().add(personnelPanel.getScrollContainer(), PERSONNEL);
        getContentPane().add(sickPanel.getScrollContainer(), SICK);
        getContentPane().add(reCountPanel.getScrollContainer(), RECOUNT);

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
        menuFilter = new JMenu("Filtrar");
        menuBar.add(menuFilter);
        //ITEM LISTA COMPLETA
        itemRefreshList = new JMenuItem("Lista Completa");
        itemRefreshList.setIcon(iconos.getIconRefresh());
        itemRefreshList.addActionListener(this);
        menuFilter.add(itemRefreshList);
        JPopupMenu.Separator separador1 = new JPopupMenu.Separator();
        menuFilter.add(separador1);
        //ITEMS FILTROS
        // Anexo vencido
        ItemCaducatedStudies = new JMenuItem("Anexos Vencidos");
        ItemCaducatedStudies.addActionListener(this);
        menuFilter.add(ItemCaducatedStudies);
        // Programa Peso Saludable
        menuFilterPPS = new JMenu("Programa Peso Saludable");
        menuFilter.add(menuFilterPPS);
        itemsPPS = new JMenuItem[MyArrays.getPPSLength()];
        for (int i = 0; i < itemsPPS.length; i++) {
            itemsPPS[i] = new JMenuItem(MyArrays.getPPS(i));
            itemsPPS[i].addActionListener(this);
            menuFilterPPS.add(itemsPPS[i]);
        }
        itemsPPS[0].setText("Filtrar por IMC...");
        //Aptitud
        menuFilterAptitude = new JMenu("Aptitud");
        menuFilter.add(menuFilterAptitude);
        itemsAptitude = new JMenuItem[MyArrays.getAptitudeLength()];
        for (int i = 0; i < itemsAptitude.length; i++) {
            itemsAptitude[i] = new JMenuItem(MyArrays.getAptitude(i));
            itemsAptitude[i].addActionListener(this);
            menuFilterAptitude.add(itemsAptitude[i]);
        }
        itemsAptitude[0].setText("Sin asignar");
        //Patologias
        menuPathologies = new JMenu("Patologias");
        menuFilter.add(menuPathologies);
        itemsPathologies = new JMenuItem[MyArrays.getPathologiesLength()];
        for (int i = 0; i < itemsPathologies.length; i++) {
            if (i == itemsPathologies.length - 1) {
                JPopupMenu.Separator separadorPatologias = new JPopupMenu.Separator();
                menuPathologies.add(separadorPatologias);
                separadorPatologias = null;
            }
            itemsPathologies[i] = new JMenuItem(MyArrays.getPathologies(i));
            itemsPathologies[i].addActionListener(this);
            menuPathologies.add(itemsPathologies[i]);
        }
        //Observaciones
        itemObs = new JMenuItem("Observaciones");
        itemObs.addActionListener(this);
        menuFilter.add(itemObs);
        JPopupMenu.Separator separador2 = new JPopupMenu.Separator();
        menuFilter.add(separador2);
        separador2 = null;
        //Destinos
        menuSubUnities = new JMenu("Mostrar por destino");
        menuFilter.add(menuSubUnities);
        itemSubUnities = new JMenuItem[MyArrays.getSubUnitiesLength()];
        for (int i = 0; i < itemSubUnities.length; i++) {
            itemSubUnities[i] = new JMenuItem(MyArrays.getSubUnities(i));
            itemSubUnities[i].addActionListener(this);
            menuSubUnities.add(itemSubUnities[i]);
        }
        itemSubUnities[0].setText("Sin asignar");
        JPopupMenu.Separator separador3 = new JPopupMenu.Separator();
        menuFilter.add(separador3);
        separador3 = null;
        //ordenamiento de la tabla
        menuOrderBy = new JMenu("Ordenar por...");
        menuFilter.add(menuOrderBy);
        itemsOrderBy = new JMenuItem[MyArrays.getOrderPersonnelMenu().length];
        for (int i = 0; i < itemsOrderBy.length; i++) {
            itemsOrderBy[i] = new JMenuItem(MyArrays.getOrderPersonnelMenu(i));
            itemsOrderBy[i].addActionListener(this);
            menuOrderBy.add(itemsOrderBy[i]);
        }

        //MENU BUSCAR-------------------------------------- 
        menuSearch = new JMenu("Buscar");
        menuBar.add(menuSearch);
        itemSearch = new JMenuItem("<html>Buscar... Ctrl+G");
        itemSearch.addActionListener(this);
        itemSearch.setIcon(iconos.getIconoSearchChico());
        menuSearch.add(itemSearch);
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
        //MENU ABOUT
        menuAbout = new JMenu("Acerca");
        menuBar.add(menuAbout);
        itemAbout = new JMenuItem("Acerca de...");
        itemAbout.addActionListener(this);
        menuAbout.add(itemAbout);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //-----------------------BOTONES----------------------------------
        if (e.getSource() == buttonSickPanel || e.getSource() == buttonSickPanel2) {
            CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
            cl.show(this.getContentPane(), SICK);
            menuFilter.setVisible(false);
            menuSearch.setVisible(false);
            setTitle(SICK);
        }
        if (e.getSource() == buttonPersonnelPanel) {
            CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
            cl.show(this.getContentPane(), PERSONNEL);
            menuFilter.setVisible(true);
            menuSearch.setVisible(true);
            setTitle(PERSONNEL);
        }
        if (e.getSource() == buttonReCountPanel) {
            CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
            cl.show(this.getContentPane(), RECOUNT);
            menuFilter.setVisible(false);
            menuSearch.setVisible(false);
            setTitle(RECOUNT);
        }

        //----------------------BARRA MENU--------------------------------------
        //MENU FILTRAR--------------------------------FILTROS-------------------
        //Lista completa
        if (e.getSource() == itemRefreshList) {
            personnelPanel.update(0, 0, 0);
            personnelPanel.setFiltered(false);
            deleteChecks();
        }
        // anexo vencido
        if (e.getSource() == ItemCaducatedStudies) {
            personnelPanel.update(1, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
            personnelPanel.setFiltered(true);
            deleteChecksFilters();
            ItemCaducatedStudies.setIcon(check);
        }
        // programa peso saludable
        for (int i = 0; i < itemsPPS.length; i++) {
            if (e.getSource() == itemsPPS[i]) {
                if (i != 0) {
                    personnelPanel.setPPSFilter(itemsPPS[i].getText());
                    personnelPanel.update(2, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                    deleteChecksFilters();
                    menuFilterPPS.setIcon(check);
                    itemsPPS[i].setIcon(check);
                } else {
                    imc.setVisible(true);
                }
            }
        }
        // aptitudes
        for (int i = 0; i < itemsAptitude.length; i++) {
            if (e.getSource() == itemsAptitude[i]) {
                if (i != 0) {
                    personnelPanel.setAptitudeFilter(itemsAptitude[i].getText());
                    personnelPanel.update(3, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                } else {
                    personnelPanel.setAptitudeFilter("NULL");
                    personnelPanel.update(3, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                }
                deleteChecksFilters();
                menuFilterAptitude.setIcon(check);
                itemsAptitude[i].setIcon(check);
            }
        }
        // patologias
        for (int i = 0; i < itemsPathologies.length; i++) {
            if (e.getSource() == itemsPathologies[i]) {
                if (i < itemsPathologies.length - 1 && i != 0) {
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
                menuPathologies.setIcon(check);
                itemsPathologies[i].setIcon(check);
            }
        }
        // observaciones 
        if (e.getSource() == itemObs) {
            personnelPanel.update(6, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
            personnelPanel.setFiltered(true);
            deleteChecksFilters();
            itemObs.setIcon(check);
        }
        // destinos
        for (int i = 0; i < itemSubUnities.length; i++) {
            if (e.getSource() == itemSubUnities[i]) {
                personnelPanel.update(personnelPanel.getFilter(), i == 0 ? -1:i, personnelPanel.getRowOrdering());
                personnelPanel.setFiltered(true);
                eliminarChecksShowBy();
                menuSubUnities.setIcon(check);
                itemSubUnities[i].setIcon(check);
            }
        }
        //ordenar por
        for (int i = 0; i < itemsOrderBy.length; i++) {
            if (e.getSource() == itemsOrderBy[i]) {
                personnelPanel.update(personnelPanel.getFilter(), personnelPanel.getShowBySubUnity(), i);
                personnelPanel.setFiltered(true);
                deleteChecksOrderBy();
                menuOrderBy.setIcon(check);
                itemsOrderBy[i].setIcon(check);
            }
        }

        //MENU BUSCAR--------------------------------------------
        if (e.getSource() == itemSearch) {
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
        //MENU ABOUT----------------------------------------------
        if(e.getSource() == itemAbout){
            about.setVisible(true);
        }
    }

    public void deleteChecksFilters() {
        ItemCaducatedStudies.setIcon(null);

        menuFilterPPS.setIcon(null);
        for (int i = 0; i < itemsPPS.length; i++) {
            itemsPPS[i].setIcon(null);
        }
        menuFilterAptitude.setIcon(null);
        for (int i = 0; i < itemsAptitude.length; i++) {
            itemsAptitude[i].setIcon(null);
        }
        menuPathologies.setIcon(null);
        for (int i = 0; i < itemsPathologies.length; i++) {
            itemsPathologies[i].setIcon(null);
        }
        itemObs.setIcon(null);
    }

    public void eliminarChecksShowBy() {
        for (int i = 0; i < itemSubUnities.length; i++) {
            itemSubUnities[i].setIcon(null);
        }
    }

    public void deleteChecksOrderBy() {
        for (int i = 0; i < itemsOrderBy.length; i++) {
            itemsOrderBy[i].setIcon(null);
        }
    }

    public void deleteChecks() {
        //FILTROS
        deleteChecksFilters();
        //DESTINOS
        menuSubUnities.setIcon(null);
        eliminarChecksShowBy();
        //ORDEN
        menuOrderBy.setIcon(null);
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

    public JMenu getMenuFilterPPS() {
        return menuFilterPPS;
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
