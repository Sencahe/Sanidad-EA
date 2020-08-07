package windows;

import mytools.Arreglos;
import mytools.Utilidades;



public class Main {
    
    public static Utilidades utilidad = new Utilidades();
    public static Arreglos arreglo = new Arreglos();
    
    public static void main(String[] args) {
        //LOOK AND FEEL
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //ARRANQUE DEL PROGRAMA
        Tabla start = new Tabla();
        utilidad = null;
        System.gc();
        start.setVisible(true);
        
    }
}
