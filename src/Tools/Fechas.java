package Tools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Fechas {

    public String getEdad(String fecha, String formatoFecha) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(formatoFecha);
        LocalDate fechaNac = LocalDate.parse(fecha, fmt);
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(fechaNac, hoy);
        String edad = String.valueOf(periodo.getYears());       
        return edad;
    }
    
    public boolean fechaValida(String fecha, String formatoFecha){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(formatoFecha);
        try {
            fmt.parse(fecha);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
