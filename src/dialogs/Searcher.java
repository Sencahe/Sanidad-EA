package dialogs;

import mytools.Icons;
import mytools.Utilities;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import panels.PersonnelPanel;

public class Searcher extends JDialog implements ActionListener {

    private JButton boton;
    private JTextField textBuscar;
    private JLabel mensaje, labelEncontrado;
    private JRadioButton radioNombre, radioDNI;
    private KeyAdapter soloNumeros;

    private int puntero;
    private int categoria;
    private String buscar;
    private boolean encontrado;
    private boolean buscarSiguiente;
    private String buscando;
    private int columna;

    private PersonnelPanel tabla;
    private MainFrame mainFrame;
    private Configurator configuracion;

    public Searcher(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        this.buscar = "";
        this.categoria = -1;
        this.encontrado = false;
        this.columna = 3;
        Componentes();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                puntero = 0;
                buscar = "";
                encontrado = false;
                categoria = -1;
                mensaje.setText("");
                setTitle("Buscar");
                dispose();
            }
        });
    }

    private void Componentes() {
        //------------------------------
        Utilities utilidad = mainFrame.getUtility();
        Icons iconos = mainFrame.getIcons();
        soloNumeros = utilidad.soloNumeros;
        // FRAME DEL BUSCADOR
        setSize(400, 175);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Buscar");
        setIconImage(iconos.getIconoSearchChico().getImage());
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
        container.setBackground(utilidad.getColorFondo());
        Dimension dimension = new Dimension(400, 175);
        container.setPreferredSize(dimension);
        container.setLayout(null);

        dimension = null;
        //Text FIELD-------------------
        textBuscar = new JTextField();
        textBuscar.setBounds(100, 45, 250, 25);
        textBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarPorNombre();
                }
            }
        });
        add(textBuscar);
        // Boton---------------------
        boton = new JButton("Buscar");
        boton.setBounds(100, 80, 90, 30);
        boton.addActionListener(this);
        add(boton);
        //RadioButton
        ButtonGroup rg = new ButtonGroup();
        radioNombre = new JRadioButton("Buscar por Nombre");
        radioNombre.setBounds(210, 80, 190, 20);
        radioNombre.setOpaque(false);
        radioNombre.addActionListener(this);
        radioNombre.setSelected(true);
        rg.add(radioNombre);
        add(radioNombre);
        radioDNI = new JRadioButton("Buscar por DNI");
        radioDNI.setBounds(210, 100, 190, 20);
        radioDNI.setOpaque(false);
        radioDNI.addActionListener(this);
        rg.add(radioDNI);
        add(radioDNI);
        // Label con mensaje resultado----------------------
        mensaje = new JLabel();
        mensaje.setFont(utilidad.getFuenteMsgBuscador());
        mensaje.setBounds(100, 100, 250, 40);
        add(mensaje);
        // label informativo
        labelEncontrado = new JLabel("Ingrese palabra para buscar por Apellido y Nombre");
        labelEncontrado.setBounds(30 + 70, 15, 300, 30);
        labelEncontrado.setFont(utilidad.getFuenteRsltBuscador());
        add(labelEncontrado);
        // Icono Buscar-----------------------------
        JLabel png = new JLabel();
        png.setBounds(15, 30, 64, 64);
        ImageIcon icono = iconos.getIconoSearch();
        png.setIcon(icono);
        add(png);
        png = null;
        //fin        
        this.getContentPane().add(container);
        iconos = null;
        utilidad = null;
    }

    //------------------------------------------------------------------------
    //-----------------METODO PARA BUSCAR EN LAS TABLAS-----------------------
    public void buscarPorNombre() {

        //SOLO BUSCA SI HAY TEXTO EN EL TEXT FIELD
        if (!"".equals(textBuscar.getText())) {
            //REINICIA EL PUNTERO SI SE CAMBIA LA PALABRA A SER BUSCADA
            if (!buscar.equals(textBuscar.getText().toLowerCase().trim())) {
                puntero = 0;
                encontrado = false;
            }
            //REINCIA EL PUNTERO SI SE CAMBIA LA TABLA EN LA QUE SE BUSCA
            if (categoria != tabla.getTabbedPane().getSelectedIndex()) {
                puntero = 0;
                encontrado = false;
                setTitle("Buscando en " + tabla.getTabbedPane().getTitleAt(tabla.getTabbedPane().getSelectedIndex()).trim());
            }

            buscando = "";
            buscar = textBuscar.getText().toLowerCase().trim();
            categoria = tabla.getTabbedPane().getSelectedIndex();
            buscarSiguiente = true;

            while (buscarSiguiente) {
                if (puntero == tabla.getTablas(categoria).getRowCount()) {
                    mensaje.setText("");
                    puntero = 0;
                    if (!encontrado) {
                        buscarSiguiente = false;
                        JOptionPane.showMessageDialog(null, new JLabel("No se ha encontrado.", JLabel.CENTER));
                    }
                } else {
                    buscando = String.valueOf(tabla.getTablas(categoria).getValueAt(puntero, columna));
                    if (buscando.contains(buscar)) {
                        configuracion.configRow.setSelected(false);
                        tabla.getScroll(categoria).getVerticalScrollBar().setValue(puntero * 16);
                        tabla.getTablas(categoria).setRowSelectionInterval(puntero, puntero);
                        mensaje.setText((String) tabla.getTablas(categoria).getValueAt(puntero, 3));
                        buscarSiguiente = false;
                        encontrado = true;
                        puntero++;
                    } else {
                        puntero++;
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boton) {
            buscarPorNombre();
            System.gc();
        }
        if (e.getSource() == radioNombre) {
            labelEncontrado.setText("Ingrese palabra para buscar por Apellido y Nombre");
            textBuscar.setText("");
            textBuscar.removeKeyListener(soloNumeros);
            columna = 3;
        }
        if (e.getSource() == radioDNI) {
            labelEncontrado.setText("Ingrese numero para buscar por DNI");
            textBuscar.setText("");
            textBuscar.addKeyListener(soloNumeros);
            columna = 5;
        }

    }

    public void setTabla(PersonnelPanel tabla) {
        this.tabla = tabla;
    }

    public void setConfiguracion(Configurator configuracion) {
        this.configuracion = configuracion;
    }

}
