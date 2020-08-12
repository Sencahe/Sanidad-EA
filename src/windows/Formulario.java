package windows;

import database.Eliminador;
import database.Emisor;
import mytools.JTextFieldLimit;
import database.Receptor;
import mytools.Fechas;
import mytools.TextPrompt;
import mytools.Utilidades;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
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
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mytools.Arreglos;
import mytools.Iconos;
import personal.Personal;
import windows.parte.FormularioParte;

public class Formulario extends javax.swing.JDialog implements ActionListener {

    private JTextField[] textField;
    private JComboBox[] comboBox;
    private JDateChooser[] dateChooser;

    private JCheckBox[] checkBox;
    private JLabel[] labels;

    private JButton botonCalcularIMC, botonAgregar, botonEliminar, botonModificar, botonParte;

    private int id;
    private int puntero; //variable que sirve de referencia para la fila seleccionada al abrir el frame   
    private boolean parteDeEnfermo;

    private Tabla tabla;
    private FormularioParte formParte;
    
    private Personal personal;

    public Formulario(Frame parent, boolean modal, Tabla tabla, FormularioParte formParte) {
        super(parent, modal);
        this.tabla = tabla;
        this.formParte = formParte;
        this.parteDeEnfermo = false;
        Componentes();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Vaciar();
                dispose();
                System.gc();
            }
        });
    }

    private void Componentes() {
        //---------------------------------------
        Utilidades utilidad = new Utilidades();
        Iconos iconos = new Iconos();
        //PROPIEDADES DEL FRAME-------------------------------------------------
        setSize(500, 530);
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
        botonAgregar = new JButton("Agregar");
        botonAgregar.setBounds(385, 355, 85, 30);
        botonAgregar.addActionListener(this);
        container.add(botonAgregar);
        botonModificar = new JButton("Guardar");
        botonModificar.setBounds(385, 355, 85, 30);
        botonModificar.addActionListener(this);
        botonModificar.setVisible(false);
        container.add(botonModificar);
        botonEliminar = new JButton("Eliminar");
        botonEliminar.setBounds(385, 390, 85, 30);
        botonEliminar.addActionListener(this);
        botonEliminar.setVisible(false);
        container.add(botonEliminar);
        JLabel labelParte = new JLabel("Parte de Enfermo");
        labelParte.setBounds(260,195,150,30);
        labelParte.setFont(utilidad.getFuenteLabelsFormulario());
        labelParte.setForeground(Color.black);
        container.add(labelParte);
        botonParte = new JButton("Agregar");
        botonParte.setBounds(275, 220, 80, 20);
        botonParte.addActionListener(this);
        botonParte.setVisible(false);
        container.add(botonParte);
        // DECLARACION DE COMPONENTES DEL FRAME MAS ALGUNAS PROPIEDADES---------
        textField = new JTextField[Arreglos.getTextFieldLength()];
        comboBox = new JComboBox[Arreglos.getComboBoxLength()];
        dateChooser = new JDateChooser[Arreglos.getDateChooserLength()];
        checkBox = new JCheckBox[Arreglos.getCheckBoxLength()];
        labels = new JLabel[textField.length + comboBox.length + dateChooser.length + 2];
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

            if (i >= 3 && i <= 6) {
                textField[i].addKeyListener(utilidad.bloquearLetras);
            }
            if (i >= 4 && i <= 6) {
                textField[i].setDocument(new JTextFieldLimit(5));
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
            dateChooser[i].setDateFormatString(utilidad.getFormatoFecha());
            container.add(dateChooser[i]);
        }
        //propiedades labels
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setFont(utilidad.getFuenteLabelsFormulario());
            container.add(labels[i]);
        }

        //UBICACION DE LOS COMPONENTES Y PROPIEDADES PARTICULARES DE CADA UNO---
        //Categoria COMBO 0
        labels[0].setBounds(15, 10, 60, 20);
        labels[0].setText("Categoria");
        comboBox[0].setBounds(80, 10, 110, 20);
        comboBox[0].addActionListener(this);
        for (int i = 0; i < Arreglos.getCategoriasLength(); i++) {
            comboBox[0].addItem(Arreglos.getCategorias(i));
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
        //Destino COMBO 2
        labels[5].setBounds(130, 95, 60, 20);
        labels[5].setText("Destino");
        comboBox[2].setBounds(130, 120, 90, 20);
        for (int i = 0; i < Arreglos.getDestinos().length; i++) {
            comboBox[2].addItem(Arreglos.getDestinos()[i]);
        }
        //DNI TEXTFIELD 3
        labels[6].setBounds(240, 95, 100, 20);
        labels[6].setText("DNI");
        textField[3].setBounds(240, 120, 80, 20);
        textField[3].setDocument(new JTextFieldLimit(9));
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
        for (int i = 0; i < Arreglos.getAptitudLength(); i++) {
            comboBox[3].addItem(Arreglos.getAptitud(i));
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
        for (int i = 0; i < Arreglos.getPPSLength(); i++) {
            comboBox[4].addItem(Arreglos.getPPS(i));
        }
        //BOTON CALCULAR IMC
        botonCalcularIMC = new JButton("Calcular IMC");
        botonCalcularIMC.setBounds(30, 305, 110, 30);
        botonCalcularIMC.addActionListener(this);
        container.add(botonCalcularIMC);
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
        //CheckBoxes D 0 - H 1 - A 2 - T 3 - ACT 4 - INF 5
        int X = 15;
        for (int i = 0; i < checkBox.length; i++) {
            checkBox[i] = new JCheckBox(Arreglos.getCheckBox(i));
            checkBox[i].setBounds(X, 400, 46, 20);
            checkBox[i].setFont(utilidad.getFuenteChecks());
            checkBox[i].setOpaque(false);
            checkBox[i].setBackground(utilidad.getTransparencia());
            checkBox[i].addActionListener(i >= 4 ? this : null);
            container.add(checkBox[i]);
            X += 44;
        }
        //LABEL ULTIMO ANEXO 27
        JLabel ultA27 = new JLabel("<HTML><U>Ultimo Anexo 27</U></HTML>");
        ultA27.setFont(utilidad.getFuenteLabelGrande());
        ultA27.setBounds(15, 165, 130, 30);
        ultA27.setBackground(Color.white);
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
            comboBox[1].removeAllItems();
            int categoria = comboBox[0].getSelectedIndex();
            for (String i : Arreglos.getGrados(categoria)) {
                comboBox[1].addItem(i);
            }
            if (categoria == 2 || categoria == 3) {
                textField[2].setEnabled(false);
                textField[2].setText("—");
                textField[2].setHorizontalAlignment((int) CENTER_ALIGNMENT);
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
                Emisor emisor = new Emisor(0);
                emisor.setInformacion(this);
                emisor.Actualizar(tabla);
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
                    Emisor enviar = new Emisor(id);
                    enviar.setInformacion(this);
                    enviar.Actualizar(tabla);
                    enviar = null;
                    try {
                        tabla.getTablas(tabla.getContenedor().getSelectedIndex()).setRowSelectionInterval(puntero, puntero);
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
                dispose();
                Eliminador eliminar = new Eliminador(id);
                eliminar.Eliminar();
                eliminar.Actualizar(tabla);
                eliminar = null;
                Vaciar();
                System.gc();
            }
        }
        //EVENTO BOTON PARTE DE ENFERMO
        if (e.getSource() == botonParte) {
            if (parteDeEnfermo) {
                JOptionPane.showMessageDialog(null, textField[0].getText() + " " + textField[1].getText()
                        + " ya cuenta con un parte de enfermo activo.");
            } else {
                formParte.setPersonal(personal);
                formParte.abrir(this);
            }
        }
    }

    //----------------------------------------------------------------------
    //-------------------METODO MODIFICAR-----------------------------------   
    public void obtenerDatos(int id, int puntero) {
        setTitle("Modificar Informacion");
        botonAgregar.setVisible(false);
        botonModificar.setVisible(true);
        botonEliminar.setVisible(true);
        botonParte.setVisible(true);

        this.id = id;
        this.puntero = puntero;

        Receptor obtener = new Receptor(id);
        obtener.getInformacion(this);
        
        int categoria = comboBox[0].getSelectedIndex();
        String grado = String.valueOf(comboBox[1].getSelectedItem());     
        String destino = String.valueOf(comboBox[2].getSelectedItem());
        String nombre = textField[0].getText() + " " + textField[1].getText();  
              
        personal = new Personal(this.id, categoria,grado,nombre,destino);
        
        obtener = null;
    }
    

    //------------------------------------------------------
    //-----------------METODO VALIDAR-----------------------
    private boolean Validar() {
        int labelIndex;
        //VALIDAR NOMBRE Y APELLIDO
        labels[2].setForeground(Color.black);
        labels[3].setForeground(Color.black);
        String campos[] = {textField[0].getText(), textField[1].getText()};
        if ("".equals(campos[0]) || "".equals(campos[1])) {
            if ("".equals(campos[0])) {
                labels[2].setForeground(Color.red);
            }
            if ("".equals(campos[1])) {
                labels[3].setForeground(Color.red);
            }
            String mensaje = "<html><center>Debe llenar minimo los campos de Apellido y Nombre.</center></html>";
            JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
            return false;
        }
        //VALIDAR LA FECHA
        labelIndex = 7;
        Fechas validar = new Fechas("dd/MM/yyyy");
        for (int i = 0; i < 2; i++) {
            labels[7].setForeground(Color.black);
            labels[8].setForeground(Color.black);
            String fecha = ((JTextField) dateChooser[i].getDateEditor().getUiComponent()).getText();
            if (!"".equals(fecha)) {
                if (!validar.fechaValida(fecha)) {
                    labels[labelIndex].setForeground(Color.red);
                    String mensaje = "<html><center>Fecha ingresada invalida, ejemplo de fecha valida: 01/01/2020 y/o 1/1/2020"
                            + "<br>Si no conoce la fecha puede dejar el campo vacio.</center></html>";
                    JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
                    validar = null;
                    return false;
                }
            }
            labelIndex++;
        }
        validar = null;
        //VALIDAR PESO, ALTURA E IMC
        labelIndex = 10;
        for (int i = 4; i < 4 + 3; i++) {
            labels[10].setForeground(Color.black);
            labels[11].setForeground(Color.black);
            labels[12].setForeground(Color.black);
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
        labels[2].setForeground(Color.black);
        labels[3].setForeground(Color.black);
        labels[7].setForeground(Color.black);
        labels[8].setForeground(Color.black);
        labels[10].setForeground(Color.black);
        labels[11].setForeground(Color.black);
        labels[12].setForeground(Color.black);
        textField[4].setForeground(Color.black);
        textField[5].setForeground(Color.black);
        textField[9].setEnabled(false);
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

    public void setParteDeEnfermo(boolean parteDeEnfermo) {
        this.parteDeEnfermo = parteDeEnfermo;
    }
    public boolean getParteDeEnfermo(){
        return this.parteDeEnfermo;
    }

}
