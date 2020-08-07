package mytools;

public class Arreglos {

    // ARREGLOS COMO PROPIEDADES PARA LOS JTABLES DEL FRAME PRINCIPAL
    private String[] columnasTabla = {"Nro", "Grado", "Arm/Serv", "Apellido y Nombre", "Destino", "DNI", "Ult. Anexo 27",
        "Edad", "Peso", "Altura", "IMC", "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones", "id"};
    //                              Nr Grd A/S AyN Dest DNI A27 Age Kgs Alt  IMC PPS APT  D   H   A   T  Inf  Act  OBS id
    private int[] TamañoColumnas = {40, 50, 70, 240, 60, 80, 110, 40, 50, 50, 50, 105, 65, 32, 32, 32, 32, 32, 32, 260, 40};
    // ARREGLO SOBRE EL CUAL SE ITERA PARA RECUPERAR LA INFORMACION DE LA BASE DE DATOS
    private String[] columnnasBD = {"Grado", "Arma", "Apellido", "Destino", "DNI", "Anexo27", "FechaNacimiento", "Peso", "Altura", "IMC",
        "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones", "id"};
    // ARREGLO PARA LOS GRADOS SEGUN SU VALOR NUMERICO, TANTO EN LA CLASE FORMULARIO COMO EN LA CLASE BASEDEDATOS
    private String[][] grados = {
        {"ST", "TT", "TP", "CT", "MY", "TC", "CR"},
        {"CB", "C1", "SG", "S1", "SA", "SP", "SM"},
        {"VS \"ec\"", "VS", "VP"},
        {"A/C"}};
    //ARREGLOS PARA LOS COMBO BOX Y CHECK BOX DE LA CLASE FORMULARIO
    private String[] PPS = {"", "SALUDABLE", "PREVENTIVO", "RECUPERACION", "PROTECCION", "BAJO PESO"};
    private String[] destinos = {"", "Cdo Ser", "Bda Mil", "Ca A", "Ca B", "Ca C", "Ca E"};
    private String[] categorias = {"Oficiales", "SubOficiales", "Soldados", "Civiles"};
    private String aptitud[] = {"", "APTO", "APTO B", "NO APTO"};
    // ARREGLOS PARA COMUNICAR LOS COMPONENTES DE FORMULARIO CON LA CLASE BASE DE DATOS
    private String[] textField = {"Apellido", "Nombre", "Arma", "DNI", "Peso", "Altura", "IMC", "Observaciones"};
    private String[] comboBox = {"Categoria", "Grado", "Destino", "Aptitud", "PPS"};
    private String[] dateChooser = {"FechaNacimiento", "Anexo27"};
    private String[] checkBox = {"D", "H", "A", "T", "Act", "Inf"};
    private int total = getTextFieldLength() + getComboBoxLength() + getDateChooserLength() + getCheckBoxLength();
    private String[] todasColumnas = new String[total];
    // ARREGLOS QUE SIRVEN DE AYUDA PARA LOS FILTROS DE LA TABLA
    private String[] patologias = {"Diabetes", "Hipertension", "Asma (Problemas resp.)", "Tabaquismo", "AJM"};
    private String[] ordenTabla = {"De mayor a menor grado", "De menor a mayor grado", "Orden Alfabetico A-Z", "Orden Alfabetico Z-A"};
    String[] ordenTablaBD = {" ORDER BY Grado DESC, Apellido ASC, Nombre ASC", " ORDER BY Grado ASC, Apellido ASC, Nombre ASC",
            " ORDER BY Apellido ASC, Nombre ASC, Grado DESC", " ORDER BY Apellido DESC, Nombre DESC, Grado DESC"};
    //=================================================================================
    //=================================================================================
    //METODOS PARA LAS TABLAS
    public String[] columnasTabla() {
        return this.columnasTabla;
    }

    public int getColumnasTablaLength() {
        return this.columnasTabla.length;
    }

    public int[] tamañoColumnas() {
        return this.TamañoColumnas;
    }

    // METODO PARA RECUPERAR LA INFORMACION DE LA BASE DE DATOS
    public String[] columnasBD() {
        return this.columnnasBD;
    }

    public int columnbasBDLength() {
        return this.columnnasBD.length;
    }

    // METODO PARA OBTENER LOS GRADOS
    public String[][] grados() {
        return this.grados;
    }

    //METODO PARA LOS COMBO BOX Y CHECK BOX DE LA CLASE FORMULARIO
    public String[] PPS() {
        return this.PPS;
    }

    public String[] Destinos() {
        return this.destinos;
    }

    public String[] Categorias() {
        return this.categorias;
    }

    public int getCategoriasLength() {
        return this.categorias.length;
    }

    public String[] Aptitud() {
        return this.aptitud;
    }

    // METODOS PARA LOS COMPONENTES DE FORMULARIO CON LA CLASE BASE DE DATOS
    public String[] textField() {
        return textField;
    }

    public int getTextFieldLength() {
        return textField.length;
    }

    public String[] comboBox() {
        return comboBox;
    }

    public int getComboBoxLength() {
        return comboBox().length;
    }

    public String[] dateChooser() {
        return dateChooser;
    }

    public int getDateChooserLength() {
        return dateChooser().length;
    }

    public String[] checkBox() {
        return this.checkBox;
    }

    public int getCheckBoxLength() {
        return this.checkBox.length;
    }

    public String[] todasColumnas() {       
        System.arraycopy(textField(), 0, todasColumnas, 0, getTextFieldLength());
        System.arraycopy(comboBox(), 0, todasColumnas, getTextFieldLength(), getComboBoxLength());
        System.arraycopy(dateChooser(), 0, todasColumnas, getTextFieldLength() + getComboBoxLength(), getDateChooserLength());
        System.arraycopy(checkBox(), 0, todasColumnas, getTextFieldLength() + getComboBoxLength() + getDateChooserLength(), getCheckBoxLength());
        return todasColumnas;
    }

    // ARREGLOS QUE SIRVEN DE AYUDA PARA LOS FILTROS DE LA TABLA
    public String[] patologias() {        
        return patologias;
    }

    public String[] ordenTabla() {        
        return ordenTabla;
    }

    public String[] ordenTablaBD() {        
        return ordenTablaBD;
    }
}
