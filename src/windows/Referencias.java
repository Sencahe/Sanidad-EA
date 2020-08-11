package windows;

import java.awt.Color;
import java.awt.Dimension;
import mytools.Iconos;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mytools.Utilidades;

public class Referencias extends javax.swing.JDialog {

    public Referencias(java.awt.Frame parent, boolean modal) {       
        super(parent, modal);
        Iconos iconos = new Iconos();
        Utilidades utilidad = new Utilidades();
        //---------------------------------
        //PROPIEDADES DEL FRAME
            setSize(280, 310);
            setResizable(false);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setTitle("Referencias");
            setIconImage(iconos.getIconoSanidad().getImage());
             JPanel container = new JPanel() {
                @Override
                protected void paintComponent(Graphics grphcs) {
                    super.paintComponent(grphcs);
                    Graphics2D g2d = (Graphics2D) grphcs;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp = new GradientPaint(200, 170,
                            getBackground().brighter().brighter(), 0, 200,
                            getBackground().darker().darker());
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            container.setBackground(utilidad.getColorFondo());
            Dimension dimension = new Dimension(280, 310);
            container.setPreferredSize(dimension);
            container.setLayout(null);
            dimension = null;
        //LABELS CON REFERENCIAS
        Font font = utilidad.getFuenteLabelsRef();
        int width = 250;
        int heigt = 30;
        int x = 15;
        int y = 15;
        int espacio = 25;
        JLabel IMC = new JLabel("IMC: Indice de Masa Coportal");
        IMC.setBounds(x, y, width, heigt);
        IMC.setFont(font);
        container.add(IMC);
        IMC = null;
        y += espacio;
        JLabel PPS = new JLabel("PPS: Programa Peso Saludable");
        PPS.setBounds(x, y, width, heigt);
        PPS.setFont(font);
        container.add(PPS);
        PPS = null;
        y += espacio;
        JLabel D = new JLabel("D: Diabetes");
        D.setBounds(x, y, width, heigt);
        D.setFont(font);
        container.add(D);
        D = null;
        y += espacio;
        JLabel H = new JLabel("H: Hipertension");
        H.setBounds(x, y, width, heigt);
        H.setFont(font);
        container.add(H);
        H = null;
        y += espacio;
        JLabel A = new JLabel("A: Asma (Problemas Respiratorios)");
        A.setBounds(x, y, width, heigt);
        A.setFont(font);
        container.add(A);
        A = null;
        y += espacio;
        JLabel T = new JLabel("T: Tabaquismo");
        T.setBounds(x, y, width, heigt);
        T.setFont(font);
        container.add(T);
        T = null;
        y += espacio;
        JLabel AJM = new JLabel("AJM: Actuacion de Justicia Militar");
        AJM.setBounds(x, y, width, heigt);
        AJM.setFont(font);
        container.add(AJM);
        AJM = null;
        y += espacio;
        JLabel Act = new JLabel("Act: Acta (AJM)");
        Act.setBounds(x, y, width, heigt);
        Act.setFont(font);
        container.add(Act);
        Act = null;
        y += espacio;
        JLabel Inf = new JLabel("Inf: Informacion (AJM)");
        Inf.setBounds(x, y, width, heigt);
        Inf.setFont(font);
        container.add(Inf);
        Inf = null;
        font = null;
        //---------------------------------      
        this.getContentPane().add(container);
        utilidad = null;
        iconos = null;
    }
}
