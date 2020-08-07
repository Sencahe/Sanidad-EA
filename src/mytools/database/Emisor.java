
package mytools.database;

import mytools.Arreglos;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import windows.Main;

public class Emisor extends BaseDeDatos{
    
    private int id;
    
    public Emisor(int id){
        this.id = id;
    }
       
    public void setInformacion(String[] datos) {
        String statement;
        String[] columnas = Main.arreglo.todasColumnas();

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
            for (String dato : datos) {
                if (!"".equals(dato)) {
                    pst.setObject(index, dato);
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
        columnas = null;
        super.Actualizar();
        System.gc();
    }

}
