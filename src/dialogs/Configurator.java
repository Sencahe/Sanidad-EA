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

    private PersonnelPanel personnelPanel;
    private SickPanel sickPanel;
    private ReCountPanel reCountPanel;
    private MainFrame mainFrame;

    public JCheckBox configColumns, configRow;

    private JButton buttonRestore, buttonSave;
    
    private JTextField textLeyend;
    private String flagLeyend;

    public Configurator(Frame parent, boolean modal) {
        super(parent, modal);
        mainFrame = (MainFrame) parent;
        componentes();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                restore();                
                dispose();
                System.gc();
            }
        });

    }

    public void componentes() {
        // PROPIEDADES DEL FRAME
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        // FRAME DEL BUSCADOR
        setSize(390, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Configuraciones");
        setIconImage(icons.getIconHealthService().getImage());
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
        container.setBackground(utility.getColorBackground());
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
        configColumns.setFont(utility.getFontLabelFormulary());
        configColumns.setOpaque(false);
        container.add(configColumns);
        configRow = new JCheckBox("Seleccionar las celdas individualmente (Ctrl+E)");
        configRow.setBounds(15, 60, 350, 30);
        configRow.addChangeListener(this);
        configRow.setFocusPainted(false);
        configRow.setFont(utility.getFontLabelFormulary());
        configRow.setOpaque(false);
        container.add(configRow);
        //botones
        buttonRestore = new JButton("<html>Ordenar Tablas</html>");
        buttonRestore.setBounds(15, 100, 115, 25);
        buttonRestore.addActionListener(this);
        buttonRestore.setFocusPainted(false);
        container.add(buttonRestore);
        JLabel labelRestaurar = new JLabel("(Ctrl+T)");
        labelRestaurar.setBounds(140, 95, 70, 30);
        labelRestaurar.setFont(utility.getFontLabelFormulary());
        container.add(labelRestaurar);
        //textfield
        textLeyend = new JTextField();
        textLeyend.setBounds(15, 140, 300, 25);
        container.add(textLeyend);
        buttonSave = new JButton(icons.getIconoSave());
        buttonSave.setBounds(320, 140, 50, 25);
        buttonSave.addActionListener(this);
        buttonSave.setFocusPainted(false);
        container.add(buttonSave);
        //obtener de la base de datos
        savedValues(false);
        //fin        
        this.getContentPane().add(container);
        icons = null;
        utility = null;

    }

    //-------------------------EVENTO BOTONES-----------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        //Ordenamiento de las tablas
        if (e.getSource() == buttonRestore) {
            personnelPanel.ordenarColumnas();
            sickPanel.restoreColumns();
            reCountPanel.restoreColumns();
        }
        if (e.getSource() == buttonSave) {
            savedValues(true);
        }
    }

    //-------------------------EVENTO CHECK BOXES-------------------------------
    @Override
    public void stateChanged(ChangeEvent e) {
        //Configuraicon DESPLAZAMIENTO de Columnas
        if (e.getSource() == configColumns) {
            boolean desplazar = configColumns.isSelected();

            JTableHeader headerRecuento = reCountPanel.getTable().getTableHeader();
            headerRecuento.setReorderingAllowed(desplazar);
            for (int i = 0; i < 4; i++) {
                //casualmente tanto tabla como parte tiene 4 tablas
                JTableHeader headerTabla = personnelPanel.getTables(i).getTableHeader();
                headerTabla.setReorderingAllowed(desplazar);
                JTableHeader headerParte = sickPanel.getTablas(i).getTableHeader();
                headerParte.setReorderingAllowed(desplazar);
            }
        }
        //Configuracion SELECCION de Filas
        if (e.getSource() == configRow) {
            boolean individualmente = configRow.isSelected();

            if (individualmente) {
                reCountPanel.getTable().setCellSelectionEnabled(individualmente);
                for (int i = 0; i < 4; i++) {
                    personnelPanel.getTables(i).setCellSelectionEnabled(individualmente);
                    sickPanel.getTablas(i).setCellSelectionEnabled(individualmente);
                }
            } else {
                reCountPanel.getTable().setCellSelectionEnabled(individualmente);
                reCountPanel.getTable().setRowSelectionAllowed(!individualmente);
                for (int i = 0; i < 4; i++) {
                    personnelPanel.getTables(i).setCellSelectionEnabled(individualmente);
                    sickPanel.getTablas(i).setCellSelectionEnabled(individualmente);
                    personnelPanel.getTables(i).setRowSelectionAllowed(!individualmente);
                    sickPanel.getTablas(i).setRowSelectionAllowed(!individualmente);
                }
            }
        }

    }

    //------------------------------- VALORES ----------------------------------
    private void savedValues(boolean cambios) {
        Configurations config = new Configurations(this);
        if(cambios){
           config.setValues(); 
        }     
        config.getValues();
        
        config = null;
    }
    
    public void restore(){
        textLeyend.setText(flagLeyend);     
        
    }

    //-------------------------GETTERS Y SETTERS--------------------------------
    public void setPersonnelPanel(PersonnelPanel personnelPanel) {
        this.personnelPanel = personnelPanel;
    }

    public void setSickPanel(SickPanel sickPanel) {
        this.sickPanel = sickPanel;
    }

    public void setReCountPanel(ReCountPanel reCountPanel) {
        this.reCountPanel = reCountPanel;
    }

    public JTextField getTextLeyend() {
        return textLeyend;
    }

    
    public String getFlagLeyend() {
        return flagLeyend;
    }

    public void setFlagLeyend(String flagLeyend) {
        this.flagLeyend = flagLeyend;
    }
    
}
