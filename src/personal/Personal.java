
package personal;

import mytools.Arreglos;


public class Personal {
       
    private int id; 
    private int categoria;
    private int grado;
    private String nombreCompleto;
    private String destino;
    private char sexo;
    
    
    public Personal(int id, int categoria, int grado, String nombreCompelto, String destino, char sexo){
        this.id = id;
        this.categoria = categoria;
        this.grado = grado;
        this.nombreCompleto = nombreCompelto;
        this.destino = destino;        
        this.sexo = sexo;
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
    
    

    
    @Override
    public String toString(){
        return "<html><pre>Grado: " + Arreglos.getGrados(categoria, grado) + "<br>Nombre: " + this.nombreCompleto 
                + "<br>Destino: " + this.destino + "</pre></html>";
    }
    
}
