package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {

    public static Connection conectar() {
        try {
            Connection cn = DriverManager.getConnection("jdbc:sqlite:DB.sqlite");
            return cn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error//Conexion//Conectar" + e 
            + "\nContactese con el desarrolador para resolver el problema.");
        }
        return (null);
    }
}
