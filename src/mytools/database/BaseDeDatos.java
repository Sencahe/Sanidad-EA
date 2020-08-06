package mytools.database;

import windows.Tabla;
import mytools.Arreglos;
import mytools.Fechas;
import mytools.Filtros;
import mytools.Utilidades;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class BaseDeDatos {
    
    private Connection cn;
    
    public BaseDeDatos(){
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
    public void setConection(){
        this.cn = conectar();
    }
    public Connection getConnection(){
        return this.cn;
    }
    
    //----------------------------------------------------------------------------
    //------------------------METODO ACTUALIZAR-----------------------------------
    //----------------------------------------------------------------------------
    public void Actualizar() {       
        //OBJETOS auxiliares
        Filtros filtrar = new Filtros();
        Arreglos arreglo = new Arreglos();

        // DECLARACION DE LOS Table model para colocar la informacion en la tabla
        DefaultTableModel[] tablaMain = new DefaultTableModel[Tabla.tablas.length];
        for (int i = 0; i < tablaMain.length; i++) {
            tablaMain[i] = (DefaultTableModel) Tabla.tablas[i].getModel();
        }
        //VACIADO DE LA TABLA ACTUAL
        for (DefaultTableModel i : tablaMain) {
            i.setRowCount(0);
        }
        // FILTRAR POR DESTINO
        String where = "";
        if (Filtros.filtroDestinos != 0) {
            where = " WHERE Destino = \"" + arreglo.Destinos()[Filtros.filtroDestinos] + "\"";
        }
        //ORDENAR LA TABLA 
        String orderBy = arreglo.ordenTablaBD()[Filtros.ordenamiento];

        //CONEXION A BASE DE DATOS 
        try {
            //consulta a la base de datos
            PreparedStatement pst = cn.prepareStatement("select * from Personal" + where + orderBy);
            ResultSet rs = pst.executeQuery();

            //llenado de la tabla
            int num[] = new int[4]; //arreglo para el numero de orden en las 4 tabla distintas 
            boolean filtrado;
            while (rs.next()) {
                //filtrado de la informacion                
                switch (Filtros.filtro) {                  
                    case 1:
                        filtrado = filtrar.AnexoVencido(rs.getString("Anexo27"));
                        break;
                    case 2:
                        filtrado = filtrar.FiltroPPS(rs.getString("PPS"));
                        break;
                    case 3:
                        filtrado = filtrar.FiltroAptitud(rs.getString("Aptitud"));
                        break;
                    case 4:
                        filtrado = rs.getString(Filtros.columPatologia) != null;
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
                    int categoria = rs.getInt("Categoria"); //obteniendo la categoria                    
                    // Arreglo para colocar los datos en su respectiva fila segun las columnas
                    Object fila[] = new Object[arreglo.columnbasBDLength() + 1]; 
                    fila[0] = ++num[categoria]; 
                    int aux = 1; //indice inicial para el resto de los datos que iran en la fila
                    for (String i : arreglo.columnasBD()) {
                        if (rs.getObject(i) != null) {
                            switch (i) {
                                case "Grado":
                                    fila[aux] = arreglo.grados()[categoria][rs.getInt("Grado")];
                                    break;
                                case "Apellido":
                                    fila[aux] = rs.getString("Apellido") + " " + rs.getString("Nombre");
                                    break;
                                case "FechaNacimiento":
                                    fila[aux] = new Fechas(rs.getString("FechaNacimiento"), new Utilidades().formatoFecha()).getEdad();
                                    break;
                                default:
                                    fila[aux] = rs.getObject(i);
                                    break;
                            }
                        }
                        aux++;
                    }
                    tablaMain[categoria].addRow(fila); //agrego la fila a la tabla segun su categoria
                    fila = null;
                }
            }
            num = null;
            cn.close();
            //Al finalizar el llenado de la tablas se actualizan los labels con el conteo
            //obtengo el arreglo con categorias y lo hago mayusculas
            String[] categoriasResumen = new String[arreglo.getCategoriasLength()];
            for (int i = 0; i < arreglo.getCategoriasLength(); i++) {
                categoriasResumen[i] = arreglo.Categorias()[i].toUpperCase();
            }
            //coloco los datos en los labels
            int total = 0;
            for (int i = 0; i < Tabla.resumen.length; i++) {
                if (i != Tabla.resumen.length - 1) {
                    Tabla.resumen[i].setText(categoriasResumen[i] + ":  " + Tabla.tablas[i].getRowCount());
                    total += Tabla.tablas[i].getRowCount();
                } else {
                    Tabla.resumen[i].setText("TOTAL:  " + total);
                }
            }
            categoriasResumen = null;
            //FIN DEL METODO ACTUALIZAR
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Actualizar " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        
        arreglo = null;
        filtrar = null;
        tablaMain = null;
        System.gc();
    }

}
