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
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import windows.Main;

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
        Filtros filtrar = null;
        if(Filtros.filtro >= 1 && Filtros.filtro <= 3){
            filtrar = new Filtros();
        }
        //VACIADO DE LA TABLA ACTUAL
        for (int i = 0; i < 4; i++) {
            Tabla.getTableModel(i).setRowCount(0);
        }
        // MOSTRAR POR DESTINOS
        String where = "";
        if (Filtros.filtroDestinos != 0) {
            where = " WHERE Destino = \"" + Main.arreglo.Destinos()[Filtros.filtroDestinos] + "\"";
        }
        //ORDENAR LA TABLA 
        String orderBy = Main.arreglo.ordenTablaBD()[Filtros.ordenamiento];

        //CONSULTA A BASE DE DATOS 
        try {
            //consulta a la base de datos
            PreparedStatement pst = cn.prepareStatement("select * from Personal" + where + orderBy);
            ResultSet rs = pst.executeQuery();

            //llenado de la tabla
            int num[] = new int[Main.arreglo.getCategoriasLength()]; //arreglo para el numero de orden en las 4 tabla distintas 
            int categoria;
            int aux;
            Object[] fila = new Object[Main.arreglo.columnbasBDLength() + 1];
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
                    categoria = rs.getInt("Categoria"); //obteniendo la categoria                                                           
                    fila[0] = ++num[categoria]; // Arreglo para colocar los datos en su respectiva fila segun las columnas 
                    aux = 1; //indice inicial para el resto de los datos que iran en la fila
                    for (String i : Main.arreglo.columnasBD()) {
                        if (rs.getObject(i) != null) {
                            switch (i) {
                                case "Grado":
                                    fila[aux] = Main.arreglo.grados()[categoria][rs.getInt("Grado")];
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
                    Tabla.getTableModel(categoria).addRow(fila);                  
                    Arrays.fill(fila,null);
                }
            }
            fila = null;
            num = null;
            cn.close();
            //Al finalizar el llenado de la tablas se actualizan los labels con el conteo
            //obtengo el arreglo con categorias y lo hago mayusculas
            String[] categoriasResumen = new String[Main.arreglo.getCategoriasLength()];
            for (int i = 0; i < Main.arreglo.getCategoriasLength(); i++) {
                categoriasResumen[i] = Main.arreglo.Categorias()[i].toUpperCase();
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
        
        filtrar = null;
        //System.gc();
    }

}
