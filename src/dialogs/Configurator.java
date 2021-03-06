package dialogs;

import database.Configurations;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
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
import mytools.TextPrompt;
import panels.PersonnelPanel;
import panels.SickPanel;
import panels.ReCountPanel;
import mytools.mycomponents.MyJButton;

public class Configurator extends JDialog implements ActionListener, ChangeListener {

    private PersonnelPanel personnelPanel;
    private SickPanel sickPanel;
    private ReCountPanel reCountPanel;
    private Advanced advanced;
    private MainFrame mainFrame;

    public JCheckBox configColumns, configRow;

    private JButton buttonRestore, buttonSave, buttonSavePass, buttonAdvanced;
    
    private JTextField textLeyend, textUnity;
    private JPasswordField textCurrentPass, textNewPass1, textNewPass2;
    private String flagLeyend, flagUnity;
    private String flagPassword;

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
        setSize(390, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Configuraciones");
        setIconImage(icons.getIconHealthService().getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel container = new JPanel();
        container.setBackground(utility.getColorBackground().brighter());
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
        JLabel labelUnity = new JLabel("Modificar Unidad");
        labelUnity.setBounds(15,130,300,20);
        labelUnity.setForeground(Color.black);
        labelUnity.setFont(utility.getFontLabelFormulary());
        container.add(labelUnity);
        textUnity = new JTextField();
        textUnity.setBounds(15, 150, 300, 25);
        container.add(textUnity);
        
        JLabel labelLeyend = new JLabel("Modificar Leyenda");
        labelLeyend.setBounds(15,180,300,20);
        labelLeyend.setForeground(Color.black);
        labelLeyend.setFont(utility.getFontLabelFormulary());
        container.add(labelLeyend);
        textLeyend = new JTextField();
        textLeyend.setBounds(15, 200, 300, 25);
        container.add(textLeyend);
        buttonSave = new MyJButton();
        buttonSave.setToolTipText("Guardar datos");
        buttonSave.setOpaque(false);
        buttonSave.setIcon( icons.getIconoSave());
        buttonSave.setBounds(330, 170, 32, 32);
        buttonSave.addActionListener(this);
        buttonSave.setFocusPainted(false);
        container.add(buttonSave);
        
        JLabel separator = new JLabel("<html>___________________________________</html>");
        separator.setBounds(15,220,200,20);
        separator.setFont(utility.getFontLabelBig());
        separator.setForeground(Color.black);
        container.add(separator);
        
        JLabel labelPass = new JLabel("Cambiar contraseña");
        labelPass.setBounds(15,250,300,20);
        labelPass.setForeground(Color.black);
        labelPass.setFont(utility.getFontLabelFormulary());
        container.add(labelPass);
        textCurrentPass = new JPasswordField();
        textCurrentPass.setBounds(15, 270, 300, 25);
        container.add(textCurrentPass);
        new TextPrompt("Contraseña Actual", textCurrentPass);
        textNewPass1 = new JPasswordField();
        textNewPass1.setBounds(15, 305, 300, 25);
        container.add(textNewPass1);
        new TextPrompt("Nueva Contraseña", textNewPass1);
        textNewPass2 = new JPasswordField();
        textNewPass2.setBounds(15, 335, 300, 25);
        container.add(textNewPass2);
        new TextPrompt("Repita Nueva Contraseña", textNewPass2);
        
        buttonSavePass =  new MyJButton();
        buttonSavePass.setToolTipText("Guardar nueva contraseña");
        buttonSavePass.setOpaque(false);
        buttonSavePass.setIcon( icons.getIconoSave());
        buttonSavePass.setBounds(330, 335, 32, 32);
        buttonSavePass.addActionListener(this);
        buttonSavePass.setFocusPainted(false);
        container.add(buttonSavePass);
             
        buttonAdvanced = new JButton("<html>Avanzado...</html>");
        buttonAdvanced.setBounds(15, 370, 80, 25);
        buttonAdvanced.addActionListener(this);
        buttonAdvanced.setFocusPainted(false);
        container.add(buttonAdvanced);
        
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
        
        if(e.getSource() == buttonAdvanced){
            advanced.setVisible(true);
        }
        
        if(e.getSource() == buttonSavePass){
            String inputCurrentPass = String.valueOf(textCurrentPass.getPassword());
            boolean currentPass = inputCurrentPass.equals(flagPassword);
            
            String newPass1 = String.valueOf(textNewPass1.getPassword());
            String newPass2 = String.valueOf(textNewPass2.getPassword());
            boolean equalsNewPass = newPass1.equals(newPass2);
            
            if(currentPass && equalsNewPass){
                Configurations config = new Configurations();
                config.setPassword(this);
                config = null;
                textCurrentPass.setText("");
                textNewPass1.setText("");
                textNewPass2.setText("");
            } else {
                if(!currentPass){
                    JOptionPane.showMessageDialog(null,"La contraseña actual ingresada es incorrecta.");
                } else if(!equalsNewPass){
                    JOptionPane.showMessageDialog(null, "La nueva contraseña ingresada debe ser igual en ambos campos.");
                }
            }
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
        Configurations config = new Configurations();
        if(cambios){
           config.setValues(this); 
        }     
        config.getSavedValues(this);
        
        config = null;
    }
    
    public void restore(){
        textLeyend.setText(flagLeyend);     
        textUnity.setText(flagUnity);
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

    public void setFlagPassword(String flagPassword) {
        this.flagPassword = flagPassword;
    }

    public String getFlagPassword() {
        return flagPassword;
    }

    public JPasswordField getTextNewPass1() {
        return textNewPass1;
    }

    public JTextField getTextUnity() {
        return textUnity;
    }

    public void setFlagUnity(String flagUnity) {
        this.flagUnity = flagUnity;
    }

    public void setAdvanced(Advanced advanced) {
        this.advanced = advanced;
    }
    
    
}
