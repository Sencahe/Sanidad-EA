package database;

import panels.PersonnelPanel;
import panels.SickPanel;
import mytools.MyArrays;
import mytools.MyDates;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class DataBase {
    
    private static final String SQLITE_URL = "jdbc:sqlite:DB.sqlite";

    private Connection cn;

    public DataBase() {
        this.cn = conectar();
    }

    //------------------------CONECTAR------------------------------------------
    private Connection conectar() {
        try {
            Connection cn = DriverManager.getConnection(SQLITE_URL);
            return cn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Conectar " + e
                    + "\n\nContactese con el desarrolador para resolver el problema.");
        }
        return (null);
    }

    public void setConnection() {
        this.cn = conectar();
    }

    public Connection getConnection() {
        try {
            if (this.cn.isClosed()) {
                setConnection();
                return this.cn;
            } else {
                return this.cn;
            }
        } catch (Exception e) {
        }
        return (null);
    }

    protected void nullConnection() {
        this.cn = null;
    }

    //--------------------------------------------------------------------------
    //------------------------METODO ACTUALIZAR---------------------------------
    //-------------------------TABLA PERSONAL-----------------------------------
    public void actualizar(PersonnelPanel tabla) {
        //OBJETOS auxiliares----------------------------------------------------
        MyDates fecha = new MyDates(MyDates.USER_DATE_FORMAT);
        String grados[][] = MyArrays.getGrados();
        int filter = tabla.getFilter();
        
        //VACIADO DE LA TABLA ACTUAL--------------------------------------------
        for (int i = 0; i < 4; i++) {
            tabla.getTableModel(i).setRowCount(0);
        }
        
        //FILTRAR TABLA---------------------------------------------------------           
        StringBuffer statement = new StringBuffer("SELECT * FROM Personal");
        switch (filter) {
            case 1:
                statement.append(" WHERE (SUBSTR(Anexo27,1,4)||SUBSTR(Anexo27,5,2)||SUBSTR(Anexo27,7,2)) <= ");
                statement.append("\"");
                statement.append(fecha.getYearAgo());
                statement.append("\"");
                break;
            case 2:
                statement.append(" WHERE PPS = \"");
                statement.append(tabla.getPPSFilter());
                statement.append("\"");
                break;
            case 3:
                statement.append(" WHERE Aptitud = \"");
                statement.append(tabla.getAptitudeFilter());
                statement.append("\"");
                break;
            case 4:
                statement.append(" WHERE ");
                statement.append(tabla.getPathologyColumn());
                statement.append(" IS NOT NULL");
                break;
            case 5:
                statement.append(" WHERE (Act IS NOT NULL OR Inf IS NOT NULL)");
                break;
            case 6:
                statement.append(" WHERE Observaciones IS NOT NULL ");
                break;
            case 7:
                statement.append(" WHERE IMC ");
                statement.append(tabla.getIMCoperator());
                statement.append(" ");
                statement.append(tabla.getIMCfilter());
                break;
            case 8:
                statement.append(" WHERE D IS NOT NULL OR H IS NOT NULL OR A IS NOT NULL");
                statement.append(" OR T IS NOT NULL OR Act IS NOT NULL OR Inf IS NOT NULL");
                break;
        }
        
        // MOSTRAR POR DESTINOS-------------------------------------------------        
        if (tabla.getShowBySubUnity() != 0) {
            statement.append(filter > 0 ? " AND Destino = \"" : " WHERE Destino = \"");
            statement.append(MyArrays.getDestinos(tabla.getShowBySubUnity()));
            statement.append("\"");
        }
        
        //ORDENAR LA TABLA------------------------------------------------------ 
        statement.append(MyArrays.getOrdenTablaBD(tabla.getRowOrdering()));

        //CONSULTA A BASE DE DATOS----------------------------------------------        
        try {
            if (cn == null || cn.isClosed()) {
                cn = conectar();
            }
            //consulta a la base de datos
            PreparedStatement pst = cn.prepareStatement(statement.toString());
            ResultSet rs = pst.executeQuery();

            //llenado de la tabla
            int num[] = new int[MyArrays.getCategoriasLength()]; //arreglo para el numero de orden en las 4 tabla distintas 
            int categoria;
            int aux;

            Object[] fila = new Object[MyArrays.getColumnasBDLength() + 1];
            String[] columnas = MyArrays.getColumnasBD();

            while (rs.next()) {
                categoria = rs.getInt("Categoria"); //obteniendo la categoria                                                           
                fila[0] = ++num[categoria];
                aux = 1; //indice inicial para el resto de los datos que iran en la fila
                for (String i : columnas) {
                    if (rs.getObject(i) != null) {
                        switch (i) {
                            case "Grado":
                                fila[aux] = grados[categoria][rs.getInt("Grado")];
                                break;
                            case "Apellido":
                                fila[aux] = rs.getString("Apellido") + " " + rs.getString("Nombre");
                                break;
                            case "Anexo27":
                                fila[aux] = fecha.toUserDate(rs.getString("Anexo27"));
                                break;
                            case "FechaNacimiento":
                                fila[aux] = fecha.getEdad(rs.getString(i));
                                break;
                            default:
                                fila[aux] = rs.getObject(i);
                                break;
                        }
                    }
                    aux++;
                }
                tabla.getTableModel(categoria).addRow(fila);
                Arrays.fill(fila, null);
            }
            cn.close();
            cn = null;
            fila = null;
            num = null;
            rs = null;
            pst = null;
            statement = null;
            fecha = null;
            //Al finalizar el llenado de la tablas se actualizan los labels con el conteo
            String[] categorias = MyArrays.getCategorias();
            int cantTabla;
            int total = 0;

            for (int i = 0; i < categorias.length + 1; i++) {
                if (i != categorias.length) {
                    cantTabla = tabla.getTablas(i).getRowCount();
                    tabla.getResumen(i).setText(categorias[i].toUpperCase() + ":  " + cantTabla);
                    total += cantTabla;
                } else {
                    tabla.getResumen(i).setText("TOTAL:  " + total);
                }
            }

            categorias = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Actualizar " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

    //--------------------------------------------------------------------------
    //------------------------METODO ACTUALIZAR---------------------------------
    //---------------------------TABLA PARTE------------------------------------
    public void actualizar(SickPanel parte) {
        MyDates fecha = new MyDates(MyDates.USER_DATE_FORMAT);
        String grados[][] = MyArrays.getGrados();

        //vaciado del Parte
        for (int i = 0; i < 4; i++) {
            parte.getTableModel(i).setRowCount(0);
        }

        try {
            if (cn == null || cn.isClosed()) {
                cn = conectar();
            }
            //Consulto los datos necesarios de Personal y de Parte para llenar las tablas de Parte de Sanidad
            PreparedStatement pst = cn.prepareStatement("SELECT Categoria, Grado, Apellido, Nombre, Destino, Expediente "
                    + ", Diagnostico, CIE, Desde, Hasta, Observacion, Parte.id, TipoParte "
                    + "FROM Personal INNER JOIN Parte ON Personal.id = Parte.id_personal  "
                    + "ORDER BY Categoria ASC, Grado DESC, Apellido ASC, Nombre ASC");
            ResultSet rs = pst.executeQuery();

            //variables y objetos para recuperar la informacion y desplegarla en las tablas de Parte
            int num[] = new int[MyArrays.getCategoriasLength()];
            Object fila[] = new Object[MyArrays.getColumnasParteLength()];

            int categoria;
            int tipoParte;
            String cie;
            String hasta;
            String desde;
            String expediente;

            while (rs.next()) {

                //informacion de Personal
                categoria = rs.getInt("Categoria");
                fila[1] = grados[categoria][rs.getInt("Grado")];
                fila[2] = rs.getString("Apellido") + " " + rs.getString("Nombre");
                fila[3] = rs.getString("Destino");

                //informacion de Parte
                cie = rs.getString("CIE");
                hasta = rs.getString("Hasta");
                desde = rs.getString("Desde");
                expediente = rs.getString("Expediente");

                fila[4] = rs.getString("Diagnostico");
                fila[5] = cie != null ? cie : "";
                fila[6] = fecha.toUserDate(desde);
                fila[7] = fecha.toUserDate(hasta);
                fila[8] = fecha.getDias(desde);
                fila[9] = expediente != null ? expediente : "No";
                fila[10] = rs.getString("Observacion");
                fila[11] = rs.getInt("id");
                tipoParte = rs.getInt("TipoParte");
                /*
                si getDias() con la fecha dedevuelve un numero positivo 
                significa que NO PASO NOVEDAD y se colocara en esa tabla
                sin tener que modificar su tipo de parte 
                */                
                if (fecha.getDias(hasta) > 0) {
                    fila[0] = ++num[3];
                    parte.getTableModel(3).addRow(fila);
                } else {
                    fila[0] = ++num[tipoParte];
                    parte.getTableModel(tipoParte).addRow(fila);
                }
                Arrays.fill(fila, null);
            }
            cn.close();
            fila = null;
            num = null;
            fecha = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Actualizar " + e
                    + "\nContactese con el desarrolador para resolver el problema.");
        }

    }

}
