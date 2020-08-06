package mytools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Fechas {
    
    private String fecha;
    private String formatoFecha;
    
    public Fechas(String fecha, String formatoFecha){
        this.fecha = fecha;
        this.formatoFecha = formatoFecha;
    }

    public String getEdad() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(formatoFecha);
        LocalDate fechaNac = LocalDate.parse(fecha, fmt);
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(fechaNac, hoy);
        String edad = String.valueOf(periodo.getYears());       

        return edad;
    }
    
    public boolean fechaValida(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(formatoFecha);
        try {
            fmt.parse(fecha);            
            return true;
        } catch (Exception e) {
  
            return false;
        }
    }
}
