import java.io.PrintStream;
/* JPEGFrame.java --
   Copyright (C)  2005  Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */




public class JPEGFrame
{
  public final static byte JPEG_COLOR_GRAY = 1;
  public final static byte JPEG_COLOR_RGB = 2;
  public final static byte JPEG_COLOR_YCbCr = 3;
  public final static byte JPEG_COLOR_CMYK = 4;

  public byte precision = 8;
  public byte colorMode = JPEGFrame.JPEG_COLOR_YCbCr;
  public byte componentCount = 0;

  public short width=0, height=0;

  public JPEGScan components;
  int max_horizontal_frequency = 0 ;
  int max_vertical_frequency = 0 ;

  public JPEGFrame()
  {
    components = new JPEGScan();
  }

  public void addComponent(byte componentID, byte sampleFactors,
                           byte quantizationTableID,boolean verbose,PrintStream p)
  {
    byte sampleHorizontalFactor = (byte)(sampleFactors >> 4);
    byte sampleVerticalFactor = (byte)(sampleFactors & 0x0f);
    components.addComponent(componentID, sampleHorizontalFactor,
                            sampleVerticalFactor, quantizationTableID);
    if (sampleHorizontalFactor> max_horizontal_frequency)
        {
          max_horizontal_frequency =sampleHorizontalFactor;
        }

        if (sampleVerticalFactor > max_vertical_frequency)
        {
          max_vertical_frequency =sampleVerticalFactor;
        }
	    if (verbose)
	    {
	      p.println("   Component " +componentID+
	      "\n   Horizontal Frequency: "+
	      sampleHorizontalFactor+
	      "\n   Vertical Frequency: "+
	         sampleVerticalFactor+
	      "\n   Quantization Table: "+ quantizationTableID);
	    }
	    

  }

  public void setPrecision(byte data)
  {
    precision = data;
  }

  public void setScanLines(short data)
  {
    height = data;
  }

  public void setSamplesPerLine(short data)
  {
    width = data;
  }

  public void setColorMode(byte data)
  {
    colorMode = data;
  }

  public void setComponentCount(int data)
  {
    componentCount = (byte) data;
  }

  public byte getComponentCount()
  {
    return componentCount;
  }

  public void setHuffmanTables(byte componentID, JPEGHuffmanTable table,
                               JPEGHuffmanTable table2)
  {
    JPEGComponent comp = (JPEGComponent)components.getComponentByID(componentID);
    comp.setACTable(table);
    comp.setDCTable(table2);
  }

 public HuffmanTable getHuffmanTables(byte componentID, int tabletype)
  {
    JPEGComponent comp = (JPEGComponent)components.getComponentByID(componentID);
    if(tabletype==0)
    return comp.getDCTable();
    else 
       return  comp.getACTable();
  }
}
