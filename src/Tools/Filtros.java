package Tools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Filtros {

    public static int filtro = 0;
    public static int filtroDestinos = 0;
    public static int ordenamiento = 0;
    public static String columPatologia = "";
    public static String FiltroPPS;
    public static String FiltroAptitud;

    public boolean AnexoVencido(String fecha) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaUltimoAnexo = LocalDate.parse(fecha, fmt);
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(fechaUltimoAnexo, hoy);
        int tiempo = periodo.getYears();
        return tiempo >= 1;
    }

    public boolean FiltroPPS(String pps) {
        return FiltroPPS.equals(pps);
    }

    public boolean FiltroAptitud(String apt) {
        return FiltroAptitud.equals(apt);
    }

    public boolean FiltroPatologias(String ptg) {
        return ptg.equals("X");
    }

}
