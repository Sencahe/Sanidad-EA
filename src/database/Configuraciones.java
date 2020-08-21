
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Configuraciones extends BaseDeDatos{
    
    
    public void setLeyenda(){
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("Update Configuracion set Leyenda = ?");;
            
        } catch (Exception e) {
        }
        
    }
    
    public String getLeyenda(){
        String leyenda = "";
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT (Leyenda) FROM Configuracion"
                    + " WHERE id = 1");;
            ResultSet rs = pst.executeQuery();
            leyenda = rs.getString(leyenda);
            super.getConnection().close();
        } catch (Exception e) {
        }
        return leyenda;
    }
}
