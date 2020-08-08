package mytools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Fechas {
    
    
    private DateTimeFormatter fmt;
    private LocalDate fechaNac;
    private LocalDate fechaHoy;
    private Period periodo;
    
    public Fechas(String formatoFecha){
        this.fmt = DateTimeFormatter.ofPattern(formatoFecha);
        this.fechaHoy = LocalDate.now();
    }

    public String getEdad(String fecha) {        
        this.fechaNac = LocalDate.parse(fecha, fmt);       
        this.periodo = Period.between(fechaNac, fechaHoy);
        String edad = String.valueOf(periodo.getYears());       
        return edad;
    }
    
    public boolean fechaValida(String fecha){       
        try {
            this.fmt.parse(fecha);            
            return true;
        } catch (Exception e) {
  
            return false;
        }
    }
}
