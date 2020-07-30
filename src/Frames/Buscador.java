package Frames;

import Tools.Arreglos;
import Tools.Iconos;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Buscador extends javax.swing.JDialog implements ActionListener {

    public static JButton boton;
    public static JTextField texto;
    public static JLabel mensaje;
    public static int puntero = 0;
    public static int categoria;
    public static String buscar = "";
    public static boolean encontrado = false;
    public static String[] categorias;
     //Hace falta que sean staticos? intentar ponerlos como privados ***

    public Buscador(java.awt.Frame parent, boolean modal) {
        // FRAME DEL BUSCADOR
        super(parent, modal);
        setLayout(null);
        setSize(400, 175);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Buscar");
        // COMPONENTES-----------------------------------------------
        //Text FIELD-------------------
        texto = new JTextField();
        texto.setBounds(100, 45, 250, 25);
        texto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    buscarPorNombre();
                }
            }   
        });
        texto.setVisible(true);
        add(texto);
        // Boton---------------------
        boton = new JButton("Buscar");
        boton.setBounds(100, 80, 90, 30);
        boton.addActionListener(this);
        add(boton);
        // Label----------------------
        mensaje = new JLabel();
        mensaje.setFont(new Font("Tahoma",1,11));
        mensaje.setBounds(100, 100, 250, 40);
        add(mensaje);
        //-------------------------------------
       
        categorias = new Arreglos().Categorias();

        //ACTIVAR COMPONENTES DE LA BARRA AL CERRARSE BUSCADOR
        try {
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    Tabla.menuFiltrar.setEnabled(true);
                    Tabla.menuRef.setEnabled(true);
                    Tabla.menuBuscar.setEnabled(true);
                    
                }
            });
        } catch (Exception e) {
        }
        // otro label
        JLabel label = new JLabel("Ingrese palabra para buscar por Apellido y Nombre");
        label.setBounds(30+70,15,300,30);
        label.setFont(new Font("Tahoma", 0, 11));
        add(label);
        // Icono Buscar-----------------------------
        JLabel png = new JLabel();
        png.setBounds(15, 30, 64, 64);
        ImageIcon icono = new Iconos().getIconoSearch();
        png.setIcon(icono);
        add(png);
        //Icono frame
        setIconImage(new Iconos().getIconoSearchChico().getImage());
        //wallpaper
        JLabel wallpaper = new JLabel();
        wallpaper.setBounds(0, 0, this.getWidth(), this.getHeight());
        add(wallpaper);
        Icon fondo = new ImageIcon((new Iconos().getWallpaperFormulario()).getImage().getScaledInstance(wallpaper.getWidth(),
                wallpaper.getHeight(), Image.SCALE_DEFAULT));
        wallpaper.setIcon(fondo);
    }    
    //-----------------CODIGO PARA BUSCAR EN LAS TABLAS------------------------
    public void buscarPorNombre() {
        //SOLO BUSCA SI HAY TEXTO EN EL TEXT FIELD
        if (!"".equals(texto.getText())) {
            //REINICIA EL PUNTERO SI SE CAMBIA LA PALABRA A SER BUSCADA
            if (!buscar.equals(texto.getText().toLowerCase().trim())) {
                puntero = 0;
                encontrado = false;
            }
            //REINCIA EL PUNTERO SI SE CAMBIA LA TABLA EN LA QUE SE BUSCA
            if (categoria != Tabla.contenedor.getSelectedIndex()) {
                puntero = 0;
                encontrado = false;
            }
            //CAMBIAR EL TITULO SEGUN LA TABLA BUSCADA
            for (int i = 0; i < categorias.length; i++) {
                if (categorias[i].equals(categorias[Tabla.contenedor.getSelectedIndex()])) {
                    setTitle("Buscando en " + categorias[i]);
                }
            }

            buscar = texto.getText().toLowerCase().trim();
            categoria = Tabla.contenedor.getSelectedIndex();
            boolean buscarSiguiente = true;

            while (buscarSiguiente) {
                if (puntero == Tabla.tablas[categoria].getRowCount()) {
                    //Tabla.tablas[categoria].getSelectionModel().clearSelection();
                    mensaje.setText("");
                    puntero = 0;
                    if (!encontrado) {
                        buscarSiguiente = false;
                        JOptionPane.showMessageDialog(null, new JLabel("No se ha encontrado.",JLabel.CENTER));
                    }
                } else {
                    String nombre = (String) Tabla.tablas[categoria].getValueAt(puntero, 3);
                    nombre = nombre.toLowerCase();
                    int buscando = nombre.indexOf(buscar);
                    if (buscando != -1) {
                        Tabla.scrolls[categoria].getVerticalScrollBar().setValue(puntero * 10);
                        Tabla.tablas[categoria].setRowSelectionInterval(puntero, puntero);
                        mensaje.setText((String) Tabla.tablas[categoria].getValueAt(puntero, 3));
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
    }

//    public static void main(String[] args) {
//        Buscador buscar = new Buscador(null, false);
//        buscar.setVisible(true);
//    }
}


