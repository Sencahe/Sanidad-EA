package dialogs;

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
import mytools.Utilities;
import panels.PersonnelPanel;

public class IMC extends JDialog implements ActionListener {

    private ButtonGroup bg;
    private JRadioButton radioMayor;
    private JRadioButton radioMenor;

    private JTextField imc;
    private JButton filtrar;

    private PersonnelPanel tabla;
    private MainFrame mainFrame;

    public IMC(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        //Propiedades del frame       
        setSize(205, 180);
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
        Utilities utilidad = new Utilities();
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
        Dimension dimension = new Dimension(250, 140);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        //Label
        JLabel label = new JLabel("Filtrar por IMC que sea: ");
        label.setFont(utilidad.getFuenteLabelsFormulario());
        label.setBounds(30, 15, 160, 30);
        container.add(label);
        //RadioButtons
        bg = new ButtonGroup();
        radioMayor = new JRadioButton("Mayor a");
        radioMayor.setBounds(30, 45, 70, 30);
        radioMayor.addActionListener(this);
        radioMayor.setOpaque(false);
        radioMayor.setFocusPainted(false);
        radioMayor.setSelected(true);
        bg.add(radioMayor);
        container.add(radioMayor);
        radioMenor = new JRadioButton("Menor a");
        radioMenor.setBounds(95, 45, 90, 30);
        radioMenor.addActionListener(this);
        radioMenor.setOpaque(false);
        radioMenor.setFocusPainted(false);
        bg.add(radioMenor);
        container.add(radioMenor);
        //TextField
        imc = new JTextField();
        imc.setBounds(45, 80, 95, 20);
        imc.addKeyListener(utilidad.bloquearLetras);
        imc.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        container.add(imc);
        imc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filtrarTabla();
                }
            }
        });
        filtrar = new JButton("Filtrar");
        filtrar.setBounds(60, 110, 60, 25);
        filtrar.addActionListener(this);
        container.add(filtrar);
        //--------------------------
        this.getContentPane().add(container);
        utilidad = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == filtrar) {
           filtrarTabla();
        }
    }

    private void filtrarTabla() {
        try {
            double inputIMC = Double.parseDouble(imc.getText());

            tabla.setIMCfilter(inputIMC);
            tabla.setIMCoperator(radioMayor.isSelected() ? ">=" : "<=");

            tabla.update(7, tabla.getShowBySubUnity(), tabla.getRowOrdering());

            mainFrame.deleteChecksFilters();
            mainFrame.getMenuFiltroPPS().setIcon(mainFrame.getCheck());
            mainFrame.getItemIMC().setIcon(mainFrame.getCheck());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Numero ingresado incorrecto.");
        }
    }

    public void setTabla(PersonnelPanel tabla) {
        this.tabla = tabla;
    }
}
