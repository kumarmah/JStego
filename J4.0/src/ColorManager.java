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

public class ColorManager {
   public static Color color[] = {
       new Color(255,0,0), /* red */
       new Color(0,0,0), /* black */
       new Color(0,255,0), /* green */
       new Color(0,0,255), /* blue */
       new Color(0,255,255), /* cyan */
       new Color(255,0,255), /* magenta */
       new Color(255,165,0), /* orange */
       new Color(255,255,255), /* white */
       new Color(255,255,0), /* yellow */
       new Color(238,130,238), /* violet */
       new Color(255,192,203), /* pink */
       new Color(160,32,240), /* purple */
       new Color(0,100,0), /* DarkGreen */
       new Color(255,239,213), /* PapayaWhip */
       new Color(255,235,205), /* BlanchedAlmond */
       new Color(255,218,185), /* PeachPuff */
       new Color(255,222,173), /* NavajoWhite */
       new Color(255,250,205), /* LemonChiffon */
       new Color(240,255,240), /* honeydew */
       new Color(245,255,250), /* MintCream */
       new Color(240,248,255), /* AliceBlue */
       new Color(255,240,245), /* LavenderBlush */
       new Color(255,228,225), /* MistyRose */
       new Color(47,79,79), /* DarkSlateGray */
       new Color(105,105,105), /* DimGray */
       new Color(112,128,144), /* SlateGray */
       new Color(119,136,153), /* LightSlateGray */
       new Color(211,211,211), /* LightGray */
       new Color(25,25,112), /* MidnightBlue */
       new Color(0,0,128), /* NavyBlue */
       new Color(100,149,237), /* CornflowerBlue */
       new Color(72,61,139), /* DarkSlateBlue */
       new Color(106,90,205), /* SlateBlue */
       new Color(123,104,238), /* MediumSlateBlue */
       new Color(132,112,255), /* LightSlateBlue */
       new Color(0,0,205), /* MediumBlue */
       new Color(65,105,225), /* RoyalBlue */
       new Color(30,144,255), /* DodgerBlue */
       new Color(0,191,255), /* DeepSkyBlue */
       new Color(135,206,235), /* SkyBlue */
       new Color(135,206,250), /* LightSkyBlue */
       new Color(70,130,180), /* SteelBlue */
       new Color(176,196,222), /* LightSteelBlue */
       new Color(173,216,230), /* LightBlue */
       new Color(176,224,230), /* PowderBlue */
       new Color(175,238,238), /* PaleTurquoise */
       new Color(0,206,209), /* DarkTurquoise */
       new Color(72,209,204), /* MediumTurquoise */
       new Color(224,255,255), /* LightCyan */
       new Color(95,158,160), /* CadetBlue */
       new Color(102,205,170), /* MediumAquamarine */
       new Color(85,107,47), /* DarkOliveGreen */
       new Color(143,188,143), /* DarkSeaGreen */
       new Color(46,139,87), /* SeaGreen */
       new Color(60,179,113), /* MediumSeaGreen */
       new Color(32,178,170), /* LightSeaGreen */
       new Color(152,251,152), /* PaleGreen */
       new Color(0,255,127), /* SpringGreen */
       new Color(124,252,0), /* LawnGreen */
       new Color(0,250,154), /* MediumSpringGreen */
       new Color(173,255,47), /* GreenYellow */
       new Color(50,205,50), /* LimeGreen */
       new Color(154,205,50), /* YellowGreen */
       new Color(34,139,34), /* ForestGreen */
       new Color(107,142,35), /* OliveDrab */
       new Color(189,183,107), /* DarkKhaki */
       new Color(238,232,170), /* PaleGoldenrod */
       new Color(250,250,210), /* LightGoldenrodYellow */
       new Color(255,255,224), /* LightYellow */
       new Color(238,221,130), /* LightGoldenrod */
       new Color(184,134,11), /* DarkGoldenrod */
       new Color(188,143,143), /* RosyBrown */
       new Color(205,92,92), /* IndianRed */
       new Color(139,69,19), /* SaddleBrown */
       new Color(244,164,96), /* SandyBrown */
       new Color(233,150,122), /* DarkSalmon */
       new Color(255,160,122), /* LightSalmon */
       new Color(255,140,0), /* DarkOrange */
       new Color(240,128,128), /* LightCoral */
       new Color(255,69,0), /* OrangeRed */
       new Color(255,105,180), /* HotPink */
       new Color(255,20,147), /* DeepPink */
       new Color(255,182,193), /* LightPink */
       new Color(219,112,147), /* PaleVioletRed */
       new Color(199,21,133), /* MediumVioletRed */
       new Color(208,32,144), /* VioletRed */
       new Color(186,85,211), /* MediumOrchid */
       new Color(153,50,204), /* DarkOrchid */
       new Color(148,0,211), /* DarkViolet */
       new Color(138,43,226), /* BlueViolet */
       new Color(147,112,219), /* MediumPurple */
       new Color(192,192,192), /* gray */
       new Color(127,255,212), /* aquamarine */
       new Color(218,165,32), /* goldenrod */
       new Color(250,240,230), /* linen */
       new Color(255,228,181), /* moccasin */
       new Color(255,248,220), /* cornsilk */
       new Color(255,255,240), /* ivory */
       new Color(255,228,196), /* bisque */
       new Color(255,245,238), /* seashell */
       new Color(220,220,220), /* gainsboro */
       new Color(240,255,255), /* azure */
       new Color(230,230,250), /* lavender */
       new Color(64,224,208), /* turquoise */
       new Color(127,255,0), /* chartreuse */
       new Color(240,230,140), /* khaki */
       new Color(255,215,0), /*  gold */
       new Color(160,82,45), /* sienna */
       new Color(205,133,63), /* peru */
       new Color(222,184,135), /* burlywood */
       new Color(245,245,220), /* beige */
       new Color(245,222,179), /* wheat */
       new Color(210,180,140), /* tan */
       new Color(210,105,30), /* chocolate */
       new Color(178,34,34), /* firebrick */
       new Color(165,42,42), /* brown */
       new Color(250,128,114), /* salmon */
       new Color(255,127,80), /* coral */
       new Color(255,99,71), /* tomato */
       new Color(176,48,96), /* maroon */
       new Color(221,160,221), /* plum */
       new Color(218,112,214), /* orchid */
       new Color(216,191,216), /* thistle */
       new Color(255,250,250), /* snow */
       new Color(248,248,255), /* GhostWhite */
       new Color(245,245,245), /* WhiteSmoke */
       new Color(255,250,240), /* FloralWhite */
       new Color(253,245,230), /* OldLace */
       new Color(250,235,215) /* AntiqueWhite */

   };
   public static Color getColor(int i) {
      return color[i%color.length];
   }
}
