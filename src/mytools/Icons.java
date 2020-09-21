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
    private ImageIcon iconDelete2;
    private ImageIcon iconConfig;
    private ImageIcon iconPlus;
    private ImageIcon iconCalculator;
    private ImageIcon iconPdf;
    
    public Icons(){        
        this.iconRefresh = new ImageIcon(getClass().getResource("/myimages/refresh.png"));
        this.iconoCheck = new ImageIcon(getClass().getResource("/myimages/check.png"));
        this.iconSearch = new ImageIcon(getClass().getResource("/myimages/searchGrande.png"));
        this.iconLittleSearch = new ImageIcon(getClass().getResource("/myimages/search.png"));
        this.iconHealthService = new ImageIcon(getClass().getResource("/myimages/Sanidad.png"));
        this.iconoSave = new ImageIcon(getClass().getResource("/myimages/save.png"));
        this.iconHealed = new ImageIcon(getClass().getResource("/myimages/alta.png"));
        this.iconDelete = new ImageIcon(getClass().getResource("/myimages/delete.png"));
        this.iconConfig = new ImageIcon(getClass().getResource("/myimages/config.png"));
        this.iconPlus = new ImageIcon(getClass().getResource("/myimages/plus.png"));
        this.iconCalculator = new ImageIcon(getClass().getResource("/myimages/calculator.png"));
        this.iconPdf = new ImageIcon(getClass().getResource("/myimages/pdf.png"));
        this.iconDelete2 = new ImageIcon(getClass().getResource("/myimages/delete2.png"));
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

    public ImageIcon getIconDelete2() {
        return iconDelete2;
    }
    

   
}
