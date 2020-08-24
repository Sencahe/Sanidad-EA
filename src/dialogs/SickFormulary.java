package dialogs;

import personnel.Personnel;
import dialogs.PersonnelFormulary;
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
import java.util.Date;
import panels.SickPanel;

public class SickFormulary extends JDialog implements ActionListener {

    private static final String ENFERMO = "Lic. por Enfermedad";
    private static final String EXCEPTUADO = "Tareas Adm";
    private static final String MATERNIDAD = "Lic. por Maternidad";
    
    private JLabel informacion;
    private JLabel labelDiagnostico, labelObservaciones, labelDesde, labelHasta, labelCie,
            labelTipoParte, labelNorasSiras, sugerenciaDiag;
   
    private JComboBox comboTipoParte, comboNorasSiras;
    private JTextField textDiagnostico, textObservaciones, textCIE;
    private JDateChooser dateDesde, dateHasta;
    private JButton botonAgregar, botonModificar, botonAlta;

    private int idParte;
    private boolean modificar;
    private boolean modificoTipoParte;
    private int flagTipoParte;
    private Date flagDesde;
    private Date flagHasta;

    private Personnel personal;

    private SickPanel parte;
    private PersonnelFormulary formulario;
    private MainFrame mainFrame;

    public SickFormulary(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;

        componentes();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                vaciar();
            }
        });
    }

    private void componentes() {
        //PROPIEDADES DEL FRAME
        //------------------------------
        Utilities utilidad = mainFrame.getUtility();
        Icons iconos = mainFrame.getIcons();
        //PROPIEDADES DEL FRAME        
        setSize(450, 345);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Agregar Nuevo Parte");
        setIconImage(iconos.getIconoSanidad().getImage());
        //Fondo del frame
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
        Dimension dimension = new Dimension(450, 345);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;

        //--------------------------------------
        //COMPONENTES PRINCIPALES
        //Labels con informacion
        informacion = new JLabel();
        informacion.setBounds(15, 5, 355, 80);
        informacion.setFont(utilidad.getFuenteLabelGrande());
        informacion.setForeground(Color.black);
        container.add(informacion);

        //combobox
        JLabel labelComboTipoParte = new JLabel("Tipo de Parte");
        labelComboTipoParte.setBounds(15, 90, 160, 20);
        labelComboTipoParte.setForeground(Color.black);
        labelComboTipoParte.setFont(utilidad.getFuenteLabelsFormulario());
        add(labelComboTipoParte);
        comboTipoParte = new JComboBox();
        comboTipoParte.addItem("Enfermo");
        comboTipoParte.addItem("Exceptuado");
        comboTipoParte.addItem("Maternidad");
        comboTipoParte.setBounds(15, 110, 160, 20);
        comboTipoParte.addActionListener(this);
        container.add(comboTipoParte);
        labelTipoParte = new JLabel("<html>Al modificar el tipo de parte se enviara al recuento la "
                + "informacion actualmente guardada y se creara un nuevo parte. Revise esa informacion antes de proceder y "
                + "recuerde modificar las fechas 'Desde' y 'Hasta' para el nuevo parte.<html>");
        labelTipoParte.setBounds(180, 55, 250, 80);
        labelTipoParte.setFont(utilidad.getFuenteLabelInfo());
        labelTipoParte.setForeground(Color.orange);
        labelTipoParte.setVisible(false);
        container.add(labelTipoParte);

        comboNorasSiras = new JComboBox();
        comboNorasSiras.addItem("NORAS");
        comboNorasSiras.addItem("SIRAS");
        comboNorasSiras.setBounds(240, 245, 160, 20);
        container.add(comboNorasSiras);
        labelNorasSiras = new JLabel("NORAS / SIRAS");
        labelNorasSiras.setBounds(240, 220, 160, 20);
        labelNorasSiras.setForeground(Color.black);
        labelNorasSiras.setFont(utilidad.getFuenteLabelsFormulario());
        container.add(labelNorasSiras);
        //datechooser
        labelDesde = new JLabel("Desde *");
        labelDesde.setBounds(15, 135, 200, 20);
        labelDesde.setFont(utilidad.getFuenteLabelsFormulario());
        labelDesde.setForeground(Color.black);
        container.add(labelDesde);
        dateDesde = new JDateChooser();
        dateDesde.setBounds(15, 155, 100, 20);
        dateDesde.setForeground(Color.black);
        dateDesde.setFont(utilidad.getFuenteTextFields());
        dateDesde.setDateFormatString(MyDates.USER_DATE_FORMAT);
        container.add(dateDesde);
        labelHasta = new JLabel("Hasta *");
        labelHasta.setBounds(130, 135, 200, 20);
        labelHasta.setFont(utilidad.getFuenteLabelsFormulario());
        labelHasta.setForeground(Color.black);
        container.add(labelHasta);
        dateHasta = new JDateChooser();
        dateHasta.setBounds(130, 155, 100, 20);
        dateHasta.setForeground(Color.black);
        dateHasta.setFont(utilidad.getFuenteTextFields());
        dateHasta.setDateFormatString(MyDates.USER_DATE_FORMAT);
        container.add(dateHasta);
        //textfield
        labelObservaciones = new JLabel("Observaciones *");
        labelObservaciones.setBounds(240, 135, 150, 20);
        labelObservaciones.setFont(utilidad.getFuenteLabelsFormulario());
        labelObservaciones.setForeground(Color.black);
        container.add(labelObservaciones);
        textObservaciones = new JTextField();
        textObservaciones.setBounds(240, 155, 170, 20);
        textObservaciones.setFont(utilidad.getFuenteTextFields());
        textObservaciones.setText(ENFERMO);
        container.add(textObservaciones);
        labelDiagnostico = new JLabel("Diagnostico *");
        labelDiagnostico.setBounds(15, 175, 200, 20);
        labelDiagnostico.setFont(utilidad.getFuenteLabelsFormulario());
        labelDiagnostico.setForeground(Color.black);
        container.add(labelDiagnostico);
        textDiagnostico = new JTextField();
        textDiagnostico.setBounds(15, 195, 215, 20);
        textDiagnostico.setFont(utilidad.getFuenteTextFields());
        container.add(textDiagnostico);

        labelCie = new JLabel("CIE");
        labelCie.setBounds(240, 175, 100, 20);
        labelCie.setFont(utilidad.getFuenteLabelsFormulario());
        labelCie.setForeground(Color.black);
        container.add(labelCie);
        textCIE = new JTextField();
        textCIE.setBounds(240, 195, 170, 20);
        textCIE.setFont(utilidad.getFuenteTextFields());
        container.add(textCIE);

        sugerenciaDiag = new JLabel("Diagnosticos sugeridos: 'Embarazo' |"
                + " (0-3 meses) 'Lactancia' |"
                + " (3-12 meses) 'Maternidad'");
        sugerenciaDiag.setBounds(10, 275, 435, 30);
        sugerenciaDiag.setForeground(Color.black);
        sugerenciaDiag.setVisible(false);
        add(sugerenciaDiag);
        //BOTONES---------------------------------------------------------------
        botonAgregar = new JButton("<html>Guardar</html>", iconos.getIconoSave());
        botonAgregar.setBounds(15, 235, 90, 30);
        botonAgregar.setHorizontalAlignment(SwingConstants.LEFT);
        botonAgregar.addActionListener(this);
        container.add(botonAgregar);
        botonModificar = new JButton("<html>Guardar</html>", iconos.getIconoSave());
        botonModificar.setBounds(15, 235, 90, 30);
        botonModificar.setHorizontalAlignment(SwingConstants.LEFT);
        botonModificar.addActionListener(this);
        botonModificar.setVisible(false);
        container.add(botonModificar);
        botonAlta = new JButton("<html>Alta</html>",iconos.getIconoAlta());
        botonAlta.setBounds(125, 235, 90, 30);
        botonAlta.addActionListener(this);
        botonAlta.setVisible(false);
        container.add(botonAlta);

        //--------------------------------------
        this.getContentPane().add(container);
        utilidad = null;
        iconos = null;
    }

    //--------------------------------------------------------------------------
    //EVENTO BOTOENS------------------------------------------------------------
    @Override
    //----------BOTON AGREGAR-----------------------
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonAgregar) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea agregar un nuevo Parte?",
                    "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                if (validar()) {
                    Transmitter emisor = new Transmitter(personal.getId(), 0);
                    emisor.setInformacion(this);
                    emisor.actualizar(parte);
                    parte.updateWindow();
                    formulario.setParteDeEnfermo(true);
                    vaciar();
                    emisor = null;
                }
            }
        }
        //---------------BOTON MODIFICARR---------------------------
        if (e.getSource() == botonModificar) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea modificar el Parte?",
                    "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                if (validar()) {
                    Transmitter emisor = new Transmitter(personal.getId(), this.idParte);
                    if (flagTipoParte == comboTipoParte.getSelectedIndex()) {
                        emisor.setInformacion(this);
                    } else {
                        emisor.setRecuento(this, false);
                    }
                    emisor.actualizar(parte);
                    parte.updateWindow();
                    dispose();
                    vaciar();
                    emisor = null;
                }
            }
        }
        //-------------------BOTON ALTA-----------------------
        if (e.getSource() == botonAlta) {
            int confirmar = JOptionPane.showConfirmDialog(null,
                    "<html><center>¿Esta seguro que desea dar de Alta a "
                    + personal.getCompleteName() + "?<br>"
                    + "Recuerde que la fecha de 'Hasta' debe coincidir con la del Alta Medica.</center></html>",
                    "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                if (validar()) {
                    Transmitter emisor = new Transmitter(personal.getId(), this.idParte);
                    emisor.setRecuento(this, true);
                    emisor.actualizar(parte);
                    parte.updateWindow();;
                    dispose();
                    emisor = null;
                    vaciar();
                }
            }
        }
        //---------------COMBO TIPO PARTE---------------------------
        if (e.getSource() == comboTipoParte) {
            //si se cambia el tipo de parte se borran las fechas
            if (modificar && flagTipoParte != comboTipoParte.getSelectedIndex()) {
                modificoTipoParte = true;
                labelTipoParte.setVisible(true);
                botonAlta.setVisible(false);
                ((JTextField) dateDesde.getDateEditor().getUiComponent()).setText("");
                ((JTextField) dateHasta.getDateEditor().getUiComponent()).setText("");

                //sino, se colocan las actuales    
            } else if(modificar){
                modificoTipoParte = false;
                labelTipoParte.setVisible(false);
                botonAlta.setVisible(true);
                dateDesde.setDate(flagDesde);
                dateHasta.setDate(flagHasta);

            }
            //----
            //se cambia el campo observacion si se cambia el tipo de parte
            if (comboTipoParte.getSelectedIndex() == 0) {
                textObservaciones.setText(ENFERMO);
                sugerenciaDiag.setVisible(false);
            }
            if (comboTipoParte.getSelectedIndex() == 1) {
                textObservaciones.setText(EXCEPTUADO);
                sugerenciaDiag.setVisible(false);
            }
            if (comboTipoParte.getSelectedIndex() == 2) {
                textObservaciones.setText(MATERNIDAD);
                sugerenciaDiag.setVisible(true);
            }

        }
    }

    //-------------------------METODO VACIAR------------------------------------
    private void vaciar() {
        setTitle("Agregar Nuevo Parte");
        botonAgregar.setVisible(true);
        botonAlta.setVisible(false);
        botonModificar.setVisible(false);


        modificar = false;
        modificoTipoParte = false;

        personal = null;

        informacion.setText("");

        comboTipoParte.setSelectedIndex(0);
        textDiagnostico.setText("");
        textObservaciones.setText(ENFERMO);
        textCIE.setText("");
        ((JTextField) dateDesde.getDateEditor().getUiComponent()).setText("");
        ((JTextField) dateHasta.getDateEditor().getUiComponent()).setText("");        
        labelTipoParte.setVisible(false);

        dispose();
        System.gc();
    }

    //------------------------METODOS PARA NUEVO PARTE--------------------------
    public void nuevoParte(PersonnelFormulary formulario) {
        this.formulario = formulario;
        this.personal = formulario.getPersonal();
        informacion.setText(personal.toString());

        setVisible(true);
    }

    //------------METODOS PARA MODIFICAR PARTES ACTUALES-----------------------
    public void obtenerDatos(int idParte) {
        this.idParte = idParte;

        setTitle("Modificar Parte");
        botonAgregar.setVisible(false);
        botonAlta.setVisible(true);
        botonModificar.setVisible(true);

        Receiver receptor = new Receiver(this.idParte);
        receptor.getInformacion(this);

        flagTipoParte = comboTipoParte.getSelectedIndex();
        modificar = true;
        modificoTipoParte = false;

        informacion.setText(personal.toString());

        setVisible(true);
    }

    //----------------------METODO VALIDAR--------------------------------------
    private boolean validar() {
        labelDiagnostico.setForeground(Color.black);
        labelObservaciones.setForeground(Color.black);
        labelDesde.setForeground(Color.black);
        labelHasta.setForeground(Color.black);
               
        //validando campos vacios
        boolean campoDiag = textDiagnostico.getText().equals("");
        boolean campoObs = textObservaciones.getText().equals("");
        boolean campoDesde = ((JTextField) dateDesde.getDateEditor().getUiComponent()).getText().equals("");
        boolean campoHasta = ((JTextField) dateHasta.getDateEditor().getUiComponent()).getText().equals("");        
        if (campoDiag || campoObs || campoDesde || campoHasta) {
            labelDiagnostico.setForeground(campoDiag ? Color.red : Color.black);
            labelObservaciones.setForeground(campoObs ? Color.red : Color.black);
            labelDesde.setForeground(campoDesde ? Color.red : Color.black);
            labelHasta.setForeground(campoHasta ? Color.red : Color.black);
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos obligatorios");
            return false;
        }
        //validando fechas mal escritas
        MyDates validar = new MyDates("dd/MM/yyyy");
        String fechaDesde = ((JTextField) dateDesde.getDateEditor().getUiComponent()).getText();
        String fechaHasta = ((JTextField) dateHasta.getDateEditor().getUiComponent()).getText();
        if (!validar.fechaValida(fechaHasta) || !validar.fechaValida(fechaDesde)) {
            labelDesde.setForeground(campoDiag ? Color.red : Color.black);
            labelHasta.setForeground(campoDiag ? Color.red : Color.black);
            String mensaje = "<html><center>Fecha ingresada invalida, "
                    + "ejemplo de fecha valida: 01/01/2020 y/o 1/1/2020</center></html>";
            JOptionPane.showMessageDialog(null, new JLabel(mensaje, JLabel.CENTER), "Advertencia", 1);
            validar = null;
            return false;
        }
        //validando la coherencia de las fechas Desde y Hasta ingresadas por el usuario
        if (modificoTipoParte) {
            if (!validar.fechaParteValida(dateDesde.getDate(),dateHasta.getDate(),flagDesde)) {
                return false;
            }
        } else {
            if (!validar.fechaParteValida(dateDesde.getDate(), dateHasta.getDate())){
                return false;
            }
        }
        //validando que no se le agrege parte de maternidad a personal M
        if (personal.getGenre() == 'M') {
            if (comboTipoParte.getSelectedIndex() == 2) {
                JOptionPane.showMessageDialog(null, "No puede agregar parte de \"Maternidad\" a un Personal Masculino.");
                return false;
            }
        }

        validar = null;
        return true;
    }

    //----------------------SETTER Y GETTERS------------------------------------
    public void setPersonal(Personnel personal) {
        this.personal = personal;
    }

    public void setParte(SickPanel parte) {
        this.parte = parte;
    }

    public void setFormulario(PersonnelFormulary formulario) {
        this.formulario = formulario;
    }

    public Personnel getPersonal() {
        return personal;
    }

    public JComboBox getComboTipoParte() {
        return comboTipoParte;
    }

    public JComboBox getNorasSiras() {
        return comboNorasSiras;
    }

    public JTextField getTextDiagnostico() {
        return textDiagnostico;
    }

    public JTextField getTextObservaciones() {
        return textObservaciones;
    }

    public JTextField getTextCIE() {
        return textCIE;
    }

    public JDateChooser getDateDesde() {
        return dateDesde;
    }

    public Date getFlagDesde() {
        return flagDesde;
    }

    public void setFlagDesde(Date flagDesde) {
        this.flagDesde = flagDesde;
    }

    public Date getFlagHasta() {
        return flagHasta;
    }

    public void setFlagHasta(Date flagHasta) {
        this.flagHasta = flagHasta;
    }

    public JDateChooser getDateHasta() {
        return dateHasta;
    }

    public void setFlagTipoParte(int flagTipoParte) {
        this.flagTipoParte = flagTipoParte;
    }

}
