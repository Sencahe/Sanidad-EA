package mytools;

import java.text.DecimalFormat;
import javax.swing.JOptionPane;

public class CalculoIMC {

    public String[] calcularIMC(double peso, double altura) {
        String index = "0";
        String IMCFinal = "";
        double IMC = peso / (altura * altura);
        DecimalFormat redondear = new DecimalFormat("0.00");
        IMCFinal = redondear.format(IMC);
        //ciclo para cambiar "," POR "." en el resultado del decimal format
        for (int i = 0; i < IMCFinal.length(); i++) {
            if (IMCFinal.charAt(i) == ',') {
                IMCFinal = IMCFinal.substring(0, i) + "." + IMCFinal.substring(i + 1);
            }
        }
        if (IMC >= 17.5 && IMC < 25) {
            index = "1";
        }
        if (IMC >= 25 && IMC < 30) {
            index = "2";
        }
        if (IMC >= 30 && IMC < 35) {
            index = "3";
        }
        if (IMC >= 35) {
            index = "4";
        }
        if (IMC < 17.5) {
            index = "5";
        }
        String[] datos = {IMCFinal, index};
        return datos;
    }

}
