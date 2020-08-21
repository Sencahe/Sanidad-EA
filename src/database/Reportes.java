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

    private String leyenda;
    private String ruta;
    private Chunk glue;
    private Document document;
    private Paragraph p, p2, p3;

    public void generarReportePersonal(Tabla tabla) {
        defaults();
        try {
            //CREACION DE LA TABLA PARA EL PDF----------------------------------               
            //float[] para el tamaño de las columnas de la tabla
            float columnsWidth[] = new float[Arreglos.getColumnasTablaLength() - 1];
            for (int i = 0; i < columnsWidth.length; i++) {
                float scaledWidth = (Arreglos.getTamañoColumnas(i) * 65) / 100;
                columnsWidth[i] = scaledWidth;
            }

            //cracion del PdfpTable
            PdfPTable[] pdfTable = new PdfPTable[4];
            for (int i = 0; i < 4; i++) {
                pdfTable[i] = new PdfPTable(columnsWidth);
                pdfTable[i].setTotalWidth(columnsWidth);
                pdfTable[i].setLockedWidth(true);
            }

            //header de la tabla   
            Paragraph header[] = new Paragraph[columnsWidth.length];
            for (int i = 0; i < columnsWidth.length; i++) {
                header[i] = new Paragraph();
                header[i].setFont(FontFactory.getFont("Times-Roman", 9, Font.BOLD, BaseColor.BLACK));
                header[i].setAlignment(Paragraph.ALIGN_CENTER);
                header[i].add(Arreglos.getColumnasTabla(i));
            }

            //ciclos para llenar las tabla
            String content;
            for (int i = 0; i < 4; i++) { //itera sobre todas las tablas
                document.add(p);
                document.add(p2);
                p3.add("CARTA DE SITUACION - PERSONAL DE " + Arreglos.getCategorias(i).toUpperCase() + "\n\n\n");
                document.add(p3);
                p3.remove(0);
                for (int j = -1; j < tabla.getTablas(i).getRowCount(); j++) { //itera sobre las filas                   
                    for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas
                        if (j == -1) {
                            pdfTable[i].addCell(header[k]);
                        } else {
                            content = String.valueOf(tabla.getTablas(i).getValueAt(j, k));
                            Paragraph para = new Paragraph();
                            para.setFont(FontFactory.getFont("Times-Roman", 8, 0, BaseColor.BLACK));
                            para.add(!content.equals("null") ? content : "");
                            pdfTable[i].addCell(para);
                            para = null;
                        }
                    }
                }
                document.add(pdfTable[i]);
                document.newPage();
            }
            document.close();
            super.getConnection().close();
            document = null;
            p = null;
            p2 = null;
            p3 = null;
            glue = null;
            header = null;
            columnsWidth = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error//BDD//Reportes// " + e
                    + "\nContactese con el desarrollador del programa para solucionar el problema");
        }

        JOptionPane.showMessageDialog(null, "Carta de Situacion confeccionada. El archivo se guardo en el Escritorio");
        System.gc();
    }

    
    public void generarReporteParte(Parte parte) {

    }

    private void defaults() {
        try {
            //creando el objeto documento para el pdf
            document = new Document();

            //recuperando los valores que deben ser guardados en la base de datos;
            PreparedStatement pst = super.getConnection().prepareStatement("SELECT (Leyenda) FROM Configuracion"
                    + " WHERE id = 1");
            ResultSet rs = pst.executeQuery();
            leyenda = rs.getString("Leyenda");

            super.getConnection().close();

            //Enviando el pdf al escritorio
            ruta = System.getProperty("user.home");
            PdfWriter.getInstance(document, new FileOutputStream(ruta + "/Desktop/Carta_De_Situacion.pdf"));

            //objeto de la libreria iText, permite colocar texto a la derecha e izquierda en la misma linea
            glue = new Chunk(new VerticalPositionMark());

            //tamaño de la hoja
            document.setPageSize(PageSize.LEGAL.rotate());
            document.open();

            //Defino el header del documento
            //texto a la izquierda --- EJERCITO ARGENTINO
            p = new Paragraph();
            p.setFont(FontFactory.getFont("Times-Roman", 16, Font.BOLD, BaseColor.BLACK));
            p.add("   Ejercito Argentino");
            //texto a la derecha ---- LEYENDA
            p.add(new Chunk(glue));
            p.setFont(FontFactory.getFont("Times-Roman", 9, Font.ITALIC, BaseColor.BLACK));
            p.add("\"" + leyenda + "\"");
            //texto a la izquierda pero abajo - REGIMIENTO DE INFANTERIA 1
            p2 = new Paragraph();
            p2.setFont(FontFactory.getFont("Times-Roman", 14, Font.BOLD, BaseColor.BLACK));
            p2.add("Regimiento de Infanteria 1\n\n\n");
            //titulo del documento
            p3 = new Paragraph();
            p3.setAlignment(Paragraph.ALIGN_CENTER);
            p3.setFont(FontFactory.getFont("Times-Roman", 18, Font.BOLD, BaseColor.BLACK));

        } catch (Exception e) {
        }
    }

}
