package Tools;

import Frames.Tabla;
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
    
    //----------------------------------------------------------------------------
    //------------------------METODO ARREGLO ID-----------------------------------
    //----------------------------------------------------------------------------
    public void totalPersonal() {
        try {
            Tabla.ID = new int[new Arreglos().getCategoriasLength()][];
            PreparedStatement pst[] = new PreparedStatement[new Arreglos().getCategoriasLength()];
            for (int i = 0; i < pst.length; i++) {
                pst[i] = cn.prepareStatement("SELECT Categoria, COUNT(*) AS count FROM Personal WHERE Categoria = " + i);
                ResultSet rs = pst[i].executeQuery();
                rs.next();
                Tabla.ID[i] = new int[rs.getInt(2)];
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//totalPersonal//" + e
                    + "\nContactese con el desarrolador para resolver el problema.");
        }
    }

    //----------------------------------------------------------------------------
    //------------------------METODO ACTUALIZAR-----------------------------------
    //----------------------------------------------------------------------------
    public void Actualizar() {
        totalPersonal();
        //OBJETOS
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
            int num[] = new int[4]; //arreglo para el index en ID[][] y numero de orden en las 4 tabla distintas 
            while (rs.next()) {
                //filtrado de la informacion
                boolean filtrado;
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
                        filtrado = (rs.getString("Act") != null || rs.getString("Inf") != null);
                        break;
                    case 6:
                        filtrado = (rs.getString("Act") != null || rs.getString("Inf") != null);
                        break;
                    default:
                        filtrado = true;
                        break;
                }
                //si la informacion pasa el filtro se procede a obtener los datos
                if (filtrado) {
                    int categoria = rs.getInt("Categoria"); //obteniendo la categoria 
                    Tabla.ID[categoria][num[categoria]] = rs.getInt("id"); //guardando el id, el index en el arreglo equivaldra al index del tabbed pane y de la fila en la tabla               
                    // Arreglo para colocar los datos en su respectiva fila segun las columnas
                    Object fila[] = new Object[arreglo.columnbasBDLength() + 1]; //el mas uno es para el numero de orden, el resto del length corresponde a las columnas q se les hara la peticion
                    fila[0] = ++num[categoria]; // numero de orden, este numero siempre sera +1 a su respectivo indice donde esta guardado el id del registro
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
                }
            }
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
            //FIN DEL METODO ACTUALIZAR
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Actualizar " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        
        arreglo = null;
        filtrar = null;
        System.gc();
    }

    //----------------------------------------------------------------------------
    //------------------------METODO GETINFORMACION--------------------------------------
    //----------------------------------------------------------------------------
    public String[][] getInformacion(int id) {
        Arreglos arreglo = new Arreglos();
        String[][] mensajero = new String[4][];
        String[] mensajeroText = new String[arreglo.getTextFieldLength()];
        String[] mensajeroCombo = new String[arreglo.getComboBoxLength()];
        String[] mensajeroDate = new String[arreglo.getDateChooserLength()];
        String[] mensajeroCheck = new String[arreglo.getCheckBoxLength()];
        try {
            PreparedStatement pst = cn.prepareStatement("select * from Personal where id = " + id);
            ResultSet rs = pst.executeQuery();
            //SOLICITO LOS DATOS QUE VAN A LOS TEXTFIELD
            for (int i = 0; i < mensajeroText.length; i++) {
                mensajeroText[i] = rs.getString(arreglo.textField()[i]);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS COMBO BOX
            for (int i = 0; i < mensajeroCombo.length; i++) {
                mensajeroCombo[i] = rs.getString(arreglo.comboBox()[i]);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS DATE CHOOSER
            for (int i = 0; i < mensajeroDate.length; i++) {
                mensajeroDate[i] = rs.getString(arreglo.dateChooser()[i]);
            }
            for (int i = 0; i < mensajeroCheck.length; i++) {
                mensajeroCheck[i] = rs.getString(arreglo.checkBox()[i]);
            }
            // ENVIO LOS DATOS A LA CLASE FORMULARIO
            cn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//getInformacion " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        mensajero[0] = mensajeroText;
        mensajero[1] = mensajeroCombo;
        mensajero[2] = mensajeroDate;
        mensajero[3] = mensajeroCheck;

        arreglo = null;
        System.gc();
        return mensajero;
    }

    //-----------------------------------------------------------------------------
    //------------------------METODO SET INFO--------------------------------------
    //-----------------------------------------------------------------------------
    public void setInformacion(String[] datos, int id) {
        String statement;
        String[] columnas = new Arreglos().todasColumnas();

        //Modificar registro
        if (id != 0) {
            statement = "update Personal set ";
            for (int i = 0; i < columnas.length; i++) {
                statement += i == columnas.length - 1 ? columnas[i] + " = ? " : columnas[i] + " = ?, ";
            }
            statement += "where id = " + id;
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
            PreparedStatement pst = cn.prepareStatement(statement);

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
            cn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//setInformacion//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
        columnas = null;
        System.gc();
    }

    //-----------------------------------------------------------------------------
    //------------------------METODO ELIMINAR--------------------------------------
    //-----------------------------------------------------------------------------
    public void Eliminar(int id) {
        int ID = id;
        try {
            PreparedStatement pst = cn.prepareStatement("delete from Personal where ID = " + ID);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Personal eliminado del registro con exito.");
            cn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Eliminar//" + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }
    
    

}
