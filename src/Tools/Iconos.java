package Tools;

import Frames.Tabla;
import javax.swing.ImageIcon;

public class Iconos {

    ImageIcon wallpaper = new ImageIcon("src/Images/FondoPrincipal.jpg");
    ImageIcon wallpaperFormulario = new ImageIcon("src/Images/FondoFormulario.jpg");
    ImageIcon iconoRefrescar = new ImageIcon("src/Images/refresh.png");
    ImageIcon iconoCheck = new ImageIcon("src/Images/check.png");
    ImageIcon iconoBuscar = new ImageIcon("src/Images/searchGrande.png");
    ImageIcon iconoBuscarChico = new ImageIcon("src/Images/search.png");
    ImageIcon iconoSanidad = new ImageIcon("src/Images/Sanidad.png");
        

    public ImageIcon getWallpaper() {
        return wallpaper;
    }
    public ImageIcon getWallpaperFormulario(){
        return wallpaperFormulario;
    }
    public ImageIcon getIconoRefrescar() {
        return iconoRefrescar;
    }
    public ImageIcon getIconoCheck(){
        return iconoCheck;
    }
    public ImageIcon getIconoSearch(){
        return iconoBuscar; 
    }
    public ImageIcon getIconoSearchChico(){
        return iconoBuscarChico; 
    }
    public ImageIcon getIconoSanidad(){
        return iconoSanidad;
    }
    
    
    public void eliminarChecksFiltros(){
        Tabla.itemAnexoVencido.setIcon(null);
        
        Tabla.menuFiltroPPS.setIcon(null);
        for (int i = 0; i < Tabla.itemsPPS.length; i++) {
            Tabla.itemsPPS[i].setIcon(null);
        }
        Tabla.menuFiltroAptitud.setIcon(null);
        for (int i = 0; i < Tabla.itemsAptitud.length; i++) {
            Tabla.itemsAptitud[i].setIcon(null);
        }
        Tabla.menuPatologias.setIcon(null);
        for (int i = 0; i < Tabla.itemsPatologias.length; i++) {
            Tabla.itemsPatologias[i].setIcon(null);
        }        
        Tabla.itemObservaciones.setIcon(null);
    }
    public void eliminarChecksDestino(){
         for (int i = 0; i < Tabla.itemsDestinos.length; i++) {
            Tabla.itemsDestinos[i].setIcon(null);
        }
    }
    public void eliminarChecksOrden(){
        for (int i = 0; i < Tabla.itemsOrdenar.length; i++) {
            Tabla.itemsOrdenar[i].setIcon(null);
        }
    }
     public void eliminarChecks(){
        //FILTROS
        eliminarChecksFiltros();
        //DESTINOS
        Tabla.menuDestinos.setIcon(null);
        eliminarChecksDestino(); 
        //ORDEN
        Tabla.menuOrdenar.setIcon(null);
        eliminarChecksOrden();
    }
}
