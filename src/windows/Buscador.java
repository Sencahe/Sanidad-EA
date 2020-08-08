package windows;

import mytools.Arreglos;
import mytools.Iconos;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import mytools.Utilidades;

public class Buscador extends javax.swing.JDialog implements ActionListener {

    private JButton boton;
    private JTextField texto;
    private JLabel mensaje;
    private int puntero;
    private int categoria;
    private String buscar;
    private boolean encontrado;
    private boolean buscarSiguiente;
    private String nombre;

    public Buscador(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.buscar = "";
        Componentes();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Tabla.habilitarMenuBuscar();
                Tabla.habilitarMenuFiltrar();
                Tabla.habilitarMenuRef();
                puntero = 0;
                buscar = "";
                encontrado = false;
                mensaje.setText("");
                dispose();
            }
        });

    }

    private void Componentes() {
        //------------------------------
        Utilidades utilidad = new Utilidades();
        Iconos iconos = new Iconos();
        // FRAME DEL BUSCADOR
        setLayout(null);
        setSize(400, 175);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Buscar");
        //Text FIELD-------------------
        texto = new JTextField();
        texto.setBounds(100, 45, 250, 25);
        texto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarPorNombre();
                }
            }
        });
        add(texto);
        // Boton---------------------
        boton = new JButton("Buscar");
        boton.setBounds(100, 80, 90, 30);
        boton.addActionListener(this);
        add(boton);
        // Label con mensaje resultado----------------------
        mensaje = new JLabel();
        mensaje.setFont(utilidad.fuenteMsgBuscador());
        mensaje.setBounds(100, 100, 250, 40);
        add(mensaje);
        // label informativo
        JLabel label = new JLabel("Ingrese palabra para buscar por Apellido y Nombre");
        label.setBounds(30 + 70, 15, 300, 30);
        label.setFont(utilidad.fuenteRsltBuscador());
        add(label);
        label = null;
        // Icono Buscar-----------------------------
        JLabel png = new JLabel();
        png.setBounds(15, 30, 64, 64);
        ImageIcon icono = iconos.getIconoSearch();
        png.setIcon(icono);
        add(png);
        png = null;
        //Icono frame
        setIconImage(iconos.getIconoSearchChico().getImage());
        //wallpaper
        JLabel wallpaper = new JLabel();
        wallpaper.setBounds(0, 0, this.getWidth(), this.getHeight());
        add(wallpaper);
        Icon fondo = new ImageIcon((iconos.getWallpaperFormulario()).getImage().getScaledInstance(wallpaper.getWidth(),
                wallpaper.getHeight(), Image.SCALE_DEFAULT));
        wallpaper.setIcon(fondo);
        fondo = null;
        wallpaper = null;
        iconos = null;
        utilidad = null;
    }

    //------------------------------------------------------------------------
    //-----------------METODO PARA BUSCAR EN LAS TABLAS-----------------------
    public void buscarPorNombre() {

        //SOLO BUSCA SI HAY TEXTO EN EL TEXT FIELD
        if (!"".equals(texto.getText())) {
            //REINICIA EL PUNTERO SI SE CAMBIA LA PALABRA A SER BUSCADA
            if (!buscar.equals(texto.getText().toLowerCase().trim())) {
                puntero = 0;
                encontrado = false;
            }
            //REINCIA EL PUNTERO SI SE CAMBIA LA TABLA EN LA QUE SE BUSCA
            if (categoria != Tabla.getContenedor().getSelectedIndex()) {
                puntero = 0;
                encontrado = false;
                setTitle("Buscando en " + Tabla.getContenedor().getSelectedComponent().getName().trim());
            }
            

            buscar = texto.getText().toLowerCase().trim();
            categoria = Tabla.getContenedor().getSelectedIndex();
            buscarSiguiente = true;

            while (buscarSiguiente) {
                if (puntero == Tabla.getTablas(categoria).getRowCount()) {
                    mensaje.setText("");
                    puntero = 0;
                    if (!encontrado) {
                        buscarSiguiente = false;
                        JOptionPane.showMessageDialog(null, new JLabel("No se ha encontrado.", JLabel.CENTER));
                    }
                } else {
                    nombre = ((String) Tabla.getTablas(categoria).getValueAt(puntero, 3)).toLowerCase();
                    if (nombre.contains(buscar)) {
                        Tabla.getScroll(categoria).getVerticalScrollBar().setValue(puntero * 10);
                        Tabla.getTablas(categoria).setRowSelectionInterval(puntero, puntero);
                        mensaje.setText((String) Tabla.getTablas(categoria).getValueAt(puntero, 3));
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
        }
        System.gc();
    }

}
