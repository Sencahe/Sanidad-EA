
package mytools.database;

import java.sql.PreparedStatement;
import javax.swing.JOptionPane;


public class Eliminador extends BaseDeDatos{
    
    private int id;
    
        public Eliminador(int id){
            this.id = id;
        }
        
        public void Eliminar() {
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("delete from Personal where ID = " + this.id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Personal eliminado del registro con exito.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Eliminar//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        super.Actualizar();
    }
    
}
