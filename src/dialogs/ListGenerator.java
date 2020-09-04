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
    private ButtonGroup bg;
    private JRadioButton yes, no;
    private JButton generate;

    public ListGenerator(Frame parent, boolean modal) {
        super(parent, modal);
        this.mainFrame = (MainFrame) parent;
        components();
    }

    public void components() {
        //---------------------------------------
        Utilities utilidad = mainFrame.getUtility();
        Icons iconos = mainFrame.getIcons();
        //PROPIEDADES DEL FRAME-------------------------------------------------
        setSize(280, 210);
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
        textTitle.setBounds(30, 50, 200, 25);
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

        civilians = new JLabel("Incluir civiles");
        civilians.setBounds(80, 75, 200, 30);
        civilians.setFont(utilidad.getFontLabelFormulary());
        container.add(civilians);

        bg = new ButtonGroup();
        yes = new JRadioButton("Si");
        yes.setBounds(75, 100, 50, 30);
        yes.setFont(utilidad.getFontLabelFormulary());
        yes.setOpaque(false);
        yes.setFocusPainted(false);
        bg.add(yes);
        container.add(yes);
        no = new JRadioButton("No");
        no.setBounds(120, 100, 50, 30);
        no.setFont(utilidad.getFontLabelFormulary());
        no.setOpaque(false);
        no.setSelected(true);
        no.setFocusPainted(false);
        bg.add(no);
        container.add(no);

        generate = new JButton("<html>Generar</html>");
        generate.setBounds(80, 130, 70, 30);
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
            boolean addCivilians = yes.isSelected();

            Report report = new Report();
            report.createListReport(personnelPanel, title, addCivilians);
            report = null;
            dispose();
        }
    }

    public void setPersonnelPanel(PersonnelPanel personnelPanel) {
        this.personnelPanel = personnelPanel;
    }

}
