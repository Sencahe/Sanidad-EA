package windows.parte;

import personal.Personal;
import windows.Formulario;
import database.Emisor;
import database.Receptor;
import mytools.Fechas;
import mytools.Iconos;
import mytools.Utilidades;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;

public class FormularioParte extends JDialog implements ActionListener {

    private JLabel informacion;
    private JLabel labelDiagnostico, labelObservaciones, labelDesde, labelHasta, labelCie,
            labelTipoParte, labelNorasSiras, sugerenciaDiag;
    private JComboBox tipoParte, comboNorasSiras;
    private JTextField diagnostico, observaciones, cie;
    private JDateChooser desde, hasta;
    private JButton botonAgregar, botonModificar, botonAlta;

    private int idParte;
    private boolean modificar;
    private boolean modificoTipoParte;
    private int flagTipoParte;
    private String flagDesde;
    private String flagHasta;

    private Personal personal;

    private Parte parte;
    private Formulario formulario;

    public FormularioParte(Frame parent, boolean modal) {
        super(parent, modal);

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
        Utilidades utilidad = new Utilidades();
        Iconos iconos = new Iconos();
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
        Dimension dimension = new Dimension(450, 315);
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
        labelComboTipoParte.setFont(utilidad.getFuenteLabelsFormulario());
        add(labelComboTipoParte);
        tipoParte = new JComboBox();
        tipoParte.addItem("Reposo");
        tipoParte.addItem("Exceptuado");
        tipoParte.addItem("Maternidad");
        tipoParte.setBounds(15, 110, 160, 20);
        tipoParte.addActionListener(this);
        container.add(tipoParte);
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
        labelNorasSiras.setFont(utilidad.getFuenteLabelsFormulario());
        container.add(labelNorasSiras);
        //datechooser
        labelDesde = new JLabel("Desde *");
        labelDesde.setBounds(15, 135, 200, 20);
        labelDesde.setFont(utilidad.getFuenteLabelsFormulario());
        labelDesde.setForeground(Color.black);
        container.add(labelDesde);
        desde = new JDateChooser();
        desde.setBounds(15, 155, 100, 20);
        desde.setForeground(Color.black);
        desde.setFont(utilidad.getFuenteTextFields());
        desde.setDateFormatString(utilidad.getFormatoFecha());
        container.add(desde);
        labelHasta = new JLabel("Hasta *");
        labelHasta.setBounds(130, 135, 200, 20);
        labelHasta.setFont(utilidad.getFuenteLabelsFormulario());
        labelHasta.setForeground(Color.black);
        container.add(labelHasta);
        hasta = new JDateChooser();
        hasta.setBounds(130, 155, 100, 20);
        hasta.setForeground(Color.black);
        hasta.setFont(utilidad.getFuenteTextFields());
        hasta.setDateFormatString(utilidad.getFormatoFecha());
        container.add(hasta);

        //textfield
        labelObservaciones = new JLabel("Observaciones *");
        labelObservaciones.setBounds(240, 135, 150, 20);
        labelObservaciones.setFont(utilidad.getFuenteLabelsFormulario());
        labelObservaciones.setForeground(Color.black);
        container.add(labelObservaciones);
        observaciones = new JTextField();
        observaciones.setBounds(240, 155, 170, 20);
        observaciones.setFont(utilidad.getFuenteTextFields());
        container.add(observaciones);
        labelDiagnostico = new JLabel("Diagnostico *");
        labelDiagnostico.setBounds(15, 175, 200, 20);
        labelDiagnostico.setFont(utilidad.getFuenteLabelsFormulario());
        labelDiagnostico.setForeground(Color.black);
        container.add(labelDiagnostico);
        diagnostico = new JTextField();
        diagnostico.setBounds(15, 195, 215, 20);
        diagnostico.setFont(utilidad.getFuenteTextFields());
        container.add(diagnostico);

        labelCie = new JLabel("CIE");
        labelCie.setBounds(240, 175, 100, 20);
        labelCie.setFont(utilidad.getFuenteLabelsFormulario());
        labelCie.setForeground(Color.black);
        container.add(labelCie);
        cie = new JTextField();
        cie.setBounds(240, 195, 170, 20);
        cie.setFont(utilidad.getFuenteTextFields());
        container.add(cie);

        sugerenciaDiag = new JLabel("Diagnosticos sugeridos: 'Embarazo' |"
                + " (0-3 meses) 'Lactancia' |"
                + " (3-12 meses) 'Maternidad'");
        sugerenciaDiag.setBounds(10, 275, 435, 30);
        sugerenciaDiag.setVisible(false);
        add(sugerenciaDiag);
        //Buttons
        botonAgregar = new JButton("<html>Guardar</html>", iconos.getIconoSave());
        botonAgregar.setBounds(15, 235, 90, 30);
        botonAgregar.setHorizontalAlignment(SwingConstants.LEFT);
        botonAgregar.setIconTextGap(10);
        botonAgregar.addActionListener(this);
        container.add(botonAgregar);
        botonModificar = new JButton("<html>Guardar</html>", iconos.getIconoSave());
        botonModificar.setBounds(15, 235, 90, 30);
        botonModificar.setHorizontalAlignment(SwingConstants.LEFT);
        botonModificar.setIconTextGap(10);
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
                    Emisor emisor = new Emisor(personal.getId(), 0);
                    emisor.setInformacion(this);
                    emisor.actualizar(parte);
                    parte.actualizarVentana();
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
                    Emisor emisor = new Emisor(personal.getId(), this.idParte);
                    if (flagTipoParte == tipoParte.getSelectedIndex()) {
                        emisor.setInformacion(this);
                    } else {
                        emisor.setRecuento(this, false);
                    }
                    emisor.actualizar(parte);
                    parte.actualizarVentana();
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
                    + personal.getNombreCompleto() + "?<br>"
                    + "Recuerde que la fecha de 'Hasta' debe coincidir con la del Alta Medica.</center></html>",
                    "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                if (validar()) {
                    Emisor emisor = new Emisor(personal.getId(), this.idParte);
                    emisor.setRecuento(this, true);
                    emisor.actualizar(parte);
                    parte.actualizarVentana();;
                    dispose();
                    emisor = null;
                    vaciar();
                }
            }
        }
        //---------------COMBO TIPO PARTE---------------------------
        if (e.getSource() == tipoParte) {
            //si se cambia el tipo de parte se borran las fechas
            if (modificar && flagTipoParte != tipoParte.getSelectedIndex()) {
                modificoTipoParte = true;
                labelTipoParte.setVisible(true);
                botonAlta.setVisible(false);
                ((JTextField) desde.getDateEditor().getUiComponent()).setText("");
                ((JTextField) hasta.getDateEditor().getUiComponent()).setText("");

                //sino, se colocan las actuales    
            } else if(modificar){
                modificoTipoParte = false;
                labelTipoParte.setVisible(false);
                botonAlta.setVisible(true);
                ((JTextField) desde.getDateEditor().getUiComponent()).setText(flagDesde);
                ((JTextField) hasta.getDateEditor().getUiComponent()).setText(flagHasta);

            }
            //----
            //se cambia el campo observacion si se cambia el tipo de parte
            if (tipoParte.getSelectedIndex() == 0) {
                observaciones.setText("Lic. por Enfermedad");
                sugerenciaDiag.setVisible(false);
            }
            if (tipoParte.getSelectedIndex() == 1) {
                observaciones.setText("Tareas Adm");
                sugerenciaDiag.setVisible(false);
            }
            if (tipoParte.getSelectedIndex() == 2) {
                observaciones.setText("Lic. por Maternidad");
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

        diagnostico.setText("");
        observaciones.setText("");
        cie.setText("");
        ((JTextField) desde.getDateEditor().getUiComponent()).setText("");
        ((JTextField) hasta.getDateEditor().getUiComponent()).setText("");
        tipoParte.setSelectedIndex(0);
        labelTipoParte.setVisible(false);

        dispose();
        System.gc();
    }

    //------------------------METODOS PARA NUEVO PARTE--------------------------
    public void nuevoParte(Formulario formulario) {
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

        Receptor receptor = new Receptor(this.idParte);
        receptor.getInformacion(this);

        flagTipoParte = tipoParte.getSelectedIndex();
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
        boolean campoDiag = diagnostico.getText().equals("");
        boolean campoObs = observaciones.getText().equals("");
        boolean campoDesde = ((JTextField) desde.getDateEditor().getUiComponent()).getText().equals("");
        boolean campoHasta = ((JTextField) hasta.getDateEditor().getUiComponent()).getText().equals("");
        if (campoDiag || campoObs || campoDesde || campoHasta) {
            labelDiagnostico.setForeground(campoDiag ? Color.red : Color.black);
            labelObservaciones.setForeground(campoObs ? Color.red : Color.black);
            labelDesde.setForeground(campoDesde ? Color.red : Color.black);
            labelHasta.setForeground(campoHasta ? Color.red : Color.black);
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos obligatorios");
            return false;
        }
        Fechas validar = new Fechas("dd/MM/yyyy");
        String fechaDesde = ((JTextField) desde.getDateEditor().getUiComponent()).getText();
        String fechaHasta = ((JTextField) hasta.getDateEditor().getUiComponent()).getText();
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
            if (!validar.fechaParteValida(fechaDesde,fechaHasta,flagDesde)) {
                return false;
            }
        } else {
            if (!validar.fechaParteValida(fechaDesde, fechaHasta)){
                return false;
            }
        }
        //validando que no se le agrege parte de maternidad a personal M
        if (personal.getSexo() == 'M') {
            if (tipoParte.getSelectedIndex() == 2) {
                JOptionPane.showMessageDialog(null, "No puede agregar parte de \"Maternidad\" a un Personal Masculino.");
                return false;
            }
        }

        validar = null;
        return true;
    }

    //----------------------SETTER Y GETTERS------------------------------------
    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public void setParte(Parte parte) {
        this.parte = parte;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public Personal getPersonal() {
        return personal;
    }

    public JComboBox getTipoParte() {
        return tipoParte;
    }

    public JComboBox getNorasSiras() {
        return comboNorasSiras;
    }

    public JTextField getDiagnostico() {
        return diagnostico;
    }

    public JTextField getObservaciones() {
        return observaciones;
    }

    public JTextField getCie() {
        return cie;
    }

    public JDateChooser getDesde() {
        return desde;
    }

    public String getFlagDesde() {
        return flagDesde;
    }

    public void setFlagDesde(String flagDesde) {
        this.flagDesde = flagDesde;
    }

    public String getFlagHasta() {
        return flagHasta;
    }

    public void setFlagHasta(String flagHasta) {
        this.flagHasta = flagHasta;
    }

    public JDateChooser getHasta() {
        return hasta;
    }

    public void setFlagTipoParte(int flagTipoParte) {
        this.flagTipoParte = flagTipoParte;
    }

}
