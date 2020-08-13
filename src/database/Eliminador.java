package database;

import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import personal.Personal;

public class Eliminador extends BaseDeDatos {

    private int id;

    public Eliminador(int id) {
        this.id = id;
    }    

    public void eliminar() {
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("delete from Personal where ID = " + this.id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Personal eliminado del registro con exito.");

            super.getConnection().close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//Eliminador//Eliminar//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

    public void alta(int idParte, Personal personal) {
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("delete from Parte where ID = " + idParte);
            pst.executeUpdate();
            
            pst = super.getConnection().prepareStatement("update from Personal set Parte = 0 where id = " + this.id);
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(null, personal.getNombreCompleto() + " se ha dado de Alta Medica.");
            
            super.getConnection().close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//Eliminar//Alta//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

}
