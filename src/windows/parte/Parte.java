package windows.parte;

import database.BaseDeDatos;
import mytools.Arreglos;
import mytools.Utilidades;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;


public class Parte extends JPanel implements ActionListener {

    private JScrollPane scrollContainer;

    private JButton botonTabla;
    
    private JLabel[] titulos;
    private JTable[] tablas;
    private JScrollPane[] scrolls;
    private Dimension dimension;

    private FormularioParte formParte;

    public Parte() {
        componentes();
    }

    private void componentes() {
        //OBJETOS AUXILIARES       
        Utilidades utilidad = new Utilidades();
        //-----PROPIEDADES DEL PANEL-----------------
        dimension = new Dimension();
        setBackground(utilidad.getColorFondo());
        setOpaque(false);
        setLayout(null);
        scrollContainer = new JScrollPane(this);
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.getVerticalScrollBar().setUnitIncrement(16);
        //BOTONES--------------------------------------------------
        botonTabla = new JButton("Volver");
        botonTabla.setBounds(1159, 15, 100, 30);
        botonTabla.setFont(utilidad.getFuenteBoton());
        botonTabla.setVisible(true);
        add(botonTabla);
        //-----------------------------------------------------------------------------------
        //TABLAS DEL PARTE------------------------------------------------------------------
        tablas = new JTable[4];
        scrolls = new JScrollPane[4];
        titulos = new JLabel[4];
        String[] tituloLabels = {"PARTE DE ENFERMO", "PARTE DE EXCEPTUADO", "PARTE DE MATERNIDAD", "PERSONAL QUE NO PASO NOVEDAD"};
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            //creacion del table model y de la tabla
            DefaultTableModel model = new DefaultTableModel();
            tablas[i] = new JTable(model) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                @Override
                public void changeSelection(int rowIndex, int columnIndex,
                        boolean toggle, boolean extend) {
                    super.changeSelection(rowIndex, columnIndex, false, false);
                }
            };
            //eventos al presionar teclas en las tablas
            tablas[i].getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            tablas[i].getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    for (int i = 0; i < 4; i++) {
                        if (ae.getSource() == tablas[i]) {
                            abrirFormulario(i);
                        }
                    }
                }
            });
            //eventos al clikear la tabla          
            tablas[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    for (int i = 0; i < 4; i++) {
                        if (e.getClickCount() >= 2) {
                            abrirFormulario(i);
                        }
                    }
                }
            });
            //seleccionar una tabla a la vez
            tablas[i].addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    super.focusLost(e);
                    for (int i = 0; i < 4; i++) {
                        if (e.getSource() == tablas[i]) {
                            int count = tablas[i].getRowCount();
                            if (count >= 1) {
                                tablas[i].removeRowSelectionInterval(0, count - 1);
                            }
                        }
                    }
                }
            });
            //propiedades de la tabla
            tablas[i].setGridColor(Color.black);
            tablas[i].setBackground(utilidad.getColorTabla());
            tablas[i].setFont(utilidad.getFuenteTabla());
            tablas[i].setRowHeight(16);
            //header
            JTableHeader header = tablas[i].getTableHeader();
            header.setFont(utilidad.getFuenteHeader());
            header.setBackground(utilidad.getColorTabla());
            header.setPreferredSize(new Dimension(40, 26));
            header.setReorderingAllowed(false);
            ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            //creacion de las columnas
            for (int j = 0; j < Arreglos.getColumnasParteLength(); j++) {
                model.addColumn(Arreglos.getColumnasParte(j));
            }
            //tamaño de las columnas
            for (int j = 0; j < Arreglos.getColumnasParteLength(); j++) {
                tablas[i].getColumnModel().getColumn(j).setMinWidth(Arreglos.getTamañoColumnParte(j));
                tablas[i].getColumnModel().getColumn(j).setMaxWidth(Arreglos.getTamañoColumnParte(j));
                tablas[i].getColumnModel().getColumn(j).setPreferredWidth(Arreglos.getTamañoColumnParte(j));
                //centrado del contenido de las columnas
                if (j != 2 && j != 4) {
                    tablas[i].getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
                }
                //ocultando la columna con el id
                if (j == 11) {
                    tablas[i].getColumnModel().removeColumn(tablas[i].getColumnModel().getColumn(j));
                }
            }
            //Titulo, Scrolls          
            titulos[i] = new JLabel(tituloLabels[i]);
            titulos[i].setFont(utilidad.getFuenteLabelTitulo());
            titulos[i].setForeground(Color.black);
            scrolls[i] = new JScrollPane(tablas[i]);
            scrolls[i].setOpaque(false);
            scrolls[i].getViewport().setOpaque(false);
            scrolls[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            add(scrolls[i]);
            add(titulos[i]);
        }
        //FINALIZACION DE LOS COMPONENTES        
        BaseDeDatos bdd = new BaseDeDatos();
        bdd.actualizar(this);
        bdd = null;

        actualizarVentana();
    }

    //FONDO DEL PANEL
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(100, 500,
                getBackground().brighter(), 500, 500,
                getBackground().darker().darker());
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    //METODO PARA ACTUALIZAR EL FRAME------------------------------------------
    public void actualizarVentana() {
        int y = 60;
        for (int i = 0; i < 4; i++) {
            int altura = 27 + tablas[i].getRowCount() * 16;
            scrolls[i].setBounds(15, y, 1261, altura);
            titulos[i].setBounds(60, y - 40, 400, 40);
            y += altura + 55;
        }

        dimension.setSize(1505, y);
        this.setPreferredSize(dimension);
    }

    //METODO PARA ABRIR EL FORMULARIO DEL PARTE---------------------------------
    public void abrirFormulario(int i) {
        int puntero = tablas[i].getSelectedRow();
        if (puntero != -1) {
            int idParte = (int) tablas[i].getModel().getValueAt(puntero, 11);
            formParte.obtenerDatos(idParte);
        }
    }

    //-----------------------SETTERS Y GETTERS---------------------------------
    public DefaultTableModel getTableModel(int index) {
        return (DefaultTableModel) tablas[index].getModel();
    }

    public JTable[] getTablas() {
        return tablas;
    }

    public JTable getTablas(int index) {
        return tablas[index];
    }

    public void setFormParte(FormularioParte formParte) {
        this.formParte = formParte;
    }

    public JScrollPane getScrollContainer() {
        return scrollContainer;
    }

    public JButton getBotonTabla() {
        return botonTabla;
    }

    
}
