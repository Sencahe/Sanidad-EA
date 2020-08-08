package mytools;

import windows.Tabla;
import javax.swing.ImageIcon;

public class Iconos {

    private ImageIcon wallpaper;
    private ImageIcon wallpaperFormulario;
    private ImageIcon iconoRefrescar;
    private ImageIcon iconoCheck;
    private ImageIcon iconoBuscar;
    private ImageIcon iconoBuscarChico;
    private ImageIcon iconoSanidad;
    
    public Iconos(){
        this.wallpaper = new ImageIcon("src/myimages/FondoPrincipal.jpg");
        this.wallpaperFormulario = new ImageIcon("src/myimages/FondoFormulario.jpg");
        this.iconoRefrescar = new ImageIcon("src/myimages/refresh.png");
        this.iconoCheck = new ImageIcon("src/myimages/check.png");
        this.iconoBuscar = new ImageIcon("src/myimages/searchGrande.png");
        this.iconoBuscarChico = new ImageIcon("src/myimages/search.png");
        this.iconoSanidad = new ImageIcon("src/myimages/Sanidad.png");
    }

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
    

   
}
