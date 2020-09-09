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
    private JMenu menuFilerGenre;
    //
    private JMenu menuSubUnities;
    //
    private JMenu menuOrderBy;

    private JMenuItem itemRefreshList;
    private JMenuItem itemCaducatedStudies;
    private JMenuItem[] itemsPPS;
    private JMenuItem[] itemsAptitude;
    private JMenuItem[] itemsPathologies;
    private JMenuItem itemObs;
    private JMenuItem[] itemGenre;
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
    private CaducatedStudies caducatedStudies;
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
        caducatedStudies = new CaducatedStudies(this,true);
        about = new About(this, true);

        personnelPanel.setFormulary(personnelFormulary);
        personnelPanel.setSearcher(searcher);
        personnelPanel.setConfig(configurator);
        personnelPanel.setListGenerator(listGenerator);
        personnelPanel.setCaducatedStudies(caducatedStudies);
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
        listGenerator.setPersonnelPanel(personnelPanel);
        caducatedStudies.setPersonnelPanel(personnelPanel);

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
        itemCaducatedStudies = new JMenuItem("Anexos Vencidos");
        itemCaducatedStudies.addActionListener(this);
        menuFilter.add(itemCaducatedStudies);
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
        itemsAptitude = new JMenuItem[MyArrays.getAptitudeLength() + 1];
        int cicle = itemsAptitude.length;
        for (int i = 0; i < cicle; i++) {
            if (i == cicle - 1) {
                JPopupMenu.Separator aptSeparator = new JPopupMenu.Separator();
                menuFilterAptitude.add(aptSeparator);
            }
            itemsAptitude[i] = new JMenuItem(i != cicle - 1 ? MyArrays.getAptitude(i) : "");
            itemsAptitude[i].addActionListener(this);
            menuFilterAptitude.add(itemsAptitude[i]);
        }
        itemsAptitude[0].setText("Sin asignar");
        itemsAptitude[itemsAptitude.length - 1].setText("APTO B Y NO APTO");
        //Patologias
        menuPathologies = new JMenu("Patologias");
        menuFilter.add(menuPathologies);
        itemsPathologies = new JMenuItem[MyArrays.getPathologiesLength()];
        for (int i = 0; i < itemsPathologies.length; i++) {
            if (i == itemsPathologies.length - 1) {
                JPopupMenu.Separator patSeparator = new JPopupMenu.Separator();
                menuPathologies.add(patSeparator);
            }
            itemsPathologies[i] = new JMenuItem(MyArrays.getPathologies(i));
            itemsPathologies[i].addActionListener(this);
            menuPathologies.add(itemsPathologies[i]);
        }
        //Observaciones
        itemObs = new JMenuItem("Observaciones");
        itemObs.addActionListener(this);
        menuFilter.add(itemObs);
        
        //genero
        menuFilerGenre = new JMenu("Genero");
        menuFilter.add(menuFilerGenre);
        itemGenre = new JMenuItem[2];
        itemGenre[0] = new JMenuItem("Femenino");
        itemGenre[0].addActionListener(this);
        menuFilerGenre.add(itemGenre[0]);
        itemGenre[1] = new JMenuItem("Masculino");
        itemGenre[1].addActionListener(this);
        menuFilerGenre.add(itemGenre[1]);
        
        //----------------------------------------------------------
        JPopupMenu.Separator separador2 = new JPopupMenu.Separator();
        menuFilter.add(separador2);
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
        //MENU FILTRAR--------------------------------
        //FILTROS--------------------
        //
        //Lista completa
        if (e.getSource() == itemRefreshList) {
            personnelPanel.update(0, 0, 0);
            personnelPanel.setFiltered(false);
            personnelPanel.setBetweenTwoDates(false);
            deleteChecks();
        }
        // anexo vencido
        if (e.getSource() == itemCaducatedStudies) {
            caducatedStudies.setVisible(true);
        }
        // programa peso saludable
        for (int i = 0; i < itemsPPS.length; i++) {
            if (e.getSource() == itemsPPS[i]) {
                
                if (i != 0) {
                    personnelPanel.setPPSFilter(itemsPPS[i].getText());
                    personnelPanel.update(PersonnelPanel.FILTER_PPS, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                    personnelPanel.setFiltered(true);
                    
                    deleteChecksPPS();
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
                if (i != 0 && i < itemsAptitude.length - 1) {
                    personnelPanel.setAptitudeFilter(" Aptitud = \"" + itemsAptitude[i].getText() + "\"");
                } else if (i == itemsAptitude.length - 1) {
                    personnelPanel.setAptitudeFilter(" (Aptitud = \"APTO B\" OR Aptitud = \"NO APTO\")");
                } else {
                    personnelPanel.setAptitudeFilter("NULL");
                }
                personnelPanel.update(PersonnelPanel.FILTER_APTITUDE, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                personnelPanel.setFiltered(true);
                
                deleteChecksAptitude();
                menuFilterAptitude.setIcon(check);
                itemsAptitude[i].setIcon(check);
            }
        }
        // patologias
        for (int i = 0; i < itemsPathologies.length; i++) {
            if (e.getSource() == itemsPathologies[i]) {
                if (i < itemsPathologies.length - 1 && i != 0) {
                    personnelPanel.setPathologyColumn(MyArrays.getCheckBox(i - 1));
                    personnelPanel.update(PersonnelPanel.FILTER_PATHOLOGY, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                } else if (i != 0) {
                    personnelPanel.update(PersonnelPanel.FILTER_AJM, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                } else {
                    personnelPanel.update(PersonnelPanel.FILTER_ALL_PATHOLOGIES, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());

                }
                personnelPanel.setFiltered(true);
                
                
                deleteChecksPathologies();
                menuPathologies.setIcon(check);
                itemsPathologies[i].setIcon(check);
            }
        }
        // observaciones 
        if (e.getSource() == itemObs) {
            personnelPanel.update(PersonnelPanel.FILTER_OBS, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
            personnelPanel.setFiltered(true);

            itemObs.setIcon(check);
        }
        
        //genero
        for (int i = 0; i < itemGenre.length; i++) {
            if(e.getSource() == itemGenre[i]){
                personnelPanel.setGenreFilter(i == 0 ? 'F' : 'M');
                personnelPanel.update(PersonnelPanel.FILTER_GENRE, personnelPanel.getShowBySubUnity(), personnelPanel.getRowOrdering());
                deleteChecksGenre();
                menuFilerGenre.setIcon(check);
                itemGenre[i].setIcon(check);
                
            }
        }
        //-----------------------------------
        // MOSTRAR POR----------------------
        // destinos
        for (int i = 0; i < itemSubUnities.length; i++) {
            if (e.getSource() == itemSubUnities[i]) {
                personnelPanel.update(-1, i == 0 ? -1 : i, personnelPanel.getRowOrdering());
                personnelPanel.setFiltered(true);
                deleteChecksShowBy();
                menuSubUnities.setIcon(check);
                itemSubUnities[i].setIcon(check);
            }
        }
        //----------------------------------
        //ORDENAR---------------------------
        //ordenar por
        for (int i = 0; i < itemsOrderBy.length; i++) {
            if (e.getSource() == itemsOrderBy[i]) {
                personnelPanel.update(-1, personnelPanel.getShowBySubUnity(), i);
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
        if (e.getSource() == itemAbout) {
            about.setVisible(true);
        }
    }
    
    
    public void deleteCheckCaducatedStudy(){
        itemCaducatedStudies.setIcon(null);
    }
    
    public void deleteChecksPPS(){
         menuFilterPPS.setIcon(null);
        for (int i = 0; i < itemsPPS.length; i++) {
            itemsPPS[i].setIcon(null);
        }
    }
         
    public void deleteChecksAptitude(){
        menuFilterAptitude.setIcon(null);
        for (int i = 0; i < itemsAptitude.length; i++) {
            itemsAptitude[i].setIcon(null);
        }
    }
    
    public void deleteChecksPathologies(){
        menuPathologies.setIcon(null);
        for (int i = 0; i < itemsPathologies.length; i++) {
            itemsPathologies[i].setIcon(null);
        }
    }
    
    public void deleteCheckObs(){
        itemObs.setIcon(null);
    }


    public void deleteChecksShowBy() {
        for (int i = 0; i < itemSubUnities.length; i++) {
            itemSubUnities[i].setIcon(null);
        }
    }

    public void deleteChecksOrderBy() {
        for (int i = 0; i < itemsOrderBy.length; i++) {
            itemsOrderBy[i].setIcon(null);
        }
    }
    
    public void deleteChecksGenre(){
        menuFilerGenre.setIcon(null);
        for (int i = 0; i < itemGenre.length; i++) {
            itemGenre[i].setIcon(null);
        }
    }
    

    public void deleteChecks() {
        //FILTROS
        deleteCheckCaducatedStudy();
        deleteChecksPPS();
        deleteChecksAptitude();
        deleteChecksPathologies();
        deleteCheckObs();
        deleteChecksGenre();
        //DESTINOS
        menuSubUnities.setIcon(null);
        deleteChecksShowBy();
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

    public JMenuItem getItemCaducatedStudies() {
        return itemCaducatedStudies;
    }
    
    

}
