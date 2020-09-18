package database;

import mytools.MyArrays;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import panels.ReCountPanel;
import mytools.MyDates;
import dialogs.PersonnelFormulary;
import dialogs.SickFormulary;
import personnel.Personnel;


public class Receiver extends DataBase {

    private int id;

    public Receiver(int id) {
        this.id = id;

    }

    public Receiver() {
        this.id = 0;
    }

    public void obtainInformation(PersonnelFormulary sickFormulary) {

        String[] text = MyArrays.getTextField();
        String[] combo = MyArrays.getComboBox();
        String[] date = MyArrays.getDateChooser();
        String[] check = MyArrays.getCheckBox();

        String receiver;

        MyDates myDates = new MyDates(MyDates.USER_DATE_FORMAT);

        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT * FROM Personal WHERE id = " + this.id);
            ResultSet rs = pst.executeQuery();
            //SOLICITO LOS DATOS QUE VAN A LOS TEXTFIELD
            for (int i = 0; i < text.length; i++) {
                receiver = rs.getString(text[i]);
                sickFormulary.getTextField(i).setText(receiver != null ? receiver : "");
            }
            //SOLICITO LOS DATOS QUE VAN A LOS COMBO BOX
            for (int i = 0; i < combo.length; i++) {
                if (i < 2) {
                    sickFormulary.getComboBox(i).setSelectedIndex(rs.getInt(combo[i]));
                } else {
                    receiver = rs.getString(combo[i]);
                    sickFormulary.getComboBox(i).setSelectedItem(receiver != null ? receiver : "");
                }
            }
            //SOLICITO LOS DATOS QUE VAN A LOS DATE CHOOSER
            for (int i = 0; i < date.length; i++) {
                receiver = rs.getString(date[i]);
                if (receiver != null) {
                    sickFormulary.getDateChooser(i).setDate(myDates.toDate(receiver));
                }
            }
            //SOLICITO LOS DATOS QUE VAN A LOS CHECK BOX
            for (int i = 0; i < check.length; i++) {
                sickFormulary.getCheckBox(i).setSelected((rs.getString(check[i]) != null));
            }
            boolean enabled = sickFormulary.getCheckBox(4).isSelected() || sickFormulary.getCheckBox(5).isSelected();
            sickFormulary.getTextField(9).setEnabled(enabled);
            //SOLICITO LOS VALORES DE LOS RADIOBUTTON
            if (rs.getString("Sexo").equals("M")) {
                sickFormulary.getM().setSelected(true);
            } else {
                sickFormulary.getF().setSelected(true);
            }
            //SOLICITO VALORES FLAG 
            sickFormulary.setSick(1 == rs.getInt("Parte"));

            //fin de la solicitud a la base de datos
            super.getConnection().close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//getInformacion " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }

        myDates = null;
        super.nullConnection();

    }

    public void obtainInformation(SickFormulary sickFormulary) {
       
        try {
            // Recupero los datos del parte de enfermo para su formulario
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT id_personal, Categoria, Grado, Arma,"
                    + "Apellido, Nombre, Destino, Sexo, DNI, TipoParte, NorasSiras, Diagnostico,Observacion, "
                    + "CIE, Desde, Hasta FROM Parte INNER JOIN Personal ON Parte.id_personal = Personal.id "
                    + "WHERE Parte.id = ?");
            pst.setInt(1,this.id);
            ResultSet rs = pst.executeQuery();

            MyDates myDates = new MyDates(MyDates.USER_DATE_FORMAT);
            //recupero las fechas
            String since = rs.getString("Desde");
            String until = rs.getString("Hasta");

            sickFormulary.setFlagSince(myDates.toDate(since));
            sickFormulary.setFlagUntil(myDates.toDate(until));
            sickFormulary.getDateSince().setDate(myDates.toDate(since));
            sickFormulary.getDateUntil().setDate(myDates.toDate(until));
            //recupero el resto de la informacion
            sickFormulary.getComboSickType().setSelectedIndex(rs.getInt("TipoParte"));
            sickFormulary.getNorasSiras().setSelectedItem(rs.getString("NorasSiras"));
            sickFormulary.getTextDiag().setText(rs.getString("Diagnostico"));
            sickFormulary.getTextObs().setText(rs.getString("Observacion"));
            sickFormulary.getTextCIE().setText(rs.getString("CIE") != null ? rs.getString("CIE") : "");

            //utilizo los datos de personal para crear el objeto Personal
            int id_personnel = rs.getInt("id_personal");
            int categorie = rs.getInt("Categoria");
            int grade = rs.getInt("Grado");
            String esp = rs.getString("Arma") != null ? rs.getString("Arma") : "";
            String name = rs.getString("Apellido") + " " + rs.getString("Nombre");
            String subUnity = rs.getString("Destino") != null ? rs.getString("Destino") : "";
            char genre = rs.getString("Sexo").charAt(0);
            int dni = rs.getInt("DNI");
            
            //creo el objeto personal
             sickFormulary.setPersonnel(new Personnel(id_personnel,categorie,grade,esp,name,subUnity,genre,dni));
            
            myDates = null;
            super.getConnection().close();

            //FIN DEL METODO
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//getInformacion " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

    public boolean obtainInformation(ReCountPanel reCount, String statement) {

        reCount.getTableModel().setRowCount(0);
        statement += " ORDER BY (SUBSTR(Alta,1,4)||SUBSTR(Alta,6,2)||SUBSTR(Alta,9,2)) ";
        statement += reCount.getRadioAsc().isSelected() ? "ASC" : "DESC";
        try {
            PreparedStatement pst = super.getConnection().prepareStatement(statement);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int num = 0;
                Object[] row = new Object[reCount.getTable().getColumnCount()];
                do {
                    row[0] = ++num;
                    row[1] = rs.getString("Grado");
                    row[2] = rs.getString("NombreCompleto");
                    row[3] = rs.getString("Destino");
                    row[4] = rs.getString("DNI");
                    row[5] = rs.getString("Diagnostico");
                    row[6] = rs.getString("CIE");
                    row[7] = rs.getString("Desde");
                    row[8] = rs.getString("Hasta");
                    row[9] = rs.getString("Dias");
                    row[10] = rs.getString("Observacion");
                    row[11] = rs.getString("TipoParte");
                    row[12] = rs.getString("NorasSiras");

                    reCount.getTableModel().addRow(row);
                    Arrays.fill(row, null);

                } while (rs.next());

                reCount.updateWindow();

                return true;
            } else {
                reCount.updateWindow();
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Receptor// " + e
                    + "\nContactese con el desarrollador del programa para solucionar el problema.");
            return false;
        }

    }

}
