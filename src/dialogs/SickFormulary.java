package dialogs;

import personnel.Personnel;
import database.Transmitter;
import database.Receiver;
import mytools.MyDates;
import mytools.Icons;
import mytools.Utilities;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import database.Deleter;
import java.util.Date;
import panels.SickPanel;
import mytools.mycomponents.MyJButton;

public class SickFormulary extends JDialog implements ActionListener {

    private static final String ENFERMO = "Lic. por Enfermedad";
    private static final String EXCEPTUADO = "Tareas Adm";
    private static final String MATERNIDAD = "Lic. por Maternidad";

    private JLabel labelPersonnelData;
    private JLabel labelDiag, labelObs, labelSince, labelUntil, labelCIE,
            labelSickType, labelNorasSiras, labelSuggestion;

    private JComboBox comboSickType, comboNorasSiras;
    private JTextField textDiag, textObs, textCIE;
    private JDateChooser dateSince, dateUntil;
    private JButton buttonAdd, buttonUpdate, buttonHeal, buttonDelete;

    private int idSick;
    private boolean beingModified;
    private boolean beingModifiedSickType;
    private int flagSickType;
    private Date flagSince;
    private Date flagUntil;

    private Personnel personnel;

    private SickPanel sickPanel;
    private PersonnelFormulary personnelFormulary;
    private MainFrame mainFrame;

    public SickFormulary(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;

        components();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                empty();
            }
        });
    }

    private void components() {
        //PROPIEDADES DEL FRAME
        //------------------------------
        Utilities utility = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        //PROPIEDADES DEL FRAME        
        setSize(450, 345);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Agregar Nuevo Parte");
        setIconImage(icons.getIconHealthService().getImage());
        //Fondo del frame
        JPanel container = new JPanel();
        container.setBackground(utility.getColorBackground().darker());
        Dimension dimension = new Dimension(450, 345);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;

        //--------------------------------------
        //COMPONENTES PRINCIPALES
        //Labels con informacion
        labelPersonnelData = new JLabel();
        labelPersonnelData.setBounds(10, 0, 355, 100);
        labelPersonnelData.setFont(utility.getFontLabelBig());
        labelPersonnelData.setForeground(Color.white);
        container.add(labelPersonnelData);

        //combobox
        JLabel labelComboSickType = new JLabel("Tipo de Parte");
        labelComboSickType.setBounds(15, 100, 160, 20);
        labelComboSickType.setForeground(Color.black);
        labelComboSickType.setFont(utility.getFontLabelFormulary());
        add(labelComboSickType);
        comboSickType = new JComboBox();
        comboSickType.addItem("Enfermo");
        comboSickType.addItem("Exceptuado");
        comboSickType.addItem("Maternidad");
        comboSickType.setBounds(15, 120, 160, 20);
        comboSickType.addActionListener(this);
        container.add(comboSickType);
        labelSickType = new JLabel("<html>Al modificar el tipo de parte se enviara al recuento la "
                + "informacion actualmente guardada y se creara un nuevo parte. Revise esa informacion antes de proceder y "
                + "recuerde modificar las fechas 'Desde' y 'Hasta' para el nuevo parte.<html>");
        labelSickType.setBounds(180, 65, 250, 80);
        labelSickType.setFont(utility.getFuenteLabelInfo());
        labelSickType.setForeground(Color.orange);
        labelSickType.setVisible(false);
        container.add(labelSickType);

        comboNorasSiras = new JComboBox();
        comboNorasSiras.addItem("NORAS");
        comboNorasSiras.addItem("SIRAS");
        comboNorasSiras.setBounds(240, 255, 100, 20);
        container.add(comboNorasSiras);
        labelNorasSiras = new JLabel("NORAS / SIRAS");
        labelNorasSiras.setBounds(240, 230, 160, 20);
        labelNorasSiras.setForeground(Color.black);
        labelNorasSiras.setFont(utility.getFontLabelFormulary());
        container.add(labelNorasSiras);
        //datechooser
        labelSince = new JLabel("Desde *");
        labelSince.setBounds(15, 145, 200, 20);
        labelSince.setFont(utility.getFontLabelFormulary());
        labelSince.setForeground(Color.black);
        container.add(labelSince);
        dateSince = new JDateChooser();
        dateSince.setBounds(15, 165, 100, 20);
        dateSince.setForeground(Color.black);
        dateSince.setFont(utility.getFontTextFields());
        dateSince.setDateFormatString(MyDates.USER_DATE_FORMAT);
        container.add(dateSince);
        labelUntil = new JLabel("Hasta *");
        labelUntil.setBounds(130, 145, 200, 20);
        labelUntil.setFont(utility.getFontLabelFormulary());
        labelUntil.setForeground(Color.black);
        container.add(labelUntil);
        dateUntil = new JDateChooser();
        dateUntil.setBounds(130, 165, 100, 20);
        dateUntil.setForeground(Color.black);
        dateUntil.setFont(utility.getFontTextFields());
        dateUntil.setDateFormatString(MyDates.USER_DATE_FORMAT);
        container.add(dateUntil);
        //textfield
        labelObs = new JLabel("Observaciones *");
        labelObs.setBounds(240, 145, 150, 20);
        labelObs.setFont(utility.getFontLabelFormulary());
        labelObs.setForeground(Color.black);
        container.add(labelObs);
        textObs = new JTextField();
        textObs.setBounds(240, 165, 170, 20);
        textObs.setFont(utility.getFontTextFields());
        textObs.setText(ENFERMO);
        container.add(textObs);
        labelDiag = new JLabel("Diagnostico *");
        labelDiag.setBounds(15, 185, 200, 20);
        labelDiag.setFont(utility.getFontLabelFormulary());
        labelDiag.setForeground(Color.black);
        container.add(labelDiag);
        textDiag = new JTextField();
        textDiag.setBounds(15, 205, 215, 20);
        textDiag.setFont(utility.getFontTextFields());
        container.add(textDiag);

        labelCIE = new JLabel("CIE");
        labelCIE.setBounds(240, 185, 100, 20);
        labelCIE.setFont(utility.getFontLabelFormulary());
        labelCIE.setForeground(Color.black);
        container.add(labelCIE);
        textCIE = new JTextField();
        textCIE.setBounds(240, 205, 170, 20);
        textCIE.setFont(utility.getFontTextFields());
        container.add(textCIE);

        labelSuggestion = new JLabel("Diagnosticos sugeridos: 'Embarazo' |"
                + " (0-3 meses) 'Lactancia' |"
                + " (3-12 meses) 'Maternidad'");
        labelSuggestion.setBounds(10, 275, 435, 30);
        labelSuggestion.setForeground(Color.black);
        labelSuggestion.setVisible(false);
        add(labelSuggestion);

        //BOTONES---------------------------------------------------------------
        buttonAdd = new MyJButton();
        buttonAdd.setToolTipText("Agregar Parte");
        buttonAdd.setOpaque(false);
        buttonAdd.setBounds(40, 245, 32, 32);
        buttonAdd.setIcon(icons.getIconoSave());
        buttonAdd.addActionListener(this);
        container.add(buttonAdd);
        buttonUpdate = new MyJButton();
        buttonUpdate.setToolTipText("Guardar Cambios");
        buttonUpdate.setOpaque(false);
        buttonUpdate.setBounds(40, 240, 32, 32);
        buttonUpdate.setIcon(icons.getIconoSave());
        buttonUpdate.addActionListener(this);
        buttonUpdate.setVisible(false);
        container.add(buttonUpdate);
        buttonHeal = new MyJButton();
        buttonHeal.setToolTipText("Dar de alta");
        buttonHeal.setOpaque(false);
        buttonHeal.setBounds(110, 240, 32, 32);
        buttonHeal.setIcon(icons.getIconHealed());
        buttonHeal.addActionListener(this);
        buttonHeal.setVisible(false);
        container.add(buttonHeal);

        buttonDelete = new MyJButton();
        buttonDelete.setToolTipText("Eliminar parte");
        buttonDelete.setOpaque(false);
        buttonDelete.setBounds(165, 240, 32, 32);
        buttonDelete.setIcon(icons.getIconDelete2());
        buttonDelete.addActionListener(this);
        buttonDelete.setVisible(false);
        container.add(buttonDelete);

        //--------------------------------------
        this.getContentPane().add(container);
        utility = null;
        icons = null;
    }

    //--------------------------------------------------------------------------
    //EVENTO BOTOENS------------------------------------------------------------
    @Override
    //----------BOTON AGREGAR-----------------------
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonAdd) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea agregar un nuevo Parte?",
                    "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                if (validation()) {
                    Transmitter transmitter = new Transmitter(personnel.getId(), 0);
                    transmitter.sendInformation(this);
                    transmitter.update(sickPanel);
                    sickPanel.updateWindow();
                    personnelFormulary.setSick(true);
                    empty();
                    transmitter = null;
                }
            }
        }
        //---------------BOTON MODIFICARR---------------------------
        if (e.getSource() == buttonUpdate) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea modificar el Parte?",
                    "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                if (validation()) {
                    Transmitter transmitter = new Transmitter(personnel.getId(), this.idSick);
                    if (flagSickType == comboSickType.getSelectedIndex()) {
                        transmitter.sendInformation(this);
                    } else {
                        transmitter.sendReCountInfo(this, false);
                    }
                    transmitter.update(sickPanel);
                    sickPanel.updateWindow();
                    dispose();
                    empty();
                    transmitter = null;
                }
            }
        }
        //-------------------BOTON ALTA-----------------------
        if (e.getSource() == buttonHeal) {
            int confirmar = JOptionPane.showConfirmDialog(null,
                    "<html><center>¿Esta seguro que desea dar de Alta a "
                    + personnel.getCompleteName() + "?<br>"
                    + "Recuerde que la fecha de 'Hasta' debe coincidir con la del Alta Medica.</center></html>",
                    "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                if (validation()) {
                    Transmitter emisor = new Transmitter(personnel.getId(), this.idSick);
                    emisor.sendReCountInfo(this, true);
                    emisor.update(sickPanel);
                    sickPanel.updateWindow();;
                    dispose();
                    emisor = null;
                    empty();
                }
            }
        }
        //------------------------BOTON BORRAR------------------------------
        if (e.getSource() == buttonDelete) {
            int option = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar el parte? "
                    + "Si lo hace, este parte no se enviara al recuento", "Confirmar", 0);
            if (option == JOptionPane.YES_OPTION) {
                Deleter deleter = new Deleter(idSick, personnel.getId());
                deleter.delete(SickPanel.TABLE_NAME);
                deleter.update(sickPanel);
                deleter = null;
                sickPanel.updateWindow();
                dispose();
                empty();
            }
        }

        //---------------COMBO TIPO PARTE---------------------------
        if (e.getSource() == comboSickType) {
            //si se cambia el tipo de parte se borran las fechas
            if (beingModified && flagSickType != comboSickType.getSelectedIndex()) {
                beingModifiedSickType = true;
                labelSickType.setVisible(true);
                buttonHeal.setVisible(false);
                dateSince.setDate(null);
                dateUntil.setDate(null);

                //sino, se colocan las actuales    
            } else if (beingModified) {
                beingModifiedSickType = false;
                labelSickType.setVisible(false);
                buttonHeal.setVisible(true);
                dateSince.setDate(flagSince);
                dateUntil.setDate(flagUntil);

            }
            //----
            //se cambia el campo observacion si se cambia el tipo de parte
            if (comboSickType.getSelectedIndex() == 0) {
                textObs.setText(ENFERMO);
                labelSuggestion.setVisible(false);
            }
            if (comboSickType.getSelectedIndex() == 1) {
                textObs.setText(EXCEPTUADO);
                labelSuggestion.setVisible(false);
            }
            if (comboSickType.getSelectedIndex() == 2) {
                textObs.setText(MATERNIDAD);
                labelSuggestion.setVisible(true);
            }

        }
    }

    //-------------------------METODO VACIAR------------------------------------
    private void empty() {
        setTitle("Agregar Nuevo Parte");
        buttonAdd.setVisible(true);
        buttonHeal.setVisible(false);
        buttonUpdate.setVisible(false);
        buttonDelete.setVisible(false);

        beingModified = false;
        beingModifiedSickType = false;

        personnel = null;

        labelPersonnelData.setText("");

        comboSickType.setSelectedIndex(0);
        textDiag.setText("");
        textObs.setText(ENFERMO);
        textCIE.setText("");
        ((JTextField) dateSince.getDateEditor().getUiComponent()).setText("");
        ((JTextField) dateUntil.getDateEditor().getUiComponent()).setText("");
        labelSickType.setVisible(false);

        dispose();
        System.gc();
    }

    //------------------------METODOS PARA NUEVO PARTE--------------------------
    public void newSick(PersonnelFormulary formulario) {
        this.personnelFormulary = formulario;
        this.personnel = formulario.getPersonnel();
        labelPersonnelData.setText(personnel.toString());

        setVisible(true);
    }

    //------------METODOS PARA MODIFICAR PARTES ACTUALES-----------------------
    public void obtainData(int idParte) {
        this.idSick = idParte;

        setTitle("Modificar Parte");
        buttonAdd.setVisible(false);
        buttonHeal.setVisible(true);
        buttonUpdate.setVisible(true);
        buttonDelete.setVisible(true);

        Receiver receiver = new Receiver(this.idSick);
        receiver.obtainInformation(this);

        flagSickType = comboSickType.getSelectedIndex();
        beingModified = true;
        beingModifiedSickType = false;

        labelPersonnelData.setText(personnel.toString());

        setVisible(true);
        receiver = null;
    }

    //----------------------METODO VALIDAR--------------------------------------
    private boolean validation() {
        labelDiag.setForeground(Color.black);
        labelObs.setForeground(Color.black);
        labelSince.setForeground(Color.black);
        labelUntil.setForeground(Color.black);

        //validando campos vacios
        boolean fieldDiag = textDiag.getText().equals("");
        boolean fieldObs = textObs.getText().equals("");
        boolean fieldSince = ((JTextField) dateSince.getDateEditor().getUiComponent()).getText().equals("");
        boolean fieldUntil = ((JTextField) dateUntil.getDateEditor().getUiComponent()).getText().equals("");
        if (fieldDiag || fieldObs || fieldSince || fieldUntil) {
            labelDiag.setForeground(fieldDiag ? Color.red : Color.black);
            labelObs.setForeground(fieldObs ? Color.red : Color.black);
            labelSince.setForeground(fieldSince ? Color.red : Color.black);
            labelUntil.setForeground(fieldUntil ? Color.red : Color.black);
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos obligatorios");
            return false;
        }
        //validando fechas mal escritas

        if (dateSince.getDate() == null || dateUntil.getDate() == null) {
            labelSince.setForeground(fieldDiag ? Color.red : Color.black);
            labelUntil.setForeground(fieldDiag ? Color.red : Color.black);
            String mensaje = "<html><center>Fecha ingresada invalida, "
                    + "ejemplo de fecha valida: 01/01/2020 y/o 1/1/2020</center></html>";
            JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
            return false;
        }
        //validando la coherencia de las fechas Desde y Hasta ingresadas por el usuario
        MyDates myDates = new MyDates(MyDates.USER_DATE_FORMAT);
        if (beingModifiedSickType) {
            if (!myDates.validateBetweenTwoDates(dateSince.getDate(), dateUntil.getDate(), flagSince)) {
                return false;
            }
        } else {
            if (!myDates.validateBetweenTwoDates(dateSince.getDate(), dateUntil.getDate())) {
                return false;
            }
        }
        //validando que no se le agrege parte de maternidad a personal M
        if (personnel.getGenre() == 'M') {
            if (comboSickType.getSelectedIndex() == 2) {
                JOptionPane.showMessageDialog(null, "No puede agregar parte de \"Maternidad\" a un Personal Masculino.");
                return false;
            }
        }

        myDates = null;
        return true;
    }

    //----------------------SETTER Y GETTERS------------------------------------
    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public void setSickPanel(SickPanel sickPanel) {
        this.sickPanel = sickPanel;
    }

    public void setPersonnelFormulary(PersonnelFormulary personnelFormulary) {
        this.personnelFormulary = personnelFormulary;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public JComboBox getComboSickType() {
        return comboSickType;
    }

    public JComboBox getNorasSiras() {
        return comboNorasSiras;
    }

    public JTextField getTextDiag() {
        return textDiag;
    }

    public JTextField getTextObs() {
        return textObs;
    }

    public JTextField getTextCIE() {
        return textCIE;
    }

    public JDateChooser getDateSince() {
        return dateSince;
    }

    public Date getFlagDesde() {
        return flagSince;
    }

    public void setFlagSince(Date flagDesde) {
        this.flagSince = flagDesde;
    }

    public Date getFlagUntil() {
        return flagUntil;
    }

    public void setFlagUntil(Date flagUntil) {
        this.flagUntil = flagUntil;
    }

    public JDateChooser getDateUntil() {
        return dateUntil;
    }

    public void setFlagSickType(int flagSickType) {
        this.flagSickType = flagSickType;
    }

}
