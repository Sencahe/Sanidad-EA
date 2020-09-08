package dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import main.MainFrame;
import panels.PersonnelPanel;
import mytools.Icons;
import mytools.Utilities;
import database.Report;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ListGenerator extends JDialog implements ActionListener {

    private MainFrame mainFrame;
    private PersonnelPanel personnelPanel;

    private JLabel message, civilians;
    private JTextField textTitle;
    private JCheckBox of, sub, sold, civ;

    private JButton generate;

    public ListGenerator(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        components();
    }

    public void components() {
        //---------------------------------------
//        Utilities utilidad = mainFrame.getUtility();
//        Icons iconos = mainFrame.getIcons();
        Utilities utilidad = new Utilities();
        Icons iconos = new Icons();
        //PROPIEDADES DEL FRAME-------------------------------------------------
        setSize(300, 210);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Generar Lista");
        setIconImage(iconos.getIconHealthService().getImage());
        //fondo del frame
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(200, 200,
                        getBackground().brighter(), 200, 300,
                        getBackground().darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

            }
        };
        container.setBackground(utilidad.getColorBackground());
        Dimension dimension = new Dimension(280, 230);
        container.setPreferredSize(dimension);
        container.setLayout(null);
        dimension = null;
        //------------------------------------------------------
        message = new JLabel("Ingrese el titulo para la lista");
        message.setBounds(30, 10, 200, 30);
        message.setFont(utilidad.getFontLabelBig());
        container.add(message);

        textTitle = new JTextField();
        textTitle.setBounds(10, 40, 260, 25);
        textTitle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createList();
                }
            }
        });
        container.add(textTitle);

        civilians = new JLabel("Incluir");
        civilians.setBounds(20, 65, 200, 30);
        civilians.setFont(utilidad.getFontLabelFormulary());
        container.add(civilians);

        of = new JCheckBox("Oficiales");
        of.setBounds(10, 90, 75, 30);
        of.setOpaque(false);
        of.setSelected(true);
        add(of);

        sub = new JCheckBox("SubOficiales");
        sub.setBounds(75, 90, 85, 30);
        sub.setOpaque(false);
        sub.setSelected(true);
        add(sub);

        sold = new JCheckBox("Soldados");
        sold.setBounds(155, 90, 75, 30);
        sold.setOpaque(false);
        sold.setSelected(true);
        add(sold);

        civ = new JCheckBox("Civiles");
        civ.setBounds(220, 90, 75, 30);
        civ.setOpaque(false);
        add(civ);

        generate = new JButton("<html>Generar</html>");
        generate.setBounds(95, 130, 70, 30);
        generate.addActionListener(this);
        container.add(generate);

        //---------------------------------
        this.getContentPane().add(container);
        utilidad = null;
        iconos = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generate) {
            createList();
        }
    }

    private void createList() {
        if (!textTitle.getText().equals("")) {
            String title = textTitle.getText();

            boolean jump[] = new boolean[4];

            jump[0] = of.isSelected();
            jump[1] = sub.isSelected();
            jump[2] = sold.isSelected();
            jump[3] = civ.isSelected();

            Report report = new Report();
            report.createListReport(personnelPanel, title, jump);
            report = null;
            dispose();
        }
    }

    public void setPersonnelPanel(PersonnelPanel personnelPanel) {
        this.personnelPanel = personnelPanel;
    }

 

}
