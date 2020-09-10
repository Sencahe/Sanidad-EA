
package personnel;

import mytools.MyArrays;


public class Personnel {
       
    private int id; 
    private int categorie;
    private int grade;
    private String completeName;
    private String subUnity;
    private char genre;
    private int dni;
    private String esp;
    
    private int diasParte;
       
    public Personnel(int id, int categorie, int grade, String esp,String completeName, String subUnity,
            char genre, int dni){
        this.id = id;
        this.categorie = categorie;
        this.grade = grade;
        this.completeName = completeName;
        this.subUnity = subUnity;        
        this.genre = genre;
        this.dni = dni;
        this.esp = esp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategorie() {
        return categorie;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getSubUnity() {
        return subUnity;
    }

    public void setSubUnity(String subUnity) {
        this.subUnity = subUnity;
    }

    public char getGenre() {
        return genre;
    }

    public void setGenre(char genre) {
        this.genre = genre;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public int getDiasParte() {
        return diasParte;
    }

    public void setDiasParte(int diasParte) {
        this.diasParte = diasParte;
    }

    public String getEsp() {
        return esp;
    }

    public void setEsp(String esp) {
        this.esp = esp;
    }
    
    
    
    @Override
    public String toString(){
        return "<html>Grado: " + MyArrays.getGrades(categorie, grade) +  "<br>Arma/Serv: " +this.esp  
                + "<br>Nombre: " + this.completeName + "<br>Destino: " + 
                this.subUnity + "<br>DNI: " + this.dni + "</html>";
    }
    
}
