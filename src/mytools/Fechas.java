package mytools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;

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
        return days + 1;
    }
    
    public int getPeriodoDias(String desde, String hasta){
        LocalDate fechaDesde = LocalDate.parse(desde, fmt);
        LocalDate fechaHasta = LocalDate.parse(hasta, fmt);
        int days = (int) ChronoUnit.DAYS.between(fechaDesde, fechaHasta);
        return days + 1;
    }
    
    //------------------METODOS QUE VALIDAN LAS FECHAS--------------------------
    public boolean fechaValida(String fecha){       
        try {
            this.fmt.parse(fecha);            
            return true;
        } catch (Exception e) {  
            return false;
        }
    }
    //valida la fecha entre el desde y el hasta 
    public boolean fechaParteValida(String desde, String hasta){
        LocalDate fechaDesde = LocalDate.parse(desde, fmt);
        LocalDate fechaHasta =  LocalDate.parse(hasta, fmt);
        if(fechaDesde.isAfter(fechaHasta) || fechaDesde.isEqual(fechaHasta)){
            JOptionPane.showMessageDialog(null,"La fecha 'Desde' no puede ser posterior o igual a la fecha 'Hasta'.");
            return false;
        } else if(fechaHasta.isBefore(this.fechaHoy)){
            JOptionPane.showMessageDialog(null,"La fecha 'Hasta' no puede ser anterior a la fecha de hoy.");
            return false;
        } 
        return true;
    }
    //valida la fecha entre el desde y el hasta, ademas compara que el desde no sea menor que el incial
    public boolean fechaParteValida(String desde, String hasta, String flagDesde){
        LocalDate fechaDesde = LocalDate.parse(desde, fmt);
        LocalDate fechaFlagDesde = LocalDate.parse(flagDesde,fmt);
        if(fechaDesde.isBefore(fechaFlagDesde) || (fechaDesde.isEqual(fechaFlagDesde))){
            JOptionPane.showMessageDialog(null,"Si modifica el tipo de Parte, "
                    + "la fecha 'Desde' no puede ser anterior o igual a la fecha inicial: " + flagDesde);
            return false;
        } else {
            return fechaParteValida(desde,hasta);
        }                
    }
    
    //Obtener la fecha de hoy en formato "dd/MM/yyyy"
    public String getHoy(){
        String hoyFecha = String.valueOf(fechaHoy);
        String hoy = hoyFecha.substring(8,10) + "/" + hoyFecha.substring(5, 7) + "/" + hoyFecha.substring(0,4);      
        return hoy;
    
    }

      
    
    public String getYearAgo(){
        LocalDate yearAgo = LocalDate.now().minusYears(1);
        String year = String.valueOf(yearAgo);
        year = year.substring(0,4) + year.substring(5, 7) + year.substring(8, 10);
        return year;
    }

    
    
    
}
