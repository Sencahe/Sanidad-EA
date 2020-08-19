package database;

import mytools.Arreglos;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import javax.swing.JTextField;
import windows.recuento.Recuento;
import personal.Personal;
import windows.Formulario;
import windows.parte.FormularioParte;

public class Receptor extends BaseDeDatos {

    private int id;

    public Receptor(int id) {
        this.id = id;

    }
    public Receptor(){
        this.id = 0;
    }

    public void getInformacion(Formulario formulario) {

        String[] text = Arreglos.getTextField();
        String[] combo = Arreglos.getComboBox();
        String[] date = Arreglos.getDateChooser();
        String[] check = Arreglos.getCheckBox();

        String receptor;

        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT * FROM Personal WHERE id = " + this.id);
            ResultSet rs = pst.executeQuery();
            //SOLICITO LOS DATOS QUE VAN A LOS TEXTFIELD
            for (int i = 0; i < text.length; i++) {
                receptor = rs.getString(text[i]);
                formulario.getTextField(i).setText(receptor != null ? receptor : "");
            }
            //SOLICITO LOS DATOS QUE VAN A LOS COMBO BOX
            for (int i = 0; i < combo.length; i++) {
                if (i < 2) {
                    formulario.getComboBox(i).setSelectedIndex(rs.getInt(combo[i]));
                } else {
                    receptor = rs.getString(combo[i]);
                    formulario.getComboBox(i).setSelectedItem(receptor != null ? receptor : "");
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
            boolean enabled = formulario.getCheckBox(4).isSelected() || formulario.getCheckBox(5).isSelected();
            formulario.getTextField(9).setEnabled(enabled);
            //SOLICITO LOS VALORES DE LOS RADIOBUTTON
            if (rs.getString("Sexo").equals("M")) {
                formulario.getM().setSelected(true);
            } else {
                formulario.getF().setSelected(true);
            }
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

    public void getInformacion(FormularioParte formParte) {
        try {
            // Recupero los datos del parte de enfermo para su formulario
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT id_personal, Categoria, Grado, Apellido,"
                    + " Nombre, Destino, Sexo, DNI, TipoParte, NorasSiras, Diagnostico,Observacion, "
                    + "CIE, Desde, Hasta FROM Parte INNER JOIN Personal ON Parte.id_personal = Personal.id "
                    + "WHERE Parte.id = " + this.id);
            ResultSet rs = pst.executeQuery();

            //recupero las fechas
            String desde = rs.getString("Desde");
            String hasta = rs.getString("Hasta");
            formParte.setFlagDesde(desde);
            formParte.setFlagHasta(hasta);
            ((JTextField) formParte.getDesde().getDateEditor().getUiComponent()).setText(desde);
            ((JTextField) formParte.getHasta().getDateEditor().getUiComponent()).setText(hasta);
            //recupero el resto de la informacion
            formParte.getTipoParte().setSelectedIndex(rs.getInt("TipoParte"));
            formParte.getNorasSiras().setSelectedItem(rs.getString("NorasSiras"));
            formParte.getDiagnostico().setText(rs.getString("Diagnostico"));
            formParte.getObservaciones().setText(rs.getString("Observacion"));
            formParte.getCie().setText(rs.getString("CIE") != null ? rs.getString("CIE") : "");
            
            //utilizo los datos de personal para crear el objeto Personal
            int id_personal = rs.getInt("id_personal");
            int categoria = rs.getInt("Categoria");
            int grado = rs.getInt("Grado");
            String nombre = rs.getString("Apellido") + " " + rs.getString("Nombre");
            String destino = rs.getString("Destino") != null ? rs.getString("Destino") : "";
            char sexo = rs.getString("Sexo").charAt(0);
            int dni = rs.getInt("DNI");
            //creo el objeto personal en la clase formularioParte
            formParte.setPersonal(new Personal(id_personal, categoria, grado, nombre, destino, sexo, dni));

            super.getConnection().close();

            //FIN DEL METODO
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//getInformacion " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }
    
    
    public void getInformacion(Recuento recuento, int dni){
        try {
            recuento.getTableModel().setRowCount(0);
            
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT * FROM RecuentoParte "
                    + "WHERE DNI = " + dni);
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){          
                int num = 0;
                Object[] fila = new Object[recuento.getTabla().getColumnCount()];
                do{
                    fila[0] = ++num;
                    fila[1] = rs.getString("Grado");
                    fila[2] = rs.getString("NombreCompleto");
                    fila[3] = rs.getString("Destino");
                    fila[4] = rs.getString("DNI");
                    fila[5] = rs.getString("Diagnostico");
                    fila[6] = rs.getString("CIE");
                    fila[7] = rs.getString("Desde");
                    fila[8] = rs.getString("Hasta");
                    fila[9] = rs.getString("Dias");
                    fila[10] = rs.getString("Observacion");
                    fila[11] = rs.getString("TipoParte");
                    fila[12] = rs.getString("NorasSiras");
                    
                    recuento.getTableModel().addRow(fila);
                    Arrays.fill(fila,null);
                    
                } while(rs.next());   
                
                recuento.actualizarVentana();
                
            } else {
                JOptionPane.showMessageDialog(null, "No han habido resultados con ese numero de DNI.");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Receptor// " + e  
                    + "\nContactese con el desarrollador del programa para solucionar el problema.");
        }
    }

}
