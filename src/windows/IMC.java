package windows;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import main.MainFrame;
import mytools.Utilidades;

public class IMC extends JDialog implements ActionListener {

    private ButtonGroup bg;
    private JRadioButton radioMayor;
    private JRadioButton radioMenor;

    private JTextField imc;
    private JButton filtrar;
    
    private Tabla tabla;
    private MainFrame mainFrame;

    public IMC(Frame parent, boolean modal) {
        super(parent, modal);
        //Propiedades del frame       
        setSize(150, 195);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Filtrar por IMC");
        componentes();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }

    private void componentes() {
        //------------------------------
        Utilidades utilidad = new Utilidades();
        //Fondo del frame
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
        Dimension dimension = new Dimension(400, 175);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        //Label
        JLabel label = new JLabel("Filtrar por IMC que sea: ");
        label.setBounds(15,15,140,30);
        container.add(label);
        //RadioButtons
        bg = new ButtonGroup();
        radioMayor = new JRadioButton("Mayor a");
        radioMayor.setBounds(15, 45, 100, 30);
        radioMayor.addActionListener(this);
        radioMayor.setOpaque(false);
        radioMayor.setSelected(true);
        bg.add(radioMayor);
        container.add(radioMayor);
        radioMenor = new JRadioButton("Menor a");
        radioMenor.setBounds(15, 75, 100, 30);
        radioMenor.addActionListener(this);
        radioMenor.setOpaque(false);
        bg.add(radioMenor);
        container.add(radioMenor);
        //TextField
        imc = new JTextField();
        imc.setBounds(15, 110, 100, 20);
        imc.addKeyListener(utilidad.bloquearLetras);
        container.add(imc);
        imc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                }
            }
        });
        filtrar = new JButton("Filtrar");
        filtrar.setBounds(30,135,70,25);
        filtrar.addActionListener(this);
        container.add(filtrar);
        //--------------------------
        this.getContentPane().add(container);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == filtrar){
            try {
                double inputIMC = Double.parseDouble(imc.getText());
                
                tabla.setIMCfilter(inputIMC);
                tabla.setIMCoperator(radioMayor.isSelected() ? ">=" : "<=");
                
                tabla.Actualizar(7, tabla.getShowByDestino(), tabla.getOrder());
                
                mainFrame.eliminarChecksFiltros();
                mainFrame.getMenuFiltroPPS().setIcon(mainFrame.getCheck());
                mainFrame.getItemIMC().setIcon(mainFrame.getCheck());
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,"Numero ingresado incorrecto.");
            }
        }
    }

    public void setTabla(Tabla tabla) {
        this.tabla = tabla;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    
}
