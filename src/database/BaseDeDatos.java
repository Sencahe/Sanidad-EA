package database;

import windows.Tabla;
import windows.parte.Parte;
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
            JOptionPane.showMessageDialog(null, "Error//BDD//Conectar" + e
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

    //--------------------------------------------------------------------------
    //------------------------METODO ACTUALIZAR---------------------------------
    //--------------------------------------------------------------------------
    public void Actualizar(Tabla tabla) {
        //OBJETOS auxiliares----------------------------------------------------
        Fechas fecha = new Fechas("dd/MM/yyyy");
        String grados[][] = Arreglos.getGrados();
        int filter = tabla.getFilter();
        //VACIADO DE LA TABLA ACTUAL--------------------------------------------
        for (int i = 0; i < 4; i++) {
            tabla.getTableModel(i).setRowCount(0);
        }
        //FILTRAR TABLA---------------------------------------------------------           
        StringBuffer statement = new StringBuffer("SELECT * FROM Personal");
        switch (filter) {
            case 1:
                statement.append(" WHERE (SUBSTR(Anexo27,7,4)||SUBSTR(Anexo27,4,2)||SUBSTR(Anexo27,1,2)) <= ");
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
                statement.append(tabla.getAptitudFilter());
                statement.append("\"");
                break;
            case 4:
                statement.append(" WHERE ");
                statement.append(tabla.getPatologiaColumn());
                statement.append(" IS NOT NULL");
                break;
            case 5:
                statement.append(" WHERE (Act IS NOT NULL OR Inf IS NOT NULL)");
                break;
            case 6:
                statement.append(" WHERE Observaciones IS NOT NULL");
        }
        // MOSTRAR POR DESTINOS-------------------------------------------------        
        if (tabla.getShowByDestino() != 0) {
            statement.append(filter > 0 ? " AND Destino = \"" : " WHERE Destino = \"");
            statement.append(Arreglos.getDestinos(tabla.getShowByDestino()));
            statement.append("\"");
        }
        //ORDENAR LA TABLA------------------------------------------------------ 
        statement.append(Arreglos.getOrdenTablaBD(tabla.getOrder()));

        //CONSULTA A BASE DE DATOS----------------------------------------------        
        try {
            if (cn == null || cn.isClosed()) {
                cn = conectar();
            }
            //consulta a la base de datos
            PreparedStatement pst = cn.prepareStatement(statement.toString());
            ResultSet rs = pst.executeQuery();

            //llenado de la tabla
            int num[] = new int[Arreglos.getCategoriasLength()]; //arreglo para el numero de orden en las 4 tabla distintas 
            int categoria;
            int aux;

            Object[] fila = new Object[Arreglos.getColumnasBDLength() + 1];
            String[] columnas = Arreglos.getColumnasBD();

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
            //Al finalizar el llenado de la tablas se actualizan los labels con el conteo
            String[] categorias = Arreglos.getCategorias();
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Actualizar " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }

        //FIN DEL METODO ACTUALIZAR------------------------
        statement = null;
        fecha = null;
    }

    public void Actualizar(Parte parte) {
        Fechas fecha = new Fechas("dd/MM/yyyy");
        String grados[][] = Arreglos.getGrados();

        //vaciado del Parte
        for (int i = 0; i < 4; i++) {
            parte.getTableModel(i).setRowCount(0);
        }

        try {
            if (cn == null || cn.isClosed()) {
                cn = conectar();
            }
            //RECUPERO PRIMERO LOS DATOS DE LA TABLA PERSONAL PARA PODER ORDENARLOS POR GRADO
            PreparedStatement pst = cn.prepareStatement("SELECT id, Categoria, Grado, Apellido, Nombre, Destino, Expediente "
                    + "FROM Personal WHERE Parte = 1 ORDER BY Categoria ASC, Grado DESC, Apellido ASC, Nombre ASC");
            ResultSet rs = pst.executeQuery();
            PreparedStatement pst2;
            ResultSet rs2;

            //variables y objetos para recuperar la informacion y desplegarla en las tablas de Parte
            int num[] = new int[Arreglos.getCategoriasLength()];
            Object fila[] = new Object[Arreglos.getColumnasParteLength()];
            int categoria;
            int tipoParte;
            int idPersonal;
            String cie;
            String hasta;
            String desde;
            String expediente;

            while (rs.next()) {
                idPersonal = rs.getInt("id");
                categoria = rs.getInt("Categoria");
                //guardo en el arreglo los datos importantes de personal
                fila[1] = grados[categoria][rs.getInt("Grado")];
                fila[2] = rs.getString("Apellido") + " " + rs.getString("Nombre");
                fila[3] = rs.getString("Destino");
                //con el id de referencia selecciono de la tabla Parte para colocarlos en la tabla del frame
                pst2 = cn.prepareStatement("SELECT * FROM Parte WHERE id_personal = " + idPersonal);
                rs2 = pst2.executeQuery();

                cie = rs2.getString("CIE");
                hasta = rs2.getString("Hasta");
                desde = rs2.getString("Desde");
                expediente = rs.getString("Expediente");

                fila[4] = rs2.getString("Diagnostico");
                fila[5] = cie != null ? cie : "";
                fila[6] = desde;
                fila[7] = hasta;
                fila[8] = fecha.getDias(desde);
                fila[9] = expediente != null ?  expediente : "No";
                fila[10] = rs2.getString("Observacion");
                fila[11] = rs2.getInt("id");
                tipoParte = rs2.getInt("TipoParte");
                //si la fecha de HASTA es numero positivo, significa que NO PASO NOVEEDAD y se colocara en esa tabla
                //sin tener que modificar su tipo de parte
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
