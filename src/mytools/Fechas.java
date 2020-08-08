package mytools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Fechas {
        
    private DateTimeFormatter fmt;
    private LocalDate fechaNac;
    private LocalDate fechaHoy;
    private Period periodo;
    
    //Constructor que al ser invocado obtiene la fecha de hoy y de parametro el formato de fecha
    public Fechas(String formatoFecha){
        this.fmt = DateTimeFormatter.ofPattern(formatoFecha);
        this.fechaHoy = LocalDate.now();
    }
    
    //metodo para obtener la edad a partir de la fecha pasada como parametro
    public String getEdad(String fecha) {        
        this.fechaNac = LocalDate.parse(fecha, fmt);       
        this.periodo = Period.between(fechaNac, fechaHoy);
        String edad = String.valueOf(periodo.getYears());       
        return edad;
    }
    //metodo para validar la fecha
    public boolean fechaValida(String fecha){       
        try {
            this.fmt.parse(fecha);            
            return true;
        } catch (Exception e) {  
            return false;
        }
    }
}
