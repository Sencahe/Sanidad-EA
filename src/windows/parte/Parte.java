package windows.parte;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import windows.Tabla;
import mytools.Arreglos;
import mytools.Iconos;
import mytools.Utilidades;

public class Parte extends JFrame implements ActionListener {

    private JTable[] tablas;
    private JScrollPane[] scrolls;
    
    private final FormularioParte formParte;

    public Parte(Tabla tabla) {
        this.formParte = tabla.getFormParte();
        componentes();     
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                tabla.setVisible(true);
                dispose();
            }
        });
    }

    private void componentes() {
        //OBJETOS AUXILIARES       
        Utilidades utilidad = new Utilidades();
        Iconos iconos = new Iconos();
        //-----PROPIEDADES DEL FRAME-----------------
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (pantalla.getWidth() < 1340 ? pantalla.getWidth() : 1340);
        int w = (int) (pantalla.getHeight() < 600 ? pantalla.getHeight() : 600);
        setSize(x, w);        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Parte de Enfermo");
        setIconImage(iconos.getIconoSanidad().getImage());
        //Fondo del frame
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(1000, 500,
                        getBackground().brighter(), 0, 200,
                        getBackground().darker().darker().darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        Dimension dimension = new Dimension(1170, 1070);
        container.setBackground(utilidad.getColorFondo());      
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        pantalla = null;
        JScrollPane scrollContainer = new JScrollPane(container);
        scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollContainer.getVerticalScrollBar().setUnitIncrement(16);
        //-----------------------------------------------------------------------------------
        //TABLAS DEL PARTE------------------------------------------------------------------
        int y = 60;
        tablas = new JTable[4];
        scrolls = new JScrollPane[4];
        JLabel[] titulo = new JLabel[4];
        String[] tituloLabels = {"PARTE DE ENFERMO", "PARTE DE EXCEPTUADO", "PARTE DE MATERNIDAD", "PERSONAL QUE NO PASO NOVEDAD"};
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            //creacion del table model
            DefaultTableModel model = new DefaultTableModel();
            //propiedades de la tabla
            tablas[i] = new JTable(model);
            tablas[i].setGridColor(Color.black);
            tablas[i].setBackground(utilidad.getColorTabla());
            tablas[i].setFont(utilidad.getFuenteTabla());
            //header
            JTableHeader header = tablas[i].getTableHeader();
            header.setFont(utilidad.getFuenteHeader());
            header.setBackground(utilidad.getColorTabla());
            header.setReorderingAllowed(false);
            ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            //creacion de las columnas
            for (int j = 0; j < Arreglos.getColumnasParteLength(); j++) {
                model.addColumn(Arreglos.getColumnasParte(j));
            }
            Object[] asd = {"Nro", "Grado", "Apellido y Nombre", "Destino", "Diagnostico", "Desde", "Hasta", "Dias", "Expediente", "Observacion", "id"};
            model.addRow(asd);
            //tamaño de las columnas
            for (int j = 0; j < Arreglos.getColumnasParteLength(); j++) {
                tablas[i].getColumnModel().getColumn(j).setMinWidth(Arreglos.getTamañoColumnParte(j));
                tablas[i].getColumnModel().getColumn(j).setMaxWidth(Arreglos.getTamañoColumnParte(j));
                tablas[i].getColumnModel().getColumn(j).setPreferredWidth(Arreglos.getTamañoColumnParte(j));
                //centrado del contenido de las columnas
                if (j != 2 && j != 9) {
                    tablas[i].getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
                }
                //ocultando la columna con el id
                if (j == 10) {
                    tablas[i].getColumnModel().removeColumn(tablas[i].getColumnModel().getColumn(j));
                }
            }
            //Titulo, Scrolls          
            titulo[i] = new JLabel(tituloLabels[i]);
            titulo[i].setFont(utilidad.getFuenteLabelTitulo());
            titulo[i].setForeground(Color.black);
            titulo[i].setBounds(30, y - 40, 400, 40);
            scrolls[i] = new JScrollPane(tablas[i]);
            scrolls[i].setBounds(15, y, 1280, 200);
            y += 250;
            scrolls[i].getViewport().setBackground(utilidad.getColorTabla());
            scrolls[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            container.add(scrolls[i]);
            container.add(titulo[i]);
        }
        //FINALIZACION DE LOS COMPONENTES
        this.getContentPane().add(scrollContainer);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    
    public DefaultTableModel getTableModel(int index) {
        return (DefaultTableModel) tablas[index].getModel();
    }
          
    
    //Main auxiliar para el desarollo del frame
    public static void main(String[] args) {
        Parte parte = new Parte(null);
        parte.setVisible(true);
    }
}
