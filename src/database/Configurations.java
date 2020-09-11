package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import dialogs.Configurator;
import mytools.Encryption;

public class Configurations extends DataBase {

    Configurator configurator;

    public Configurations() {

    }

    public void setValues(Configurator configuracion) {
        this.configurator = configuracion;

        try {
            PreparedStatement pst = super.getConnection().prepareStatement("UPDATE Configuracion SET Leyenda = ? WHERE id = 1");

            pst.setString(1, configurator.getTextLeyend().getText().toUpperCase());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se han guardado los cambios.");

            super.getConnection().close();

        } catch (Exception e) {
        }

    }

    public void getSavedValues(Configurator configuracion) {
        this.configurator = configuracion;
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT Leyenda FROM Configuracion"
                    + " WHERE id = 1");

            ResultSet rs = pst.executeQuery();

            configurator.setFlagLeyend(rs.getString("Leyenda"));

            configurator.restore();

            super.getConnection().close();

        } catch (Exception e) {
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
            System.out.println(e);
            return false;
        }
    }

}
