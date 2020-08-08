
package mytools.database;

import mytools.Arreglos;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import windows.Main;


public class Receptor extends BaseDeDatos{
    
    private int id;
   
    
    public Receptor(int id){
        this.id = id;
        
    }
    
    public String[][] getInformacion() {
        Arreglos arreglo = new Arreglos();
        String[][] mensajero = new String[4][];
        String[] mensajeroText = new String[arreglo.getTextFieldLength()];
        String[] mensajeroCombo = new String[arreglo.getComboBoxLength()];
        String[] mensajeroDate = new String[arreglo.getDateChooserLength()];
        String[] mensajeroCheck = new String[arreglo.getCheckBoxLength()];
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("select * from Personal where id = " + this.id);
            ResultSet rs = pst.executeQuery();
            //SOLICITO LOS DATOS QUE VAN A LOS TEXTFIELD
            for (int i = 0; i < mensajeroText.length; i++) {
                mensajeroText[i] = rs.getString(arreglo.getTextField()[i]);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS COMBO BOX
            for (int i = 0; i < mensajeroCombo.length; i++) {
                mensajeroCombo[i] = rs.getString(arreglo.getComboBox()[i]);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS DATE CHOOSER
            for (int i = 0; i < mensajeroDate.length; i++) {
                mensajeroDate[i] = rs.getString(arreglo.getDateChooser()[i]);
            }
            for (int i = 0; i < mensajeroCheck.length; i++) {
                mensajeroCheck[i] = rs.getString(arreglo.getCheckBox()[i]);
            }
            
            super.getConnection().close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//getInformacion " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        // ENVIO LOS DATOS A LA CLASE FORMULARIO
        mensajero[0] = mensajeroText;
        mensajero[1] = mensajeroCombo;
        mensajero[2] = mensajeroDate;
        mensajero[3] = mensajeroCheck;
        arreglo = null;
        mensajeroText = null;
        mensajeroCombo = null;
        mensajeroDate = null;
        mensajeroCheck = null;
        super.nullConnection();
        return mensajero;
        
    }
    
}
