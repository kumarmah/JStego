/*
* The contents of this file are subject to the BT "ZEUS" Open Source 
* Licence (L77741), Version 1.0 (the "Licence"); you may not use this file 
* except in compliance with the Licence. You may obtain a copy of the Licence
* from $ZEUS_INSTALL/licence.html or alternatively from
* http://www.labs.bt.com/projects/agents/zeus/licence.htm
* 
* Except as stated in Clause 7 of the Licence, software distributed under the
* Licence is distributed WITHOUT WARRANTY OF ANY KIND, either express or 
* implied. See the Licence for the specific language governing rights and 
* limitations under the Licence.
* 
* The Original Code is within the package zeus.*.
* The Initial Developer of the Original Code is British Telecommunications
* public limited company, whose registered office is at 81 Newgate Street, 
* London, EC1A 7AJ, England. Portions created by British Telecommunications 
* public limited company are Copyright 1996-9. All Rights Reserved.
* 
* THIS NOTICE MUST BE INCLUDED ON ANY COPY OF THIS FILE
*/

import java.util.*;
import java.awt.*;
//import zeus.gui.ColorManager;
//import zeus.util.Misc;

public class XYGraph implements DrawObject {
   protected static final int TYPE1 = 0;
   protected static final int TYPE2 = 1;
   protected static double TINY = 1E-6;
   protected static int    LEFT = 10;
   //protected static int    LEFT = 100;
     protected static int    STEP = 5;
   protected static int    KEY_LINE_LENGTH = 15;
   protected String title;
   protected int x, y = 0;
   protected FontMetrics fm;
   protected Font        font;
   protected int         gap = 10;
   protected double      max_x = 0.0, min_x = 0.0, max_y = 0.0, min_y = 0.0;
   protected double[][]  x_values, y_values;
   protected String[]    keys;
   protected int         type = -1;

   public XYGraph() {
   }
   public XYGraph(double[] y_values, double[] x_values, String title) {
      setData(y_values, x_values, title);
   }
   public XYGraph(double[][] y_values, double[][] x_values, String[] keys,
                  String title) {
      setData(y_values, x_values, keys, title);
   }

   public void setData(double[] y_values, double[] x_values, String title) {
      type = TYPE1;
      this.title = title;
      this.y_values = new double[1][y_values.length];
      this.y_values[0] = y_values;
      this.x_values = new double[1][x_values.length];
      this.x_values[0] = x_values;
      computeMinMax();
   }

   public void setData(double[][] y_values, double[][] x_values, String[] keys,
                       String title) {
      type = TYPE2;
      this.title = title;
      this.keys = keys;
      this.y_values = y_values;
      this.x_values = x_values;
      computeMinMax();
   }

   protected void computeMinMax() {
      max_y = min_y = max_x = min_x = 0.0;
      for(int i = 0; i < y_values.length; i++ ) {
         for(int j = 0; j < y_values[i].length; j++) {
            max_y = Math.max(max_y,y_values[i][j]);
            min_y = Math.min(min_y,y_values[i][j]);
            max_x = Math.max(max_x,x_values[i][j]);
            min_x = Math.min(min_x,x_values[i][j]);
         }
      }
   }

   public void drawYourSelf(Graphics g) {
      // TEST(1);
      font = new Font("Arial", Font.BOLD, 14);
      fm = g.getFontMetrics(font);
      g.setFont(font);
      int w  = fm.stringWidth(title);
      int h  = fm.getHeight();
      g.drawString(title,(x-w)/2,h);

      // TEST(2);
      if ( min_x == 0 && max_x == 0 && min_y == 0 && max_y == 0 ) return;


      font = new Font("Arial", Font.PLAIN, 12);
      fm = g.getFontMetrics(font);
      g.setFont(font);
      // TEST(3);

      double max_w = min_x > 0.0 ? max_x : Math.abs(max_x-min_x);
      double max_h = min_y > 0.0 ? max_y : Math.abs(max_y-min_y);

      // enforce non-zero
      if ( isZero(max_w) ) max_w = 1E-6;
      if ( isZero(max_h) ) max_h = 1E-6;

      int x0 = 0, y0 = 0, x1, y1, x2, y2, xp, yp;
      int mw = 0, x_length = 0, y_length = 0;
      // TEST(4);

      if ( type == TYPE2 )
      for(int j = 0; j < keys.length; j++ )
         mw = Math.max(mw,fm.stringWidth(keys[j]));

      // TEST(5);
      mw += 20 + 10 + KEY_LINE_LENGTH + 10;
      x_length = x - LEFT - mw;
      y_length = y-5*h;

  //    g.drawLine(LEFT,3*h,LEFT,y-2*h);  // Y-axis

      x0 = LEFT;
      x1 = x0 + x_length;

      y0 = (int)(3*h + y_length - (0-min_y)*y_length/max_h);
      // TEST(6);
      y1 = y0;
      g.drawLine(x0,y0,x1,y1); // X-axis
 
      
      //g.drawLine((x1-x0)/2-LEFT,3*h,(x1-x0)/2-LEFT,y-2*h);  // Y-axis

      // TEST(7);
      for(int i = 0; i < y_values.length; i++ ) {
         if ( y_values[i].length > 0 ) {
            x0 = (int)((x_values[i][0] - min_x)*x_length/max_w + LEFT);
            y0 = (int)(y_length - (y_values[i][0]-min_y)*y_length/max_h + 3*h);
            g.setColor(ColorManager.getColor(i));
            GraphicsSymbol.drawSymbol(g,i,10,x0,y0);
            //System.out.println(x0+"     "+y0);
            for(int j = 1; j < y_values[i].length; j++ ) {
               x1 = (int)((x_values[i][j]-min_x)*x_length/max_w + LEFT);
               y1 = (int)(3*h + y_length - (y_values[i][j]-min_y)*y_length/max_h);
               GraphicsSymbol.drawSymbol(g,i,10,x1,y1);
               g.drawLine(x0,y0,x1,y1);
               x0 = x1;
               y0 = y1;
            }
         }
      }

      // TEST(8);
      double v;
      x0 = (int)(LEFT + (0-min_x)*x_length/max_w);
      y0 = (int)(3.0*h + y_length - (0-min_y)*y_length/max_h);

      double dy = Math.max(Math.abs(max_y),Math.abs(min_y))/STEP;
      if ( dy + 0.51  > 1 )
         dy = (int)(dy+0.51);
      int step = (int)(dy*y_length/max_h);
 
      g.setColor(Color.black);

      // TEST(9);
      v = 0;
      for(int i = 0; (!isZero(dy) || i == 0) && v + dy <= max_y; i++ ) {
         // TEST(91);
         v = i*dy;
         String yval = Misc.decimalPlaces(v,2);
         xp = x0;
 
         yp = y0 - i*step;
         g.drawLine(xp,yp,xp-10,yp);
         w = fm.stringWidth(yval);
         g.drawString(yval,xp-10-w-5,yp);
      }
      // TEST(10);
      if ( min_y < 0.0 ) {
         v = -1*dy;
         for(int i = 1; !isZero(dy) && v - dy >= min_y; i++ ) {
            // TEST(101);
            v = -1*i*dy;
            String yval = Misc.decimalPlaces(v,2);
            xp = x0;
            yp = y0 + i*step;
            g.drawLine(xp,yp,xp-10,yp);
            w = fm.stringWidth(yval);
            g.drawString(yval,xp-10-w-5,yp);
         }
      }

      // TEST(11);
      x0 = (int)(LEFT + (0-min_x)*x_length/max_w);
      y0 = (int)(3.0*h + y_length - (0-min_y)*y_length/max_h);

      double dx = Math.max(Math.abs(max_x),Math.abs(min_x))/STEP;
      if ( dx + 0.51  > 1 )
         dx = (int)(dx+0.51);
      step = (int)(dx*x_length/max_w);
      //System.out.println(step+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

      // TEST(12);
      g.setColor(Color.black);
      v = 0;
      for(int i = 0; (!isZero(dx) || i == 0) && v + dx <= max_x; i++ ) {
         // TEST(121);
         v = i*dx;
         String xval = Misc.decimalPlaces(v,2);
         yp = y0;
         xp = x0 + i*step;
         if(v==0.0){
             g.drawLine(xp,4*h,xp,y-2*h);  // Y-axis
          	     
           }

         g.drawLine(xp,yp,xp,yp+10);
         w = fm.stringWidth(xval);
         g.drawString(xval,xp-w/2,yp+10+h);
      }
      // TEST(13);
      if ( min_x < 0.0 ) {
         v = -1*dx;
         for(int i = 1; !isZero(dx) && v - dx >= min_x; i++ ) {
            // TEST(131);
            v = -1*i*dx;
            String xval = Misc.decimalPlaces(v,2);
            yp = y0;
            xp = x0 - i*step;
            g.drawLine(xp,yp,xp,yp-10);
            w = fm.stringWidth(xval);
            g.drawString(xval,xp-w/2,yp-10-h);
         }
      }

      // TEST(14);
      if ( type == TYPE2 ) {
         x1 = LEFT+x_length+20; y1 = 3*h;
         int dh = Math.max(h+10,20);
         g.setColor(Color.black);
         g.drawString("Key",x1,y1);
         for(int i = 0; i < keys.length; i++ ) {
            y1 += dh;
            g.setColor(ColorManager.getColor(i));
            g.drawLine(x1,y1,x1+KEY_LINE_LENGTH,y1);
            g.setColor(Color.black);
            g.drawString(keys[i],x1+KEY_LINE_LENGTH + 10,y1);
         }
      }
      // TEST(15);
   }

   void TEST(int num) {
      if ( y_values[0].length == 0 || y_values[1].length == 0 )
         System.err.println("drawYourSelf position" + num);
   }

   boolean isZero(double num) {
      return (Math.abs(num) < TINY);
   }

   public void setXY(int xc, int yc) {
      x = xc; y = yc;
   }
}
