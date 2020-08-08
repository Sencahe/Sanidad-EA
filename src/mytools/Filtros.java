package mytools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Filtros {

    private static int filter;
    private static int showByDestino;
    private static int order;
    
    private static String patologiaColumn;
    private static String PPSColumn;
    private static String AptitudColumn;

    public boolean expiredAnexo(String fecha) {
        if (fecha != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaUltimoAnexo = LocalDate.parse(fecha, fmt);
            LocalDate hoy = LocalDate.now();
            Period periodo = Period.between(fechaUltimoAnexo, hoy);
            int tiempo = periodo.getYears();
            return tiempo >= 1;
        } else {
            return false;
        }
    }

    public boolean PPSFilter(String pps) {
        if (pps != null) {
            return PPSColumn.equals(pps);
        } else {
            return false;
        }

    }

    public boolean aptitudFilter(String apt) {
        if (apt != null) {
            return AptitudColumn.equals(apt);
        } else {
            return false;
        }
    }
    //------------------------------------------------------------
    //SETTERS Y GETTERS
    public static int getFilter(){
        return Filtros.filter;
    }
    public static void setFilter(int filter){
        Filtros.filter = filter;
    }
    public static int getShowByDestino(){
        return Filtros.showByDestino;
    }
    public static void setShowByDestino(int destino){
        Filtros.showByDestino = destino;
    }
    public static int getOrder(){
        return Filtros.order;
    }
    public static void setOrder(int order){
        Filtros.order = order;
    }
    //-----------
    public static String getPatologiaColumn(){
        return Filtros.patologiaColumn;
    }
    public static void setPatologiaColumn(String patologia){
        Filtros.patologiaColumn = patologia;
    }
    public static String getPPSColumn(){
        return Filtros.PPSColumn;
    }
    public static void setPPSColumn(String pps) {
        Filtros.PPSColumn = pps;
    }
    public static String getAptitudColumn(){
        return Filtros.AptitudColumn; 
    }
    public static void setAptitudColumn(String apt){
        Filtros.AptitudColumn = apt;
    }
}
