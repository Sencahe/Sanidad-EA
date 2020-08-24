package dialogs;

import database.Configurations;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;
import main.MainFrame;
import mytools.Icons;
import mytools.Utilities;
import panels.PersonnelPanel;
import panels.SickPanel;
import panels.ReCountPanel;

public class Configurator extends JDialog implements ActionListener, ChangeListener {

    private PersonnelPanel tabla;
    private SickPanel parte;
    private ReCountPanel recuento;
    private MainFrame mainFrame;

    public JCheckBox configColumns, configRow;

    private JButton botonRestaurar, botonGuardar;
    
    private JTextField textLeyenda;
    private String flagLeyenda;

    public Configurator(Frame parent, boolean modal) {
        super(parent, modal);
        mainFrame = (MainFrame) parent;
        componentes();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                restaurar();                
                dispose();
                System.gc();
            }
        });

    }

    public void componentes() {
        // PROPIEDADES DEL FRAME
        Utilities utilidad = mainFrame.getUtility();
        Icons iconos = mainFrame.getIcons();
        // FRAME DEL BUSCADOR
        setSize(390, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Configuraciones");
        setIconImage(iconos.getIconoSanidad().getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(200, 170,
                        getBackground().brighter().brighter(), 0, 200,
                        getBackground().darker().darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        container.setBackground(utilidad.getColorFondo());
        Dimension dimension = new Dimension(390, 270);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        
        //CHECKBOX
        configColumns = new JCheckBox("Habilitar el desplazamiento de columnas (Ctrl+R)");
        configColumns.setBounds(15, 25, 350, 30);
        configColumns.setSelected(true);
        configColumns.addChangeListener(this);
        configColumns.setFocusPainted(false);
        configColumns.setFont(utilidad.getFuenteLabelsFormulario());
        configColumns.setOpaque(false);
        container.add(configColumns);
        configRow = new JCheckBox("Seleccionar las celdas individualmente (Ctrl+E)");
        configRow.setBounds(15, 60, 350, 30);
        configRow.addChangeListener(this);
        configRow.setFocusPainted(false);
        configRow.setFont(utilidad.getFuenteLabelsFormulario());
        configRow.setOpaque(false);
        container.add(configRow);
        //botones
        botonRestaurar = new JButton("<html>Ordenar Tablas</html>");
        botonRestaurar.setBounds(15, 100, 115, 25);
        botonRestaurar.addActionListener(this);
        botonRestaurar.setFocusPainted(false);
        container.add(botonRestaurar);
        JLabel labelRestaurar = new JLabel("(Ctrl+T)");
        labelRestaurar.setBounds(140, 95, 70, 30);
        labelRestaurar.setFont(utilidad.getFuenteLabelsFormulario());
        container.add(labelRestaurar);
        //textfield
        textLeyenda = new JTextField();
        textLeyenda.setBounds(15, 140, 300, 25);
        container.add(textLeyenda);
        botonGuardar = new JButton(iconos.getIconoSave());
        botonGuardar.setBounds(320, 140, 50, 25);
        botonGuardar.addActionListener(this);
        botonGuardar.setFocusPainted(false);
        container.add(botonGuardar);
        //obtener de la base de datos
        valoresGuardados(false);
        //fin        
        this.getContentPane().add(container);
        iconos = null;
        utilidad = null;

    }

    //-------------------------EVENTO BOTONES-----------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        //Ordenamiento de las tablas
        if (e.getSource() == botonRestaurar) {
            tabla.ordenarColumnas();
            parte.restoreColumns();
            recuento.restoreColumns();
        }
        if (e.getSource() == botonGuardar) {
            valoresGuardados(true);
        }
    }

    //-------------------------EVENTO CHECK BOXES-------------------------------
    @Override
    public void stateChanged(ChangeEvent e) {
        //Configuraicon DESPLAZAMIENTO de Columnas
        if (e.getSource() == configColumns) {
            boolean desplazar = configColumns.isSelected();

            JTableHeader headerRecuento = recuento.getTable().getTableHeader();
            headerRecuento.setReorderingAllowed(desplazar);
            for (int i = 0; i < 4; i++) {
                //casualmente tanto tabla como parte tiene 4 tablas
                JTableHeader headerTabla = tabla.getTablas(i).getTableHeader();
                headerTabla.setReorderingAllowed(desplazar);
                JTableHeader headerParte = parte.getTablas(i).getTableHeader();
                headerParte.setReorderingAllowed(desplazar);
            }
        }
        //Configuracion SELECCION de Filas
        if (e.getSource() == configRow) {
            boolean individualmente = configRow.isSelected();

            if (individualmente) {
                recuento.getTable().setCellSelectionEnabled(individualmente);
                for (int i = 0; i < 4; i++) {
                    tabla.getTablas(i).setCellSelectionEnabled(individualmente);
                    parte.getTablas(i).setCellSelectionEnabled(individualmente);
                }
            } else {
                recuento.getTable().setCellSelectionEnabled(individualmente);
                recuento.getTable().setRowSelectionAllowed(!individualmente);
                for (int i = 0; i < 4; i++) {
                    tabla.getTablas(i).setCellSelectionEnabled(individualmente);
                    parte.getTablas(i).setCellSelectionEnabled(individualmente);
                    tabla.getTablas(i).setRowSelectionAllowed(!individualmente);
                    parte.getTablas(i).setRowSelectionAllowed(!individualmente);
                }
            }
        }

    }

    //------------------------------- VALORES ----------------------------------
    private void valoresGuardados(boolean cambios) {
        Configurations configurar = new Configurations(this);
        if(cambios){
           configurar.setValores(); 
        }     
        configurar.getValores();
        
        configurar = null;
    }
    
    public void restaurar(){
        textLeyenda.setText(flagLeyenda);     
        
    }

    //-------------------------GETTERS Y SETTERS--------------------------------
    public void setTabla(PersonnelPanel tabla) {
        this.tabla = tabla;
    }

    public void setParte(SickPanel parte) {
        this.parte = parte;
    }

    public void setRecuento(ReCountPanel recuento) {
        this.recuento = recuento;
    }

    public JTextField getTextLeyenda() {
        return textLeyenda;
    }

    
    public String getFlagLeyenda() {
        return flagLeyenda;
    }

    public void setFlagLeyenda(String flagLeyenda) {
        this.flagLeyenda = flagLeyenda;
    }
    
}
