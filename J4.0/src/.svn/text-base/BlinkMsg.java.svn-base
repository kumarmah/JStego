/*
 * BlinkMsg.java
 *
 * Created on October 26, 2007, 9:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Mahendra Kumar(kumarmahATgmail.com)
 */
public class BlinkMsg implements Runnable{
    javax.swing.JLabel processmsg;
    boolean c=true;
    /** Creates a new instance of BlinkMsg */
    public BlinkMsg(javax.swing.JLabel processmsg) {
        this.processmsg=processmsg;
        Thread t=new Thread(this);
        t.start();
    }
    public void run(){
        while(true){
            if(c==false){
        processmsg.setText("");
            processmsg.repaint();
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        processmsg.setText("Processing......");
        processmsg.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
       // processmsg.setText("");
        }

    }
    
    public void  makefalse(){
        c=false;
        
    }
}
