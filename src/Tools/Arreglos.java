package Tools;

public class Arreglos {

    // ARREGLOS COMO PROPIEDADES PARA LOS JTABLES DEL FRAME PRINCIPAL
    public String[] columnasTabla() {
        String[] columnasTabla = {"Nro", "Grado", "Arm/Serv", "Apellido y Nombre", "Destino", "DNI", "Ult. Anexo 27",
            "Edad", "Peso", "Altura", "IMC", "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones"};
        return columnasTabla;
    }
    public int getColumnasTablaLength(){
        return columnasTabla().length;
    }

    public int[] tamañoColumnas() {
        //Nr Grd A/S AyN Dest DNI A27 Age Kgs Alt  IMC PPS APT  D   H   A   T  Inf  Act  OBS 
        int[] TamañoColumnas = {40, 50, 70, 240, 60, 80, 110, 40, 50, 50, 50, 105, 65, 32, 32, 32, 32, 32, 32, 260};
        return TamañoColumnas;
    }

    // ARREGLO SOBRE EL CUAL SE ITERA PARA RECUPERAR LA INFORMACION DE LA BASE DE DATOS
    public String[] columnasBD() {
        String[] column = {"Grado", "Arma", "Apellido", "Destino", "DNI", "Anexo27", "FechaNacimiento", "Peso", "Altura", "IMC",
            "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones"};

        return column;
    }

    // ARREGLO PARA LOS GRADOS SEGUN SU VALOR NUMERICO, TANTO EN LA CLASE FORMULARIO COMO EN LA CLASE BASEDEDATOS
    public String[][] grados() {
        String[][] grados = {
            {"ST", "TT", "TP", "CT", "MY", "TC", "CR"},
            {"CB", "C1", "SG", "S1", "SA", "SP", "SM"},
            {"VS \"ec\"", "VS", "VP"},
            {"A/C"}};
        return grados;
    }

    //ARREGLOS PARA LOS COMBO BOX Y CHECK BOX DE LA CLASE FORMULARIO
    public String[] PPS() {
        String[] PPS = {"", "SALUDABLE", "PREVENTIVO", "RECUPERACION", "PROTECCION", "BAJO PESO"};
        return PPS;
    }

    public String[] Destinos() {
        String[] destinos = {"", "Cdo Ser", "Bda Mil", "Ca A", "Ca B", "Ca C", "Ca E"};
        return destinos;
    }

    public String[] Categorias() {
        String[] categorias = {"Oficiales", "SubOficiales", "Soldados", "Civiles"};
        return categorias;
    }
    public int getCategoriasLength(){
        return Categorias().length;
    }

    public String[] Aptitud() {
        String aptitud[] = {"", "APTO", "APTO B", "NO APTO"};
        return aptitud;
    }

    // ARREGLOS PARA COMUNICAR LOS COMPONENTES DE FORMULARIO CON LA CLASE BASE DE DATOS
    public String[] textField() {
        String[] text = {"Apellido", "Nombre", "Arma", "DNI", "Peso", "Altura", "IMC", "Observaciones"};
        return text;
    }
    public int getTextFieldLength(){
        return textField().length;
    }

    public String[] comboBox() {
        String[] combo = {"Categoria", "Grado", "Destino", "Aptitud", "PPS"};
        return combo;
    }
    public int getComboBoxLength(){
        return comboBox().length;
    }
    public String[] dateChooser() {
        String[] dateChooser = {"FechaNacimiento", "Anexo27"};
        return dateChooser;
    }
    public int getDateChooserLength(){
        return dateChooser().length;
    }

    public String[] checkBox() {
        String[] checkBox = {"D", "H", "A", "T", "Act", "Inf"};
        return checkBox;
    }
    public int getCheckBoxLength(){
        return checkBox().length;
    }

    public String[] todasColumnas() {
        String[] text = textField();
        String[] combo = comboBox();
        String[] dateChooser = dateChooser();
        String[] checks = checkBox();
        int total = text.length + combo.length + dateChooser.length + checks.length;
        String[] todos = new String[total];
        System.arraycopy(text, 0, todos, 0, text.length);
        System.arraycopy(combo, 0, todos, text.length, combo.length);
        System.arraycopy(dateChooser, 0, todos, text.length + combo.length, dateChooser.length);
        System.arraycopy(checks, 0, todos, text.length + combo.length + dateChooser.length, checks.length);
        return todos;
    }

    // ARREGLOS QUE SIRVEN DE AYUDA PARA LOS FILTROS DE LA TABLA
    public String[] patologias() { // Este arreglo va de la mano con el checkBox[]
        String[] patologias = {"Diabetes", "Hipertension", "Asma (Problemas resp.)", "Tabaquismo", "AJM"};
        return patologias;
    }

    public String[] ordenTabla() {
        String[] ordenTabla = {"De mayor a menor grado", "De menor a mayor grado", "Orden Alfabetico A-Z", "Orden Alfabetico Z-A"};
        return ordenTabla;
    }

    public String[] ordenTablaBD() {
        String[] ordenTabla = {" ORDER BY Grado DESC, Apellido ASC, Nombre ASC", " ORDER BY Grado ASC, Apellido ASC, Nombre ASC",
            " ORDER BY Apellido ASC, Nombre ASC, Grado DESC", " ORDER BY Apellido DESC, Nombre DESC, Grado DESC"};
        return ordenTabla;
    }
}
