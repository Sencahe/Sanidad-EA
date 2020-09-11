package dialogs;


import mytools.Icons;
import mytools.Utilities;
import main.MainFrame;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;

public class References extends javax.swing.JDialog {
    
    private MainFrame mainFrame;

    public References(java.awt.Frame parent, boolean modal) {       
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        Icons icons = mainFrame.getIcons();
        Utilities utility = mainFrame.getUtility();
        //---------------------------------
        //PROPIEDADES DEL FRAME
            setSize(280, 310);
            setResizable(false);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setTitle("Referencias");
            setIconImage(icons.getIconHealthService().getImage());
             JPanel container = new JPanel();
            container.setBackground(utility.getColorBackground().brighter());
            Dimension dimension = new Dimension(280, 310);
            container.setPreferredSize(dimension);
            container.setLayout(null);
            dimension = null;
        //LABELS CON REFERENCIAS
        Font font = utility.getFuenteLabelsRef();
        int width = 250;
        int heigt = 30;
        int x = 15;
        int y = 15;
        int separation = 25;
        JLabel IMC = new JLabel("IMC: Indice de Masa Coportal");
        IMC.setBounds(x, y, width, heigt);
        IMC.setFont(font);
        container.add(IMC);
        IMC = null;
        y += separation;
        JLabel PPS = new JLabel("PPS: Programa Peso Saludable");
        PPS.setBounds(x, y, width, heigt);
        PPS.setFont(font);
        container.add(PPS);
        PPS = null;
        y += separation;
        JLabel D = new JLabel("D: Diabetes");
        D.setBounds(x, y, width, heigt);
        D.setFont(font);
        container.add(D);
        D = null;
        y += separation;
        JLabel H = new JLabel("H: Hipertension");
        H.setBounds(x, y, width, heigt);
        H.setFont(font);
        container.add(H);
        H = null;
        y += separation;
        JLabel A = new JLabel("A: Asma (Problemas Respiratorios)");
        A.setBounds(x, y, width, heigt);
        A.setFont(font);
        container.add(A);
        A = null;
        y += separation;
        JLabel T = new JLabel("T: Tabaquismo");
        T.setBounds(x, y, width, heigt);
        T.setFont(font);
        container.add(T);
        T = null;
        y += separation;
        JLabel AJM = new JLabel("AJM: Actuacion de Justicia Militar");
        AJM.setBounds(x, y, width, heigt);
        AJM.setFont(font);
        container.add(AJM);
        AJM = null;
        y += separation;
        JLabel Act = new JLabel("Act: Acta (AJM)");
        Act.setBounds(x, y, width, heigt);
        Act.setFont(font);
        container.add(Act);
        Act = null;
        y += separation;
        JLabel Inf = new JLabel("Inf: Informacion (AJM)");
        Inf.setBounds(x, y, width, heigt);
        Inf.setFont(font);
        container.add(Inf);
        Inf = null;
        font = null;
        //---------------------------------      
        this.getContentPane().add(container);
        utility = null;
        icons = null;
    }
}
