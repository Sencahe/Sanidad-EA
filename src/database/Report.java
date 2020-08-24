package database;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import mytools.MyArrays;
import panels.PersonnelPanel;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import panels.SickPanel;
import panels.ReCountPanel;

public class Report extends DataBase {

    private String leyenda;
    private String ruta;
    private Chunk glue;
    private Document document;
    private Paragraph p, p2, p3, p4;

    private int dia;
    private String mes;
    private int año;

    public Report() {
        LocalDate hoy = LocalDate.now();
        dia = hoy.getDayOfMonth();
        mes = MyArrays.getMes(hoy.getMonthValue());
        año = hoy.getYear();
    }

    public void generarCartaDeSituacion(PersonnelPanel tabla) {
        defaults("Carta De Situacion.pdf");
        try {
            //tamaño de la hoja                    
            document.setPageSize(PageSize.LEGAL.rotate());
            document.open();
            //CREACION DE LA TABLA PARA EL PDF----------------------------------               
            //float[] para el tamaño de las columnas de la tabla
            float columnsWidth[] = new float[MyArrays.getColumnasTablaLength() - 1];
            for (int i = 0; i < columnsWidth.length; i++) {
                float scaledWidth = (MyArrays.getTamañoColumnas(i) * 65) / 100;
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
                header[i].add(MyArrays.getColumnasTabla(i));
            }

            //ciclos para llenar las tabla
            p3.setFont(FontFactory.getFont("Times-Roman", 18, Font.BOLD, BaseColor.BLACK));
            String content;
            for (int i = 0; i < 4; i++) { //itera sobre todas las tablas
                document.add(p);
                document.add(p2);
                p3.add("CARTA DE SITUACION - PERSONAL DE " + MyArrays.getCategorias(i).toUpperCase() + "\n\n\n");
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
                document.add(p4);
                document.newPage();
            }
            document.close();
            super.getConnection().close();
            document = null;
            p = null;
            p2 = null;
            p3 = null;
            p4 = null;
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

    public void generarReporteLista(PersonnelPanel tabla, String titulo, boolean civilians) {
        int cicles = civilians ? 4 : 3;
        defaults("Lista.pdf");
        try {
            //tamaño de la hoja                    
            document.setPageSize(PageSize.LEGAL);
            document.open();
            //CREACION DE LA TABLA PARA EL PDF----------------------------------               
            //float[] para el tamaño de las columnas de la tabla
            int cantColumnas = 6;
            float columnsWidth[] = new float[cantColumnas];
            for (int i = 0; i < columnsWidth.length; i++) {
                if (i != 5) {
                    float scaledWidth = (MyArrays.getTamañoColumnas(i) * 65) / 100;
                    columnsWidth[i] = scaledWidth;
                } else {
                    float scaledWidth = 150;
                    columnsWidth[i] = scaledWidth;
                }
            }

            PdfPTable pdfTable = new PdfPTable(columnsWidth);
            pdfTable.setTotalWidth(columnsWidth);
            pdfTable.setLockedWidth(true);

            //encabezado
            document.add(p);
            document.add(p2);
            p3.setFont(FontFactory.getFont("Times-Roman", 12, Font.BOLD, BaseColor.BLACK));
            p3.add(titulo + "\n\n\n");
            document.add(p3);
            p3.remove(0);

            //header de la tabla   
            Paragraph header[] = new Paragraph[columnsWidth.length];
            for (int i = 0; i < columnsWidth.length; i++) {
                header[i] = new Paragraph();
                header[i].setFont(FontFactory.getFont("Times-Roman", 10, Font.BOLD, BaseColor.BLACK));
                header[i].setAlignment(Paragraph.ALIGN_CENTER);
                if (i != 5) {
                    header[i].add(MyArrays.getColumnasTabla(i));
                } else {
                    header[i].add("Observaciones");
                }
                pdfTable.addCell(header[i]);
            }

            //ciclos para llenar las tabla            
            String content;
            int num = 0;
            for (int i = 0; i < cicles; i++) { //itera sobre todas las tablas
                for (int j = 0; j < tabla.getTablas(i).getRowCount(); j++) { //itera sobre las filas                   
                    for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas
                        if (k == 0) {
                            content = String.valueOf(++num);
                        } else if (k == 5) {
                            content = "";
                        } else {
                            content = String.valueOf(tabla.getTablas(i).getValueAt(j, k));
                        }
                        Paragraph para = new Paragraph();
                        para.setFont(FontFactory.getFont("Times-Roman", 9, 0, BaseColor.BLACK));
                        para.add(!content.equals("null") ? content : "");
                        pdfTable.addCell(para);
                        para = null;

                    }
                }
            }
            document.add(pdfTable);
            document.add(p4);
            document.close();
            super.getConnection().close();
            document = null;
            p = null;
            p2 = null;
            p3 = null;
            p4 = null;
            glue = null;
            header = null;
            columnsWidth = null;

            JOptionPane.showMessageDialog(null, titulo + " confeccionado. El archivo se guardo en el Escritorio");
            System.gc();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void generarReporteParte(SickPanel parte, boolean diagnostico) {
        //obener la fecha del dia presente

        String confidencial = diagnostico ? " CONFIDENCIAL" : "";

        defaults("Parte de Sanidad " + dia + " " + mes.substring(0, 3).toUpperCase()
                + " " + año + confidencial + ".pdf");
        try {
            //tamaño de la hoja
            document.setPageSize(PageSize.LEGAL);
            document.open();
            //CREACION DE LA TABLA PARA EL PDF----------------------------------                           
            String[] columnasParte = MyArrays.getColumnasParte();
            int[] tamañoColumnas = MyArrays.getTamañoColumnParte();
            int saltear; //variable auxiliar en caso de recuperar la informacion sin diagnostico

            //float[] para el tamaño escalado de las columnas de la tabla 
            float columnsWidth[] = new float[columnasParte.length - 1 - (!diagnostico ? 2 : 0)];
            for (int i = 0; i < columnsWidth.length; i++) {
                saltear = i >= 4 && !diagnostico ? 2 : 0;
                float scaledWidth = (tamañoColumnas[i + saltear] * 50) / 100;
                columnsWidth[i] = scaledWidth;
            }

            //cracion del PdfpTable
            PdfPTable[] pdfTable = new PdfPTable[MyArrays.getTiposDeParteLength()];
            for (int i = 0; i < 4; i++) {
                pdfTable[i] = new PdfPTable(columnsWidth);
                pdfTable[i].setTotalWidth(columnsWidth);
                pdfTable[i].setLockedWidth(true);
                pdfTable[i].setHorizontalAlignment(Element.ALIGN_LEFT);
            }

            //header de la tabla   
            Paragraph header[] = new Paragraph[columnsWidth.length];
            for (int i = 0; i < columnsWidth.length; i++) {
                saltear = i >= 4 && !diagnostico ? 2 : 0;
                header[i] = new Paragraph();
                header[i].setFont(FontFactory.getFont("Times-Roman", 8, Font.BOLD, BaseColor.BLACK));
                header[i].add(columnasParte[i + saltear]);
            }

            //encabezado
            String content;
            document.add(p);
            document.add(p2);
            p3.setFont(FontFactory.getFont("Times-Roman", 12, Font.BOLD, BaseColor.BLACK));
            p3.add("PARTE DIARIO DE SANIDAD DE LA UNIDAD DEL " + dia + " DE " + mes.toUpperCase() + " DE " + año + "\n\n");
            document.add(p3);
            Paragraph p5 = new Paragraph();
            p5.setFont(FontFactory.getFont("Times-Roman", 10, Font.BOLD, BaseColor.BLACK));

            //ciclos para llenar las tabla
            for (int i = 0; i < pdfTable.length; i++) { //itera sobre todas las tablas

                p5.add(MyArrays.getTiposDeParte(i).toUpperCase() + "\n\n");
                document.add(p5);
                p5.remove(0);

                for (int j = -1; j < parte.getTablas(i).getRowCount(); j++) { //itera sobre las filas                         
                    for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas
                        if (j == -1) {
                            //agregar los headers
                            pdfTable[i].addCell(header[k]);
                        } else {
                            //agregar el contenido de las celdas
                            saltear = k >= 4 && !diagnostico ? 2 : 0;
                            content = String.valueOf(parte.getTablas(i).getValueAt(j, k + saltear));
                            Paragraph para = new Paragraph();
                            para.setFont(FontFactory.getFont("Times-Roman", 7, 0, BaseColor.BLACK));
                            para.add(!content.equals("null") ? content : "");
                            pdfTable[i].addCell(para);
                            para = null;
                        }
                    }
                }
                document.add(pdfTable[i]);
            }

            document.add(p4);
            document.close();
            super.getConnection().close();
            document = null;
            p = null;
            p2 = null;
            p3 = null;
            p5 = null;
            glue = null;
            header = null;
            columnsWidth = null;

            JOptionPane.showMessageDialog(null, "Parte de sanidad confeccionado. El archivo se guardo en el Escritorio");
            System.gc();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void generarReporteRecuento(ReCountPanel recuento) {

        defaults("Recuento Parte de Sanidad.pdf");
        try {
            //tamaño de la hoja
            document.setPageSize(PageSize.LEGAL.rotate());
            document.open();

            //encabezado
            document.add(p);
            document.add(p2);
            p3.setFont(FontFactory.getFont("Times-Roman", 12, Font.BOLD, BaseColor.BLACK));
            p3.add("RECUENTO PARTE DE SANIDAD A LA FECHA DEL " + dia + " DE " + mes.toUpperCase() + " DE" + año + "\n\n\n");
            document.add(p3);
            p3.remove(0);

            //CREACION DE LA TABLA PARA EL PDF----------------------------------                           
            String[] columnasRecuento = MyArrays.getColumnasRecuento();
            int[] tamañoColumnas = MyArrays.getTamañoColumnasRecuento();
            //float[] para el tamaño escalado de las columnas de la tabla 
            float columnsWidth[] = new float[columnasRecuento.length];
            for (int i = 0; i < columnsWidth.length; i++) {
                float scaledWidth = (tamañoColumnas[i] * 65) / 100;
                columnsWidth[i] = scaledWidth;
            }

            PdfPTable pdfTable = new PdfPTable(columnsWidth);
            pdfTable.setTotalWidth(columnsWidth);
            pdfTable.setLockedWidth(true);

            //header de la tabla   
            Paragraph header[] = new Paragraph[columnsWidth.length];
            for (int i = 0; i < columnsWidth.length; i++) {
                header[i] = new Paragraph();
                header[i].setFont(FontFactory.getFont("Times-Roman", 10, Font.BOLD, BaseColor.BLACK));
                header[i].setAlignment(Paragraph.ALIGN_CENTER);
                header[i].add(columnasRecuento[i]);
                pdfTable.addCell(header[i]);
            }

            //ciclos para llenar la tabla
            String content;
            for (int j = 0; j < recuento.getTable().getRowCount(); j++) { //itera sobre las filas                   
                for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas
                    content = String.valueOf(recuento.getTable().getValueAt(j, k));
                    Paragraph para = new Paragraph();
                    para.setFont(FontFactory.getFont("Times-Roman", 9, 0, BaseColor.BLACK));
                    para.add(!content.equals("null") ? content : "");
                    pdfTable.addCell(para);
                    para = null;
                }
            }
            
            document.add(pdfTable);
            document.add(p4);
            document.close();
            super.getConnection().close();
            document = null;
            p = null;
            p2 = null;
            p3 = null;
            p4 = null;
            glue = null;
            header = null;
            columnsWidth = null;

            JOptionPane.showMessageDialog(null, "Recuento confeccionado. El archivo se guardo en el Escritorio");
            System.gc();

        } catch (Exception e) {

        }
    }

    private void defaults(String nombreArchivo) {
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
            PdfWriter.getInstance(document, new FileOutputStream(ruta + "/Desktop/" + nombreArchivo));

            //objeto de la libreria iText, permite colocar texto a la derecha e izquierda en la misma linea
            glue = new Chunk(new VerticalPositionMark());

            //Defino el header del documento
            //texto a la izquierda --- EJERCITO ARGENTINO
            p = new Paragraph();
            p.setFont(FontFactory.getFont("Times-Roman", 16, Font.BOLD, BaseColor.BLACK));
            p.add("   Ejercito Argentino");
            //texto a la derecha ---- LEYENDA
            p.add(new Chunk(glue));
            p.setFont(FontFactory.getFont("Times-Roman", 9, Font.ITALIC, BaseColor.BLACK));
            p.add(leyenda);
            //texto a la izquierda pero abajo - REGIMIENTO DE INFANTERIA 1
            p2 = new Paragraph();
            p2.setFont(FontFactory.getFont("Times-Roman", 14, Font.BOLD, BaseColor.BLACK));
            p2.add("Regimiento de Infanteria 1\n\n\n");
            //titulo del documento
            p3 = new Paragraph();
            p3.setAlignment(Paragraph.ALIGN_CENTER);
            //footer del documento
            p4 = new Paragraph();
            p4.setAlignment(Paragraph.ALIGN_RIGHT);
            p4.setFont(FontFactory.getFont("Times-Roman", 12, Font.NORMAL, BaseColor.BLACK));
            p4.add("Ciudad Autónoma de Buenos Aires, " + dia + " de " + mes + " de " + año);

        } catch (Exception e) {
        }
    }

}
