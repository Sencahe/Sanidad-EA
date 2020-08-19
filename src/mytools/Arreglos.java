package mytools;

public class Arreglos {

    // ARREGLOS PARA LOS JTABLE
    private static String[] columnasTabla = {"Nro", "Grado", "Arm/Serv", "Apellido y Nombre", "Destino", "DNI", "Anexo 27",
        "Edad", "Peso", "Altura", "IMC", "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones", "id"};
    //                                      Nr Grd A/S AyN Dest  DNI A27 Age Kgs Alt IMC PPS APT  D   H   A   T  Inf  Act  OBS  id
    private static int[] tamañoColumnas = {40, 50, 70, 240, 60, 70, 85, 40, 50, 50, 50, 110, 65, 32, 32, 32, 32, 32, 32, 288, 40};

    private static String[] columnasParte = {"Nro", "Grado", "Apellido y Nombre", "Destino", "Diagnostico","CIE",
        "Desde", "Hasta", "Dias", "Expediente", "Observacion", "id"};
    //                                         Nro Grd  AyN Dest Diag cie Dsd  Hst Dias Exp  Obs id
    private static int[] tamañoColumnasParte = {40, 50, 240, 60, 260, 50, 100, 100, 50, 110, 200, 40};
    //
    private static String[] columnasRecuento = {"Nro","Grado","Apellido Y Nombre","Destino",
        "DNI","Diagnostico","CIE","Desde","Hasta","Dias","Observacion","Parte","N / S"};
    
    private static int[] tamañoColumnasRecuento = {40,50,240,60,70,260,50,100,100,50,200,80,50};
    //=================================================================================
    //ARREGLOS PARA RECUPERAR INFORMACION DE LA BASE DE DATOS A LOS JTABLE
    private static String[] columnasBD = {"Grado", "Arma", "Apellido", "Destino" ,"DNI","Anexo27", "FechaNacimiento", "Peso", "Altura", "IMC",
        "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones", "id"};

    //private static String[] columnasBDParte = {};

    private static String[] formularioBD = {  //textField
        "Apellido", "Nombre", "Arma", "DNI", "Peso", "Altura", "IMC", "Observaciones", "Legajo", "Expediente", 
        "Categoria", "Grado", "Destino", "Aptitud", "PPS", //comboBoxes
        "FechaNacimiento", "Anexo27", //dateChooser
        "D", "H", "A", "T", "Act", "Inf", //checkBoxes
        "Sexo", //radioButton
        "Parte"};  //flags
    //=================================================================================
    //ARREGLOS PARA LOS COMPONENTES DE FORMULARIO y LA BASE DE DATOS
    private static String[] textField = {"Apellido", "Nombre", "Arma", "DNI", "Peso", "Altura", "IMC", "Observaciones", "Legajo", "Expediente"};
    private static String[] comboBox = {"Categoria", "Grado", "Destino", "Aptitud", "PPS"};
    private static String[] dateChooser = {"FechaNacimiento", "Anexo27"};
    private static String[] checkBox = {"D", "H", "A", "T", "Act", "Inf"};
    //=================================================================================
    private static String[] categorias = {"Oficiales", "SubOficiales", "Soldados", "Civiles"};
    private static String[][] grados = {
        {"ST", "TT", "TP", "CT", "MY", "TC", "CR"},
        {"CB", "C1", "SG", "S1", "SA", "SP", "SM"},
        {"VS \"ec\"", "VS", "VP"},
        {"A/C"}};
    private static String[] destinos = {"", "Cdo Ser", "Bda Mil", "Ca A", "Ca B", "Ca C", "Ca E"};
    private static String[] PPS = {"", "SALUDABLE", "PREVENTIVO", "RECUPERACION", "PROTECCION", "BAJO PESO"};
    private static String[] aptitud = {"", "APTO", "APTO B", "NO APTO"};
    //=================================================================================
    private static String[] patologias = {"Todos","Diabetes", "Hipertension", "Asma (Problemas resp.)", "Tabaquismo", "AJM"};
    private static String[] ordenTabla = {"De mayor a menor grado", "De menor a mayor grado", "Orden Alfabetico A-Z", "Orden Alfabetico Z-A"};
    private static String[] ordenTablaBD = {" ORDER BY Grado DESC, Apellido ASC, Nombre ASC", " ORDER BY Grado ASC, Apellido ASC, Nombre ASC",
        " ORDER BY Apellido ASC, Nombre ASC, Grado DESC", " ORDER BY Apellido DESC, Nombre DESC, Grado DESC"};

    //=================================================================================
    //=================================================================================
    private Arreglos() {

    }

    //=================================================================================
    //=================================================================================
    // ARREGLOS COMO PROPIEDADES PARA LOS JTABLES DEL FRAME PRINCIPAL
    public static String[] getColumnasTabla() {
        return columnasTabla;
    }

    public static String getColumnasTabla(int i) {
        return columnasTabla[i];
    }

    public static int getColumnasTablaLength() {
        return columnasTabla.length;
    }

    public static int[] getTamañoColumnas() {
        return tamañoColumnas;
    }

    public static int getTamañoColumnas(int i) {
        return tamañoColumnas[i];
    }

    public static String getColumnasRecuento(int index) {
        return columnasRecuento[index];
    }

    public static int getTamañoColumnasRecuento(int index) {
        return tamañoColumnasRecuento[index];
    }
    public static int getColumnasRecuentoLength(){
        return columnasRecuento.length;
    }
    

    //ARREGLOS COMO PROPIEDADES LOS JTABLES DEL PARTE DE ENFERMO
    public static String[] getColumnasParte() {
        return columnasParte;
    }

    public static String getColumnasParte(int i) {
        return columnasParte[i];
    }

    public static int getColumnasParteLength() {
        return columnasParte.length;
    }

    public static int[] getTamañoColumnParte() {
        return tamañoColumnasParte;
    }

    public static int getTamañoColumnParte(int i) {
        return tamañoColumnasParte[i];
    }

    //=================================================================================
    // ARREGLO SOBRE EL CUAL SE ITERA PARA RECUPERAR LA INFORMACION DE LA BASE DE DATOS A LAS TABLAS
    public static String[] getColumnasBD() {
        return columnasBD;
    }

    public static String getColumnasBD(int i) {
        return columnasBD[i];
    }

    public static int getColumnasBDLength() {
        return columnasBD.length;
    }

//    public static String[] getColumnasParteBD() {
//        return columnasBDParte;
//    }
//
//    public static String getColumnasParteBD(int i) {
//        return columnasBDParte[i];
//    }

    // ARREGLO PARA LOS GRADOS SEGUN SU VALOR NUMERICO, TANTO EN LA CLASE FORMULARIO COMO EN LA CLASE BASEDEDATOS
    public static String[][] getGrados() {
        return grados;
    }

    public static String[] getGrados(int i) {
        return grados[i];
    }

    public static String getGrados(int i, int j) {
        return grados[i][j];
    }

    public static int getGradosLength(int i) {
        return grados[i].length;
    }

    //ARREGLOS PARA LOS COMBO BOX Y CHECK BOX DE LA CLASE FORMULARIO
    public static String[] getCategorias() {
        return categorias;
    }

    public static String getCategorias(int i) {
        return categorias[i];
    }

    public static int getCategoriasLength() {
        return categorias.length;
    }

    public static String[] getPPS() {
        return PPS;
    }

    public static String getPPS(int i) {
        return PPS[i];
    }

    public static int getPPSLength() {
        return PPS.length;
    }

    public static String[] getDestinos() {
        return destinos;
    }

    public static String getDestinos(int i) {
        return destinos[i];
    }

    public static int getDestinosLength() {
        return destinos.length;
    }

    public static String[] getAptitud() {
        return aptitud;
    }

    public static String getAptitud(int i) {
        return aptitud[i];
    }

    public static int getAptitudLength() {
        return aptitud.length;
    }

    // ARREGLOS PARA COMUNICAR LOS COMPONENTES DE FORMULARIO CON LA CLASE BASE DE DATOS
    public static String[] getTextField() {
        return textField;
    }

    public static String getTextField(int i) {
        return textField[i];
    }

    public static int getTextFieldLength() {
        return textField.length;
    }

    public static String[] getComboBox() {
        return comboBox;
    }

    public static String getComboBox(int i) {
        return comboBox[i];
    }

    public static int getComboBoxLength() {
        return comboBox.length;
    }

    public static String[] getDateChooser() {
        return dateChooser;
    }

    public static String getDateChooser(int i) {
        return dateChooser[i];
    }

    public static int getDateChooserLength() {
        return dateChooser.length;
    }

    public static String[] getCheckBox() {
        return checkBox;
    }

    public static String getCheckBox(int i) {
        return checkBox[i];
    }

    public static int getCheckBoxLength() {
        return checkBox.length;
    }

    public static String[] getFormularioBD() {
        return formularioBD;
    }

    public static String getFormularioBD(int i) {
        return formularioBD[i];
    }

    // ARREGLOS QUE SIRVEN DE AYUDA PARA LOS FILTROS DE LA TABLA
    public static String[] getPatologias() {
        return patologias;
    }

    public static String getPatologias(int i) {
        return patologias[i];
    }

    public static int getPatologiasLength() {
        return patologias.length;
    }

    public static String[] getOrdenTabla() {
        return ordenTabla;
    }

    public static String getOrdenTabla(int i) {
        return ordenTabla[i];
    }

    public static String getOrdenTablaBD(int i) {
        return ordenTablaBD[i];
    }
}
