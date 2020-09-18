package dialogs;

import personnel.Personnel;
import database.Transmitter;
import database.Receiver;
import database.Deleter;
import mytools.MyArrays;
import mytools.Icons;
import mytools.MyDates;
import mytools.Utilities;
import mytools.TextPrompt;
import mytools.JTextFieldLimit;
import main.MainFrame;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.border.Border;
import panels.PersonnelPanel;

public class PersonnelFormulary extends JDialog implements ActionListener {

    private JTextField[] textField;
    private JComboBox[] comboBox;
    private JDateChooser[] dateChooser;
    private JCheckBox[] checkBox;
    private JRadioButton M;
    private JRadioButton F;
    private ButtonGroup bg;

    private JLabel[] labels;

    private JButton buttonCalcIMC, buttonAdd, buttonDelete, buttonModify, buttonSick;
    private JLabel labelSick;

    private int id;
    private int pointer; //variable que sirve de referencia para la fila seleccionada al abrir el frame   
    private boolean sick;

    private PersonnelPanel personnelPanel;
    private SickFormulary sickFormulary;
    private Personnel personnel;
    private MainFrame mainFrame;

    public PersonnelFormulary(Frame parent, boolean modal) {
        super(parent, modal);
        this.sick = false;
        this.mainFrame = (MainFrame) parent;
        components();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                empty();
                dispose();
                System.gc();
            }
        });
    }

    private void components() {
        //---------------------------------------
        Utilities utilities = mainFrame.getUtility();
        Icons icons = mainFrame.getIcons();
        //PROPIEDADES DEL FRAME-------------------------------------------------
        setSize(500, 520);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Agregar Personal");
        setIconImage(icons.getIconHealthService().getImage());
        //fondo del frame
        JPanel container = new JPanel() ;
        container.setBackground(utilities.getColorBackground().darker());
        Dimension dimension = new Dimension(480, 500);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        //BOTONES PRINCIPALES         
        buttonAdd = utilities.customButton();
        buttonAdd.setToolTipText("Agregar a la base de datos");
        buttonAdd.setOpaque(false);
        buttonAdd.setIcon( icons.getIconoSave());
        buttonAdd.setBounds(430, 360, 32, 32);
        buttonAdd.addActionListener(this);
        container.add(buttonAdd);
        buttonModify = utilities.customButton();
        buttonModify.setToolTipText("Guardar cambios");
        buttonModify.setOpaque(false);
        buttonModify.setIcon( icons.getIconoSave());
        buttonModify.setBounds(430, 360, 32, 32);
        buttonModify.addActionListener(this);
        buttonModify.setVisible(false);
        container.add(buttonModify);
        buttonDelete = utilities.customButton();
        buttonDelete.setToolTipText("Eliminar de la base de datos");
        buttonDelete.setOpaque(false);
        buttonDelete.setIcon( icons.getIconDelete2());
        buttonDelete.setBounds(375, 360, 32, 32);
        buttonDelete.addActionListener(this);
        buttonDelete.setVisible(false);
        container.add(buttonDelete);
        buttonCalcIMC = utilities.customButton();
        buttonCalcIMC.setToolTipText("Calcular IMC");
        buttonCalcIMC.setOpaque(false);
        buttonCalcIMC.setIcon( icons.getIconCalculator());
        buttonCalcIMC.setBounds(170, 298, 32, 32);
        buttonCalcIMC.addActionListener(this);
        container.add(buttonCalcIMC);
        labelSick = new JLabel("<html>Parte de Sanidad<html>");
        labelSick.setBounds(375, 405, 150, 30);
        labelSick.setFont(utilities.getFontLabelFormulary());
        labelSick.setForeground(Color.black);
        labelSick.setVisible(false);
        container.add(labelSick);
        buttonSick =utilities.customButton();
        buttonSick.setToolTipText("Agregar este personal al parte");
        buttonSick.setOpaque(false);
        buttonSick.setIcon( icons.getIconPlus());
        buttonSick.setBounds(405, 435,32, 32);
        buttonSick.addActionListener(this);
        buttonSick.setVisible(false);
        container.add(buttonSick);
        // DECLARACION DE COMPONENTES DEL FRAME MAS ALGUNAS PROPIEDADES---------
        textField = new JTextField[MyArrays.getTextFieldLength()];
        comboBox = new JComboBox[MyArrays.getComboBoxLength()];
        dateChooser = new JDateChooser[MyArrays.getDateChooserLength()];
        checkBox = new JCheckBox[MyArrays.getCheckBoxLength()];
        labels = new JLabel[textField.length + comboBox.length + dateChooser.length + 1];
        //propiedades text field 
        for (int i = 0; i < textField.length; i++) {
            textField[i] = new JTextField();
            textField[i].setFont(utilities.getFontTextFields());
            if (i == 0 || i == 1 || i == 3) {
                new TextPrompt("Campo obligatorio", textField[i]);
            }
            if (i >= 4 && i <= 6) {
                textField[i].addKeyListener(utilities.bloquearLetras);
                textField[i].setDocument(new JTextFieldLimit(6));
            }
            container.add(textField[i]);
        }
        //propiedades combo box
        for (int i = 0; i < comboBox.length; i++) {
            comboBox[i] = new JComboBox();
            container.add(comboBox[i]);
        }
        //propiedades dateChooser
        for (int i = 0; i < dateChooser.length; i++) {
            dateChooser[i] = new JDateChooser();
            dateChooser[i].setForeground(Color.black);
            dateChooser[i].setFont(utilities.getFontTextFields());
            dateChooser[i].setDateFormatString(MyDates.USER_DATE_FORMAT);
            container.add(dateChooser[i]);
        }
        //propiedades labels
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setForeground(Color.black);
            labels[i].setFont(utilities.getFontLabelFormulary());
            container.add(labels[i]);
        }
        //RadioButtons
        bg = new ButtonGroup();
        M = new JRadioButton("M");
        F = new JRadioButton("F");
        //UBICACION DE LOS COMPONENTES Y PROPIEDADES PARTICULARES DE CADA UNO---
        //RadioButtons
        labels[17].setText("Sexo");
        labels[17].setBounds(285, 10, 30, 20);
        M.setBounds(320, 10, 40, 20);
        M.setOpaque(false);
        M.setFocusPainted(false);
        M.setForeground(Color.black);
        M.setFont(utilities.getFontLabelFormulary());
        M.setSelected(true);
        bg.add(M);
        container.add(M);
        F.setBounds(360, 10, 40, 20);
        F.setOpaque(false);
        F.setFocusPainted(false);
        F.setFont(utilities.getFontLabelFormulary());
        bg.add(F);
        container.add(F);
        //Categoria COMBO 0
        labels[0].setBounds(15, 10, 60, 20);
        labels[0].setText("Categoria");
        comboBox[0].setBounds(80, 10, 110, 22);
        comboBox[0].addActionListener(this);
        for (int i = 0; i < MyArrays.getCategoriesLength(); i++) {
            comboBox[0].addItem(MyArrays.getCategories(i));
        }
        //Grado COMBO 1
        labels[1].setBounds(15, 45, 60, 20);
        labels[1].setText("Grado");
        comboBox[1].setBounds(15, 70, 80, 22);
        //apellido TEXTFIELD 0 
        labels[2].setBounds(105, 45, 65, 20);
        labels[2].setText("Apellido *");
        textField[0].setBounds(105, 70, 175, 22);
        //nombre TEXTFIELD 1
        labels[3].setBounds(285, 45, 65, 20);
        labels[3].setText("Nombre *");
        textField[1].setBounds(285, 70, 175, 22);
        //arma/servicio TEXTFIELD 2
        labels[4].setBounds(15, 95, 100, 20);
        labels[4].setText("Arma / Servicio");
        textField[2].setBounds(15, 120, 95, 22);
        textField[2].setHorizontalAlignment((int) CENTER_ALIGNMENT);
        //Destino COMBO 2
        labels[5].setBounds(130, 95, 60, 20);
        labels[5].setText("Destino");
        comboBox[2].setBounds(130, 120, 90, 22);
        for (int i = 0; i < MyArrays.getSubUnities().length; i++) {
            comboBox[2].addItem(MyArrays.getSubUnities()[i]);
        }
        //DNI TEXTFIELD 3
        labels[6].setBounds(240, 95, 100, 20);
        labels[6].setText("DNI *");
        textField[3].setBounds(240, 120, 80, 22);
        textField[3].setDocument(new JTextFieldLimit(9));
        textField[3].addKeyListener(utilities.soloNumeros);
        new TextPrompt("Obligatorio", textField[3]);
        //Nacimiento DATE CHOOSER 0  
        labels[7].setBounds(335, 95, 150, 20);
        labels[7].setText("Fecha de Nacimiento");
        dateChooser[0].setBounds(335, 120, 100, 22);
         //DM TEXTFIELD10
        labels[18].setBounds(15, 145, 120, 20);
        labels[18].setText("D.M.");
        textField[10].setBounds(15, 170, 140, 22);
        //-------------------------
        //Ultimo Anexo27 DATE CHOOSER 1
        labels[8].setBounds(15, 230, 200, 20);
        labels[8].setText("Fecha Ult. Anexo27");
        dateChooser[1].setBounds(15, 255, 110, 22);
        //Aptitud COMBO 3
        labels[9].setBounds(155, 230, 70, 20);
        labels[9].setText("Aptitud");
        comboBox[3].setBounds(145, 255, 80, 22);
        for (int i = 0; i < MyArrays.getAptitudeLength(); i++) {
            comboBox[3].addItem(MyArrays.getAptitude(i));
        }
        //Peso TEXT 4
        labels[10].setBounds(15, 280, 70, 20);
        labels[10].setText("Peso (Kgs)");
        textField[4].setBounds(20, 305, 50, 22);
        //Altura TEXT 5
        labels[11].setBounds(90, 280, 80, 20);
        labels[11].setText("Altura (Mts)");
        textField[5].setBounds(100, 305, 50, 22);
        //IMC TEXT 6
        labels[12].setBounds(230, 280, 80, 20);
        labels[12].setText("IMC");
        textField[6].setBounds(220, 305, 50, 22);
        //PPS COMBO 4
        labels[13].setBounds(290, 280, 220, 20);
        labels[13].setText("Programa Peso Saludable");
        comboBox[4].setBounds(290, 305, 130, 22);
        for (int i = 0; i < MyArrays.getPPSLength(); i++) {
            comboBox[4].addItem(MyArrays.getPPS(i));
        }
        //Observaciones TEXTFIELD 7
        labels[14].setBounds(15, 345, 90, 20);
        labels[14].setText("Observaciones");
        textField[7].setBounds(15, 370, 265, 22);
        //Legajo TEXTFIELD 8
        labels[15].setBounds(15, 425, 120, 20);
        labels[15].setText("Legajo");
        textField[8].setBounds(15, 445, 140, 22);
        //Expediente TEXTFIELD 9
        labels[16].setBounds(195, 425, 120, 20);
        labels[16].setText("Nro de Expediente");
        textField[9].setBounds(195, 445, 140, 22);
        textField[9].setEnabled(false);
       
        //CheckBoxes D 0 - H 1 - A 2 - T 3 - ACT 4 - INF 5
        int X = 15;
        for (int i = 0; i < checkBox.length; i++) {
            checkBox[i] = new JCheckBox(MyArrays.getCheckBox(i));
            checkBox[i].setBounds(X, 400, 50, 20);
            checkBox[i].setFont(utilities.getFontChecks());
            checkBox[i].setOpaque(false);
            checkBox[i].setFocusPainted(false);
            checkBox[i].setBorderPaintedFlat(true);
            checkBox[i].setBorderPainted(false);
            checkBox[i].addActionListener(i >= 4 ? this : null);
            container.add(checkBox[i]);
            X += 46;
        }

        //LABEL ULTIMO ANEXO 27
        JLabel ultA27 = new JLabel("________________________");
        ultA27.setFont(utilities.getFontLabelBig());
        ultA27.setForeground(Color.black);
        ultA27.setBounds(15, 190, 250, 30);
        container.add(ultA27);
        ultA27 = null;
        //----------------------------------------------------------------------        
        utilities = null;
        icons = null;
        this.getContentPane().add(container);
    }

    //--------------------------------------------------------------------------
    //-------------------EVENTO BOTONES-----------------------------------------
    //--------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        //EVENTO AL CAMBIAR DE CATEGORIA
        if (e.getSource() == comboBox[0]) {
            textField[2].setEnabled(true);
            comboBox[2].setEnabled(true);
            comboBox[1].removeAllItems();
            int categorie = comboBox[0].getSelectedIndex();

            for (String i : MyArrays.getGrades(categorie)) {
                comboBox[1].addItem(i);
            }
            if (categorie == 2 || categorie == 3) {
                textField[2].setEnabled(false);
                textField[2].setText(categorie == 2 ? "OP" : "Civil");
            }
            if (categorie == 3) {
                comboBox[2].setSelectedIndex(1);
                comboBox[2].setEnabled(false);
            }
        }
        //EVENTO AL MARCAR ACT O INF
        for (int i = 4; i < checkBox.length; i++) {
            if (e.getSource() == checkBox[i]) {
                boolean enabled = checkBox[4].isSelected() || checkBox[5].isSelected();
                textField[9].setEnabled(enabled);
            }
        }
        //EVENTO BOTON CALCULAR IMC
        if (e.getSource() == buttonCalcIMC) {
            for (int i = 0; i < 2; i++) {
                textField[i + 4].setForeground(Color.black);
            }
            try {
                String[] dataIMC = calculateIMC(Double.parseDouble(textField[4].getText()), Double.parseDouble(textField[5].getText()));
                textField[6].setText(dataIMC[0]);
                comboBox[4].setSelectedIndex(Integer.parseInt(dataIMC[1]));
            } catch (Exception ex) {
                for (int i = 0; i < 2; i++) {
                    textField[i + 4].setForeground(Color.red);
                }
                JOptionPane.showMessageDialog(null, "Error en el calculo, revise los datos ingresados.");
            }
        }
        //EVENTO BOTON AGREGAR
        if (e.getSource() == buttonAdd) {
            if (validation()) {
                dispose();
                Transmitter emisor = new Transmitter(0);
                emisor.sendInformation(this);
                emisor.update(personnelPanel);
                emisor = null;
                empty();
                System.gc();
            }
        }
        //EVENTO BOTON MODIFICAR
        if (e.getSource() == buttonModify) {

            int opcion = JOptionPane.showConfirmDialog(null,
                    "¿Esta seguro que desea guardar los cambios?",
                    "Guardar Cambios", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_NO_OPTION) {
                if (validation()) {
                    dispose();
                    Transmitter enviar = new Transmitter(id);
                    enviar.sendInformation(this);
                    enviar.update(personnelPanel);
                    enviar = null;
                    try {
                        personnelPanel.getTables(personnelPanel.getTabbedPane().getSelectedIndex()).setRowSelectionInterval(pointer, pointer);
                    } catch (Exception ex) {
                    }
                    empty();
                    System.gc();
                }
            }
        }
        //EVENTO BOTON ELIMINAR
        if (e.getSource() == buttonDelete) {
            int opcion = JOptionPane.showConfirmDialog(null,
                    "¿Esta seguro que desea eliminar esta informacion?",
                    "Eliminar informacion", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                if (sick) {
                    JOptionPane.showMessageDialog(null, "<html><center>" + personnel.getCompleteName()
                            + " tiene un Parte de Sanidad activo.<br>"
                            + "Debe darlo de alta en el sistema antes de poder eliminarlo.</center></html>");
                } else {
                    dispose();
                    Deleter eliminar = new Deleter(id);
                    eliminar.delete(PersonnelPanel.TABLE_NAME);
                    eliminar.update(personnelPanel);
                    eliminar = null;
                    empty();
                    System.gc();
                }

            }
        }
        //EVENTO BOTON PARTE DE ENFERMO
        if (e.getSource() == buttonSick) {
            if (sick) {
                JOptionPane.showMessageDialog(null, personnel.getCompleteName()
                        + " ya cuenta con un Parte de Sanidad activo.");
                
            } else {
                sickFormulary.newSick(this);
            }
        }
    }

    //-------------------------------------------------------------------------
    //-------------------METODOS PARA ABRIR FORMULARIO--------------------------
    public void newPersonnel() {
        setVisible(true);
    }

    public void obtainData(int id, int pointer) {
        setTitle("Modificar Personal");
        buttonAdd.setVisible(false);
        buttonModify.setVisible(true);
        buttonDelete.setVisible(true);
        buttonSick.setVisible(true);
        labelSick.setVisible(true);

        this.id = id;
        this.pointer = pointer;

        Receiver receiver = new Receiver(id);
        receiver.obtainInformation(this);

        int categorie = comboBox[0].getSelectedIndex();
        int grade = comboBox[1].getSelectedIndex();
        String esp = textField[2].getText();
        String subUnity = String.valueOf(comboBox[2].getSelectedItem());
        String name = textField[0].getText() + " " + textField[1].getText();
        char genre = M.isSelected() ? 'M' : 'F';
        int dni = !textField[3].getText().equals("") ? Integer.parseInt(textField[3].getText()) : 0;

        personnel = new Personnel(this.id, categorie, grade, esp, name, subUnity, genre, dni);

        this.setVisible(true);

        receiver = null;
    }

    //------------------------------------------------------
    //-----------------METODO VALIDAR-----------------------
    private boolean validation() {
        int labelIndex;
        labels[2].setForeground(Color.black);
        labels[3].setForeground(Color.black);
        labels[6].setForeground(Color.black);
        labels[7].setForeground(Color.black);
        labels[8].setForeground(Color.black);
        labels[10].setForeground(Color.black);
        labels[11].setForeground(Color.black);
        labels[12].setForeground(Color.black);

        //VALIDAR CAMPOS OBLIGATORIOS  
        String[] fields = {textField[0].getText().trim(), textField[1].getText().trim(), textField[3].getText().trim()};
        if ("".equals(fields[0]) || "".equals(fields[1]) || "".equals(fields[2])) {
            if ("".equals(fields[0])) {
                labels[2].setForeground(Color.red);
            }
            if ("".equals(fields[1])) {
                labels[3].setForeground(Color.red);
            }
            if ("".equals(fields[2])) {
                labels[6].setForeground(Color.red);
            }
            String message = "<html><center>Debe llenar los campos obligatorios.</center></html>";
            JOptionPane.showMessageDialog(null, new JLabel(message, JLabel.CENTER), "Advertencia", 1);
            return false;
        }
        fields = null;
        //VALIDAR DNI
        if (!textField[3].getText().equals("")) {
            try {
                Integer.parseInt(textField[3].getText().trim());
            } catch (Exception e) {
                labels[6].setForeground(Color.red);
                String message = "<html><center>Numero de DNI invalido.</center></html>";
                JOptionPane.showMessageDialog(null, new JLabel(message, JLabel.CENTER), "Advertencia", 1);
                return false;
            }
        }
        //VALIDAR LA FECHA
        labelIndex = 7;
        for (int i = 0; i < dateChooser.length; i++) {    
            String date = ((JTextField) dateChooser[i].getDateEditor().getUiComponent()).getText();
            if (!date.equals("") && dateChooser[i].getDate() == null) {
                labels[labelIndex].setForeground(Color.red);
                String message = "<html><center>Fecha ingresada invalida, ejemplo de fecha valida: 01/01/2020 y/o 1/1/2020"
                        + "<br>Si no conoce la fecha puede dejar el campo vacio.</center></html>";
                JOptionPane.showMessageDialog(null, new JLabel(message, JLabel.CENTER), "Advertencia", 1);
                return false;
            }
            labelIndex++;
        }
        //VALIDAR PESO, ALTURA E IMC
        labelIndex = 10;
        for (int i = 4; i < 4 + 3; i++) {
            if (!"".equals(textField[i].getText())) {
                try {
                    Double.parseDouble(textField[i].getText().trim());
                } catch (Exception e) {
                    labels[labelIndex].setForeground(Color.red);
                    String message = "<html><center>Numero ingresado incorrecto.</center></html>";
                    JOptionPane.showMessageDialog(null, new JLabel(message, JLabel.CENTER), "Advertencia", 1);
                    return false;
                }
            }
            labelIndex++;
        }
        return true;
    }

    //------------------------------------------------------
    //-----------------METODO VACIAR-----------------------
    private void empty() {
        id = 0;
        personnel = null;
        sick = false;
        setTitle("Agregar Personal");

        buttonAdd.setVisible(true);
        buttonModify.setVisible(false);
        buttonDelete.setVisible(false);
        buttonSick.setVisible(false);
        labelSick.setVisible(false);

        for (JTextField i : textField) {
            i.setText("");
        }
        for (JComboBox i : comboBox) {
            i.setSelectedIndex(0);
        }
        for (JDateChooser i : dateChooser) {
            i.setDate(null);
        }
        for (JCheckBox i : checkBox) {
            i.setSelected(false);
        }

        for (int i = 2; i < 13; i++) {
            if (i != 4 && i != 5 && i != 9) {
                labels[i].setForeground(Color.black);
            }
        }

        textField[4].setForeground(Color.black);
        textField[5].setForeground(Color.black);
        textField[9].setEnabled(false);
        M.setSelected(true);
    }

    //------------------------------------------------------
    //-----------------METODO CALCULAR IMC------------------
    private String[] calculateIMC(double weight, double height) {
        String index = "0";
        String IMCFinal;
        double IMC = weight / (height * height);
        DecimalFormat roundOut = new DecimalFormat("0.00");
        IMCFinal = roundOut.format(IMC);
        //ciclo para cambiar "," POR "." en el resultado del decimal format
        for (int i = 0; i < IMCFinal.length(); i++) {
            if (IMCFinal.charAt(i) == ',') {
                IMCFinal = IMCFinal.substring(0, i) + "." + IMCFinal.substring(i + 1);
            }
        }
        if (IMC >= 17.5 && IMC < 25) {
            index = "1";
        }
        if (IMC >= 25 && IMC < 30) {
            index = "2";
        }
        if (IMC >= 30 && IMC < 35) {
            index = "3";
        }
        if (IMC >= 35) {
            index = "4";
        }
        if (IMC < 17.5) {
            index = "5";
        }
        String[] data = {IMCFinal, index};
        roundOut = null;
        return data;
    }

    //---------------------SETTERS Y GETTERS----------------------------
    public void setCategorie(int index) {
        this.comboBox[0].setSelectedIndex(index);
    }

    public JTextField getTextField(int index) {
        return textField[index];
    }

    public JComboBox getComboBox(int index) {
        return comboBox[index];
    }

    public JDateChooser getDateChooser(int index) {
        return dateChooser[index];
    }

    public JCheckBox getCheckBox(int index) {
        return checkBox[index];
    }

    public int getTextFieldLength() {
        return textField.length;
    }

    public int getComboBoxLength() {
        return comboBox.length;
    }

    public int getDateChooserLength() {
        return dateChooser.length;
    }

    public int getCheckBoxLength() {
        return checkBox.length;
    }

    public JRadioButton getM() {
        return M;
    }

    public JRadioButton getF() {
        return F;
    }

    public ButtonModel getMModel() {
        return M.getModel();
    }

    public ButtonModel getFModel() {
        return F.getModel();
    }

    public ButtonGroup getBg() {
        return bg;
    }

    public void setSick(boolean sick) {
        this.sick = sick;
    }

    public boolean getParteDeEnfermo() {
        return this.sick;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public PersonnelPanel getPersonnelPanel() {
        return personnelPanel;
    }

    public void setPersonnelPanel(PersonnelPanel personnelPanel) {
        this.personnelPanel = personnelPanel;
    }

    public SickFormulary getSickFormulary() {
        return sickFormulary;
    }

    public void setSickFormulary(SickFormulary sickFormulary) {
        this.sickFormulary = sickFormulary;
    }

}
