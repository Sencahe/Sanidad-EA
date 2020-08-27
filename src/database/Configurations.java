
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import dialogs.Configurator;

public class Configurations extends DataBase{
    
    Configurator configurator;
    
    public Configurations(Configurator configuracion){
        this.configurator = configuracion;
    }
    
    
    public void setValues(){
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("UPDATE Configuracion SET Leyenda = ? WHERE id = 1");
            
            pst.setString(1,configurator.getTextLeyend().getText().toUpperCase());
            
            pst.executeUpdate();        
            
            JOptionPane.showMessageDialog(null, "Se han guardado los cambios.");
            
            super.getConnection().close();          
            
        } catch (Exception e) {
        }
        
    }
    
    public void getValues(){
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT (Leyenda) FROM Configuracion"
                    + " WHERE id = 1");
            
            ResultSet rs = pst.executeQuery();
            
            configurator.setFlagLeyend(rs.getString("Leyenda"));   
            
            configurator.restore();
            
            super.getConnection().close();
            
        } catch (Exception e) {
        }
    }
}
