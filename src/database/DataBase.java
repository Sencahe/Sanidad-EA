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
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class DataBase {

    private static final String SQLITE_URL = "jdbc:sqlite:backup/DB.sqlite";

    private Connection cn;

    public DataBase() {
        this.cn = connect();
    }

    //------------------------CONECTAR------------------------------------------
    private Connection connect() {
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
        this.cn = connect();
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
    //--------------------------METODO ACTUALIZAR-------------------------------
    //---------------------------TABLA PERSONAL---------------------------------
    public void update(PersonnelPanel personnelPanel) {
        //OBJETOS auxiliares----------------------------------------------------
        MyDates myDates = new MyDates(MyDates.USER_DATE_FORMAT);
        String[][] grades = MyArrays.getGrades();

        //VACIADO DE LA TABLA ACTUAL--------------------------------------------
        for (int i = 0; i < MyArrays.getCategoriesLength(); i++) {
            personnelPanel.getTableModel(i).setRowCount(0);
        }

        //FILTRAR TABLA---------------------------------------------------------           
        StringBuffer statement = new StringBuffer("SELECT * FROM Personal");
        int initStatement = statement.length();
        ArrayList<Integer> filterList = personnelPanel.getFilterList();

        if (filterList.contains(PersonnelPanel.FILTER_A27)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            statement.append(personnelPanel.isBetweenTwoDates() ? "(" : "");
            statement.append("(SUBSTR(Anexo27,1,4)||SUBSTR(Anexo27,6,2)||SUBSTR(Anexo27,9,2)) <= ");
            statement.append("\"");
            statement.append(personnelPanel.getStudiesFilter());
            statement.append("\"");
            if (personnelPanel.isBetweenTwoDates()) {
                statement.append(" AND (SUBSTR(Anexo27,1,4)||SUBSTR(Anexo27,6,2)||SUBSTR(Anexo27,9,2)) >= ");
                statement.append("\"");
                statement.append(personnelPanel.getStudiesFilter2());
                statement.append("\")");
            }
        }
        if (filterList.contains(PersonnelPanel.FILTER_PPS)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            statement.append("PPS = \"");
            statement.append(personnelPanel.getPPSFilter());
            statement.append("\"");
        }
        if (filterList.contains(PersonnelPanel.FILTER_APTITUDE)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            boolean unassigned = personnelPanel.getAptitudeFilter().equals("NULL");
            statement.append(unassigned ? "Aptitud IS NULL" : personnelPanel.getAptitudeFilter());
            statement.append(unassigned ? "" : "");
        }
        if (filterList.contains(PersonnelPanel.FILTER_PATHOLOGY)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            statement.append(personnelPanel.getPathologyColumn());
            statement.append(" IS NOT NULL");
        }
        if (filterList.contains(PersonnelPanel.FILTER_AJM)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            statement.append("(Act IS NOT NULL OR Inf IS NOT NULL)");
        }
        if (filterList.contains(PersonnelPanel.FILTER_OBS)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            statement.append(personnelPanel.getObsFilter());
        }
        if (filterList.contains(PersonnelPanel.FILTER_IMC)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            statement.append("IMC ");
            statement.append(personnelPanel.getIMCoperator());
            statement.append(" ");
            statement.append(personnelPanel.getIMCfilter());
        }
        if (filterList.contains(PersonnelPanel.FILTER_ALL_PATHOLOGIES)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            statement.append("(D IS NOT NULL OR H IS NOT NULL OR A IS NOT NULL");
            statement.append(" OR T IS NOT NULL OR Act IS NOT NULL OR Inf IS NOT NULL)");
        }
        if (filterList.contains(PersonnelPanel.FILTER_GENRE)) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            statement.append("Sexo = \"");
            statement.append(personnelPanel.getGenreFilter());
            statement.append("\"");
        }

        // MOSTRAR POR DESTINOS-------------------------------------------------        
        if (personnelPanel.getShowBySubUnity() != 0) {
            statement.append(initStatement < statement.length() ? " AND " : " WHERE ");
            boolean unassigned = personnelPanel.getShowBySubUnity() == -1;
            statement.append(unassigned ? "Destino IS NULL" : "Destino =\"");
            statement.append(unassigned ? "" : MyArrays.getSubUnities(personnelPanel.getShowBySubUnity()));
            statement.append(unassigned ? "" : "\"");
        }

        //ORDENAR LA TABLA------------------------------------------------------ 
        statement.append(MyArrays.getOrderPersonnel(personnelPanel.getRowOrdering()));


        //CONSULTA A BASE DE DATOS----------------------------------------------        
        try {
            if (cn == null || cn.isClosed()) {
                cn = connect();
            }
            //consulta a la base de datos
            PreparedStatement pst = cn.prepareStatement(statement.toString());
            ResultSet rs = pst.executeQuery();

            //llenado de la tabla
            int num[] = new int[MyArrays.getCategoriesLength()]; //arreglo para el numero de orden en las 4 tabla distintas 
            int categorie;
            int aux;

            Object[] row = new Object[MyArrays.getPersonnelPanelDBLength() + 1];
            String[] columnsName = MyArrays.getPersonnelPanelDB();

            while (rs.next()) {
                categorie = rs.getInt("Categoria"); //obteniendo la categoria                                                           
                row[0] = ++num[categorie];
                aux = 1; //indice inicial para el resto de los datos que iran en la fila
                for (String i : columnsName) {
                    if (rs.getObject(i) != null) {
                        switch (i) {
                            case "Grado":
                                row[aux] = grades[categorie][rs.getInt("Grado")];
                                break;
                            case "Apellido":
                                row[aux] = rs.getString("Apellido") + " " + rs.getString("Nombre");
                                break;
                            case "Anexo27":
                                row[aux] = myDates.toUserDate(rs.getString("Anexo27"));
                                break;
                            case "FechaNacimiento":
                                row[aux] = myDates.getEdad(rs.getString(i));
                                break;
                            default:
                                row[aux] = rs.getObject(i);
                                break;
                        }
                    }
                    aux++;
                }
                personnelPanel.getTableModel(categorie).addRow(row);
                Arrays.fill(row, null);
            }
            cn.close();
            cn = null;
            row = null;
            num = null;
            rs = null;
            pst = null;
            statement = null;
            myDates = null;

            //Al finalizar el llenado de la tablas se actualizan los labels con el conteo
            String[] categories = MyArrays.getCategories();
            int rowCount;
            int total = 0;

            for (int i = 0; i < categories.length + 1; i++) {
                if (i != categories.length) {
                    rowCount = personnelPanel.getTables(i).getRowCount();
                    personnelPanel.getSummary(i).setText(categories[i].toUpperCase() + ":  " + rowCount);
                    total += rowCount;
                } else {
                    personnelPanel.getSummary(i).setText("TOTAL:  " + total);
                }
            }

            categories = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Actualizar " + e
                    + "\nContactese con el desarrolador del programa para solucionar el problema.");
        }
    }

    //--------------------------------------------------------------------------
    //------------------------METODO ACTUALIZAR---------------------------------
    //---------------------------TABLA PARTE------------------------------------
    public void update(SickPanel sickPanel) {
        MyDates myDates = new MyDates(MyDates.USER_DATE_FORMAT);
        String[][] grades = MyArrays.getGrades();

        //vaciado del Parte
        for (int i = 0; i < 4; i++) {
            sickPanel.getTableModel(i).setRowCount(0);
        }

        try {
            if (cn == null || cn.isClosed()) {
                cn = connect();
            }
            //Consulto los datos necesarios de Personal y de Parte para llenar las tablas de Parte de Sanidad
            PreparedStatement pst = cn.prepareStatement("SELECT Categoria, Grado, Apellido, Nombre, Destino, Expediente "
                    + ", Diagnostico, CIE, Desde, Hasta, Observacion, Parte.id, TipoParte "
                    + "FROM Personal INNER JOIN Parte ON Personal.id = Parte.id_personal  "
                    + "ORDER BY Categoria ASC, Grado DESC, Apellido ASC, Nombre ASC");
            ResultSet rs = pst.executeQuery();

            //variables y objetos para recuperar la informacion y desplegarla en las tablas de Parte
            int num[] = new int[MyArrays.getSickTypesLength()];
            Object[] row = new Object[MyArrays.getSickColumnsLength()];

            int categorie;
            int sickType;
            String cie;
            String until;
            String since;
            String record;

            while (rs.next()) {

                //informacion de Personal
                categorie = rs.getInt("Categoria");
                row[1] = grades[categorie][rs.getInt("Grado")];
                row[2] = rs.getString("Apellido") + " " + rs.getString("Nombre");
                row[3] = rs.getString("Destino");

                //informacion de Parte
                cie = rs.getString("CIE");
                until = rs.getString("Hasta");
                since = rs.getString("Desde");
                record = rs.getString("Expediente");

                row[4] = rs.getString("Diagnostico");
                row[5] = cie != null ? cie : "";
                row[6] = myDates.toUserDate(since);
                row[7] = myDates.toUserDate(until);
                row[8] = myDates.getDays(since);
                row[9] = record != null ? record : "No";
                row[10] = rs.getString("Observacion");
                row[11] = rs.getInt("id");
                sickType = rs.getInt("TipoParte");
                /*
                si getDays() con la fecha de until devuelve un numero positivo 
                significa que NO PASO NOVEDAD, es decir que no se presento en la 
                fecha de control. En consecuencia se lo colocara en esa tabla
                sin tener que modificar su tipo de parte 
                 */
                if (myDates.getDays(until) - 1 > 0) {
                    row[0] = ++num[SickPanel.EXPIRED_TABLE];
                    sickPanel.getTableModel(SickPanel.EXPIRED_TABLE).addRow(row);
                } else {
                    row[0] = ++num[sickType];
                    sickPanel.getTableModel(sickType).addRow(row);
                }

                Arrays.fill(row, null);
            }
            cn.close();
            row = null;
            num = null;
            myDates = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Actualizar " + e
                    + "\nContactese con el desarrolador para resolver el problema.");
        }
    }
}
