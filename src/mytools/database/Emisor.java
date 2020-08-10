
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
        StringBuffer statement = new StringBuffer();
        String[] columnas = arreglo.getTodasColumnas();
        int iteracion = columnas.length;
        //Modificar registro
        if (id != 0) {
            statement.append("update Personal set ") ;
            for (int i = 0; i < iteracion; i++) {
                statement.append( columnas[i]);
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


            for (int i = 0; i < datos.length; i++) {
                if (!"".equals(datos[i])) {
                    pst.setObject(i+1, datos[i]);
                } else {
                    pst.setObject(i+1, null);
                }
            }
            pst.executeUpdate();
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//setInformacion//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        arreglo = null;
        columnas = null;
        statement = null;
        
    }

}
