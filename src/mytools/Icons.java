package mytools;

import panels.PersonnelPanel;
import javax.swing.ImageIcon;

public class Icons {

    private ImageIcon iconoRefrescar;
    private ImageIcon iconoCheck;
    private ImageIcon iconoBuscar;
    private ImageIcon iconoBuscarChico;
    private ImageIcon iconoSanidad;
    private ImageIcon iconoSave;
    private ImageIcon iconoAlta;
    private ImageIcon iconoDelete;
    private ImageIcon iconoConfig;
    private ImageIcon iconoPlus;
    private ImageIcon iconoCalculator;
    private ImageIcon iconoPdf;
    
    public Icons(){        
        this.iconoRefrescar = new ImageIcon("src/myimages/refresh.png");
        this.iconoCheck = new ImageIcon("src/myimages/check.png");
        this.iconoBuscar = new ImageIcon("src/myimages/searchGrande.png");
        this.iconoBuscarChico = new ImageIcon("src/myimages/search.png");
        this.iconoSanidad = new ImageIcon("src/myimages/Sanidad.png");
        this.iconoSave = new ImageIcon("src/myimages/save.png");
        this.iconoAlta = new ImageIcon("src/myimages/alta.png");
        this.iconoDelete = new ImageIcon("src/myimages/delete.png");
        this.iconoConfig = new ImageIcon("src/myimages/config.png");
        this.iconoPlus = new ImageIcon("src/myimages/plus.png");
        this.iconoCalculator = new ImageIcon("src/myimages/calculator.png");
        this.iconoPdf = new ImageIcon("src/myimages/pdf.png");
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

    public ImageIcon getIconoSave() {
        return iconoSave;
    }

    public ImageIcon getIconoAlta() {
        return iconoAlta;
    }

    public ImageIcon getIconoDelete() {
        return iconoDelete;
    }

    public ImageIcon getIconoConfig() {
        return iconoConfig;
    }

    public ImageIcon getIconoPlus() {
        return iconoPlus;
    }

    public ImageIcon getIconoCalculator() {
        return iconoCalculator;
    }

    public ImageIcon getIconoPdf() {
        return iconoPdf;
    }
    

   
}
