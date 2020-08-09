package mytools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Filtros {

    public static int filter;
    public static int showByDestino;
    public static int order;
    
    public static String patologiaColumn;
    public static String PPSColumn;
    public static String aptitudColumn;

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
            return aptitudColumn.equals(apt);
        } else {
            return false;
        }
    }

}
