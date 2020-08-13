
package personal;

import mytools.Arreglos;


public class Personal {
       
    private int id; 
    private int categoria;
    private int grado;
    private String nombreCompleto;
    private String destino;
    private char sexo;
    private int dni;
    
    private int diasParte;
       
    public Personal(int id, int categoria, int grado, String nombreCompelto, String destino,
            char sexo,int dni){
        this.id = id;
        this.categoria = categoria;
        this.grado = grado;
        this.nombreCompleto = nombreCompelto;
        this.destino = destino;        
        this.sexo = sexo;
        this.dni = dni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
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
    
    

    
    @Override
    public String toString(){
        return "<html><pre>Grado: " + Arreglos.getGrados(categoria, grado) + "<br>Nombre: " + this.nombreCompleto 
                + "<br>Destino: " + this.destino + "<br>DNI: " + this.dni + "</pre></html>";
    }
    
}
