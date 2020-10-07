package database;

import dialogs.Advanced;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import dialogs.Configurator;
import mytools.Encryption;
import org.json.JSONArray;

public class Configurations extends DataBase {

    Configurator configurator;

    public Configurations() {

    }

    public void setValues(Configurator configuracion) {
        this.configurator = configuracion;

        try {
            PreparedStatement pst = super.getConnection().prepareStatement("UPDATE Configuracion SET Leyenda = ?, Unidad = ? WHERE id = 1");

            pst.setString(1, configurator.getTextLeyend().getText().toUpperCase().trim());
            pst.setString(2, configurator.getTextUnity().getText().trim());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se han guardado los cambios.");

            super.getConnection().close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void getSavedValues(Configurator configuracion) {
        this.configurator = configuracion;
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT Leyenda, Unidad FROM Configuracion"
                    + " WHERE id = 1");

            ResultSet rs = pst.executeQuery();

            configurator.setFlagLeyend(rs.getString("Leyenda"));
            configurator.setFlagUnity(rs.getString("Unidad"));

            configurator.restore();

            super.getConnection().close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setPassword(Configurator configuracion) {
        this.configurator = configuracion;
        try {

            //Encriptando la password
            String newPassword = String.valueOf(configuracion.getTextNewPass1().getPassword());

            String salt = Encryption.getSalt(30);
            String encryptedPassword = Encryption.generateSecurePassword(newPassword, salt);

            //enviandola
            PreparedStatement pst = super.getConnection().prepareStatement("UPDATE Configuracion SET Password = ? , Salt = ?"
                    + "WHERE id = 1");

            pst.setString(1, encryptedPassword);
            pst.setString(2, salt);

            pst.executeUpdate();

            configuracion.setFlagPassword(newPassword);

            JOptionPane.showMessageDialog(null, "Se ha cambiado la contraseña.");
            super.getConnection().close();

        } catch (Exception e) {
        }

    }

    public boolean getPassword(String inputPass) {
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT Password, Salt FROM Configuracion"
                    + " WHERE id = 1");

            ResultSet rs = pst.executeQuery();

            String pass = rs.getString(1);
            String salt = rs.getString(2);

            super.getConnection().close();

            //DECRIPTANDO LA PASSWORD
            boolean passwordMatch = Encryption.verifyUserPassword(inputPass, pass, salt);
            if (passwordMatch) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña Incorrecta");

                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public String[] getSubUnitiesArray(boolean userInterface) {
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT Destinos_Json FROM Configuracion"
                    + " WHERE id = 1");
            ResultSet rs = pst.executeQuery();
            
            JSONArray jsonA = new JSONArray(rs.getString(1));
            
            if(!userInterface){
                jsonA.remove(0);
            }
                 
            String[] subUnities = new String[jsonA.length()];
            for (int i = 0; i < subUnities.length; i++) {
                subUnities[i] = jsonA.getString(i);
            }
            super.getConnection().close();
            return subUnities;

        } catch (Exception e) {
            return null;
        }
    }

    public void setSubUnitiesArray(Advanced advanced) {

        int iterations = advanced.getComboSubUnities().getSelectedIndex() + 1;
        
        JSONArray jsonA = new JSONArray();
        jsonA.put("");
        for (int i = 0; i < iterations; i++) {
            jsonA.put(advanced.getTextSubUnities(i).getText());
        }
        
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("UPDATE Configuracion SET Destinos_json = ?");
            pst.setString(1, jsonA.toString());
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Se han modificado los Destinos.");
            
        } catch (Exception e) {
        }
    }
}
