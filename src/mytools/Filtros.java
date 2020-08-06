package mytools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Filtros {

    public static int filtro;
    public static int filtroDestinos;
    public static int ordenamiento;
    public static String columPatologia;
    public static String filtroPPS;
    public static String filtroAptitud;

    public boolean AnexoVencido(String fecha) {
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

    public boolean FiltroPPS(String pps) {
        if (pps != null) {
            return filtroPPS.equals(pps);
        } else {
            return false;
        }

    }

    public boolean FiltroAptitud(String apt) {
        if (apt != null) {
            return filtroAptitud.equals(apt);
        } else {
            return false;
        }

    }

}
