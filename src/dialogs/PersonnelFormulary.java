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
import java.util.Date;
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

    private JButton botonCalcularIMC, botonAgregar, botonEliminar, botonModificar, botonParte;
    private JLabel labelParte;

    private int id;
    private int puntero; //variable que sirve de referencia para la fila seleccionada al abrir el frame   
    private boolean parteDeEnfermo;

    private PersonnelPanel tabla;
    private SickFormulary formParte;
    private Personnel personal;
    private MainFrame mainFrame;

    public PersonnelFormulary(Frame parent, boolean modal) {
        super(parent, modal);
        this.parteDeEnfermo = false;
        this.mainFrame = (MainFrame) parent;
        Componentes();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Vaciar();
                dispose();
                System.gc();
            }
        });
    }

    private void Componentes() {
        //---------------------------------------
        Utilities utilidad = mainFrame.getUtility();
        Icons iconos = mainFrame.getIcons();
        //PROPIEDADES DEL FRAME-------------------------------------------------
        setSize(500, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Agregar Nuevo Registro");
        setIconImage(iconos.getIconoSanidad().getImage());
        //fondo del frame
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(50, 500,
                        getBackground().brighter(), 200, 170,
                        getBackground().darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

            }
        };
        container.setBackground(utilidad.getColorFondo());
        Dimension dimension = new Dimension(480, 500);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        //BOTONES PRINCIPALES         
        botonAgregar = new JButton("<html>Guardar</html>", iconos.getIconoSave());
        botonAgregar.setBounds(385, 360, 85, 30);
        botonAgregar.addActionListener(this);
        container.add(botonAgregar);
        botonModificar = new JButton("<html>Guardar</html>", iconos.getIconoSave());
        botonModificar.setBounds(385, 320, 85, 30);
        botonModificar.addActionListener(this);
        botonModificar.setVisible(false);
        container.add(botonModificar);
        botonEliminar = new JButton("<html>Eliminar</html>", iconos.getIconoDelete());
        botonEliminar.setBounds(385, 360, 85, 30);
        botonEliminar.addActionListener(this);
        botonEliminar.setVisible(false);
        container.add(botonEliminar);
        botonCalcularIMC = new JButton("<html><center>Calcular IMC</center><html>", iconos.getIconoCalculator());
        botonCalcularIMC.setBounds(45, 305, 85, 30);
        botonCalcularIMC.addActionListener(this);
        container.add(botonCalcularIMC);
        labelParte = new JLabel("<html>Parte de Sanidad<html>");
        labelParte.setBounds(370, 395, 150, 30);
        labelParte.setFont(utilidad.getFuenteLabelsFormulario());
        labelParte.setForeground(Color.black);
        labelParte.setVisible(false);
        container.add(labelParte);
        botonParte = new JButton("<html>Agregar</html>", iconos.getIconoPlus());
        botonParte.setBounds(385, 420, 85, 30);
        botonParte.addActionListener(this);
        botonParte.setVisible(false);
        container.add(botonParte);
        // DECLARACION DE COMPONENTES DEL FRAME MAS ALGUNAS PROPIEDADES---------
        textField = new JTextField[MyArrays.getTextFieldLength()];
        comboBox = new JComboBox[MyArrays.getComboBoxLength()];
        dateChooser = new JDateChooser[MyArrays.getDateChooserLength()];
        checkBox = new JCheckBox[MyArrays.getCheckBoxLength()];
        labels = new JLabel[textField.length + comboBox.length + dateChooser.length + 1];
        //propiedades text field 
        for (int i = 0; i < textField.length; i++) {
            textField[i] = new JTextField();
            textField[i].setFont(utilidad.getFuenteTextFields());
            if (i == 0 || i == 1) {
                TextPrompt holder = new TextPrompt("Campo obligatorio", textField[i]);
                holder.setFont(utilidad.getFuenteHolders());
                holder.setForeground(Color.GRAY);
                holder = null;
            }
            if (i >= 4 && i <= 6) {
                textField[i].addKeyListener(utilidad.bloquearLetras);
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
            dateChooser[i].setFont(utilidad.getFuenteTextFields());
            dateChooser[i].setDateFormatString(MyDates.USER_DATE_FORMAT);
            container.add(dateChooser[i]);
        }
        //propiedades labels
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setForeground(Color.black);
            labels[i].setFont(utilidad.getFuenteLabelsFormulario());
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
        M.setFont(utilidad.getFuenteLabelsFormulario());
        M.setSelected(true);
        bg.add(M);
        container.add(M);
        F.setBounds(360, 10, 40, 20);
        F.setOpaque(false);
        F.setFocusPainted(false);
        F.setFont(utilidad.getFuenteLabelsFormulario());
        bg.add(F);
        container.add(F);
        //Categoria COMBO 0
        labels[0].setBounds(15, 10, 60, 20);
        labels[0].setText("Categoria");
        comboBox[0].setBounds(80, 10, 110, 20);
        comboBox[0].addActionListener(this);
        for (int i = 0; i < MyArrays.getCategoriasLength(); i++) {
            comboBox[0].addItem(MyArrays.getCategorias(i));
        }
        //Grado COMBO 1
        labels[1].setBounds(15, 45, 60, 20);
        labels[1].setText("Grado");
        comboBox[1].setBounds(15, 70, 80, 20);
        //apellido TEXTFIELD 0 
        labels[2].setBounds(105, 45, 65, 20);
        labels[2].setText("Apellido *");
        textField[0].setBounds(105, 70, 175, 20);
        //nombre TEXTFIELD 1
        labels[3].setBounds(285, 45, 65, 20);
        labels[3].setText("Nombre *");
        textField[1].setBounds(285, 70, 175, 20);
        //arma/servicio TEXTFIELD 2
        labels[4].setBounds(15, 95, 100, 20);
        labels[4].setText("Arma / Servicio");
        textField[2].setBounds(15, 120, 95, 20);
        textField[2].setHorizontalAlignment((int) CENTER_ALIGNMENT);
        //Destino COMBO 2
        labels[5].setBounds(130, 95, 60, 20);
        labels[5].setText("Destino");
        comboBox[2].setBounds(130, 120, 90, 20);
        for (int i = 0; i < MyArrays.getDestinos().length; i++) {
            comboBox[2].addItem(MyArrays.getDestinos()[i]);
        }
        //DNI TEXTFIELD 3
        labels[6].setBounds(240, 95, 100, 20);
        labels[6].setText("DNI *");
        textField[3].setBounds(240, 120, 80, 20);
        textField[3].setDocument(new JTextFieldLimit(9));
        textField[3].addKeyListener(utilidad.soloNumeros);
        //Nacimiento DATE CHOOSER 0  
        labels[7].setBounds(335, 95, 150, 20);
        labels[7].setText("Fecha de Nacimiento");
        dateChooser[0].setBounds(335, 120, 100, 20);
        //Ultimo Anexo27 DATE CHOOSER 1
        labels[8].setBounds(15, 200, 160, 20);
        labels[8].setText("Fecha");
        dateChooser[1].setBounds(15, 225, 100, 20);
        //Aptitud COMBO 3
        labels[9].setBounds(130, 200, 70, 20);
        labels[9].setText("Aptitud");
        comboBox[3].setBounds(130, 225, 80, 20);
        for (int i = 0; i < MyArrays.getAptitudLength(); i++) {
            comboBox[3].addItem(MyArrays.getAptitud(i));
        }
        //Peso TEXT 4
        labels[10].setBounds(15, 250, 70, 20);
        labels[10].setText("Peso (Kgs)");
        textField[4].setBounds(20, 275, 50, 20);
        //Altura TEXT 5
        labels[11].setBounds(90, 250, 80, 20);
        labels[11].setText("Altura (Mts)");
        textField[5].setBounds(100, 275, 50, 20);
        //IMC TEXT 6
        labels[12].setBounds(190, 250, 80, 20);
        labels[12].setText("IMC");
        textField[6].setBounds(180, 275, 50, 20);
        //PPS COMBO 4
        labels[13].setBounds(250, 250, 220, 20);
        labels[13].setText("Programa Peso Saludable");
        comboBox[4].setBounds(250, 275, 130, 20);
        for (int i = 0; i < MyArrays.getPPSLength(); i++) {
            comboBox[4].addItem(MyArrays.getPPS(i));
        }
        //Observaciones TEXTFIELD 7
        labels[14].setBounds(15, 345, 90, 20);
        labels[14].setText("Observaciones");
        textField[7].setBounds(15, 370, 265, 20);
        //Legajo TEXTFIELD 8
        labels[15].setBounds(15, 425, 120, 20);
        labels[15].setText("Legajo");
        textField[8].setBounds(15, 445, 140, 20);
        //Expediente TEXTFIELD 9
        labels[16].setBounds(195, 425, 120, 20);
        labels[16].setText("Nro de Expediente");
        textField[9].setBounds(195, 445, 140, 20);
        textField[9].setEnabled(false);
        //DM TEXTFIELD10
        labels[17].setBounds(15, 465, 120, 20);
        labels[17].setText("D.M.");
        textField[10].setBounds(15, 485, 140, 20);
        //CheckBoxes D 0 - H 1 - A 2 - T 3 - ACT 4 - INF 5
        int X = 15;
        for (int i = 0; i < checkBox.length; i++) {
            checkBox[i] = new JCheckBox(MyArrays.getCheckBox(i));
            checkBox[i].setBounds(X, 400, 50, 20);
            checkBox[i].setFont(utilidad.getFuenteChecks());
            checkBox[i].setOpaque(false);
            checkBox[i].setFocusPainted(false);
            checkBox[i].setBorderPaintedFlat(true);
            checkBox[i].setBorderPainted(false);
            checkBox[i].addActionListener(i >= 4 ? this : null);
            container.add(checkBox[i]);
            X += 46;
        }

        //LABEL ULTIMO ANEXO 27
        JLabel ultA27 = new JLabel("<HTML><U>Ultimo Anexo 27</U></HTML>");
        ultA27.setFont(utilidad.getFuenteLabelGrande());
        ultA27.setForeground(Color.black);
        ultA27.setBounds(15, 165, 130, 30);
        container.add(ultA27);
        ultA27 = null;
        //----------------------------------------------------------------------        
        utilidad = null;
        iconos = null;
        this.getContentPane().add(container);
    }

    //--------------------------------------------------------------------------
    //-------------------EVENTO BOTONES-----------------------------------------
    //--------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        //EVENTO AL CAMBIAR DE CATEGORIA
        if (e.getSource() == comboBox[0]) {
            textField[2].setText("");
            textField[2].setEnabled(true);
            comboBox[2].setEnabled(true);
            comboBox[1].removeAllItems();
            int categoria = comboBox[0].getSelectedIndex();

            for (String i : MyArrays.getGrados(categoria)) {
                comboBox[1].addItem(i);
            }
            if (categoria == 2 || categoria == 3) {
                textField[2].setEnabled(false);
                textField[2].setText(categoria == 2 ? "Operacional" : "Civil");
            }
            if (categoria == 3) {
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
        if (e.getSource() == botonCalcularIMC) {
            for (int i = 0; i < 2; i++) {
                textField[i + 4].setForeground(Color.black);
            }
            try {
                String[] datosIMC = calcularIMC(Double.parseDouble(textField[4].getText()), Double.parseDouble(textField[5].getText()));
                textField[6].setText(datosIMC[0]);
                comboBox[4].setSelectedIndex(Integer.parseInt(datosIMC[1]));
            } catch (Exception ex) {
                for (int i = 0; i < 2; i++) {
                    textField[i + 4].setForeground(Color.red);
                }
                JOptionPane.showMessageDialog(null, "Error en el calculo, revise los datos ingresados.");
            }
        }
        //EVENTO BOTON AGREGAR
        if (e.getSource() == botonAgregar) {
            if (Validar()) {
                dispose();
                Transmitter emisor = new Transmitter(0);
                emisor.setInformacion(this);
                emisor.actualizar(tabla);
                emisor = null;
                Vaciar();
                System.gc();
            }
        }
        //EVENTO BOTON MODIFICAR
        if (e.getSource() == botonModificar) {

            int opcion = JOptionPane.showConfirmDialog(null,
                    "¿Esta seguro que desea guardar los cambios?",
                    "Guardar Cambios", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_NO_OPTION) {
                if (Validar()) {
                    dispose();
                    Transmitter enviar = new Transmitter(id);
                    enviar.setInformacion(this);
                    enviar.actualizar(tabla);
                    enviar = null;
                    try {
                        tabla.getTables(tabla.getTabbedPane().getSelectedIndex()).setRowSelectionInterval(puntero, puntero);
                    } catch (Exception ex) {
                    }
                    Vaciar();
                    System.gc();
                }
            }
        }
        //EVENTO BOTON ELIMINAR
        if (e.getSource() == botonEliminar) {
            int opcion = JOptionPane.showConfirmDialog(null,
                    "¿Esta seguro que desea eliminar esta informacion?",
                    "Eliminar informacion", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                if (parteDeEnfermo) {
                    JOptionPane.showMessageDialog(null, "<html><center>" + personal.getCompleteName()
                            + " tiene un Parte de Sanidad activo.<br>"
                            + "Debe darlo de alta en el sistema antes de poder eliminarlo.</center></html>");
                } else {
                    dispose();
                    Deleter eliminar = new Deleter(id);
                    eliminar.eliminar(PersonnelPanel.TABLE_NAME);
                    eliminar.actualizar(tabla);
                    eliminar = null;
                    Vaciar();
                    System.gc();
                }

            }
        }
        //EVENTO BOTON PARTE DE ENFERMO
        if (e.getSource() == botonParte) {
            if (parteDeEnfermo) {
                JOptionPane.showMessageDialog(null, personal.getCompleteName()
                        + " ya cuenta con un Parte de Sanidad activo.");
            } else {
                formParte.nuevoParte(this);
            }
        }
    }

    //-------------------------------------------------------------------------
    //-------------------METODOS PARA ABRIR FORMULARIO--------------------------
    public void nuevoFormulario() {
        setVisible(true);
    }

    public void obtenerDatos(int id, int puntero) {
        setTitle("Modificar Informacion");
        botonAgregar.setVisible(false);
        botonModificar.setVisible(true);
        botonEliminar.setVisible(true);
        botonParte.setVisible(true);
        labelParte.setVisible(true);

        this.id = id;
        this.puntero = puntero;

        Receiver obtener = new Receiver(id);
        obtener.getInformacion(this);

        int categoria = comboBox[0].getSelectedIndex();
        int grado = comboBox[1].getSelectedIndex();
        String destino = String.valueOf(comboBox[2].getSelectedItem());
        String nombre = textField[0].getText() + " " + textField[1].getText();
        char sexo = M.isSelected() ? 'M' : 'F';
        int dni = !textField[3].getText().equals("") ? Integer.parseInt(textField[3].getText()) : 0;

        personal = new Personnel(this.id, categoria, grado, nombre, destino, sexo, dni);

        this.setVisible(true);

        obtener = null;
    }

    //------------------------------------------------------
    //-----------------METODO VALIDAR-----------------------
    private boolean Validar() {
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
        String campos[] = {textField[0].getText().trim(), textField[1].getText().trim(), textField[3].getText().trim()};
        if ("".equals(campos[0]) || "".equals(campos[1]) || "".equals(campos[2])) {
            if ("".equals(campos[0])) {
                labels[2].setForeground(Color.red);
            }
            if ("".equals(campos[1])) {
                labels[3].setForeground(Color.red);
            }
            if ("".equals(campos[2])) {
                labels[6].setForeground(Color.red);
            }
            String mensaje = "<html><center>Debe llenar los campos obligatorios.</center></html>";
            JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
            return false;
        }
        campos = null;
        //VALIDAR DNI
        if (!textField[3].getText().equals("")) {
            try {
                Integer.parseInt(textField[3].getText());
            } catch (Exception e) {
                labels[6].setForeground(Color.red);
                String mensaje = "<html><center>Numero de DNI invalido.</center></html>";
                JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
                return false;
            }
        }
        //VALIDAR LA FECHA
        labelIndex = 7;
        MyDates validar = new MyDates(MyDates.USER_DATE_FORMAT);
        for (int i = 0; i < 2; i++) {
            String date = ((JTextField)dateChooser[i].getDateEditor().getUiComponent()).getText();
            if (!date.equals("") && !validar.fechaValida(date)) {
                labels[labelIndex].setForeground(Color.red);
                String mensaje = "<html><center>Fecha ingresada invalida, ejemplo de fecha valida: 01/01/2020 y/o 1/1/2020"
                        + "<br>Si no conoce la fecha puede dejar el campo vacio.</center></html>";
                JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
                validar = null;
                return false;
            }
            labelIndex++;
        }
        validar = null;
        //VALIDAR PESO, ALTURA E IMC
        labelIndex = 10;
        for (int i = 4; i < 4 + 3; i++) {
            if (!"".equals(textField[i].getText())) {
                try {
                    Double.parseDouble(textField[i].getText());
                } catch (Exception e) {
                    labels[labelIndex].setForeground(Color.red);
                    String mensaje = "<html><center>Numero ingresado incorrecto.</center></html>";
                    JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
                    return false;
                }
            }
            labelIndex++;
        }
        return true;
    }

    //------------------------------------------------------
    //-----------------METODO VACIAR-----------------------
    private void Vaciar() {
        id = 0;
        personal = null;
        parteDeEnfermo = false;

        botonAgregar.setVisible(true);
        botonModificar.setVisible(false);
        botonEliminar.setVisible(false);
        botonParte.setVisible(false);
        labelParte.setVisible(false);

        for (JTextField i : textField) {
            i.setText("");
        }
        for (JComboBox i : comboBox) {
            i.setSelectedIndex(0);
        }
        for (JDateChooser i : dateChooser) {
            ((JTextField) i.getDateEditor().getUiComponent()).setText("");
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
    private String[] calcularIMC(double peso, double altura) {
        String index = "0";
        String IMCFinal;
        double IMC = peso / (altura * altura);
        DecimalFormat redondear = new DecimalFormat("0.00");
        IMCFinal = redondear.format(IMC);
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
        String[] datos = {IMCFinal, index};
        redondear = null;
        return datos;
    }

    //---------------------SETTERS Y GETTERS----------------------------
    public void setCategoria(int index) {
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

    public void setParteDeEnfermo(boolean parteDeEnfermo) {
        this.parteDeEnfermo = parteDeEnfermo;
    }

    public boolean getParteDeEnfermo() {
        return this.parteDeEnfermo;
    }

    public Personnel getPersonal() {
        return personal;
    }

    public void setPersonal(Personnel personal) {
        this.personal = personal;
    }

    public PersonnelPanel getTabla() {
        return tabla;
    }

    public void setTabla(PersonnelPanel tabla) {
        this.tabla = tabla;
    }

    public SickFormulary getFormParte() {
        return formParte;
    }

    public void setFormParte(SickFormulary formParte) {
        this.formParte = formParte;
    }

}
