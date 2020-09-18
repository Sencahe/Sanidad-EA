package mytools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import panels.PersonnelPanel;
import panels.ReCountPanel;
import panels.SickPanel;


public class Utilities {

    private final Color colorTable, colorBackground, colorButton, colorButtonFont;

    private final Font fontTabbedPane, fontTable, fontButton, fontLabelSummary, fontTableHeader;
    private final Font fontLabelFormulary, fontTextFields, fontHolder, fontChecks, fontLabelBig;
    private final Font fontLabelSearcher, fontLabelResult;
    private final Font fuenteLabelsRef;
    private final Font fontLabelTitle;
    private final Font fuenteLabelInfo;

    private final Cursor pointCursor;

    public Utilities() {
        this.colorTable = new Color(255, 255, 255);
        this.colorBackground = new Color(0, 100, 220);
        this.colorButton = new Color(0, 70, 170);
        this.colorButtonFont = new Color(240, 240, 240);
        this.fontTabbedPane = new Font("Tahoma", 1, 16);
        this.fontTable = new Font("Arial", 0, 13);
        this.fontButton = new Font("Tahoma", 1, 13);
        this.fontLabelSummary = new Font("Tahoma", 1, 14);
        this.fontTableHeader = new Font("Tahoma", 1, 12);
        this.fontLabelFormulary = fontTableHeader;
        this.fontTextFields = fontTable;
        this.fontHolder = new Font("Tahoma", 2, 10);
        this.fontChecks = fontTableHeader;
        this.fontLabelBig = fontLabelSummary;
        this.fontLabelSearcher = new Font("Tahoma", 1, 11);
        this.fontLabelResult = new Font("Tahoma", 0, 11);
        this.fuenteLabelInfo = fontLabelResult;
        this.fuenteLabelsRef = fontButton;
        this.fontLabelTitle = new Font("Impact", 0, 20);
        this.pointCursor = new Cursor(Cursor.HAND_CURSOR);
    }

    //-------------------------BOTON PERSONALIZADO------------------------------
    public JButton customButton() {
        JButton button = new JButton();
        button.setFont(getFontButton());
        button.setBackground(getColorButton());
        button.setForeground(getColorButtonFont());
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(getPointCursor());
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
        });
        return button;
    }

    //---------------------TABLA PERSONALIZADA----------------------------------
    public JTable customTable(JPanel panel,int column1, int column2) {
        
        boolean personnelPanelInstance = panel instanceof PersonnelPanel;
        boolean sickPanelInstance = panel instanceof SickPanel;
        boolean reCountPanelInstance = panel instanceof ReCountPanel;
        
        //default table model
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        //-------------JTable-----------------
        JTable table = new JTable(model);
        table.setGridColor(Color.black);
        table.setBackground(getColorTable());
        table.setFont(getFontTable());
        table.setRowHeight(16);

        //----------------- TableHeader ----------------
        UIManager.put("TableHeader.font", getFontTableHeader());
        JTableHeader header = table.getTableHeader();
        header.setFont(getFontTableHeader());
        header.setBackground(getColorTable());
        header.setPreferredSize(new Dimension(40, 27));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        //-----------------columnas----------------          
        String columns[];
        int columnsWidth[];

        if (personnelPanelInstance) {
            columns = MyArrays.getPersonnelColumns();
            columnsWidth = MyArrays.getPersonnelColumnsSize();
        } else if (sickPanelInstance) {
            columns = MyArrays.getSickColumns();
            columnsWidth = MyArrays.getSickColumnsSize();
        } else {
            columns = MyArrays.getReCountColumns();
            columnsWidth = MyArrays.getReCountColumnsSize();
        }

        for (int i = 0; i < columns.length; i++) {
            model.addColumn(columns[i]);
        }
        for (int i = 0; i < columnsWidth.length; i++) {
            table.getColumnModel().getColumn(i).setMinWidth(columnsWidth[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(columnsWidth[i]);
            table.getColumnModel().getColumn(i).setPreferredWidth(columnsWidth[i]);
            if (!sickPanelInstance) {
                if (i != column1 && i != column2) {
                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    centerRenderer = null;
                }
            }
            if (!reCountPanelInstance && i == columnsWidth.length - 1) {
                table.getColumnModel().removeColumn(table.getColumnModel().getColumn(i));
            }
        }

        return table;
    }

    //----------------------TABLE CELL RENDERER---------------------------------
    public TableCellRenderer sickCellRenderer() {
        TableCellRenderer cellRenderer = new TableCellRenderer() {

            DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();

            MyDates myDates = new MyDates(MyDates.USER_DATE_FORMAT);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                if (column != 2 && column != 4) {
                    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);
                } else {
                    defaultRenderer.setHorizontalAlignment(JLabel.LEFT);
                }

                Component c = defaultRenderer.getTableCellRendererComponent(table,
                        value, isSelected, hasFocus, row, column);

                String date = String.valueOf(table.getModel().getValueAt(row, 7));
                date = myDates.userDateToLocalDate(date);
                int dias = myDates.getDays(date) - 1;
                if (dias >= 0) {
                    c.setForeground(Color.red);
                    if(isSelected){
                        c.setForeground(Color.ORANGE);
                    }
                } else {
                    c.setForeground(Color.black);
                    if(isSelected){
                        c.setForeground(Color.white);
                    }
                }
             

                return c;
            }
        };
        return cellRenderer;
    }

    //----------------------------KEY ADAPTERS----------------------------------
    public KeyAdapter bloquearLetras = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            char caracter = e.getKeyChar();
            if (((caracter < '0') || (caracter > '9')) && (caracter != '.') && (caracter != ',')) {
                e.consume();
            }
            if (caracter == ',') {
                e.setKeyChar('.');
            }
        }
    };
    public KeyAdapter soloNumeros = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            char caracter = e.getKeyChar();
            if (((caracter < '0') || (caracter > '9'))) {
                e.consume();
            }
        }
    };

    //COLORES      
    //tabla
    public Color getColorTable() {
        return colorTable;
    }

    public Color getColorBackground() {
        return colorBackground;
    }

    public Color getColorButton() {
        return colorButton;
    }

    public Color getColorButtonFont() {
        return colorButtonFont;
    }
    //CURSOR

    public Cursor getPointCursor() {
        return pointCursor;
    }

    //--------------------GETTERS Y SETTERS-------------------------------------
    public Font getFontTabbedPane() {
        return fontTabbedPane;
    }

    public Font getFontTable() {
        return fontTable;
    }

    public Font getFontButton() {
        return fontButton;
    }

    public Font getFontLabelSummary() {
        return fontLabelSummary;
    }

    public Font getFontTableHeader() {
        return fontTableHeader;
    }

    //formulario
    public Font getFontLabelFormulary() {
        return fontLabelFormulary;
    }

    public Font getFontTextFields() {
        return fontTextFields;
    }

    public Font getFontHolder() {
        return fontHolder;
    }

    public Font getFontChecks() {
        return fontChecks;
    }

    public Font getFontLabelBig() {
        return fontLabelBig;
    }

    public Font getFontLabelTitle() {
        return fontLabelTitle;
    }

    //Buscador
    public Font getFuenteMsgBuscador() {
        return fontLabelSearcher;
    }

    public Font getFuenteRsltBuscador() {
        return fontLabelResult;
    }

    //Referencias
    public Font getFuenteLabelsRef() {
        return fuenteLabelsRef;
    }

    public Font getFuenteLabelInfo() {
        return fuenteLabelInfo;
    }
    
    
}


