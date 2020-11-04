package database;

import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import panels.SickPanel;


public class Deleter extends DataBase {

    private int id;
    private int parentId;
    
    public Deleter(int id) {
        this.id = id;
    }    
    
    public Deleter(int id, int parentId){
        this.id = id;
        this.parentId = parentId;
    }

    public void delete(String tableNameDB) {
        try {
                                    
            PreparedStatement pst = super.getConnection().prepareStatement("DELETE FROM " + tableNameDB + " WHERE id = ?");
            
            pst.setInt(1, this.id);
                    
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Eliminado con exito.");
                                   
            if(tableNameDB.equals(SickPanel.TABLE_NAME)){
                pst = super.getConnection().prepareStatement("UPDATE Personal SET Parte = ? WHERE id = ?");
                pst.setInt( 1 , 0);
                pst.setInt(2 , parentId);
                pst.executeUpdate();
            }
            
            super.getConnection().close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//Eliminador//Eliminar//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

 

}
