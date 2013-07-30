
/***********************************************************************************************
* File Info: $Id: JPEGEncoder.java,v 1.1.1.1 2002/03/31 14:11:52 nathaniel_auvil Exp $
* Copyright (C) 2000
* Author: Nathaniel G. Auvil
* Contributor(s):
*
* This library is free software; you can redistribute it and/or modify it under the terms of the
* GNU Lesser General Public License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
* Boston, MA  02111-1307  USA
************************************************************************************************/





import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.sun.image.codec.jpeg.*;
import javax.imageio.ImageIO;

import sun.awt.image.codec.JPEGParam;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGDecodeParam;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGQTable;


/*******************************************************************************************/
public final class JPEGEncoder2
{
	public static final String MIME_TYPE= "image/jpeg";

	private static final String JPEG= "jpeg";

	static
	{
		//---do not use a file cache as hurts performance
		ImageIO.setUseCache( false );
	}


	/******************************************************************************************
	* Convenience method to call from a Servlet or JSP.  This method will set the appropriate
	*  mime type and then export the chart as the response.
	*
	* @param chart
	* @param quality float value from 0.0f(worst image quality) - 1.0f(best image quality)
	* @param httpServletResponse
	* @throws Throwable
	*******************************************************************************************/


	/******************************************************************************************
	* Encodes the chart to a JPEG format. If you are generating large dimension images, the file
	*  size can get quite large.  You can try decreasing the quality to decrease the file size.
	*
	* @param outputStream
	* @param quality float value from 0.0f(worst image quality) - 1.0f(best image quality)
	* @throws Throwable
	*******************************************************************************************/
	public static final void encode(String infile,  String outfile, JPEGFrame frame )
	{
		FileInputStream in;
		try {
			
			in = new FileInputStream(infile);
		BufferedImage img = ImageIO.read(in);
		
		FileOutputStream outputStream = new FileOutputStream(outfile);
		JPEGComponent comp0 = (JPEGComponent) frame.components.get(0);
		JPEGComponent comp1 = (JPEGComponent) frame.components.get(1);
		JPEGComponent comp2 = (JPEGComponent) frame.components.get(2);

		
		JPEGQTable jeq0=new JPEGQTable(comp0.quantizationTable);
		JPEGQTable jeq1=new JPEGQTable(comp1.quantizationTable);
		JPEGQTable jeq2=new JPEGQTable(comp2.quantizationTable);
		int colorid=JPEGParam.getDefaultColorId(img.getColorModel());
		Raster ras=img.getRaster();
		
		JPEGEncodeParam enParam = JPEGCodec.getDefaultJPEGEncodeParam(img.getRaster(),colorid);
		//com.sun.media.jai.codec.JPEGEncodeParam enParam = new com.sun.media.jai.codec.JPEGEncodeParam();
		//enParam.ge.
		enParam.setQTable(0,jeq0);
		enParam.setQTable(1,jeq1);
		enParam.setQTable(2,jeq2);
		
		JPEGImageEncoder jencoder = JPEGCodec.createJPEGEncoder(outputStream); 
		jencoder.setJPEGEncodeParam(enParam);
		//jencoder.encode(arg0, arg1)
		jencoder.encode(img);
		outputStream.close();
		in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}


