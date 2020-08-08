
package mytools.database;

import mytools.Arreglos;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;

public class Emisor extends BaseDeDatos{
    
    private int id;
    
    public Emisor(int id){
        this.id = id;
    }
       
    public void setInformacion(String[] datos) {
        Arreglos arreglo = new Arreglos();
        String statement;
        String[] columnas = arreglo.getTodasColumnas();

        //Modificar registro
        if (id != 0) {
            statement = "update Personal set ";
            for (int i = 0; i < columnas.length; i++) {
                statement += i == columnas.length - 1 ? columnas[i] + " = ? " : columnas[i] + " = ?, ";
            }
            statement += "where id = " + this.id;
            // Aregar nuevo registro
        } else {
            statement = "insert into Personal (";
            for (int i = 0; i < columnas.length; i++) {
                statement += i != columnas.length - 1 ? columnas[i] + ", " : columnas[i] + ") ";
            }
            statement += "values(";
            for (int i = 0; i < columnas.length; i++) {
                statement += i != columnas.length - 1 ? "?," : "?)";
            }
        }

        try {
            PreparedStatement pst = super.getConnection().prepareStatement(statement);

            int index = 1;
            for (int i = 0; i < datos.length; i++) {
                if (!"".equals(datos[i])) {
                    pst.setObject(index, datos[i]);
                } else {
                    pst.setObject(index, null);
                }
                index++;
            }
            pst.executeUpdate();
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//setInformacion//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        arreglo = null;
        columnas = null;
        
    }

}
