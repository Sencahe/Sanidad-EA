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
import javax.swing.JOptionPane;
import panels.Parte;

public class Reportes extends BaseDeDatos {

    public void generarReportePersonal(Tabla tabla) {

        Document documento = new Document();

        try {
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT (Leyenda) FROM Configuracion"
                    + " WHERE id = 1");
            ResultSet rs = pst.executeQuery();

            //Enviando el pdf al escritorio
            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Carta_De_Situacion.pdf"));

            //objeto de la libreria IText, permite colocar texto a la derecha e izquierda en la misma linea
            Chunk glue = new Chunk(new VerticalPositionMark());

            //tamaño de la hoja
            documento.setPageSize(PageSize.LEGAL.rotate());
            documento.open();

            //Defino el header del documento
            //texto a la izquierda --- EJERCITO ARGENTINO
            Paragraph p = new Paragraph();
            p.setFont(FontFactory.getFont("Times Roman", 16, Font.BOLD, BaseColor.BLACK));
            p.add("   Ejercito Argentino");
            //texto a la derecha ---- LEYENDA
            p.add(new Chunk(glue));
            p.setFont(FontFactory.getFont("Times Roman", 9, Font.ITALIC, BaseColor.BLACK));
            p.add("\"" + rs.getString("Leyenda") + "\"");            
            //texto a la izquierda pero abajo - REGIMIENTO DE INFANTERIA 1
            Paragraph p2 = new Paragraph();
            p2.setFont(FontFactory.getFont("Times Roman", 14, Font.BOLD, BaseColor.BLACK));
            p2.add("Regimiento de Infanteria 1\n\n\n");            
            //titulo del documento
            Paragraph p3 = new Paragraph();
            p3.setAlignment(Paragraph.ALIGN_CENTER);
            p3.setFont(FontFactory.getFont("Times Roman", 18, Font.BOLD, BaseColor.BLACK));
       
            //-------------------------------------------------------------------
            //CREACION DE LA TABLA PARA EL PDF----------------------------------
            //float[] para el tamaño de las columnas de la tabla
            float columnsWidth[] = new float[10];
            for (int i = 0; i < 10; i++) {
                float userUnit = Arreglos.getTamañoColumnas(i);
                columnsWidth[i] = userUnit;
            }
            
             //header de la tabla   
            Paragraph header[] = new Paragraph[10];
            for (int i = 0; i < 10; i++) {
                header[i] = new Paragraph();
                header[i].setFont(FontFactory.getFont("Times Roman", 12, Font.BOLD, BaseColor.BLACK));
                header[i].add(Arreglos.getColumnasTabla(i));
            }
            
            
            //cracion del pdfpTable
            PdfPTable table[] = new PdfPTable[4];
            for (int i = 0; i < 4; i++) {
                table[i] = new PdfPTable(columnsWidth);
                table[i].setTotalWidth(columnsWidth);
                table[i].setLockedWidth(true);
            }

           

            //ciclos para llenar las tabla
            String content;
            for (int i = 0; i < 4; i++) { //itera sobre todas las tablas
                documento.add(p);
                documento.add(p2);
                p3.add("CARTA DE SITUACION - PERSONAL DE " + Arreglos.getCategorias(i).toUpperCase() + "\n\n\n");
                documento.add(p3);
                p3.remove(0);
                for (int j = 0; j < tabla.getTablas(i).getRowCount()+1; j++) { //itera sobre las filas
                    for (int k = 0; k < 10; k++) { //itera sobre las columnas
                        if (j == 0) {
                            table[i].addCell(header[k]);
                        } else {
                            content = String.valueOf(tabla.getTablas(i).getValueAt(j-1, k));
                            table[i].addCell(content != "null" ? content : "");
                        }
                    }
                }
                documento.add(table[i]);
                documento.newPage();
            }

            documento.close();
            JOptionPane.showMessageDialog(null,"Carta de Situacion confeccionada. El archivo se guardo en el Escritorio");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void generarReporteParte(Parte parte) {

    }
}
