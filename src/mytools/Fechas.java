package mytools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Fechas {
        
    private DateTimeFormatter fmt;
    private LocalDate fecha;
    private LocalDate fechaHoy;
    private Period periodo;
    
    //Constructor que al ser invocado obtiene la fecha de hoy y de parametro el formato de fecha
    public Fechas(String formatoFecha){
        this.fmt = DateTimeFormatter.ofPattern(formatoFecha);
        this.fechaHoy = LocalDate.now();
    }
    
    //metodo para obtener la edad a partir de la fecha pasada como parametro
    public int getEdad(String fecha) {        
        this.fecha = LocalDate.parse(fecha, fmt);       
        this.periodo = Period.between(this.fecha, fechaHoy);       
        return (periodo.getYears());
    }
    public int getDias(String fecha){
        this.fecha = LocalDate.parse(fecha, fmt);    
        int days = (int) ChronoUnit.DAYS.between(this.fecha, fechaHoy);
        this.periodo = Period.between(this.fecha, fechaHoy);     
        return days;
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
    
    public String getYearAgo(){
        LocalDate yearAgo = LocalDate.now().minusYears(1);
        String yearago = String.valueOf(yearAgo);
        yearago = yearago.substring(0,4) + yearago.substring(5, 7) + yearago.substring(8, 10);
        return yearago;
    }
}
