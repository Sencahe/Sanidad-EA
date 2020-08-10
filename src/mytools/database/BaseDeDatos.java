package mytools.database;

import windows.Tabla;
import mytools.Arreglos;
import mytools.Fechas;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class BaseDeDatos {

    public static int filter;
    public static int showByDestino;
    public static int order;

    public static String PPSFilter;
    public static String aptitudFilter;
    public static String patologiaColumn;

    private Connection cn;

    public BaseDeDatos() {
        this.cn = conectar();
    }

    //------------------------CONECTAR------------------------------------------
    private Connection conectar() {
        try {
            Connection cn = DriverManager.getConnection("jdbc:sqlite:DB.sqlite");
            return cn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error//Conexion//Conectar" + e
                    + "\nContactese con el desarrolador para resolver el problema.");
        }
        return (null);
    }

    public void setConection() {
        this.cn = conectar();
    }

    public Connection getConnection() {
        return this.cn;
    }

    protected void nullConnection() {
        this.cn = null;
    }

    //----------------------------------------------------------------------------
    //------------------------METODO ACTUALIZAR-----------------------------------
    //----------------------------------------------------------------------------
    public void Actualizar() {
        //OBJETOS auxiliares
        Arreglos arreglo = new Arreglos();
        Fechas fecha = new Fechas("dd/MM/yyyy");
        //VACIADO DE LA TABLA ACTUAL
        for (int i = 0; i < 4; i++) {
            Tabla.getTableModel(i).setRowCount(0);
        }
        //FILTRAR TABLA           
        String statement = "select * from Personal";
        switch (filter) {
            case 1:
                statement += " WHERE (SUBSTR(Anexo27,7,4)||SUBSTR(Anexo27,4,2)||SUBSTR(Anexo27,1,2)) <= " + "\"" + fecha.getYearAgo() + "\"";
                break;
            case 2:
                statement += " WHERE PPS = \"" + PPSFilter + "\"";
                break;
            case 3:
                statement += " WHERE Aptitud = \"" + aptitudFilter + "\"";
                break;
            case 4:
                statement += " WHERE \"" + patologiaColumn + "\" = \"X\"";
                break;
            case 5:
                statement += " WHERE (Act IS NOT NULL OR Inf IS NOT NULL)";
                break;
            case 6:
                statement += " WHERE Observaciones IS NOT NULL";
        }
        // MOSTRAR POR DESTINOS            
        if (showByDestino != 0) {
            statement += filter > 0 ? " AND" : " WHERE";
            statement += " Destino = \"" + arreglo.getDestinos()[showByDestino] + "\"";
        }
        //ORDENAR LA TABLA 
        statement += arreglo.getOrdenTablaBD()[order];

        //CONSULTA A BASE DE DATOS 
        try {
            if (cn == null || cn.isClosed()) {
                conectar();
            }
            //consulta a la base de datos
            PreparedStatement pst = cn.prepareStatement(statement);
            ResultSet rs = pst.executeQuery();
            //llenado de la tabla
            int num[] = new int[arreglo.getCategoriasLength()]; //arreglo para el numero de orden en las 4 tabla distintas 
            int categoria;
            int aux;
            Object[] fila = new Object[arreglo.getColumnbasBDLength() + 1];
            String[] columnas = arreglo.getColumnasBD();

            while (rs.next()) {
                categoria = rs.getInt("Categoria"); //obteniendo la categoria                                                           
                fila[0] = ++num[categoria];
                aux = 1; //indice inicial para el resto de los datos que iran en la fila
                for (String i : columnas) {
                    if (rs.getObject(i) != null) {
                        switch (i) {
                            case "Grado":
                                fila[aux] = arreglo.getGrados()[categoria][rs.getInt("Grado")];
                                break;
                            case "Apellido":
                                fila[aux] = rs.getString("Apellido") + " " + rs.getString("Nombre");
                                break;
                            case "FechaNacimiento":
                                fila[aux] = fecha.getEdad(rs.getString("FechaNacimiento"));
                                break;
                            default:
                                fila[aux] = rs.getObject(i);
                                break;
                        }
                    }
                    aux++;
                }
                Tabla.getTableModel(categoria).addRow(fila);
                Arrays.fill(fila, null);
            }
            cn.close();
            cn = null;
            fila = null;
            num = null;
            columnas = null;
            //Al finalizar el llenado de la tablas se actualizan los labels con el conteo
            String[] categorias = arreglo.getCategorias();
            int cantTabla;
            int total = 0;
            for (int i = 0; i < categorias.length + 1; i++) {
                if (i != categorias.length) {
                    cantTabla = Tabla.getTablas(i).getRowCount();
                    Tabla.getResumen(i).setText(categorias[i].toUpperCase() + ":  " + cantTabla);
                    total += Tabla.getTablas(i).getRowCount();
                } else {
                    Tabla.getResumen(i).setText("TOTAL:  " + total);
                }
            }
            categorias = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Actualizar " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        //FIN DEL METODO ACTUALIZAR------------------------
        arreglo = null;
        fecha = null;
    }
}
