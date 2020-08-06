package windows;

import mytools.database.Eliminador;
import mytools.database.Emisor;
import mytools.JTextFieldLimit;
import mytools.Arreglos;
import mytools.CalculoIMC;
import mytools.database.Receptor;
import mytools.Fechas;
import mytools.TextPrompt;
import mytools.Utilidades;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Formulario extends javax.swing.JDialog implements ActionListener {

    private JTextField[] textField;
    public JComboBox[] comboBox;
    private JDateChooser[] dateChooser;
    private JCheckBox[] checkBox;
    private JLabel[] labels;

    private JButton calcularIMC, Agregar, Eliminar, Modificar;
    private int id;
    private int puntero; //variable que sirve de referencia para la fila seleccionada al abrir el frame

    public Formulario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        Componentes();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {                
                Vaciar();
                System.gc();
                dispose();
            }
        });
    }

    private void Componentes() {
        //OBJETOS 
        Utilidades utilidad = new Utilidades();
        Arreglos arreglo = new Arreglos();
        //PROPIEDADES DEL FRAME        
        setLayout(null);
        setSize(500, 510);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Agregar Nuevo Registro");
        setIconImage(Tabla.iconos.getIconoSanidad().getImage());
        //BOTONES PRINCIPALES 
        Agregar = new JButton("Agregar");
        Agregar.setBounds(385, 365, 85, 30);
        Agregar.addActionListener(this);
        add(Agregar);
        Modificar = new JButton("Guardar");
        Modificar.setBounds(385, 365, 85, 30);
        Modificar.addActionListener(this);
        Modificar.setVisible(false);
        add(Modificar);
        Eliminar = new JButton("Eliminar");
        Eliminar.setBounds(385, 400, 85, 30);
        Eliminar.addActionListener(this);
        Eliminar.setVisible(false);
        add(Eliminar);        
        // DECLARACION DE COMPONENTES DEL FRAME MAS ALGUNAS PROPIEDADES
        textField = new JTextField[arreglo.getTextFieldLength()];
        comboBox = new JComboBox[arreglo.getComboBoxLength()];
        dateChooser = new JDateChooser[arreglo.getDateChooserLength()];
        checkBox = new JCheckBox[arreglo.getCheckBoxLength()];
        labels = new JLabel[textField.length + comboBox.length + dateChooser.length];
        //propiedades text field 
        for (int i = 0; i < textField.length; i++) {
            textField[i] = new JTextField();
            textField[i].setFont(utilidad.fuenteTextFields());
            if (i == 0 || i == 1) {
                TextPrompt holder = new TextPrompt("Campo obligatorio", textField[i]);
                holder.setFont(utilidad.fuenteHolders());
                holder.setForeground(Color.GRAY);
            }
            
            if (i >= 3 && i <= 6) {
                textField[i].addKeyListener(utilidad.bloquearLetras);
            }
            if (i >= 4 && i <= 6) {
                textField[i].setDocument(new JTextFieldLimit(5));
            }
            add(textField[i]);
        }
        //propiedades combo box
        for (int i = 0; i < comboBox.length; i++) {
            comboBox[i] = new JComboBox();
            add(comboBox[i]);
        }
        //propiedades dateChooser
        for (int i = 0; i < dateChooser.length; i++) {
            dateChooser[i] = new JDateChooser();
            dateChooser[i].setForeground(Color.black);
            dateChooser[i].setFont(utilidad.fuenteTextFields());
            dateChooser[i].setDateFormatString(utilidad.formatoFecha());
            add(dateChooser[i]);
        }
        //propiedades labels
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setFont(utilidad.fuenteLabelsFormulario());
            add(labels[i]);
        }
        //UBICACION DE LOS COMPONENTES Y PROPIEDADES PARTICULARES DE CADA UNO
        //Categoria COMBO 0
        labels[0].setBounds(15, 10, 60, 20);
        labels[0].setText("Categoria");
        comboBox[0].setBounds(80, 10, 110, 20);
        comboBox[0].addActionListener(this);
        String categorias[] = arreglo.Categorias();
        for (String i : categorias) {
            comboBox[0].addItem(i);
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
        String destinos[] = arreglo.Destinos();
        for (String i : destinos) {
            comboBox[2].addItem(i);
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
        for (int i = 0; i < arreglo.Aptitud().length; i++) {
            comboBox[3].addItem(arreglo.Aptitud()[i]);
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
        for (int i = 0; i < arreglo.PPS().length; i++) {
            comboBox[4].addItem(arreglo.PPS()[i]);
        }
        //BOTON CALCULAR IMC
        calcularIMC = new JButton("Calcular IMC");
        calcularIMC.setBounds(30, 305, 110, 30);
        calcularIMC.addActionListener(this);
        add(calcularIMC);
        //Observaciones TEXTFIELD 7
        labels[14].setBounds(15, 345, 90, 20);
        labels[14].setText("Observaciones");
        textField[7].setBounds(15, 370, 265, 20);
        //CheckBoxes D 0 - H 1 - A 2 - T 3 - ACT 4 - INF 5
        int X = 15;
        for (int i = 0; i < checkBox.length; i++) {
            checkBox[i] = new JCheckBox(arreglo.checkBox()[i]);
            checkBox[i].setBounds(X, 405, 46, 20);
            checkBox[i].setFont(utilidad.fuenteChecks());
            checkBox[i].setOpaque(false);
            checkBox[i].setBackground(utilidad.transparencia());
            add(checkBox[i]);
            X += 44;
        }
        //LABEL ULTIMO ANEXO 27
        JLabel ultA27 = new JLabel("<HTML><U>Ultimo Anexo 27</U></HTML>");
        ultA27.setFont(utilidad.fuenteLabelGrande());
        ultA27.setBounds(15, 165, 130, 30);
        add(ultA27);

        //WALPAPER
        JLabel wallpaper = new JLabel();
        wallpaper.setBounds(0, 0, this.getWidth(), this.getHeight());
        add(wallpaper);
        Icon fondo = new ImageIcon((Tabla.iconos.getWallpaperFormulario()).getImage().getScaledInstance(wallpaper.getWidth(),
                wallpaper.getHeight(), Image.SCALE_DEFAULT));
        wallpaper.setIcon(fondo);
      
        utilidad = null;
        arreglo = null;
        System.gc();
    }

    //------------------------------------------------------
    //-------------------EVENTO BOTONES--------------------
    //------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {       
        //EVENTO AL CAMBIAR DE CATEGORIA
        if (e.getSource() == comboBox[0]) {
            comboBox[1].removeAllItems();
            int cat = comboBox[0].getSelectedIndex();
            String grados[][] = new Arreglos().grados();
            for (String i : grados[cat]) {
                comboBox[1].addItem(i);
            }
            if (cat == 2 || cat == 3) {
                textField[2].setEnabled(false);
                textField[2].setText("—");
                textField[2].setHorizontalAlignment((int) CENTER_ALIGNMENT);
            }
            if (cat == 3) {
                comboBox[2].setSelectedIndex(1);
                comboBox[2].setEnabled(false);
            }
        }
        //EVENTO BOTON CALCULAR IMC
        if (e.getSource() == calcularIMC) {
            for (int i = 0; i < 2; i++) {
                textField[i + 4].setForeground(Color.black);
            }
            try {
                String[] datosIMC = new CalculoIMC().calcularIMC(Double.parseDouble(textField[4].getText()), Double.parseDouble(textField[5].getText()));
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
        if (e.getSource() == Agregar) {
            if (Validar()) {
                Emisor emisor = new Emisor(0);
                emisor.setInformacion(enviarDatos());               
                emisor = null;
                Vaciar();
                dispose();
            }
        }
        //EVENTO BOTON MODIFICAR
        if (e.getSource() == Modificar) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea guardar los cambios?", "Guardar Cambios", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_NO_OPTION) {
                if (Validar()) {
                    Emisor enviar = new Emisor(id);
                    enviar.setInformacion(enviarDatos());                                      
                    enviar = null;
                    Tabla.tablas[Tabla.contenedor.getSelectedIndex()].setRowSelectionInterval(puntero, puntero);
                    Vaciar();
                    dispose();
                }
            }

        }
        //EVENTO BOTON ELIMINAR
        if (e.getSource() == Eliminar) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar esta informacion?", "Eliminar informacion", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                Eliminador eliminar = new Eliminador(id);
                eliminar.Eliminar();            
                eliminar = null;
                Vaciar();
                dispose();
            }
        }
    }

    //------------------------------------------------------
    //-------------------METODO MODIFICAR--------------------    
    public void obtenerDatos(int id, int puntero) {
        setTitle("Modificar Informacion");
        Agregar.setVisible(false);
        Modificar.setVisible(true);
        Eliminar.setVisible(true);

        this.id = id;
        this.puntero = puntero;
        Receptor obtener = new Receptor(id);        
        String[][] datos = obtener.getInformacion();

        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < datos[i].length; j++) {
                if (datos[i][j] != null) {
                    // agrego datos a text field
                    if (i == 0) {
                        textField[j].setText(datos[i][j]);
                    }
                    // agrego datos a combo box
                    if (i == 1) {
                        if (j == 1 || j == 0) {
                            comboBox[j].setSelectedIndex(Integer.parseInt(datos[i][j]));
                        } else {
                            comboBox[j].setSelectedItem(datos[i][j]);
                        }
                    }
                    //agrego datos a dateChooser
                    if (i == 2) {
                        try {                                 
                            Date fecha = new SimpleDateFormat(new Utilidades().formatoFecha()).parse(datos[i][j]);
                            dateChooser[j].setDate(fecha);
                        } catch (ParseException ex) {
                            JOptionPane.showMessageDialog(null, "ErrorDateChooser//Formulario//obtenerDatos" + ex
                                    + "Contactese con el desarrolador del programa para solucionar el problema");
                        }
                    }
                    //agrego datos a checkbox
                    if (i == 3) {
                        if ("X".equals(datos[i][j])) {
                            checkBox[j].setSelected(true);
                        }
                    }
                }
            }
        }
        obtener = null;
        datos = null;
    }

    //---------------------------------------------------------
    //-----------------METODO ENVIAR DATOS---------------------
    private String[] enviarDatos() {
        int comboIndex = textField.length;
        int dateIndex = comboIndex + comboBox.length;
        int checkIndex = dateIndex + dateChooser.length;
        int total = textField.length + comboBox.length + dateChooser.length + checkBox.length;
        String mensajero[] = new String[total];

        for (int i = 0; i < textField.length; i++) {
            mensajero[i] = i == 0 ? textField[i].getText().toUpperCase().trim() : textField[i].getText().trim();
        }
        for (int i = 0; i < comboBox.length; i++) {
            mensajero[comboIndex + i] = i == 0 || i == 1 ? String.valueOf(comboBox[i].getSelectedIndex()) : String.valueOf(comboBox[i].getSelectedItem());
        }
        for (int i = 0; i < dateChooser.length; i++) {
            mensajero[dateIndex + i] = ((JTextField) dateChooser[i].getDateEditor().getUiComponent()).getText();
        }
        for (int i = 0; i < checkBox.length; i++) {
            mensajero[checkIndex + i] = checkBox[i].isSelected() ? "X" : "";
        }
              
        return mensajero;
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
        for (int i = 0; i < 2; i++) {
            labels[7].setForeground(Color.black);
            labels[8].setForeground(Color.black);
            String fecha = ((JTextField) dateChooser[i].getDateEditor().getUiComponent()).getText();
            if (!"".equals(fecha)) {
                if (!new Fechas(fecha, new Utilidades().formatoFecha()).fechaValida()) {
                    labels[labelIndex].setForeground(Color.red);
                    String mensaje = "<html><center>Fecha ingresada invalida, ejemplo de fecha valida: 01/01/2020 y/o 1/1/2020"
                            + "<br>Si no conoce la fecha puede dejar el campo vacio.</center></html>";
                    JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
                    return false;
                }
            }
            labelIndex++;
        }
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

    public void Vaciar() {
        id = 0;
        Agregar.setVisible(true);
        Modificar.setVisible(false);
        Eliminar.setVisible(false);
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
    }

}
