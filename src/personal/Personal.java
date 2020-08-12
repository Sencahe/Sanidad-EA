
package personal;

import mytools.Arreglos;


public class Personal {
       
    private int id; 
    private int categoria;
    private int grado;
    private String nombreCompleto;
    private String destino;
    
    
    public Personal(int id, int categoria, int grado, String nombreCompelto, String destino){
        this.id = id;
        this.categoria = categoria;
        this.grado = grado;
        this.nombreCompleto = nombreCompelto;
        this.destino = destino;        
    }
    
    

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDestino() {
        return destino;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }
    
    @Override
    public String toString(){
        return "<html><pre>Grado: " + Arreglos.getGrados(categoria, grado) + "<br>Nombre: " + this.nombreCompleto 
                + "<br>Destino: " + this.destino + "</pre></html>";
    }
    
}
