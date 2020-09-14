package dialogs;

import mytools.Icons;
import mytools.Utilities;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import panels.PersonnelPanel;

public class Searcher extends JDialog implements ActionListener {

    private JButton button;
    private JTextField textSearch;
    private JLabel labelMessage, labelFound;
    private JRadioButton radioName, radioDNI;
    private KeyAdapter onlyNumbers;

    private int rowPointer;
    private int pointer;
    private int categorie;
    private String search;
    private String searching;
    private boolean found;
    private boolean searchNext;    
    private int column;

    private PersonnelPanel personnelPanel;
    private MainFrame mainFrame;
    private Configurator configurator;

    public Searcher(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        this.search = "";
        this.categorie = 0;
        this.found = false;
        this.column = PersonnelPanel.NAME_COLUMN;
        Componentes();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                pointer = 0;
                search = "";
                found = false;
                labelMessage.setText("");
                setTitle("Buscar");
                dispose();
            }
        });
    }

    private void Componentes() {
        //------------------------------
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        onlyNumbers = utility.soloNumeros;
        // FRAME DEL BUSCADOR
        setSize(400, 175);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Buscar");
        setIconImage(icons.getIconoSearchChico().getImage());
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(200, 50,
                        getBackground().brighter().brighter(), 250, 200,
                        getBackground().darker().darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        container.setBackground(utility.getColorBackground());
        Dimension dimension = new Dimension(400, 175);
        container.setPreferredSize(dimension);
        container.setLayout(null);

        dimension = null;
        //Text FIELD-------------------
        textSearch = new JTextField();
        textSearch.setBounds(100, 45, 250, 25);
        textSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarPorNombre();
                }
            }
        });
        add(textSearch);
        // Boton---------------------
        button = new JButton("Buscar");
        button.setBounds(100, 80, 90, 30);
        button.addActionListener(this);
        add(button);
        //RadioButton
        ButtonGroup rg = new ButtonGroup();
        radioName = new JRadioButton("Buscar por Nombre");
        radioName.setBounds(210, 80, 190, 20);
        radioName.setOpaque(false);
        radioName.addActionListener(this);
        radioName.setSelected(true);
        rg.add(radioName);
        add(radioName);
        radioDNI = new JRadioButton("Buscar por DNI");
        radioDNI.setBounds(210, 100, 190, 20);
        radioDNI.setOpaque(false);
        radioDNI.addActionListener(this);
        rg.add(radioDNI);
        add(radioDNI);
        // Label con mensaje resultado----------------------
        labelMessage = new JLabel();
        labelMessage.setFont(utility.getFuenteMsgBuscador());
        labelMessage.setBounds(100, 110, 250, 40);
        add(labelMessage);
        // label informativo
        labelFound = new JLabel("Ingrese palabra para buscar por Apellido y Nombre");
        labelFound.setBounds(30 + 70, 15, 300, 30);
        labelFound.setFont(utility.getFuenteRsltBuscador());
        add(labelFound);
        // Icono Buscar-----------------------------
        JLabel png = new JLabel();
        png.setBounds(15, 30, 64, 64);
        ImageIcon icono = icons.getIconoSearch();
        png.setIcon(icono);
        add(png);
        png = null;
        //fin        
        this.getContentPane().add(container);
        icons = null;
        utility = null;
    }

    //------------------------------------------------------------------------
    //-----------------METODO PARA BUSCAR EN LAS TABLAS-----------------------
    public void buscarPorNombre() {
           
        //SOLO BUSCA SI HAY TEXTO EN EL TEXT FIELD
        if (!"".equals(textSearch.getText())) {
            //REINICIA EL PUNTERO SI SE CAMBIA LA PALABRA A SER BUSCADA
            if (!search.equals(textSearch.getText().toLowerCase().trim())) {
                pointer = 0;
                found = false;
            }
            //REINCIA EL PUNTERO SI SE CAMBIA LA TABLA EN LA QUE SE BUSCA
            if (categorie != personnelPanel.getTabbedPane().getSelectedIndex()) {
                pointer = 0;
                found = false;
                setTitle("Buscando en " + personnelPanel.getTabbedPane().getTitleAt(personnelPanel.getTabbedPane().getSelectedIndex()).trim());
            }
            if(rowPointer != personnelPanel.getTables(categorie).getRowCount()){
                pointer = 0;
                found = false;
            }
            
            searching = "";
            search = textSearch.getText().toLowerCase().trim();
            categorie = personnelPanel.getTabbedPane().getSelectedIndex();
            rowPointer = personnelPanel.getTables(categorie).getRowCount();
            
            searchNext = true;

            while (searchNext) {
                if (pointer == personnelPanel.getTables(categorie).getRowCount()) {
                    labelMessage.setText("");
                    pointer = 0;
                    if (!found) {
                        searchNext = false;
                        JOptionPane.showMessageDialog(null, new JLabel("No se ha encontrado.", JLabel.CENTER));
                    }
                } else {
                    searching = String.valueOf(personnelPanel.getTables(categorie).getValueAt(pointer, column)).toLowerCase();
                    if (searching.contains(search)) {
                        configurator.configRow.setSelected(false);
                        personnelPanel.getScroll(categorie).getVerticalScrollBar().setValue(pointer * 16);
                        personnelPanel.getTables(categorie).setRowSelectionInterval(pointer, pointer);
                        labelMessage.setText((String) personnelPanel.getTables(categorie).getValueAt(pointer, 3));
                        searchNext = false;
                        found = true;
                        pointer++;
                    } else {
                        pointer++;
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            buscarPorNombre();
            System.gc();
        }
        if (e.getSource() == radioName) {
            labelFound.setText("Ingrese palabra para buscar por Apellido y Nombre");
            textSearch.setText("");
            textSearch.removeKeyListener(onlyNumbers);
            column = PersonnelPanel.NAME_COLUMN;
        }
        if (e.getSource() == radioDNI) {
            labelFound.setText("Ingrese numero para buscar por DNI");
            textSearch.setText("");
            textSearch.addKeyListener(onlyNumbers);
            column = PersonnelPanel.DNI_COLUMN;
        }

    }

    public void setPersonnelPanel(PersonnelPanel tabla) {
        this.personnelPanel = tabla;
    }

    public void setConfigurator(Configurator configuracion) {
        this.configurator = configuracion;
    }

}
