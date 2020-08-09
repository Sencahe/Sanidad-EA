package mytools.database;

import windows.Tabla;
import mytools.Arreglos;
import mytools.Fechas;
import mytools.Filtros;
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
        Fechas edad = new Fechas("dd/MM/yyyy");
        Filtros filtered = null;
        if (Filtros.filter >= 1 && Filtros.filter <= 3) {
            filtered = new Filtros();
        }

        //VACIADO DE LA TABLA ACTUAL
        for (int i = 0; i < 4; i++) {
            Tabla.getTableModel(i).setRowCount(0);
        }
        // MOSTRAR POR DESTINOS
        String where = "";
        if (Filtros.showByDestino != 0) {
            where = " WHERE Destino = \"" + arreglo.getDestinos()[Filtros.showByDestino] + "\"";
        }
        //ORDENAR LA TABLA 
        String orderBy = arreglo.getOrdenTablaBD()[Filtros.order];

        //CONSULTA A BASE DE DATOS 
        try {
            if (cn == null || cn.isClosed()) {
                conectar();
            }
            //consulta a la base de datos
            PreparedStatement pst = cn.prepareStatement("select * from Personal" + where + orderBy);
            ResultSet rs = pst.executeQuery();

            //llenado de la tabla
            int num[] = new int[arreglo.getCategoriasLength()]; //arreglo para el numero de orden en las 4 tabla distintas 
            int categoria;
            int aux;
            Object[] fila = new Object[arreglo.getColumnbasBDLength() + 1];
            String[] columnas = arreglo.getColumnasBD();
            boolean filtrado;

            int filtro = Filtros.filter;
            while (rs.next()) {
                //filtrado de la informacion                
                switch (filtro) {
                    case 1:
                        filtrado = filtered.expiredAnexo(rs.getString("Anexo27"));
                        break;
                    case 2:
                        filtrado = filtered.PPSFilter(rs.getString("PPS"));
                        break;
                    case 3:
                        filtrado = filtered.aptitudFilter(rs.getString("Aptitud"));
                        break;
                    case 4:
                        filtrado = rs.getString(Filtros.patologiaColumn) != null;
                        break;
                    case 5:
                        filtrado = rs.getString("Act") != null || rs.getString("Inf") != null;
                        break;
                    case 6:
                        filtrado = rs.getString("Observaciones") != null;
                        break;
                    default:
                        filtrado = true;
                        break;
                }
                //si la informacion pasa el filtro se procede a obtener los datos
                if (filtrado) {
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
                                    fila[aux] = edad.getEdad(rs.getString("FechaNacimiento"));
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
        filtered = null;
        edad = null;
    }

}
