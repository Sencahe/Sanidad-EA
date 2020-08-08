package mytools;

public class Arreglos {

    public Arreglos(){
        
    }
    //=================================================================================
    //=================================================================================
    // ARREGLOS COMO PROPIEDADES PARA LOS JTABLES DEL FRAME PRINCIPAL
    public String[] getColumnasTabla() {
        String[] columnasTabla = {"Nro", "Grado", "Arm/Serv", "Apellido y Nombre", "Destino", "DNI", "Ult. Anexo 27",
            "Edad", "Peso", "Altura", "IMC", "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones", "id"};
        return columnasTabla;
    }

    public int getColumnasTablaLength() {
        return getColumnasTabla().length;
    }

    public int[] getTamañoColumnas() {
        //                      Nr Grd A/S AyN Dest DNI A27 Age Kgs Alt  IMC PPS APT  D   H   A   T  Inf  Act  OBS id
        int[] TamañoColumnas = {40, 50, 70, 240, 60, 80, 110, 40, 50, 50, 50, 105, 65, 32, 32, 32, 32, 32, 32, 258, 40};
        return TamañoColumnas;
    }

    // ARREGLO SOBRE EL CUAL SE ITERA PARA RECUPERAR LA INFORMACION DE LA BASE DE DATOS A LA TABLA PRINCIPAL
    public String[] getColumnasBD() {
        String[] columnnasBD = {"Grado", "Arma", "Apellido", "Destino", "DNI", "Anexo27", "FechaNacimiento", "Peso", "Altura", "IMC",
            "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones", "id"};
        return columnnasBD;
    }

    public int getColumnbasBDLength() {
        return getColumnasBD().length;
    }

    // ARREGLO PARA LOS GRADOS SEGUN SU VALOR NUMERICO, TANTO EN LA CLASE FORMULARIO COMO EN LA CLASE BASEDEDATOS
    public String[][] getGrados() {
        String[][] grados = {
            {"ST", "TT", "TP", "CT", "MY", "TC", "CR"},
            {"CB", "C1", "SG", "S1", "SA", "SP", "SM"},
            {"VS \"ec\"", "VS", "VP"},
            {"A/C"}};
        return grados;
    }

    public int getGradosLength() {
        return getGrados().length;
    }

    //ARREGLOS PARA LOS COMBO BOX Y CHECK BOX DE LA CLASE FORMULARIO
    public String[] getCategorias() {
        String[] categorias = {"Oficiales", "SubOficiales", "Soldados", "Civiles"};
        return categorias;
    }

    public int getCategoriasLength() {
        return getCategorias().length;
    }

    public String[] getPPS() {
        String[] PPS = {"", "SALUDABLE", "PREVENTIVO", "RECUPERACION", "PROTECCION", "BAJO PESO"};
        return PPS;
    }

    public int getPPSLength() {
        return getPPS().length;
    }

    public String[] getDestinos() {
        String[] destinos = {"", "Cdo Ser", "Bda Mil", "Ca A", "Ca B", "Ca C", "Ca E"};
        return destinos;
    }

    public int getDestinosLength() {
        return getDestinos().length;
    }

    public String[] getAptitud() {
        String aptitud[] = {"", "APTO", "APTO B", "NO APTO"};
        return aptitud;
    }

    public int getAptitudLength() {
        return getAptitud().length;
    }

    // ARREGLOS PARA COMUNICAR LOS COMPONENTES DE FORMULARIO CON LA CLASE BASE DE DATOS
    public String[] getTextField() {
        String[] textField = {"Apellido", "Nombre", "Arma", "DNI", "Peso", "Altura", "IMC", "Observaciones"};
        return textField;
    }

    public int getTextFieldLength() {
        return getTextField().length;
    }

    public String[] getComboBox() {
        String[] comboBox = {"Categoria", "Grado", "Destino", "Aptitud", "PPS"};
        return comboBox;
    }

    public int getComboBoxLength() {
        return getComboBox().length;
    }

    public String[] getDateChooser() {
        String[] dateChooser = {"FechaNacimiento", "Anexo27"};
        return dateChooser;
    }

    public int getDateChooserLength() {
        return getDateChooser().length;
    }

    public String[] getCheckBox() {
        String[] checkBox = {"D", "H", "A", "T", "Act", "Inf"};
        return checkBox;
    }

    public int getCheckBoxLength() {
        return getCheckBox().length;
    }

    public String[] getTodasColumnas() {
        int total = getTextFieldLength() + getComboBoxLength() + getDateChooserLength() + getCheckBoxLength();
        String[] todasColumnas = new String[total];
        System.arraycopy(getTextField(), 0, todasColumnas, 0, getTextFieldLength());
        System.arraycopy(getComboBox(), 0, todasColumnas, getTextFieldLength(), getComboBoxLength());
        System.arraycopy(getDateChooser(), 0, todasColumnas, getTextFieldLength() + getComboBoxLength(), getDateChooserLength());
        System.arraycopy(getCheckBox(), 0, todasColumnas, getTextFieldLength() + getComboBoxLength() + getDateChooserLength(), getCheckBoxLength());
        return todasColumnas;
    }

    // ARREGLOS QUE SIRVEN DE AYUDA PARA LOS FILTROS DE LA TABLA
    public String[] getPatologias() {
        String[] patologias = {"Diabetes", "Hipertension", "Asma (Problemas resp.)", "Tabaquismo", "AJM"};
        return patologias;
    }

    public String[] getOrdenTabla() {
        String[] ordenTabla = {"De mayor a menor grado", "De menor a mayor grado", "Orden Alfabetico A-Z", "Orden Alfabetico Z-A"};
        return ordenTabla;
    }

    public String[] getOrdenTablaBD() {
        String[] ordenTablaBD = {" ORDER BY Grado DESC, Apellido ASC, Nombre ASC", " ORDER BY Grado ASC, Apellido ASC, Nombre ASC",
        " ORDER BY Apellido ASC, Nombre ASC, Grado DESC", " ORDER BY Apellido DESC, Nombre DESC, Grado DESC"};
        return ordenTablaBD;
    }
}
