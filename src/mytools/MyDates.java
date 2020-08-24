package mytools;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.swing.JOptionPane;

public class MyDates {

    public static final String USER_DATE_FORMAT = "dd/MM/yyyy";

    private DateTimeFormatter dateTimeFormatter;
    private SimpleDateFormat simpleDateFormat;
    private LocalDate fechaHoy;

    //Constructor que al ser invocado obtiene la fecha de hoy y de parametro el formato de fecha
    public MyDates(String formatoFecha) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(formatoFecha);
        this.simpleDateFormat = new SimpleDateFormat(formatoFecha);
        this.fechaHoy = LocalDate.now();
    }

    //-------------------------------------------------------------------------
    public LocalDate toLocalDate(Date date) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
        return localDate;
    }

    public LocalDate toLocalDate(String localDate) {
        return LocalDate.parse(localDate);
    }

    //------------------
    public Date toDate(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        return date;
    }

    public Date toDate(String localDate) {
        LocalDate localdate = LocalDate.parse(localDate);
        return toDate(localdate);
    }

    //-------------------
    public String toUserDate(Date date) {
        return simpleDateFormat.format(date);
    }

    public String toUserDate(LocalDate localDate) {
        return simpleDateFormat.format(toDate(localDate));
    }

    public String toUserDate(String stringDate) {
        LocalDate localDate = LocalDate.parse(stringDate);
        return simpleDateFormat.format(toDate(localDate));
    }

    //--------------------------------------------------------------------------
    //metodo para obtener la edad a partir de la fecha pasada como parametro
    public int getEdad(String bornDate) {
        LocalDate localDate = LocalDate.parse(bornDate);
        Period periodo = Period.between(localDate, fechaHoy);
        return (periodo.getYears());
    }

    public int getDias(String date) {
        LocalDate localDate = LocalDate.parse(date);
        int days = (int) ChronoUnit.DAYS.between(localDate, fechaHoy);
        return days + 1;
    }

    public int getPeriodoDias(LocalDate since, LocalDate until) {
        return ((int) ChronoUnit.DAYS.between(since, until)) + 1;

    }

    public int getYearAgo() {
        LocalDate yearAgo = LocalDate.now().minusYears(1);
        String year = String.valueOf(yearAgo);
        year = year.substring(0, 4) + year.substring(5, 7) + year.substring(8, 10);
        return Integer.parseInt(year);
    }

    //------------------METODOS QUE VALIDAN LAS FECHAS--------------------------
    public boolean fechaValida(String fecha) {
        try {
            this.dateTimeFormatter.parse(fecha);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //valida la fecha entre el desde y el hasta 
    public boolean fechaParteValida(Date desde, Date hasta) {
        LocalDate fechaDesde = toLocalDate(desde);
        LocalDate fechaHasta = toLocalDate(hasta);
        if (fechaDesde.isAfter(fechaHasta) || fechaDesde.isEqual(fechaHasta)) {
            JOptionPane.showMessageDialog(null, "La fecha 'Desde' no puede ser posterior o igual a la fecha 'Hasta'.");
            return false;
        } else if (fechaHasta.isBefore(this.fechaHoy)) {
            JOptionPane.showMessageDialog(null, "La fecha 'Hasta' no puede ser anterior a la fecha de hoy.");
            return false;
        }
        return true;
    }

    //valida la fecha entre el desde y el hasta, ademas compara que el desde no sea menor que el incial
    public boolean fechaParteValida(Date desde, Date hasta, Date flagDesde) {
        LocalDate fechaDesde = toLocalDate(desde);
        LocalDate fechaFlagDesde = toLocalDate(flagDesde);
        if (fechaDesde.isBefore(fechaFlagDesde) || (fechaDesde.isEqual(fechaFlagDesde))) {
            JOptionPane.showMessageDialog(null, "Si modifica el tipo de Parte, "
                    + "la fecha 'Desde' no puede ser anterior o igual a la fecha inicial: " + flagDesde);
            return false;
        } else {
            return fechaParteValida(desde, hasta);
        }
    }

}
