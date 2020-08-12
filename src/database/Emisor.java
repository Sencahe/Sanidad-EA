package database;

import java.awt.TextField;
import mytools.Arreglos;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import javax.swing.JTextField;
import windows.Formulario;
import windows.parte.FormularioParte;

public class Emisor extends BaseDeDatos {

    private int id, idParte;
    

    public Emisor(int id) {
        this.id = id;
    }
    public Emisor(int id, int idParte){
        this.id = id;
        this.idParte = idParte;
    }

    public void setInformacion(Formulario formulario) {
        StringBuffer statement = new StringBuffer();
        String[] columnas = Arreglos.getFormularioBD();
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
            int numEmisor;
            
            //Se envian los datos de los textField
            for (int i = 0; i < formulario.getTextFieldLength(); i++) {
                if (i == 0) {
                    emisor = formulario.getTextField(i).getText().toUpperCase().trim();
                } else {
                    emisor = formulario.getTextField(i).getText().trim();
                }
                pst.setObject(index++, !emisor.equals("") ? emisor:null);
            }
            //Se envian los datos de los comboBox
            for (int i = 0; i < formulario.getComboBoxLength(); i++) {
                if(i < 2){
                    numEmisor = formulario.getComboBox(i).getSelectedIndex();
                    pst.setInt(index++, numEmisor);
                    
                } else {
                    emisor = String.valueOf(formulario.getComboBox(i).getSelectedItem());
                    pst.setObject(index++, !emisor.equals("") ? emisor:null);   
                }
                             
            }
            //Se envian los datos de los dateChooser
            for (int i = 0; i < formulario.getDateChooserLength(); i++) {
                emisor = ((JTextField) formulario.getDateChooser(i).getDateEditor().getUiComponent()).getText();
                pst.setString(index++,!emisor.equals("") ? emisor:null);
            }
            //Se envian los datos de los checkBox
            for (int i = 0; i < formulario.getCheckBoxLength(); i++) {
                pst.setString(index++, formulario.getCheckBox(i).isSelected() ? "X":null);
            }
            //Se envian los flags
            pst.setInt(index++, formulario.getParteDeEnfermo() ? 1:0);
            pst.executeUpdate();

            super.getConnection().close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//setInformacion//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }

        columnas = null;
        statement = null;
    }
    
    public void setInformacion(FormularioParte formParte){
        String statement;
        
        
        if(this.idParte != 0){
            statement = "update Parte set id_personal, = ? Diagnostico, = ? Observacion, = ? Desde, = ?, Hasta = ?, "
                    + "CIE = ?, TipoParte = ? where id = " + this.idParte;
        } else {
            statement = "insert into Parte (id_personal, Diagnostico, Observacion, Desde, Hasta, CIE, TipoParte)"
                    + " values(?,?,?,?,?,?,?)";
        }
        
        try {            
            PreparedStatement pst = super.getConnection().prepareStatement(statement);
            
            String emisorDesde = ((JTextField)formParte.getDesde().getDateEditor().getUiComponent()).getText();
            String emisorHasta = ((JTextField)formParte.getHasta().getDateEditor().getUiComponent()).getText();;
            String emisorCIE = formParte.getCie().getText();
            
            pst.setInt(1, this.id);
            pst.setString(2, formParte.getDiagnostico().getText());
            pst.setString(3, formParte.getObservaciones().getText());
            pst.setString(4,emisorDesde);
            pst.setString(5, emisorHasta);
            pst.setString(6, !emisorCIE.equals("") ? emisorCIE:null);
            pst.setInt(7,formParte.getTipoParte().getSelectedIndex());
                       
            pst.executeUpdate();
            
            pst = super.getConnection().prepareStatement("update Personal set Parte = 1 where id = " + this.id);

            pst.executeUpdate();
            
            super.getConnection().close();
        } catch (Exception e) {
        }
    }

}
