package mytools;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Utilidades {
    
    private final Color colorTabla, colorFondo,transparencia;
    
    private final Font fuentePestañas, fuenteTabla, fuenteBoton, fuenteLabelsResumen, fuenteHeader;
    
    private final Font fuenteLabelsFormulario,fuenteTextFields,fuenteHolders,fuenteChecks,fuenteLabelGrande;
    
    private final Font fuenteLabelBuscador, fuenteLabelResultado;
    
    private final Font fuenteLabelsRef;
    
    private final Font fuenteLabelTitulo;
    
    public Utilidades(){
        this.colorTabla = new Color(255,255,255);
        this.transparencia = new Color(255,255,255,1);
        this.colorFondo = new Color(20,170,20);
        this.fuentePestañas = new Font ("Tahoma", 1, 16);
        this.fuenteTabla = new Font("Tahoma", 0, 13);
        this.fuenteBoton = new Font("Tahoma", 1, 13);
        this.fuenteLabelsResumen = new Font("Tahoma", 1, 14);
        this.fuenteHeader = new Font("Tahoma", 1, 12);
        this.fuenteLabelsFormulario = fuenteHeader;
        this.fuenteTextFields = fuenteTabla;
        this.fuenteHolders = new Font("Tahoma", 2, 10);
        this.fuenteChecks = fuenteHeader;
        this.fuenteLabelGrande = fuenteLabelsResumen;       
        this.fuenteLabelBuscador = new Font("Tahoma", 1, 11);
        this.fuenteLabelResultado = new Font("Tahoma", 0, 11);
        this.fuenteLabelsRef = fuenteBoton;
        this.fuenteLabelTitulo = new Font("Times New Roman",1,20);
    }
    
    //COLORES      
    //tabla
    public Color getColorTabla(){       
        return colorTabla;
    }
    //transparencia
    public Color getTransparencia(){
        return transparencia;
    }
    public Color getColorFondo(){
        return colorFondo;
    }
    //FUENTES
    //tabla
    public Font getFuentePestañas(){
        return fuentePestañas;
    }
    public Font getFuenteTabla(){
        return fuenteTabla;
    }
    public Font getFuenteBoton(){
        return fuenteBoton;
    }
    public Font getFuenteLabelsResumen(){
        return fuenteLabelsResumen;
    }
    public Font getFuenteHeader(){
        return fuenteHeader;
    }
    //formulario
    public Font getFuenteLabelsFormulario(){
        return fuenteLabelsFormulario;
    }
    public Font getFuenteTextFields(){
        return fuenteTextFields;
    }
    public Font getFuenteHolders(){
        return fuenteHolders;
    }
    public Font getFuenteChecks(){
        return fuenteChecks;
    }
    public Font getFuenteLabelGrande(){
        return fuenteLabelGrande;
    }
    public Font getFuenteLabelTitulo(){
        return fuenteLabelTitulo;
    }
    //FORMATO FECHA
    public String getFormatoFecha(){
        return "dd/MM/yyyy";
    }
    //Buscador
    public Font getFuenteMsgBuscador(){
        return fuenteLabelBuscador;
    }
    public Font getFuenteRsltBuscador(){
        return fuenteLabelResultado;
    }
    //Referencias
    public Font getFuenteLabelsRef(){
        return fuenteLabelsRef;
    }  
    //OTROS
     public KeyAdapter bloquearLetras = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            char caracter = e.getKeyChar();
            if (((caracter < '0') || (caracter > '9')) && (caracter != '.') && (caracter != ',')) {
                e.consume();
            }
            if (caracter == ',') {
                e.setKeyChar('.');
            }
        }
    };
}
