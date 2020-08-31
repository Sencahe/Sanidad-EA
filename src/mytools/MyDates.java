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
    private LocalDate todayLocalDate;

    //Constructor que al ser invocado obtiene la fecha de hoy y de parametro el formato de fecha
    public MyDates(String dateFormat) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        this.simpleDateFormat = new SimpleDateFormat(dateFormat);
        this.todayLocalDate = LocalDate.now();
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
    
    public String userDateToLocalDate(String userDate){
        LocalDate localDate = LocalDate.parse(userDate, dateTimeFormatter);
        return localDate.toString();
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
        Period periodo = Period.between(localDate, todayLocalDate);
        return (periodo.getYears());
    }

    public int getDays(String date) {
        LocalDate localDate = LocalDate.parse(date);
        int days = (int) ChronoUnit.DAYS.between(localDate, todayLocalDate);
        return days + 1;
    }

    public int getPeriodOfDays(LocalDate since, LocalDate until) {
        return ((int) ChronoUnit.DAYS.between(since, until)) + 1;

    }

    public int getYearAgo() {
        LocalDate yearAgo = LocalDate.now().minusYears(1);
        String year = String.valueOf(yearAgo);
        year = year.substring(0, 4) + year.substring(5, 7) + year.substring(8, 10);
        return Integer.parseInt(year);
    }

    //------------------METODOS QUE VALIDAN LAS FECHAS--------------------------

    //valida la fecha entre el desde y el hasta 
    public boolean sickValidDate(Date since, Date until) {
        LocalDate sinceLocalDate = toLocalDate(since);
        LocalDate untilLocalDate = toLocalDate(until);
        if (sinceLocalDate.isAfter(untilLocalDate) || sinceLocalDate.isEqual(untilLocalDate)) {
            JOptionPane.showMessageDialog(null, "La fecha 'Desde' no puede ser posterior o igual a la fecha 'Hasta'.");
            return false;
        } else if (untilLocalDate.isBefore(this.todayLocalDate)) {
            JOptionPane.showMessageDialog(null, "La fecha 'Hasta' no puede ser anterior a la fecha de hoy.");
            return false;
        }
        return true;
    }

    //valida la fecha entre el desde y el hasta, ademas compara que el desde no sea menor que el incial
    public boolean sickValidDate(Date since, Date until, Date flagSince) {
        LocalDate sinceLocalDate = toLocalDate(since);
        LocalDate flagSinceLocalDate = toLocalDate(flagSince);
        if (sinceLocalDate.isBefore(sinceLocalDate) || (sinceLocalDate.isEqual(sinceLocalDate))) {
            JOptionPane.showMessageDialog(null, "Si modifica el tipo de Parte, "
                    + "la fecha 'Desde' no puede ser anterior o igual a la fecha inicial: " + flagSinceLocalDate);
            return false;
        } else {
            return MyDates.this.sickValidDate(since, until);
        }
    }

}
