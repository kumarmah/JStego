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

public class GraphicsSymbol {
   private static final int types = 5;
   public static void drawSymbol(Graphics g, int sym, int size, int x, int y) {
      if ( g == null ) return;
      sym = sym%types;
      switch(sym) {
         case 0: // +
            g.drawLine(x-size/2,y,x+size/2,y);
            g.drawLine(x,y-size/2,x,y+size/2);
            break;
         case 1: // x
            g.drawLine(x-size/2,y-size/2,x+size/2,y+size/2);
            g.drawLine(x-size/2,y+size/2,x+size/2,y-size/2);
            break;
         case 2: // box
            g.fillRect(x-size/2,y-size/2,size,size);
            break;
         case 3: // x
            g.fillOval(x-size/2,y-size/2,size,size);
            break;
         case 4: // + & x
            g.drawLine(x-size/2,y,x+size/2,y);
            g.drawLine(x,y-size/2,x,y+size/2);
            g.drawLine(x-size/2,y-size/2,x+size/2,y+size/2);
            g.drawLine(x-size/2,y+size/2,x+size/2,y-size/2);
            break;
      }
   }
}
