package database;

import java.sql.PreparedStatement;
import javax.swing.JOptionPane;


public class Eliminador extends BaseDeDatos {

    private int id;
    
    
    public Eliminador(int id) {
        this.id = id;
    }    

    public void eliminar(String tablaBDD) {
        try {
                                    
            PreparedStatement pst = super.getConnection().prepareStatement("DELETE FROM " + tablaBDD + " where ID = " + this.id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Personal eliminado del registro con exito.");

            super.getConnection().close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//Eliminador//Eliminar//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

 

}
