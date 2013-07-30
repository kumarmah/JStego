import java.awt.image.*;
import java.awt.*;
import java.applet.*;

public class Histogram extends Applet {
  Image myImage;

  public void init() {
    int x = 500;
    int y = 500;
    myImage = createImage(x,y);
    Graphics g = myImage.getGraphics();
    g.drawLine(0,0,x,y);
    g.drawLine(x,0,0,y);
    for(int i=0; i < x; i+=1){
       setPixel(myImage, 50, i, new Color(0).blue);
       
       setPixel(myImage, 51, i, new Color(0).green);
       }
    }

  public void paint(Graphics g) {
    g.drawImage(myImage,0,0,this);
    }

  public void setPixel(Image image, int x, int y, Color color ) {
    Graphics g = image.getGraphics( );
    g.setColor( color );
    g.fillRect( x, y, 1, 1 );
    g.dispose( );
    }
}