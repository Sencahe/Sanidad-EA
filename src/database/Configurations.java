
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import dialogs.Configurator;

public class Configurations extends DataBase{
    
    Configurator configuracion;
    
    public Configurations(Configurator configuracion){
        this.configuracion = configuracion;
    }
    
    
    public void setValores(){
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("UPDATE Configuracion SET Leyenda = ? WHERE id = 1");
            
            pst.setString(1,configuracion.getTextLeyenda().getText().toUpperCase());
            
            pst.executeUpdate();        
            
            JOptionPane.showMessageDialog(null, "Se han guardado los cambios.");
            
            super.getConnection().close();          
            
        } catch (Exception e) {
        }
        
    }
    
    public void getValores(){
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT (Leyenda) FROM Configuracion"
                    + " WHERE id = 1");
            
            ResultSet rs = pst.executeQuery();
            
            configuracion.setFlagLeyenda(rs.getString("Leyenda"));   
            
            configuracion.restaurar();
            
            super.getConnection().close();
            
        } catch (Exception e) {
        }
    }
}
