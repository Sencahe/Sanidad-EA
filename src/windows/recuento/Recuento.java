package windows.recuento;

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
import java.awt.event.KeyEvent;
import javax.swing.*;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import main.Configuracion;
import main.MainFrame;
import mytools.Arreglos;
import mytools.Utilidades;

public class Recuento extends JPanel implements ActionListener {

    private MainFrame mainFrame;
    private JButton botonParte;
    private JButton botonRecuento;
    private JLabel labelRecuento;
    private JTextField textRecuento;
    private JTable tabla;
    private JScrollPane scrollTabla;
    private DefaultTableModel model;
    private Dimension dimension;

    private JScrollPane scrollContainer;
    private Configuracion config;

    public Recuento(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
        labelRecuento = new JLabel("Ingrese el DNI para buscar en el recuento:");
        labelRecuento.setBounds(40, 60, 300, 30);
        labelRecuento.setFont(utilidad.getFuenteLabelGrande());
        labelRecuento.setForeground(Color.black);
        add(labelRecuento);
        textRecuento = new JTextField();
        textRecuento.setBounds(345, 60, 70, 25);
        textRecuento.addKeyListener(utilidad.soloNumeros);
        add(textRecuento);
        botonRecuento = utilidad.customButton();
        botonRecuento.setText("<html>Buscar</html>");
        botonRecuento.setBounds(420, 60, 70, 25);
        botonRecuento.addActionListener(this);
        add(botonRecuento);
        botonParte = utilidad.customButton();
        botonParte.setText("<html><center>Volver al Parte</center></html>");
        botonParte.setBounds(40, 15, 100, 35);
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
            if (i != 2 && i != 5 && i != 10) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        //scroll
        scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBounds(40, 100, 1350, 27);
        scrollTabla.setOpaque(false);
        scrollTabla.getViewport().setOpaque(false);
        scrollTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        add(scrollTabla);
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
    //METODO PARA ACTUALIZAR EL TAMAÑO DEL PANEL--------------------------------
    public void actualizarVentana() {
        int y = 100;

        int altura = 27 + tabla.getRowCount() * 16;
        scrollTabla.setBounds(15, y, 1350, altura);
        y += altura + 30;

        dimension.setSize(1505, y);
        this.setPreferredSize(dimension);
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
        if (e.getSource() == botonRecuento) {
            try {
                int dni = Integer.parseInt(textRecuento.getText());               
                Receptor receptor = new Receptor();
                receptor.getInformacion(this,dni);
                receptor = null;                              
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,"El numero ingresado es incorrecto.");
            }

        }
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
