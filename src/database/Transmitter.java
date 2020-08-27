package database;

import mytools.MyArrays;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import mytools.MyDates;
import personnel.Personnel;
import dialogs.PersonnelFormulary;
import dialogs.SickFormulary;
import java.time.LocalDate;
import java.util.Date;

public class Transmitter extends DataBase {

    private int id, idSick;

    public Transmitter(int id) {
        this.id = id;
    }

    public Transmitter(int id, int idSick) {
        this.id = id;
        this.idSick = idSick;
    }

    public void sendInformation(PersonnelFormulary personnelFormulary) {
        StringBuffer statement = new StringBuffer();
        
        MyDates myDates = new MyDates(MyDates.USER_DATE_FORMAT);
        
        String[] columns = MyArrays.getPersonnelFormularyDB();
        
        int iteration = columns.length;

        //Modificar registro
        if (id != 0) {
            statement.append("update Personal set ");
            for (int i = 0; i < iteration; i++) {
                statement.append(columns[i]);
                statement.append(i == iteration - 1 ? " = ? " : " = ?, ");
            }
            statement.append("where id = ");
            statement.append(this.id);
            // Aregar nuevo registro
        } else {
            statement.append("insert into Personal (");
            for (int i = 0; i < iteration; i++) {
                statement.append(columns[i]);
                statement.append(i != iteration - 1 ? ", " : ") ");
            }
            statement.append("values(");
            for (int i = 0; i < iteration; i++) {
                statement.append(i != iteration - 1 ? "?," : "?)");
            }
        }

        try {
            PreparedStatement pst = super.getConnection().prepareStatement(statement.toString());

            int index = 1;
            String transmitter;   
            Date date;
            LocalDate localDate;
            int intTransmitter;

            //Se envian los datos de los textField
            for (int i = 0; i < personnelFormulary.getTextFieldLength(); i++) {
                if (i == 0) {
                    transmitter = personnelFormulary.getTextField(i).getText().toUpperCase().trim();
                } else {
                    transmitter = personnelFormulary.getTextField(i).getText().trim();
                }
                pst.setObject(index++, !transmitter.equals("") ? transmitter : null);
            }
            //Se envian los datos de los comboBox
            for (int i = 0; i < personnelFormulary.getComboBoxLength(); i++) {
                if (i < 2) {
                    intTransmitter = personnelFormulary.getComboBox(i).getSelectedIndex();
                    pst.setInt(index++, intTransmitter);

                } else {
                    transmitter = String.valueOf(personnelFormulary.getComboBox(i).getSelectedItem());
                    pst.setObject(index++, !transmitter.equals("") ? transmitter : null);
                }

            }
            //Se envian los datos de los dateChooser
            for (int i = 0; i < personnelFormulary.getDateChooserLength(); i++) {
                date = personnelFormulary.getDateChooser(i).getDate();
                localDate = date != null ? myDates.toLocalDate(date) : null;
                pst.setString(index++, localDate != null ? localDate.toString() : null);
            }
            //Se envian los datos de los checkBox
            for (int i = 0; i < personnelFormulary.getCheckBoxLength(); i++) {
                pst.setString(index++, personnelFormulary.getCheckBox(i).isSelected() ? "X" : null);
            }
            //Se envia los datos de radiobuttons           
            pst.setString(index++, personnelFormulary.getM().isSelected() ? "M" : "F");
            //Se envian los flags
            pst.setInt(index++, personnelFormulary.getParteDeEnfermo() ? 1 : 0);
            
            
            pst.executeUpdate();
            super.getConnection().close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//setInformacion//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }

        columns = null;
        statement = null;
        myDates = null;
    }

    public void sendInformation(SickFormulary sickFormulary) {
        String statement;
        String message;
        

        MyDates myDates = new MyDates(MyDates.USER_DATE_FORMAT);

        if (this.idSick != 0) {
            statement = "update Parte set id_personal = ?, Diagnostico = ?, Observacion = ?, Desde = ?, Hasta = ?, "
                    + "CIE = ?, TipoParte = ?, NorasSiras = ? where id = " + this.idSick;
            message = "Se ha modificado el parte.";
        } else {
            statement = "insert into Parte (id_personal, Diagnostico, Observacion, Desde, Hasta, CIE, TipoParte, NorasSiras)"
                    + " values(?,?,?,?,?,?,?,?)";
            message = "Se ha creado un nuevo parte.";
        }

        try {
            PreparedStatement pst = super.getConnection().prepareStatement(statement);

            //recupero las fechas
            LocalDate since =  myDates.toLocalDate(sickFormulary.getDateSince().getDate());
            LocalDate until = myDates.toLocalDate(sickFormulary.getDateUntil().getDate());
            //recupero el campo cie para verificar que este vacio antes de guardarlo
            String CIE = sickFormulary.getTextCIE().getText();

            //envio la informacion a la base de datos
            pst.setInt(1, this.id);
            pst.setString(2, sickFormulary.getTextDiag().getText());
            pst.setString(3, sickFormulary.getTextObs().getText());
            pst.setString(4, since.toString());
            pst.setString(5, until.toString());
            pst.setString(6, !CIE.equals("") ? CIE : null);
            pst.setInt(7, sickFormulary.getComboSickType().getSelectedIndex());
            pst.setString(8, sickFormulary.getNorasSiras().getSelectedItem().toString());

            pst.executeUpdate();

            pst = super.getConnection().prepareStatement("update Personal set Parte = 1 where id = " + this.id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, message);

            
            super.getConnection().close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//setInformacion//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

    public void sendReCountInfo(SickFormulary sickFormulary, boolean definetlyHealed) {
        Personnel personnel = sickFormulary.getPersonnel();

        //GUARDO LA INFORMACION ANTERIOR EN EL RECUENTO PARA SER ARCHIVADO
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("INSERT INTO RecuentoParte"
                    + " (id_personal,Categoria,Grado,NombreCompleto,Destino,DNI,"
                    + "Diagnostico,CIE,Observacion,NorasSiras,TipoParte,Desde,Hasta,Dias) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            PreparedStatement pst2 = super.getConnection().prepareStatement("SELECT * FROM Parte "
                    + "WHERE id = " + this.idSick);
            ResultSet rs = pst2.executeQuery();

            //Informacion propia de PERSONAL
            //se guardan los valores Strings de cada dato, ya que esta tabla sirve de archivo
            //en caso de que un personal sea eliminado de la base de datos, quedaran los registros
            //de sus partes los cuales se accederan por el DNI
            String categorie = MyArrays.getCategories(personnel.getCategorie());
            String grade = MyArrays.getGrades(personnel.getCategorie(), personnel.getGrade());

            pst.setInt(1, this.id);
            pst.setString(2, categorie);
            pst.setString(3, grade);
            pst.setString(4, personnel.getCompleteName());
            pst.setString(5, personnel.getSubUnity());
            pst.setInt(6, personnel.getDni());
            //---------------------------           
            //Informacion propia de PARTE.
            //Se recupera la informacion de la base de datos ya que es la guardada.                  
            //de esta manera, el nuevo parte comenzara el mismo dia que termino el anterior   
            String sickType = sickFormulary.getComboSickType().getItemAt(rs.getInt("TipoParte")).toString();

            pst.setString(7, rs.getString("Diagnostico"));
            pst.setObject(8, rs.getObject("CIE"));
            pst.setString(9, rs.getString("Observacion"));
            pst.setString(10, rs.getString("NorasSiras"));
            pst.setString(11, sickType);

            //dependiendo de si es un alta definitiva o no, se guardaran las fechas de distinta manera
            MyDates myDates = new MyDates("dd/MM/yyyy");
            LocalDate since;
            LocalDate until;
            int days;
            if (definetlyHealed) {
                since = myDates.toLocalDate(sickFormulary.getDateSince().getDate());
                until = myDates.toLocalDate(sickFormulary.getDateUntil().getDate());
                days = myDates.getPeriodOfDays(since, until);
            } else {  
                //se utiliza el desde de la base de Datos, fecha inicial de parte 
                //se utiliza el hasta como el "desde" del nuevo parte
                since = myDates.toLocalDate(rs.getString("Desde"));            
                until =  myDates.toLocalDate(sickFormulary.getDateSince().getDate()); 
                days = myDates.getPeriodOfDays(since, until);
            }
            
            pst.setString(12, myDates.toUserDate(since));
            pst.setString(13, myDates.toUserDate(until));
            pst.setInt(14, days);         
            pst.executeUpdate();
            
            pst = super.getConnection().prepareStatement("DELETE FROM PARTE WHERE id = " + this.idSick);           
            pst.executeUpdate();
            
            if (definetlyHealed) {
                //en caso de ser un alta definitiva paso a False en la base de datos SQLite
                //      el campo "flag" que indica si tiene o no un parte activo
                pst = super.getConnection().prepareStatement("update Personal set Parte = 0 where id = " + this.id);
                pst.executeUpdate();
                super.getConnection().close();
                JOptionPane.showMessageDialog(null, personnel.getCompleteName() + " se ha dado de Alta Medica.");
            } else {
                //llamo al metodo de esta clase para crear un nuevo Parte
                //en caso de no ser un alta definitiva
                this.idSick = 0;
                sendInformation(sickFormulary);
            }
            
            myDates = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//AltaParcial// " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

}
