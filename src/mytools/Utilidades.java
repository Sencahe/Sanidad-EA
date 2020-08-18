package mytools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Utilidades {
    
    private final Color colorTabla, colorFondo, colorBoton, colorFuenteBoton;
    
    private final Font fuentePestañas, fuenteTabla, fuenteBoton, fuenteLabelsResumen, fuenteHeader;
    
    private final Font fuenteLabelsFormulario,fuenteTextFields,fuenteHolders,fuenteChecks,fuenteLabelGrande;
    
    private final Font fuenteLabelBuscador, fuenteLabelResultado;
    
    private final Font fuenteLabelsRef;
    
    private final Font fuenteLabelTitulo;
    
    private final Font fuenteLabelInfo;
    
    private final Cursor pointCursor;
    
    public Utilidades(){
        this.colorTabla = new Color(255,255,255);
        this.colorFondo = new Color (0,100,220);      
        this.colorBoton = new Color(0,70,170);
        this.colorFuenteBoton = new Color(240,240,240);
        this.fuentePestañas = new Font ("Tahoma", 1, 16);
        this.fuenteTabla = new Font("Arial", 0, 13);
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
        this.fuenteLabelInfo = fuenteLabelResultado;
        this.fuenteLabelsRef = fuenteBoton;
        this.fuenteLabelTitulo = new Font("Impact",0,20);
        this.pointCursor = new Cursor(Cursor.HAND_CURSOR);
    }
    
    //COLORES      
    //tabla
    public Color getColorTabla(){       
        return colorTabla;
    }

    public Color getColorFondo(){
        return colorFondo;
    }

    public Color getColorBoton() {
        return colorBoton;
    }

    public Color getColorFuenteBoton() {
        return colorFuenteBoton;
    }
    //CURSOR

    public Cursor getPointCursor() {
        return pointCursor;
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

    public Font getFuenteLabelInfo() {
        return fuenteLabelInfo;
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
    public KeyAdapter soloNumeros = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            char caracter = e.getKeyChar();
            if (((caracter < '0') || (caracter > '9')) ) {
                e.consume();
            }
        }
    };
}
