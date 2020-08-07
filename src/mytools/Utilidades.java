package mytools;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Utilidades {
    
    private final Color colorTabla, transparencia;
    
    private final Font fuentePestañas, fuenteTabla, fuenteBoton, fuenteLabelsResumen, fuenteHeader;
    
    private final Font fuenteLabelsFormulario,fuenteTextFields,fuenteHolders,fuenteChecks,fuenteLabelGrande;
    
    private final Font fuenteLabelBuscador, fuenteLabelResultado;
    
    private final Font fuenteLabelsRef;
    
    public Utilidades(){
        this.colorTabla = new Color(200,250,150);
        this.transparencia = new Color(255,255,255,1);
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
    }
    
    //COLORES      
    //tabla
    public Color colorTabla(){       
        return colorTabla;
    }
    //transparencia
    public Color transparencia(){
        return transparencia;
    }
    //FUENTES
    //tabla
    public Font fuentePestañas(){
        return fuentePestañas;
    }
    public Font fuenteTabla(){
        return fuenteTabla;
    }
    public Font fuenteBoton(){
        return fuenteBoton;
    }
    public Font fuenteLabelsResumen(){
        return fuenteLabelsResumen;
    }
    public Font fuenteHeader(){
        return fuenteHeader;
    }
    //formulario
    public Font fuenteLabelsFormulario(){
        return fuenteLabelsFormulario;
    }
    public Font fuenteTextFields(){
        return fuenteTextFields;
    }
    public Font fuenteHolders(){
        return fuenteHolders;
    }
    public Font fuenteChecks(){
        return fuenteChecks;
    }
    public Font fuenteLabelGrande(){
        return fuenteLabelGrande;
    }
    //FORMATO FECHA
    public String formatoFecha(){
        return "dd/MM/yyyy";
    }
    //Buscador
    public Font fuenteMsgBuscador(){
        return fuenteLabelBuscador;
    }
    public Font fuenteRsltBuscador(){
        return fuenteLabelResultado;
    }
    //Referencias
    public Font fuenteLabelsRef(){
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
            System.gc();
        }
    };
}
