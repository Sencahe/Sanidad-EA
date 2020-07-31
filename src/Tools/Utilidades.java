
package Tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Utilidades {
    
    //COLORES
    //tabla
    public Color colorTabla(){
        return new Color(200,250,150);
    }
    //transparencia
    public Color transparencia(){
        return new Color(255,255,255,1);
    }
    //FUENTES
    //tabla
    public Font fuentePestañas(){
        return new Font("Tahoma", 1, 16);
    }
    public Font fuenteTabla(){
        return new Font("Tahoma", 0, 13);
    }
    public Font fuenteBoton(){
        return new Font("Tahoma", 1, 13);
    }
    public Font fuenteLabelsResumen(){
        return new Font("Tahoma", 1, 14);
    }
    //formulario
    public Font fuenteLabelsFormulario(){
        return new Font("Tahoma", 1, 12);
    }
    public Font fuenteTextFields(){
        return new Font("Tahoma", 0, 13);
    }
    public Font fuenteHolders(){
        return new Font("Tahoma", 2, 10);
    }
    public Font fuenteChecks(){
        return new Font("Tahoma", 1, 12);
    }
    public Font fuenteLabelGrande(){
        return new Font("Tahoma", 1, 14);
    }
    //FORMATO FECHA
    public String formatoFecha(){
        return "dd/MM/yyyy";
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
