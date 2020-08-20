package database;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import mytools.Arreglos;
import panels.Tabla;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.control.Cell;
import javax.swing.JOptionPane;
import panels.Parte;

public class Reportes extends BaseDeDatos {

    public void generarReportePersonal(Tabla tabla) {

        Document documento = new Document();
        String leyenda = "";
        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT (Leyenda) FROM Configuracion"
                    + " WHERE id = 1");
            ResultSet rs = pst.executeQuery();
            leyenda = rs.getString("Leyenda");
            super.getConnection().close();

            //Enviando el pdf al escritorio
            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Carta_De_Situacion.pdf"));

        } catch (Exception e) {
            System.out.println(e);
        }

        //objeto de la libreria IText, permite colocar texto a la derecha e izquierda en la misma linea
        Chunk glue = new Chunk(new VerticalPositionMark());

        //tamaño de la hoja
        documento.setPageSize(PageSize.LEGAL.rotate());

        documento.open();

        //Defino el header del documento
        //texto a la izquierda --- EJERCITO ARGENTINO
        Paragraph p = new Paragraph();
        p.setFont(FontFactory.getFont("Arial", 16, Font.BOLD, BaseColor.BLACK));
        p.add("   Ejercito Argentino");
        //texto a la derecha ---- LEYENDA
        p.add(new Chunk(glue));
        p.setFont(FontFactory.getFont("Arial", 9, Font.ITALIC, BaseColor.BLACK));

        p.add("\"" + leyenda + "\"");
        //texto a la izquierda pero abajo - REGIMIENTO DE INFANTERIA 1
        Paragraph p2 = new Paragraph();
        p2.setFont(FontFactory.getFont("Arial", 14, Font.BOLD, BaseColor.BLACK));
        p2.add("Regimiento de Infanteria 1\n\n\n");
        //titulo del documento
        Paragraph p3 = new Paragraph();
        p3.setAlignment(Paragraph.ALIGN_CENTER);
        p3.setFont(FontFactory.getFont("Arial", 18, Font.BOLD, BaseColor.BLACK));

        //-------------------------------------------------------------------
        //CREACION DE LA TABLA PARA EL PDF----------------------------------
        String content;
        
        //float[] para el tamaño de las columnas de la tabla
        float columnsWidth[] = new float[20];
        for (int i = 0; i < columnsWidth.length; i++) {
            float userUnit = (Arreglos.getTamañoColumnas(i) * 65) / 100;
            columnsWidth[i] = userUnit;
        }

        //header de la tabla   
        Paragraph header[] = new Paragraph[columnsWidth.length];
        for (int i = 0; i < columnsWidth.length; i++) {
            header[i] = new Paragraph();
            header[i].setFont(FontFactory.getFont("Arial", 9, Font.BOLD, BaseColor.BLACK));
            header[i].setAlignment(Paragraph.ALIGN_CENTER);
            header[i].add(Arreglos.getColumnasTabla(i));
        }

        //cracion del pdfpTable
        PdfPTable table[] = new PdfPTable[4];
        try {
            for (int i = 0; i < 4; i++) {
                table[i] = new PdfPTable(columnsWidth);
                table[i].setTotalWidth(columnsWidth);
                table[i].setLockedWidth(true);
            }
        } catch (Exception e) {
        }

        //ciclos para llenar las tabla
        for (int i = 0; i < 4; i++) { //itera sobre todas las tablas
            try {
                documento.add(p);
                documento.add(p2);
                p3.add("CARTA DE SITUACION - PERSONAL DE " + Arreglos.getCategorias(i).toUpperCase() + "\n\n\n");
                documento.add(p3);
                p3.remove(0);
            } catch (Exception e) {
            }
            for (int j = -1; j < tabla.getTablas(i).getRowCount(); j++) { //itera sobre las filas                   

                for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas
                    if (j == -1) {
                        table[i].addCell(header[k]);
                    } else {
                        content = String.valueOf(tabla.getTablas(i).getValueAt(j, k));
                        Paragraph cell = new Paragraph();
                        cell.setFont(FontFactory.getFont("Arial", 8, 0, BaseColor.BLACK));
                        cell.add(!content.equals("null") ? content : "");
                        table[i].addCell(cell);
                    }
                }
            }

            try {
                documento.add(table[i]);
                documento.newPage();
            } catch (Exception e) {
            }

        }

        documento.close();
        JOptionPane.showMessageDialog(null, "Carta de Situacion confeccionada. El archivo se guardo en el Escritorio");

    }

    public void generarReporteParte(Parte parte) {

    }
}
