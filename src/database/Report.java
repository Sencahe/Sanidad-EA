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

    private String leyend;
    private String route;
    private Chunk glue;
    private Document document;
    private Paragraph p, p2, p3, p4;

    private int day;
    private String month;
    private int year;

    public Report() {
        LocalDate today = LocalDate.now();
        day = today.getDayOfMonth();
        month = MyArrays.getMonths(today.getMonthValue());
        year = today.getYear();
    }

    public void createSituationChart(PersonnelPanel tabla) {
        defaults("Carta De Situacion.pdf");
        try {
            //tamaño de la hoja                    
            document.setPageSize(PageSize.LEGAL.rotate());
            document.open();
            //CREACION DE LA TABLA PARA EL PDF----------------------------------               
            //float[] para el tamaño de las columnas de la tabla
            float columnsWidth[] = new float[MyArrays.getPersonnelColumnsLength() - 1];
            for (int i = 0; i < columnsWidth.length; i++) {
                float scaledWidth = (MyArrays.getPersonnelColumnsSize(i) * 65) / 100;
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
                header[i].add(MyArrays.getPersonnelColumns(i));
            }

            //ciclos para llenar las tabla
            p3.setFont(FontFactory.getFont("Times-Roman", 18, Font.BOLD, BaseColor.BLACK));
            String content;
            for (int i = 0; i < 4; i++) { //itera sobre todas las tablas
                document.add(p);
                document.add(p2);
                p3.add("CARTA DE SITUACION - PERSONAL DE " + MyArrays.getCategories(i).toUpperCase() + "\n\n\n");
                document.add(p3);
                p3.remove(0);
                for (int j = -1; j < tabla.getTables(i).getRowCount(); j++) { //itera sobre las filas                   
                    for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas
                        if (j == -1) {
                            pdfTable[i].addCell(header[k]);
                        } else {
                            content = String.valueOf(tabla.getTables(i).getValueAt(j, k));
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

    public void createListReport(PersonnelPanel personnelPanel, String title, String body, boolean jump[]) {

        defaults(title + ".pdf");
        try {
            //tamaño de la hoja                    
            document.setPageSize(PageSize.LEGAL);
            document.open();
            //CREACION DE LA TABLA PARA EL PDF----------------------------------               
            //float[] para el tamaño de las columnas de la tabla
            float columnsWidth[] = new float[6];
            for (int i = 0; i < columnsWidth.length; i++) {
                if (i != 5) {
                    float scaledWidth = (MyArrays.getPersonnelColumnsSize(i) * 70) / 100;
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
            p3.add(title + "\n\n");
            document.add(p3);
            p3.remove(0);

            //cuerpo
            if (!body.trim().equals("")) {
                p3.setFont(FontFactory.getFont("Times-Roman", 10, Font.NORMAL, BaseColor.BLACK));
                p3.setTabSettings(new TabSettings(56f));
                p3.add(Chunk.TABBING);
                p3.add(Chunk.TABBING);
                p3.setAlignment(Paragraph.ALIGN_JUSTIFIED);
                p3.add(body + "\n\n");
                document.add(p3);
            }

            //header de la tabla   
            Paragraph header[] = new Paragraph[columnsWidth.length];
            for (int i = 0; i < columnsWidth.length; i++) {
                header[i] = new Paragraph();
                header[i].setFont(FontFactory.getFont("Times-Roman", 10, Font.BOLD, BaseColor.BLACK));
                header[i].setAlignment(Paragraph.ALIGN_CENTER);
                if (i != 5) {
                    header[i].add(MyArrays.getPersonnelColumns(i));
                } else {
                    header[i].add("Observaciones");
                }
                pdfTable.addCell(header[i]);
            }

            //ciclos para llenar las tabla            
            String content;
            int num = 0;
            for (int i = 0; i < 4; i++) { //itera sobre todas las tablas
                if (jump[i]) {
                    //se puede iterar sobre esa columna
                    for (int j = 0; j < personnelPanel.getTables(i).getRowCount(); j++) { //itera sobre las filas                   
                        for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas
                            if (k == 0) {
                                content = String.valueOf(++num);
                            } else if (k == 5) {
                                content = "";
                            } else {
                                content = String.valueOf(personnelPanel.getTables(i).getValueAt(j, k));
                            }
                            Paragraph para = new Paragraph();
                            para.setFont(FontFactory.getFont("Times-Roman", 9, 0, BaseColor.BLACK));
                            para.add(!content.equals("null") ? content : "");
                            pdfTable.addCell(para);
                            para = null;

                        }
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

            JOptionPane.showMessageDialog(null, title + " confeccionado. El archivo se guardo en el Escritorio");
            System.gc();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void createSickReport(SickPanel parte, boolean diagnostico) {
        //obener la fecha del dia presente

        String confidencial = diagnostico ? " CONFIDENCIAL" : "";

        defaults("Parte de Sanidad " + day + " " + month.substring(0, 3).toUpperCase()
                + " " + year + confidencial + ".pdf");
        try {
            //tamaño de la hoja
            document.setPageSize(PageSize.LEGAL);
            document.open();
            //CREACION DE LA TABLA PARA EL PDF----------------------------------                           
            String[] columnasParte = MyArrays.getSickColumns();
            int[] tamañoColumnas = MyArrays.getSickColumnsSize();
            int jump; //variable auxiliar en caso de recuperar la informacion sin diagnostico

            //float[] para el tamaño escalado de las columnas de la tabla 
            float columnsWidth[] = new float[columnasParte.length - 1 - (!diagnostico ? 2 : 0)];
            for (int i = 0; i < columnsWidth.length; i++) {
                jump = i >= 4 && !diagnostico ? 2 : 0;
                float scaledWidth = (tamañoColumnas[i + jump] * 50) / 100;
                columnsWidth[i] = scaledWidth;
            }

            //cracion del PdfpTable
            PdfPTable[] pdfTable = new PdfPTable[MyArrays.getSickTypesLength()];
            for (int i = 0; i < 4; i++) {
                pdfTable[i] = new PdfPTable(columnsWidth);
                pdfTable[i].setTotalWidth(columnsWidth);
                pdfTable[i].setLockedWidth(true);
                pdfTable[i].setHorizontalAlignment(Element.ALIGN_LEFT);
            }

            //header de la tabla   
            Paragraph header[] = new Paragraph[columnsWidth.length];
            for (int i = 0; i < columnsWidth.length; i++) {
                jump = i >= 4 && !diagnostico ? 2 : 0;
                header[i] = new Paragraph();
                header[i].setFont(FontFactory.getFont("Times-Roman", 8, Font.BOLD, BaseColor.BLACK));
                header[i].add(columnasParte[i + jump]);
            }

            //encabezado
            String content;
            document.add(p);
            document.add(p2);
            p3.setFont(FontFactory.getFont("Times-Roman", 12, Font.BOLD, BaseColor.BLACK));
            p3.add("PARTE DIARIO DE SANIDAD DE LA UNIDAD DEL " + day + " DE " + month.toUpperCase() + " DE " + year);
            document.add(p3);
            Paragraph p5 = new Paragraph();
            p5.setFont(FontFactory.getFont("Times-Roman", 10, Font.BOLD, BaseColor.BLACK));

            //ciclos para llenar las tabla
            for (int i = 0; i < pdfTable.length; i++) { //itera sobre todas las tablas

                p5.add("\n" + MyArrays.getSickTypes(i).toUpperCase() + "\n\n");
                document.add(p5);
                p5.remove(0);

                for (int j = -1; j < parte.getTablas(i).getRowCount(); j++) { //itera sobre las filas                         
                    for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas
                        if (j == -1) {
                            //agregar los headers
                            pdfTable[i].addCell(header[k]);
                        } else {
                            //agregar el contenido de las celdas
                            jump = k >= 4 && !diagnostico ? 2 : 0;
                            content = String.valueOf(parte.getTablas(i).getValueAt(j, k + jump));
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

    public void createReCountReport(ReCountPanel reCountPanel, String title, boolean diag) {

        defaults("Recuento Parte de Sanidad" + (diag ? " CONFIDENCIAL" : "") + ".pdf");
        try {
            //tamaño de la hoja
            document.setPageSize(PageSize.LEGAL.rotate());
            document.open();

            //encabezado
            document.add(p);
            document.add(p2);
            p3.setFont(FontFactory.getFont("Times-Roman", 12, Font.BOLD, BaseColor.BLACK));
            p3.add(title + "\n\n");
            document.add(p3);
            p3.remove(0);

            //CREACION DE LA TABLA PARA EL PDF----------------------------------
            int jump;
            String[] columnasRecuento = MyArrays.getReCountColumns();
            int[] tamañoColumnas = MyArrays.getReCountColumnsSize();
            //float[] para el tamaño escalado de las columnas de la tabla 
            float columnsWidth[] = new float[columnasRecuento.length - (!diag ? 2 : 0)];
            for (int i = 0; i < columnsWidth.length; i++) {
                jump = i >= 5 && !diag ? 2 : 0;
                float scaledWidth = (tamañoColumnas[i + jump] * 65) / 100;
                columnsWidth[i] = scaledWidth;
            }

            PdfPTable pdfTable = new PdfPTable(columnsWidth);
            pdfTable.setTotalWidth(columnsWidth);
            pdfTable.setLockedWidth(true);

            //header de la tabla   
            Paragraph header[] = new Paragraph[columnsWidth.length];
            for (int i = 0; i < columnsWidth.length; i++) {
                jump = i >= 5 && !diag ? 2 : 0;
                header[i] = new Paragraph();
                header[i].setFont(FontFactory.getFont("Times-Roman", 10, Font.BOLD, BaseColor.BLACK));
                header[i].setAlignment(Paragraph.ALIGN_CENTER);
                header[i].add(columnasRecuento[i + jump]);
                pdfTable.addCell(header[i]);
            }

            //ciclos para llenar la tabla
            String content;
            for (int j = 0; j < reCountPanel.getTable().getRowCount(); j++) { //itera sobre las filas                   
                for (int k = 0; k < columnsWidth.length; k++) { //itera sobre las columnas   
                    jump = k >= 5 && !diag ? 2 : 0;
                    content = String.valueOf(reCountPanel.getTable().getValueAt(j, k + jump));
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
            System.out.println(e);
        }
    }

    public void createOverWeightControlList() {
        boolean keepAsking = true;
        boolean next = false;
        int imc = 0;
        while (keepAsking) {
            try {
                String input = JOptionPane.showInputDialog(null,
                        "Ingrese el IMC a partir del cual se haran los controles.",
                        "Control IMC", 1);
                if (input != null) {
                    imc = Integer.parseInt(input);
                    keepAsking = false;
                    next = true;
                } else {
                    keepAsking = false;
                    next = false;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Numero ingresado incorrecto");
            }
        }

        if (next) {
            try {

                defaults("Control de IMC.pdf");
                document.setPageSize(PageSize.LEGAL.rotate());

                //tamaño de la hoja
                document.setPageSize(PageSize.LEGAL.rotate());
                document.open();

                //encabezado
                document.add(p);
                document.add(p2);
                p3.setFont(FontFactory.getFont("Times-Roman", 12, Font.BOLD, BaseColor.BLACK));
                p3.add("LISTA PARA CONTROL DE PERSONAL CON SOBREPESO\n\n\n");
                document.add(p3);
                p3.remove(0);

                //CREACION DE LA TABLA PARA EL PDF----------------------------------                           
                String[] columnasRecuento = {"Nro", "Grado", "Apellido y Nombre", "Destino", "DNI", "IMC",
                    "IMC 1", "Fecha", "IMC 2", "Fecha", "IMC 3", "Fecha", "IMC 4", "Fecha",
                    "IMC 5", "Fecha", "IMC 6", "Fecha"};
                //                                            control y fecha
                int[] columnsSize = {40, 65, 240, 68, 70, 50, 60, 70, 60, 70, 60, 70, 60, 70, 60, 70, 60, 70};
                //float[] para el tamaño escalado de las columnas de la tabla 
                float columnsWidth[] = new float[columnasRecuento.length];
                for (int i = 0; i < columnsWidth.length; i++) {
                    float scaledWidth = (columnsSize[i] * 65) / 100;
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

                // ciclos para llenar la tabla
                String content;
                int num = 0;

                PreparedStatement pst2 = super.getConnection().prepareStatement("SELECT "
                        + " Grado, Apellido,Nombre,Destino,DNI,IMC,Categoria FROM Personal WHERE IMC >= " + imc
                        + " AND Categoria != 3"
                        + " " + MyArrays.getOrderPersonnel(0));
                ResultSet rs = pst2.executeQuery();

                if (rs.next()) {
                    do {
                        for (int i = 0; i < 7; i++) { //itera sobre las columnas
                            switch (i) {
                                case 0:
                                    content = String.valueOf(++num);
                                    break;
                                case 1:
                                    content = MyArrays.getGrades(rs.getInt("Categoria"), rs.getInt("Grado"));
                                    break;
                                case 2:
                                    content = rs.getString(i) + " " + rs.getString(++i);
                                    break;
                                default:
                                    content = rs.getString(i) != null ? rs.getString(i) : "";
                                    break;
                            }
                            Paragraph para = new Paragraph();
                            para.setFont(FontFactory.getFont("Times-Roman", 9, 0, BaseColor.BLACK));
                            para.add(!content.equals("null") ? content : "");
                            pdfTable.addCell(para);
                            para = null;
                        }
                        for (int i = 0; i < columnasRecuento.length - 6; i++) {
                            pdfTable.addCell("");
                        }

                    } while (rs.next());

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

                    JOptionPane.showMessageDialog(null, "Control de IMC confeccionado. El archivo se guardo en el Escritorio");
                    System.gc();
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha encontrado Personal con un IMC mayor a " + imc);
                }
            } catch (Exception e) {
                int line = e.getStackTrace()[0].getLineNumber();
                System.out.println(e + " " + line);
            }
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
            leyend = rs.getString("Leyenda");

            super.getConnection().close();

            //Enviando el pdf al escritorio
            route = System.getProperty("user.home");
            PdfWriter.getInstance(document, new FileOutputStream(route + "/Desktop/" + nombreArchivo));

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
            p.add(leyend);
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
            p4.add("Ciudad Autónoma de Buenos Aires, " + day + " de " + month + " de " + year);

        } catch (Exception e) {

        }
    }

}
