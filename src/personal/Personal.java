
package personal;


public class Personal {
       
    private int id; 
    private int categoria;
    private String nombreCompleto;
    private String destino;
    private String grado;
    
    public Personal(int id, int categoria, String grado, String nombreCompelto, String destino){
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

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }
    
    
}
