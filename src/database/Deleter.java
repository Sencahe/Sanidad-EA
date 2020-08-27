package database;

import java.sql.PreparedStatement;
import javax.swing.JOptionPane;


public class Deleter extends DataBase {

    private int id;
    
    
    public Deleter(int id) {
        this.id = id;
    }    

    public void delete(String tableNameDB) {
        try {
                                    
            PreparedStatement pst = super.getConnection().prepareStatement("DELETE FROM " + tableNameDB + " where ID = " + this.id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Personal eliminado del registro con exito.");

            super.getConnection().close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//Eliminador//Eliminar//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

 

}
