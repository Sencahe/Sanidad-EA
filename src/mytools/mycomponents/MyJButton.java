package mytools.mycomponents;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

public class MyJButton extends JButton implements MouseListener {
    
    private static final Color FOREGROUND = new Color(240,240,240);
    private static final Color BACKGROUND = new Color(0, 70, 170);
    private static final Font FONT = new Font("Tahoma", 1, 13);
    private static final Cursor CURSOR = new Cursor(Cursor.HAND_CURSOR);

    public MyJButton() {
        super(null,null);
        super.setFont(FONT);
        super.setBackground(BACKGROUND);
        super.setForeground(FOREGROUND);
        super.setCursor(CURSOR);
        super.setContentAreaFilled(false);
        super.setOpaque(true);
        super.setFocusPainted(false);
        super.setBorderPainted(false);
        super.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(this.getBackground().brighter());
    }   

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(this.getBackground().darker());
    }

}
