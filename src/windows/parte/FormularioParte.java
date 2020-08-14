package windows.parte;

import com.toedter.calendar.JDateChooser;
import database.Emisor;
import database.Receptor;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mytools.Fechas;
import mytools.Iconos;
import mytools.Utilidades;
import personal.Personal;
import windows.Formulario;

public class FormularioParte extends JDialog implements ActionListener {

    private JLabel informacion;
    private JLabel labelDiagnostico, labelObservaciones, labelDesde, labelHasta, labelCie,
            labelTipoParte, labelNorasSiras;
    private JComboBox tipoParte, comboNorasSiras;
    private JTextField diagnostico, observaciones, cie;
    private JDateChooser desde, hasta;
    private JButton botonAgregar, botonModificar, botonAlta;

    private int idParte;
    private boolean modificar;
    private int flagTipoParte;

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
        setSize(450, 315);
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
        informacion.setBounds(15, 5, 435, 80);
        informacion.setFont(utilidad.getFuenteLabelGrande());
        informacion.setForeground(Color.black);
        container.add(informacion);

        //combobox
        tipoParte = new JComboBox();
        tipoParte.addItem("Reposo");
        tipoParte.addItem("Exceptuado");
        tipoParte.addItem("Maternidad");
        tipoParte.setBounds(15, 90, 160, 20);
        tipoParte.addActionListener(this);
        container.add(tipoParte);
        labelTipoParte = new JLabel("<html>Al modificar el tipo de parte se enviara al recuento la "
                + "informacion previamente guardada y se creara un nuevo parte. "
                + "Se sugiere modificar el campo 'Desde' a la fecha del dia presente.<html>");
        labelTipoParte.setBounds(180, 60, 250, 60);
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
        labelDesde.setBounds(15, 125, 200, 20);
        labelDesde.setFont(utilidad.getFuenteLabelsFormulario());
        labelDesde.setForeground(Color.black);
        container.add(labelDesde);
        desde = new JDateChooser();
        desde.setBounds(15, 145, 100, 20);
        desde.setForeground(Color.black);
        desde.setFont(utilidad.getFuenteTextFields());
        desde.setDateFormatString(utilidad.getFormatoFecha());
        container.add(desde);
        labelHasta = new JLabel("Hasta *");
        labelHasta.setBounds(130, 125, 200, 20);
        labelHasta.setFont(utilidad.getFuenteLabelsFormulario());
        labelHasta.setForeground(Color.black);
        container.add(labelHasta);
        hasta = new JDateChooser();
        hasta.setBounds(130, 145, 100, 20);
        hasta.setForeground(Color.black);
        hasta.setFont(utilidad.getFuenteTextFields());
        hasta.setDateFormatString(utilidad.getFormatoFecha());
        container.add(hasta);

        //textfield
        labelObservaciones = new JLabel("Observaciones *");
        labelObservaciones.setBounds(240, 125, 150, 20);
        labelObservaciones.setFont(utilidad.getFuenteLabelsFormulario());
        labelObservaciones.setForeground(Color.black);
        container.add(labelObservaciones);
        observaciones = new JTextField();
        observaciones.setBounds(240, 145, 170, 20);
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
        cie.setBounds(240, 195, 150, 20);
        cie.setFont(utilidad.getFuenteTextFields());
        container.add(cie);

        //Buttons
        botonAgregar = new JButton("Agregar");
        botonAgregar.setBounds(15, 235, 90, 30);
        botonAgregar.addActionListener(this);
        container.add(botonAgregar);
        botonModificar = new JButton("Modificar");
        botonModificar.setBounds(15, 235, 90, 30);
        botonModificar.addActionListener(this);
        botonModificar.setVisible(false);
        container.add(botonModificar);
        botonAlta = new JButton("Alta");
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonAgregar) {
            if (validar()) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea agregar un nuevo Parte?",
                        "Confirmacion", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    Emisor emisor = new Emisor(personal.getId(), 0);
                    emisor.setInformacion(this);
                    emisor.actualizar(parte);
                    parte.actualizarVentana();
                    formulario.setParteDeEnfermo(true);
                    JOptionPane.showMessageDialog(null, "Nuevo parte de enfermo agregado con exito.");
                    vaciar();
                }
            }
        }
        if (e.getSource() == botonModificar) {
            if (validar()) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea modificar el Parte?",
                        "Confirmacion", JOptionPane.YES_NO_OPTION);
                 if (opcion == JOptionPane.YES_OPTION) {
                 Emisor emisor = new Emisor(personal.getId(), this.idParte);
                if (flagTipoParte == tipoParte.getSelectedIndex()) {
                    emisor.setInformacion(this);
                } else {
                    emisor.altaParcial(this);
                }
                JOptionPane.showMessageDialog(null, "Parte modificado con exito.");
                dispose();
                emisor.actualizar(parte);
                parte.actualizarVentana();
                vaciar();
                emisor = null;
                 }                
            }
        }

        if (e.getSource() == botonAlta) {

        }

        if (e.getSource() == tipoParte) {
            if (modificar && flagTipoParte != tipoParte.getSelectedIndex()) {
                labelTipoParte.setVisible(true);
            } else {
                labelTipoParte.setVisible(false);
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

    public JDateChooser getHasta() {
        return hasta;
    }

    public void setFlagTipoParte(int flagTipoParte) {
        this.flagTipoParte = flagTipoParte;
    }

}
