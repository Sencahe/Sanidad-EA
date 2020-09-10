
package mytools.mycomponents;

import javax.swing.JScrollPane;
import javax.swing.JTable;


public class MyJScrollPane extends JScrollPane{
    
    public MyJScrollPane(JTable t){
        super(t);
        this.setViewport(new MyViewport());
        this.setViewportView(t);
        
        
    }
}
