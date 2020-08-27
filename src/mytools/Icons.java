package mytools;

import panels.PersonnelPanel;
import javax.swing.ImageIcon;

public class Icons {

    private ImageIcon iconRefresh;
    private ImageIcon iconoCheck;
    private ImageIcon iconSearch;
    private ImageIcon iconLittleSearch;
    private ImageIcon iconHealthService;
    private ImageIcon iconoSave;
    private ImageIcon iconHealed;
    private ImageIcon iconDelete;
    private ImageIcon iconConfig;
    private ImageIcon iconPlus;
    private ImageIcon iconCalculator;
    private ImageIcon iconPdf;
    
    public Icons(){        
        this.iconRefresh = new ImageIcon("src/myimages/refresh.png");
        this.iconoCheck = new ImageIcon("src/myimages/check.png");
        this.iconSearch = new ImageIcon("src/myimages/searchGrande.png");
        this.iconLittleSearch = new ImageIcon("src/myimages/search.png");
        this.iconHealthService = new ImageIcon("src/myimages/Sanidad.png");
        this.iconoSave = new ImageIcon("src/myimages/save.png");
        this.iconHealed = new ImageIcon("src/myimages/alta.png");
        this.iconDelete = new ImageIcon("src/myimages/delete.png");
        this.iconConfig = new ImageIcon("src/myimages/config.png");
        this.iconPlus = new ImageIcon("src/myimages/plus.png");
        this.iconCalculator = new ImageIcon("src/myimages/calculator.png");
        this.iconPdf = new ImageIcon("src/myimages/pdf.png");
    }

   
    public ImageIcon getIconRefresh() {
        return iconRefresh;
    }
    public ImageIcon getIconoCheck(){
        return iconoCheck;
    }
    public ImageIcon getIconoSearch(){
        return iconSearch; 
    }
    public ImageIcon getIconoSearchChico(){
        return iconLittleSearch; 
    }
    public ImageIcon getIconHealthService(){
        return iconHealthService;
    }

    public ImageIcon getIconoSave() {
        return iconoSave;
    }

    public ImageIcon getIconHealed() {
        return iconHealed;
    }

    public ImageIcon getIconDelete() {
        return iconDelete;
    }

    public ImageIcon getIconConfig() {
        return iconConfig;
    }

    public ImageIcon getIconPlus() {
        return iconPlus;
    }

    public ImageIcon getIconCalculator() {
        return iconCalculator;
    }

    public ImageIcon getIconPdf() {
        return iconPdf;
    }
    

   
}
