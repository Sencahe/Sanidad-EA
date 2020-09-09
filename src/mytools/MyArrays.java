package mytools;

import java.util.HashMap;

public class MyArrays {

    // ARREGLOS PARA LOS JTABLE
    private static String[] personnelColumns = {"Nro", "Grado", "Arm/Serv", "Apellido y Nombre", "Destino", "DNI", "Anexo 27",
        "Edad", "Peso", "Altura", "IMC", "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones", "id"};
    //                                            Nr Grd A/S AyN Dest DNI A27 Age Kgs Alt IMC PPS APT  D   H   A   T  Inf  Act  OBS  id
    private static int[] personnelColumnsSize = {40, 60, 75, 240, 68, 70, 75, 45, 50, 50, 50, 110, 65, 30, 30, 30, 30, 36, 36, 270, 40};

    private static String[] sickColumns = {"Nro", "Grado", "Apellido y Nombre", "Destino", "Diagnostico","CIE",
        "Desde", "Hasta", "Dias", "Expediente", "Observacion", "id"};
    //                                     Nro Grd  AyN Dest Diag cie Dsd Hst Dias Exp Obs id
    private static int[] sickColumnsSize = {40, 70, 240, 60, 220, 70, 72, 72, 45, 110, 136, 40};
    //
    private static String[] reCountColumns = {"Nro","Grado","Apellido Y Nombre","Destino",
        "DNI","Diagnostico","CIE","Desde","Hasta","Dias","Observacion","Parte","N / S"};
    
    private static int[] reCountColumnsSize = {40,65,240,60,70,265,70,80,80,50,190,80,60};
          
    //=================================================================================
    //ARREGLOS PARA RECUPERAR INFORMACION DE LA BASE DE DATOS A LOS JTABLE
    private static String[] personnelPanelDB = {"Grado", "Arma", "Apellido", "Destino" ,"DNI","Anexo27", "FechaNacimiento", "Peso", "Altura", "IMC",
        "PPS", "Aptitud", "D", "H", "A", "T", "Act", "Inf", "Observaciones", "id"};

    //private static String[] columnasBDParte = {};

    private static String[] personnelFormularyDB = {  //textField
        "Apellido", "Nombre", "Arma", "DNI", "Peso", "Altura", "IMC", "Observaciones", "Legajo", "Expediente","DM", 
        "Categoria", "Grado", "Destino", "Aptitud", "PPS", //comboBoxes
        "FechaNacimiento", "Anexo27", //dateChooser
        "D", "H", "A", "T", "Act", "Inf", //checkBoxes
        "Sexo", //radioButton
        "Parte"};  //flags
    //=================================================================================
    //ARREGLOS PARA LOS COMPONENTES DE FORMULARIO y LA BASE DE DATOS
    private static String[] textField = {"Apellido", "Nombre", "Arma", "DNI", "Peso", "Altura", "IMC", 
        "Observaciones", "Legajo", "Expediente","DM"};
    private static String[] comboBox = {"Categoria", "Grado", "Destino", "Aptitud", "PPS"};
    private static String[] dateChooser = {"FechaNacimiento", "Anexo27"};
    private static String[] checkBox = {"D", "H", "A", "T", "Act", "Inf"};
    //=================================================================================
    private static String[] categories = {"Oficiales", "SubOficiales", "Soldados", "Civiles"};
    private static String[][] grades = {
        {"ST ec","ST", "TT ec","TT","TP ec", "TP", "CT", "MY", "TC", "CR"},
        {"CB Art 11","CB ec","CB", "CI Art 11","CI ec","CI", "SG", "SI", "SA", "SP", "SM"},
        {"VS \"ec\"", "VS", "VP"},
        {"A/C"}};
    private static String[] subUnities = {"", "Cdo Ser", "Bda Mil", "Ca A", "Ca B", "Ca C", "Ca E"};
    private static String[] PPS = {"", "SALUDABLE", "PREVENTIVO", "RECUPERACION", "PROTECCION", "BAJO PESO"};
    private static String[] aptitude = {"", "APTO", "APTO B", "NO APTO"};
    
    private static String[] sickTypes = {"PARTES DE ENFERMO", "PARTES DE EXCEPTUADO","PARTES DE MATERNIDAD","PERSONAL QUE NO PASO NOVEDAD"};
    //=================================================================================
    
    private static String[] pathologies = {"Todas","Diabetes", "Hipertension", "Asma (Problemas resp.)", "Tabaquismo", "AJM"};
    
    private static String[] orderPersonnelMenu = {"De mayor a menor grado", 
        "De menor a mayor grado",
        "Orden Alfabetico A-Z",
        "Orden Alfabetico Z-A",
        "Anexo 27 mas moderno",
        "Anexo 27 mas antiguo"};
    
    private static String[] orderPersonnel = {" ORDER BY Grado DESC, Apellido ASC, Nombre ASC",
        " ORDER BY Grado ASC, Apellido ASC, Nombre ASC",
        " ORDER BY Apellido ASC, Nombre ASC, Grado DESC", 
        " ORDER BY Apellido DESC, Nombre DESC, Grado DESC",
        " ORDER BY (SUBSTR(Anexo27,1,4)||SUBSTR(Anexo27,6,2)||SUBSTR(Anexo27,9,2)) DESC, Grado DESC, Apellido ASC, Nombre ASC",
        " ORDER BY (SUBSTR(Anexo27,1,4)||SUBSTR(Anexo27,6,2)||SUBSTR(Anexo27,9,2)) ASC, Grado DESC, Apellido ASC, Nombre ASC"};
    
    //=================================================================================
    private static String[] months = {"","enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
    //=================================================================================
    //=================================================================================
    private MyArrays() {

    }

    //=================================================================================
    //=================================================================================
    // ARREGLOS COMO PROPIEDADES PARA LOS JTABLES DEL FRAME PRINCIPAL
    public static String[] getPersonnelColumns() {
        return personnelColumns;
    }

    public static String getPersonnelColumns(int i) {
        return personnelColumns[i];
    }

    public static int getPersonnelColumnsLength() {
        return personnelColumns.length;
    }

    public static int[] getPersonnelColumnsSize() {
        return personnelColumnsSize;
    }

    public static int getPersonnelColumnsSize(int i) {
        return personnelColumnsSize[i];
    }
    

    //ARREGLOS COMO PROPIEDADES LOS JTABLES DEL PARTE DE ENFERMO
    public static String[] getSickColumns() {
        return sickColumns;
    }

    public static String getSickColumns(int i) {
        return sickColumns[i];
    }

    public static int getSickColumnsLength() {
        return sickColumns.length;
    }

    public static int[] getSickColumnsSize() {
        return sickColumnsSize;
    }

    public static int getSickColumnsSize(int i) {
        return sickColumnsSize[i];
    }
    //ARREGLOS COMO PROPIEDADES LOS JTABLES DEL RECUENTO

    public static String[] getReCountColumns() {
        return reCountColumns;
    }

    public static int[] getReCountColumnsSize() {
        return reCountColumnsSize;
    }
  
   
    public static String getReCountColumns(int i) {
        return reCountColumns[i];
    }
    
    public static int getReCountColumnsSize(int i) {
        return reCountColumnsSize[i];
    }
    public static int getReCountColumnsLength(){
        return reCountColumns.length;
    }

    //=================================================================================
    // ARREGLO SOBRE EL CUAL SE ITERA PARA RECUPERAR LA INFORMACION DE LA BASE DE DATOS A LAS TABLAS
    public static String[] getPersonnelPanelDB() {
        return personnelPanelDB;
    }

    public static String getPersonnelPanelDB(int i) {
        return personnelPanelDB[i];
    }

    public static int getPersonnelPanelDBLength() {
        return personnelPanelDB.length;
    }


    // ARREGLO PARA LOS GRADOS SEGUN SU VALOR NUMERICO, TANTO EN LA CLASE FORMULARIO COMO EN LA CLASE BASEDEDATOS
    public static String[][] getGrades() {
        return grades;
    }

    public static String[] getGrades(int i) {
        return grades[i];
    }

    public static String getGrades(int i, int j) {
        return grades[i][j];
    }

    public static int getGradesLength(int i) {
        return grades[i].length;
    }

    //ARREGLOS PARA LOS COMBO BOX Y CHECK BOX DE LA CLASE FORMULARIO
    public static String[] getCategories() {
        return categories;
    }

    public static String getCategories(int i) {
        return categories[i];
    }

    public static int getCategoriesLength() {
        return categories.length;
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

    public static String[] getSubUnities() {
        return subUnities;
    }

    public static String getSubUnities(int i) {
        return subUnities[i];
    }

    public static int getSubUnitiesLength() {
        return subUnities.length;
    }

    public static String[] getAptitude() {
        return aptitude;
    }

    public static String getAptitude(int i) {
        return aptitude[i];
    }

    public static int getAptitudeLength() {
        return aptitude.length;
    }
    //ARREGLOS PARA EL PARTE

    public static String[] getSickTypes() {
        return sickTypes;
    }
    
    public static String getSickTypes(int i){
        return sickTypes[i];
    }
    public static int getSickTypesLength(){
        return sickTypes.length;
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

    public static String[] getPersonnelFormularyDB() {
        return personnelFormularyDB;
    }

    public static String getPersonnelFormularyDBLength(int i) {
        return personnelFormularyDB[i];
    }

    // ARREGLOS QUE SIRVEN DE AYUDA PARA LOS FILTROS DE LA TABLA
    public static String[] getPathologies() {
        return pathologies;
    }

    public static String getPathologies(int i) {
        return pathologies[i];
    }

    public static int getPathologiesLength() {
        return pathologies.length;
    }

    public static String[] getOrderPersonnelMenu() {
        return orderPersonnelMenu;
    }

    public static String getOrderPersonnelMenu(int i) {
        return orderPersonnelMenu[i];
    }

    public static String[] getOrderPersonnel() {
        return orderPersonnel;
    }
      

    public static String getOrderPersonnel(int i) {
        return orderPersonnel[i];
    }
    
    //meses

    public static String[] getMonths() {
        return months;
    }
    
    
    public static String getMonths(int i){
        return months[i];
    }
}
