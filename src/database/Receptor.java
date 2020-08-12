package database;

import mytools.Arreglos;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTextField;
import windows.Formulario;
import windows.parte.FormularioParte;

public class Receptor extends BaseDeDatos {

    private int id;

    public Receptor(int id) {
        this.id = id;

    }

    public void getInformacion(Formulario formulario) {
        
        String[] text = Arreglos.getTextField();
        String[] combo = Arreglos.getComboBox();
        String[] date = Arreglos.getDateChooser();
        String[] check = Arreglos.getCheckBox();
        
        String receptor;        

        try {
            PreparedStatement pst = super.getConnection().prepareStatement("select * from Personal where id = " + this.id);
            ResultSet rs = pst.executeQuery();            
            //SOLICITO LOS DATOS QUE VAN A LOS TEXTFIELD
            for (int i = 0; i < text.length; i++) {
                receptor = rs.getString(text[i]);
                formulario.getTextField(i).setText(receptor != null ? receptor:"");
            }
            //SOLICITO LOS DATOS QUE VAN A LOS COMBO BOX
            for (int i = 0; i < combo.length; i++) {
                if (i < 2) {
                    formulario.getComboBox(i).setSelectedIndex(rs.getInt(combo[i]));
                } else {
                    receptor = rs.getString(combo[i]);
                    formulario.getComboBox(i).setSelectedItem(receptor != null ? receptor:"");
                }
            }
            //SOLICITO LOS DATOS QUE VAN A LOS DATE CHOOSER
            for (int i = 0; i < date.length; i++) {
                receptor = rs.getString(date[i]);
                ((JTextField) formulario.getDateChooser(i).getDateEditor().getUiComponent()).setText(receptor);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS CHECK BOX
            for (int i = 0; i < check.length; i++) {
                formulario.getCheckBox(i).setSelected((rs.getString(check[i]) != null));
            }            
            boolean enabled  = formulario.getCheckBox(4).isSelected() || formulario.getCheckBox(5).isSelected();       
            formulario.getTextField(9).setEnabled(enabled);
            //SOLICITO VALORES FLAG 
            formulario.setParteDeEnfermo(1 == rs.getInt("Parte"));
            
                       
            //fin de la solicitud a la base de datos
            super.getConnection().close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//getInformacion " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }

        super.nullConnection();
        
    }
    
    public void getInformacion(FormularioParte formParte){
        
    }

}
