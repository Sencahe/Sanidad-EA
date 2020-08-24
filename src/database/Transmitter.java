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

    private int id, idParte;

    public Transmitter(int id) {
        this.id = id;
    }

    public Transmitter(int id, int idParte) {
        this.id = id;
        this.idParte = idParte;
    }

    public void setInformacion(PersonnelFormulary formulario) {
        StringBuffer statement = new StringBuffer();
        
        MyDates fecha = new MyDates("dd/MM/yyyy");
        
        String[] columnas = MyArrays.getFormularioBD();
        
        int iteracion = columnas.length;

        //Modificar registro
        if (id != 0) {
            statement.append("update Personal set ");
            for (int i = 0; i < iteracion; i++) {
                statement.append(columnas[i]);
                statement.append(i == iteracion - 1 ? " = ? " : " = ?, ");
            }
            statement.append("where id = ");
            statement.append(this.id);
            // Aregar nuevo registro
        } else {
            statement.append("insert into Personal (");
            for (int i = 0; i < iteracion; i++) {
                statement.append(columnas[i]);
                statement.append(i != iteracion - 1 ? ", " : ") ");
            }
            statement.append("values(");
            for (int i = 0; i < iteracion; i++) {
                statement.append(i != iteracion - 1 ? "?," : "?)");
            }
        }

        try {
            PreparedStatement pst = super.getConnection().prepareStatement(statement.toString());

            int index = 1;
            String emisor;   
            Date date;
            LocalDate localDate;
            int numEmisor;

            //Se envian los datos de los textField
            for (int i = 0; i < formulario.getTextFieldLength(); i++) {
                if (i == 0) {
                    emisor = formulario.getTextField(i).getText().toUpperCase().trim();
                } else {
                    emisor = formulario.getTextField(i).getText().trim();
                }
                pst.setObject(index++, !emisor.equals("") ? emisor : null);
            }
            //Se envian los datos de los comboBox
            for (int i = 0; i < formulario.getComboBoxLength(); i++) {
                if (i < 2) {
                    numEmisor = formulario.getComboBox(i).getSelectedIndex();
                    pst.setInt(index++, numEmisor);

                } else {
                    emisor = String.valueOf(formulario.getComboBox(i).getSelectedItem());
                    pst.setObject(index++, !emisor.equals("") ? emisor : null);
                }

            }
            //Se envian los datos de los dateChooser
            for (int i = 0; i < formulario.getDateChooserLength(); i++) {
                date = formulario.getDateChooser(i).getDate();
                localDate = date != null ? fecha.toLocalDate(date) : null;
                pst.setString(index++, localDate != null ? localDate.toString() : null);
            }
            //Se envian los datos de los checkBox
            for (int i = 0; i < formulario.getCheckBoxLength(); i++) {
                pst.setString(index++, formulario.getCheckBox(i).isSelected() ? "X" : null);
            }
            //Se envia los datos de radiobuttons           
            pst.setString(index++, formulario.getM().isSelected() ? "M" : "F");
            //Se envian los flags
            pst.setInt(index++, formulario.getParteDeEnfermo() ? 1 : 0);
            
            
            pst.executeUpdate();
            super.getConnection().close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//setInformacion//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }

        columnas = null;
        statement = null;
        fecha = null;
    }

    public void setInformacion(SickFormulary formParte) {
        String statement;
        String mensaje;
        

        MyDates fecha = new MyDates(MyDates.USER_DATE_FORMAT);

        if (this.idParte != 0) {
            statement = "update Parte set id_personal = ?, Diagnostico = ?, Observacion = ?, Desde = ?, Hasta = ?, "
                    + "CIE = ?, TipoParte = ?, NorasSiras = ? where id = " + this.idParte;
            mensaje = "Se ha modificado el parte.";
        } else {
            statement = "insert into Parte (id_personal, Diagnostico, Observacion, Desde, Hasta, CIE, TipoParte, NorasSiras)"
                    + " values(?,?,?,?,?,?,?,?)";
            mensaje = "Se ha creado un nuevo parte.";
        }

        try {
            PreparedStatement pst = super.getConnection().prepareStatement(statement);

            //recupero las fechas
            LocalDate desde =  fecha.toLocalDate(formParte.getDateDesde().getDate());
            LocalDate hasta = fecha.toLocalDate(formParte.getDateHasta().getDate());
            //recupero el campo cie para verificar que este vacio antes de guardarlo
            String emisorCIE = formParte.getTextCIE().getText();

            //envio la informacion a la base de datos
            pst.setInt(1, this.id);
            pst.setString(2, formParte.getTextDiagnostico().getText());
            pst.setString(3, formParte.getTextObservaciones().getText());
            pst.setString(4, desde.toString());
            pst.setString(5, hasta.toString());
            pst.setString(6, !emisorCIE.equals("") ? emisorCIE : null);
            pst.setInt(7, formParte.getComboTipoParte().getSelectedIndex());
            pst.setString(8, formParte.getNorasSiras().getSelectedItem().toString());

            pst.executeUpdate();

            pst = super.getConnection().prepareStatement("update Personal set Parte = 1 where id = " + this.id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, mensaje);

            
            super.getConnection().close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//setInformacion//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

    public void setRecuento(SickFormulary formParte, boolean altaDefinitiva) {
        Personnel personal = formParte.getPersonal();

        //GUARDO LA INFORMACION ANTERIOR EN EL RECUENTO PARA SER ARCHIVADO
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("INSERT INTO RecuentoParte"
                    + " (id_personal,Categoria,Grado,NombreCompleto,Destino,DNI,"
                    + "Diagnostico,CIE,Observacion,NorasSiras,TipoParte,Desde,Hasta,Dias) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            PreparedStatement pst2 = super.getConnection().prepareStatement("SELECT * FROM Parte "
                    + "WHERE id = " + this.idParte);
            ResultSet rs = pst2.executeQuery();

            //Informacion propia de PERSONAL
            //se guardan los valores Strings de cada dato, ya que esta tabla sirve de archivo
            //en caso de que un personal sea eliminado de la base de datos, quedaran los registros
            //de sus partes los cuales se accederan por el DNI
            String categoria = MyArrays.getCategorias(personal.getCategorie());
            String grado = MyArrays.getGrados(personal.getCategorie(), personal.getGrade());

            pst.setInt(1, this.id);
            pst.setString(2, categoria);
            pst.setString(3, grado);
            pst.setString(4, personal.getCompleteName());
            pst.setString(5, personal.getSubUnity());
            pst.setInt(6, personal.getDni());
            //---------------------------           
            //Informacion propia de PARTE.
            //Se recupera la informacion de la base de datos ya que es la guardada.                  
            //de esta manera, el nuevo parte comenzara el mismo dia que termino el anterior   
            String tipoDeParte = formParte.getComboTipoParte().getItemAt(rs.getInt("TipoParte")).toString();

            pst.setString(7, rs.getString("Diagnostico"));
            pst.setObject(8, rs.getObject("CIE"));
            pst.setString(9, rs.getString("Observacion"));
            pst.setString(10, rs.getString("NorasSiras"));
            pst.setString(11, tipoDeParte);

            //dependiendo de si es un alta definitiva o no, se guardaran las fechas de distinta manera
            MyDates fecha = new MyDates("dd/MM/yyyy");
            LocalDate desde;
            LocalDate hasta;
            int dias;
            if (altaDefinitiva) {
                desde = fecha.toLocalDate(formParte.getDateDesde().getDate());
                hasta = fecha.toLocalDate(formParte.getDateHasta().getDate());
                dias = fecha.getPeriodoDias(desde, hasta);
            } else {  
                //se utiliza el desde de la base de Datos, fecha inicial de parte 
                //se utiliza el hasta como el "desde" del nuevo parte
                desde = fecha.toLocalDate(rs.getString("Desde"));            
                hasta =  fecha.toLocalDate(formParte.getDateDesde().getDate()); 
                dias = fecha.getPeriodoDias(desde, hasta);
            }
            
            pst.setString(12, fecha.toUserDate(desde));
            pst.setString(13, fecha.toUserDate(hasta));
            pst.setInt(14, dias);         
            pst.executeUpdate();
            
            pst = super.getConnection().prepareStatement("DELETE FROM PARTE WHERE id = " + this.idParte);           
            pst.executeUpdate();
            
            if (altaDefinitiva) {
                //en caso de ser un alta definitiva paso a False en la base de datos SQLite
                //      el campo "flag" que indica si tiene o no un parte activo
                pst = super.getConnection().prepareStatement("update Personal set Parte = 0 where id = " + this.id);
                pst.executeUpdate();
                super.getConnection().close();
                JOptionPane.showMessageDialog(null, personal.getCompleteName() + " se ha dado de Alta Medica.");
            } else {
                //llamo al metodo de esta clase para crear un nuevo Parte
                //en caso de no ser un alta definitiva
                this.idParte = 0;
                setInformacion(formParte);
            }
            
            fecha = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//AltaParcial// " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

}
