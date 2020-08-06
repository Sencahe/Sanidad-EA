package Tools;

import Frames.Tabla;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class BaseDeDatos {

    //----------------------------------------------------------------------------
    //------------------------METODO ARREGLO ID-----------------------------------
    //----------------------------------------------------------------------------
    public void totalPersonal() {
        try {
            java.sql.Connection cn = Conexion.conectar();
            Tabla.ID = new int[new Arreglos().getCategoriasLength()][];
            PreparedStatement pst[] = new PreparedStatement[new Arreglos().getCategoriasLength()];
            for (int i = 0; i < pst.length; i++) {
                pst[i] = cn.prepareStatement("SELECT Categoria, COUNT(*) AS count FROM Personal WHERE Categoria = " + i);
                ResultSet rs = pst[i].executeQuery();
                rs.next();
                Tabla.ID[i] = new int[rs.getInt(2)];
            }
            cn.close();

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
        // ARREGLOS PARA PASAR VALORES NUMERICOS A SUS CORRESPONDIENTES STRINGS       
        String grados[][] = arreglo.grados();
        String columnasBD[] = arreglo.columnasBD();
        String destinos[] = arreglo.Destinos();
        String orden[] = arreglo.ordenTablaBD();

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
            where = " WHERE Destino = \"" + destinos[Filtros.filtroDestinos] + "\"";
        }
        //ORDENAR LA TABLA 
        String orderBy = orden[Filtros.ordenamiento];

        //CONEXION A BASE DE DATOS 
        try {
            //consulta a la base de datos
            java.sql.Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select * from Personal" + where + orderBy);
            ResultSet rs = pst.executeQuery();

            //llenado de la tabla
            int num[] = new int[4]; //arreglo para el index en ID[][] y numero de orden en las 4 tabla distintas 
            while (rs.next()) {
                //filtrado de la informacion
                boolean filtrado = true;
                switch (Filtros.filtro) {
                    case 0:
                        filtrado = true;
                        break;
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
                }
                //si la informacion pasa el filtro se procede a obtener los datos
                if (filtrado) {
                    int categoria = rs.getInt("Categoria"); //obteniendo la categoria 
                    Tabla.ID[categoria][num[categoria]] = rs.getInt("id"); //guardando el id, el index en el arreglo equivaldra al index del tabbed pane y de la fila en la tabla               
                    // Arreglo para colocar los datos en su respectiva fila segun las columnas
                    Object fila[] = new Object[columnasBD.length + 1]; //el mas uno es para el numero de orden, el resto del length corresponde a las columnas q se les hara la peticion
                    fila[0] = ++num[categoria]; // numero de orden, este numero siempre sera +1 a su respectivo indice donde esta guardado el id del registro
                    int aux = 1; //indice inicial para el resto de los datos que iran en la fila
                    for (String i : columnasBD) {
                        if (rs.getObject(i) != null) {
                            switch (i) {
                                case "Grado":
                                    fila[aux] = grados[categoria][rs.getInt("Grado")];
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
            String[] categoriasResumen = new Arreglos().Categorias();
            for (int i = 0; i < categoriasResumen.length; i++) {
                categoriasResumen[i] = categoriasResumen[i].toUpperCase();
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
        String[] textField = arreglo.textField();
        String[] comboBox = arreglo.comboBox();
        String[] dateChooser = arreglo.dateChooser();
        String[] checkBox = arreglo.checkBox();
        String[] mensajeroText = new String[textField.length];
        String[] mensajeroCombo = new String[comboBox.length];
        String[] mensajeroDate = new String[dateChooser.length];
        String[] mensajeroCheck = new String[checkBox.length];
        try {
            java.sql.Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select * from Personal where id = " + id);
            ResultSet rs = pst.executeQuery();
            //SOLICITO LOS DATOS QUE VAN A LOS TEXTFIELD
            for (int i = 0; i < textField.length; i++) {
                mensajeroText[i] = rs.getString(textField[i]);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS COMBO BOX
            for (int i = 0; i < comboBox.length; i++) {
                mensajeroCombo[i] = rs.getString(comboBox[i]);
            }
            //SOLICITO LOS DATOS QUE VAN A LOS DATE CHOOSER
            for (int i = 0; i < dateChooser.length; i++) {
                mensajeroDate[i] = rs.getString(dateChooser[i]);
            }
            for (int i = 0; i < checkBox.length; i++) {
                mensajeroCheck[i] = rs.getString(checkBox[i]);
            }
            // ENVIO LOS DATOS A LA CLASE FORMULARIO
            cn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//getInfoPorId " + e
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
        String statement = "";
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
            java.sql.Connection cn = Conexion.conectar();
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
            java.sql.Connection cn = Conexion.conectar();
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
