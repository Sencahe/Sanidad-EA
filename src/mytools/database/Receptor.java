
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
        String[][] mensajero = new String[4][];
        String[] mensajeroText = new String[Main.arreglo.getTextFieldLength()];
        String[] mensajeroCombo = new String[Main.arreglo.getComboBoxLength()];
        String[] mensajeroDate = new String[Main.arreglo.getDateChooserLength()];
        String[] mensajeroCheck = new String[Main.arreglo.getCheckBoxLength()];
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("select * from Personal where id = " + this.id);
            ResultSet rs = pst.executeQuery();
            //SOLICITO LOS DATOS QUE VAN A LOS TEXTFIELD
            for (int i = 0; i < mensajeroText.length; i++) {
                mensajeroText[i] = rs.getString(Main.arreglo.textField()[i]);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS COMBO BOX
            for (int i = 0; i < mensajeroCombo.length; i++) {
                mensajeroCombo[i] = rs.getString(Main.arreglo.comboBox()[i]);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS DATE CHOOSER
            for (int i = 0; i < mensajeroDate.length; i++) {
                mensajeroDate[i] = rs.getString(Main.arreglo.dateChooser()[i]);
            }
            for (int i = 0; i < mensajeroCheck.length; i++) {
                mensajeroCheck[i] = rs.getString(Main.arreglo.checkBox()[i]);
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

        System.gc();
        return mensajero;
    }
    
}
