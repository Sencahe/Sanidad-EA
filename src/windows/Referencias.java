package windows;

import mytools.Iconos;
import java.awt.Font;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import mytools.Utilidades;

public class Referencias extends javax.swing.JDialog {

    public Referencias(java.awt.Frame parent, boolean modal) {
        //PROPIEDADES DEL FRAME
        super(parent, modal);
        Iconos iconos = new Iconos();
        Utilidades utilidad = new Utilidades();
        setLayout(null);
        setSize(280, 310);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Referencias");
        setIconImage(iconos.getIconoSanidad().getImage());
        //LABELS CON REFERENCIAS
        Font font = utilidad.fuenteLabelsRef();
        int width = 250;
        int heigt = 30;
        int x = 15;
        int y = 15;
        int espacio = 25;
        JLabel IMC = new JLabel("IMC: Indice de Masa Coportal");
        IMC.setBounds(x, y, width, heigt);
        IMC.setFont(font);
        add(IMC);
        IMC = null;
        y += espacio;
        JLabel PPS = new JLabel("PPS: Programa Peso Saludable");
        PPS.setBounds(x, y, width, heigt);
        PPS.setFont(font);
        add(PPS);
        PPS = null;
        y += espacio;
        JLabel D = new JLabel("D: Diabetes");
        D.setBounds(x, y, width, heigt);
        D.setFont(font);
        add(D);
        D = null;
        y += espacio;
        JLabel H = new JLabel("H: Hipertension");
        H.setBounds(x, y, width, heigt);
        H.setFont(font);
        add(H);
        H = null;
        y += espacio;
        JLabel A = new JLabel("A: Asma (Problemas Respiratorios)");
        A.setBounds(x, y, width, heigt);
        A.setFont(font);
        add(A);
        A = null;
        y += espacio;
        JLabel T = new JLabel("T: Tabaquismo");
        T.setBounds(x, y, width, heigt);
        T.setFont(font);
        add(T);
        T = null;
        y += espacio;
        JLabel AJM = new JLabel("AJM: Actuacion de Justicia Militar");
        AJM.setBounds(x, y, width, heigt);
        AJM.setFont(font);
        add(AJM);
        AJM = null;
        y += espacio;
        JLabel Act = new JLabel("Act: Acta (AJM)");
        Act.setBounds(x, y, width, heigt);
        Act.setFont(font);
        add(Act);
        Act = null;
        y += espacio;
        JLabel Inf = new JLabel("Inf: Informacion (AJM)");
        Inf.setBounds(x, y, width, heigt);
        Inf.setFont(font);
        add(Inf);
        Inf = null;

        font = null;
        //WALPAPER
        JLabel wallpaper = new JLabel();
        wallpaper.setBounds(0, 0, this.getWidth(), this.getHeight());
        add(wallpaper);
        Icon fondo = new ImageIcon((iconos.getWallpaperFormulario()).getImage().getScaledInstance(wallpaper.getWidth(),
                wallpaper.getHeight(), Image.SCALE_DEFAULT));
        wallpaper.setIcon(fondo);
        utilidad = null;
        iconos = null;
        fondo = null;
        wallpaper = null;
    }
}
