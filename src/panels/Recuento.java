package panels;

import database.Receptor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import dialogs.Configuracion;
import main.MainFrame;
import mytools.Arreglos;
import mytools.Utilidades;

public class Recuento extends JPanel implements ActionListener {

    private MainFrame mainFrame;
    private JButton botonParte;
    private JButton botonRecuentoDNI, botonRecuentoTodos, botonLimpiar, botonBuscarPorNombre;
    private JLabel labelRecuento, labelBuscarRecuento, labelInfoBuscarRecuento;
    private JTextField textRecuento, textBuscarRecuento;
    private JTable tabla;
    private JScrollPane scrollTabla;
    private DefaultTableModel model;
    private Dimension dimension;

    private JScrollPane scrollContainer;
    private Configuracion config;

    int puntero;
    boolean encontrado;

    public Recuento(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        puntero = 0;
        encontrado = false;
        componentes();
        shortcuts();
    }

    public void componentes() {
        //------------------------------------------
        Utilidades utilidad = mainFrame.getUtilidad();
        //PROPIEDADES DEL PANEL-------------------------------------------------
        setBackground(utilidad.getColorFondo());
        dimension = new Dimension(1505, 580);
        setPreferredSize(dimension);
        setLayout(null);
        setOpaque(false);
        scrollContainer = new JScrollPane(this);
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //BOTON---------------------------------------------------------
        textRecuento = new JTextField();
        textRecuento.setBounds(345, 65, 70, 20);
        textRecuento.addKeyListener(utilidad.soloNumeros);
        textRecuento.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarPorDNI();
                }
            }
        });
        add(textRecuento);
        textBuscarRecuento = new JTextField();
        textBuscarRecuento.setBounds(840, 65, 150, 20);
        textBuscarRecuento.setVisible(false);
        textBuscarRecuento.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarPorNombre();
                }
            }
        });
        add(textBuscarRecuento);
        //labels-----------------------------------
        labelRecuento = new JLabel("Ingrese el DNI para buscar en el recuento:");
        labelRecuento.setBounds(40, 60, 300, 30);
        labelRecuento.setFont(utilidad.getFuenteLabelGrande());
        labelRecuento.setForeground(Color.black);
        add(labelRecuento);
        labelBuscarRecuento = new JLabel("Buscar por nombre:");
        labelBuscarRecuento.setBounds(690, 60, 170, 30);
        labelBuscarRecuento.setFont(utilidad.getFuenteLabelGrande());
        labelBuscarRecuento.setForeground(Color.black);
        labelBuscarRecuento.setVisible(false);
        add(labelBuscarRecuento);
        labelInfoBuscarRecuento = new JLabel("<html>La lista completa del recuento esta "
                + "ordenada en funcion al momento que se les dio de alta medica en el sistema.</html>");
        labelInfoBuscarRecuento.setBounds(690, 30, 280, 30);
        labelInfoBuscarRecuento.setForeground(Color.black);
        labelInfoBuscarRecuento.setVisible(false);
        add(labelInfoBuscarRecuento);
        //botones------------------------------------
        botonRecuentoDNI = utilidad.customButton();
        botonRecuentoDNI.setText("<html>Buscar</html>");
        botonRecuentoDNI.setBounds(420, 61, 70, 25);
        botonRecuentoDNI.addActionListener(this);
        add(botonRecuentoDNI);
        botonRecuentoTodos = utilidad.customButton();
        botonRecuentoTodos.setText("<html>Todos</html>");
        botonRecuentoTodos.setBounds(510, 61, 70, 25);
        botonRecuentoTodos.addActionListener(this);
        add(botonRecuentoTodos);
        botonLimpiar = utilidad.customButton();
        botonLimpiar.setText("<html>Limpiar</html>");
        botonLimpiar.setBounds(600, 61, 70, 25);
        botonLimpiar.addActionListener(this);
        add(botonLimpiar);
        botonBuscarPorNombre = utilidad.customButton();
        botonBuscarPorNombre.setText("<html>Buscar</html>");
        botonBuscarPorNombre.setBounds(1005, 61, 70, 25);
        botonBuscarPorNombre.addActionListener(this);
        botonBuscarPorNombre.setVisible(false);
        add(botonBuscarPorNombre);
              
        botonParte = utilidad.customButton();
        botonParte.setText("<html><center>Volver al Parte</center></html>");
        botonParte.setBounds(40, 15, 100, 35);
        botonParte.addActionListener(this);
        add(botonParte);

        //TABLA CON RECUENTO----------------------------------------------------
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(model);
        tabla.setGridColor(Color.black);
        tabla.setBackground(utilidad.getColorTabla());
        tabla.setFont(utilidad.getFuenteTabla());
        tabla.setRowHeight(16);
        JTableHeader header = tabla.getTableHeader();
        header.setFont(utilidad.getFuenteHeader());
        header.setBackground(utilidad.getColorTabla());
        header.setPreferredSize(new Dimension(40, 27));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        //creacion de las columnas
        for (int i = 0; i < Arreglos.getColumnasRecuentoLength(); i++) {
            model.addColumn(Arreglos.getColumnasRecuento(i));
        }
        //tamaño de las columnas y filas
        for (int i = 0; i < Arreglos.getColumnasRecuentoLength(); i++) {
            tabla.getColumnModel().getColumn(i).setMinWidth(Arreglos.getTamañoColumnasRecuento(i));
            tabla.getColumnModel().getColumn(i).setMaxWidth(Arreglos.getTamañoColumnasRecuento(i));
            tabla.getColumnModel().getColumn(i).setPreferredWidth(Arreglos.getTamañoColumnasRecuento(i));
            //centrado del contenido de las columnas
            if (i != 2 && i != 5) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        //scroll
        scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBounds(40, 100, 1367, 27);
        scrollTabla.setPreferredSize(new Dimension(1350, 1300));
        scrollTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollTabla);
    }

    //--------------------------------------------------------------------------
    //----------------------------FONDO DEL PANEL-------------------------------
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(200, 500,
                getBackground().brighter().brighter(), 1000, 500,
                getBackground().darker().darker());
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    //-----------------------------EVENTOS--------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonRecuentoDNI) {
            buscarPorDNI();
        }

        if (e.getSource() == botonRecuentoTodos) {
            Receptor receptor = new Receptor();
            receptor.getInformacion(this, 0, true);
            labelInfoBuscarRecuento.setVisible(true);
            labelBuscarRecuento.setVisible(true);
            textBuscarRecuento.setVisible(true);
            botonBuscarPorNombre.setVisible(true);
        }
        if (e.getSource() == botonLimpiar) {
            ((DefaultTableModel) tabla.getModel()).setRowCount(0);
            actualizarVentana();
            botonBuscarPorNombre.setVisible(false);
            labelBuscarRecuento.setVisible(false);
            labelInfoBuscarRecuento.setVisible(false);
            textBuscarRecuento.setVisible(false);
        }
        if (e.getSource() == botonParte) {
            puntero = 0;
            encontrado = false;
            ((DefaultTableModel) tabla.getModel()).setRowCount(0);
            actualizarVentana();
            botonBuscarPorNombre.setVisible(false);
            labelBuscarRecuento.setVisible(false);
            labelInfoBuscarRecuento.setVisible(false);
            textBuscarRecuento.setVisible(false);
        }
        if(e.getSource() == botonBuscarPorNombre){
            buscarPorNombre();
        }
    }

    //Buscar por dni
    private void buscarPorDNI() {
        try {
            long dni = Long.parseLong(textRecuento.getText());
            labelBuscarRecuento.setVisible(false);
            textBuscarRecuento.setVisible(false);
            botonBuscarPorNombre.setVisible(false);
            labelInfoBuscarRecuento.setVisible(false);
            Receptor receptor = new Receptor();
            receptor.getInformacion(this, dni, false);
            receptor = null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "El numero ingresado es incorrecto." + ex);
        }
    }

    //buscar por nombre
    private void buscarPorNombre() {
        String nombre = "";
        String buscar = textBuscarRecuento.getText().toLowerCase().trim();
        boolean buscarSiguiente = true;
        while (buscarSiguiente) {
            if (puntero == tabla.getRowCount()) {
                puntero = 0;
                if (!encontrado) {
                    buscarSiguiente = false;
                    JOptionPane.showMessageDialog(null, new JLabel("No se ha encontrado.", JLabel.CENTER));
                }
            } else {
                nombre = ((String) tabla.getValueAt(puntero, 2)).toLowerCase();
                if (nombre.contains(buscar)) {
                    scrollTabla.getVerticalScrollBar().setValue(puntero * 16);
                    tabla.setRowSelectionInterval(puntero, puntero);
                    buscarSiguiente = false;
                    encontrado = true;
                    puntero++;
                } else {
                    puntero++;
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    //----------------------------ACCESOS RAPIDOS-------------------------------
    private void shortcuts() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "ordenarColumnas");
        getActionMap().put("ordenarColumnas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarColumnas();
            }
        });
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), "bloquearTablas");
        getActionMap().put("bloquearTablas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (config.configColumns.isSelected()) {
                    config.configColumns.setSelected(false);
                } else {
                    config.configColumns.setSelected(true);
                }
            }
        });
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "seleccionarCeldas");
        getActionMap().put("seleccionarCeldas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (config.configRow.isSelected()) {
                    config.configRow.setSelected(false);

                } else {
                    config.configRow.setSelected(true);
                }
            }
        });
    }

    //--------------------------------------------------------------------------
    //---------------------------ORDENAR COLUMNAS-------------------------------
    public void ordenarColumnas() {
        TableModel model = tabla.getModel();
        TableColumnModel tcm = tabla.getColumnModel();
        for (int j = 0; j < model.getColumnCount() - 1; j++) {
            int location = tcm.getColumnIndex(model.getColumnName(j));
            tcm.moveColumn(location, j);
        }
        model = null;
        tcm = null;

    }

    //--------------------------------------------------------------------------
    //---------------METODO PARA ACTUALIZAR EL TAMAÑO DEL PANEL-----------------
    public void actualizarVentana() {
        int y = 100;

        int altura = 28 + tabla.getRowCount() * 16;

        if (altura > 460) {
            scrollTabla.setBounds(40, y, 1367, 460);
        } else {
            scrollTabla.setBounds(40, y, 1367, altura);
        }

        y += altura + 30;

    }

    //----------------------------GETTER Y SETTER-------------------------------
    public JScrollPane getScrollContainer() {
        return scrollContainer;
    }

    public JButton getBotonParte() {
        return botonParte;
    }

    public void setConfig(Configuracion config) {
        this.config = config;
    }

    public JTable getTabla() {
        return tabla;
    }

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) tabla.getModel();
    }

}
