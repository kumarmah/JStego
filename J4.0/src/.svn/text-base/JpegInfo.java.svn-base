import java.awt.AWTException;
import java.awt.Image;
import java.awt.image.PixelGrabber;


public class JpegInfo

{

	String Comment;

	public Image imageobj;

	public int imageHeight;

	public int imageWidth;

	public int BlockWidth[];

	public int BlockHeight[];

	// the following are set as the default

	public int Precision = 8;

	public int NumberOfComponents = 0;

	public Object Components[];

	public int[] CompID = { 1, 2, 3 };

	public int[] HsampFactor = { 1, 1, 1 };

	public int[] VsampFactor = {1, 1, 1 };

	public int[] QtableNumber = { 0, 1, 1 };

	public int[] DCtableNumber = { 0, 1, 1 };

	public int[] ACtableNumber = { 0, 1, 1 };

	public boolean[] lastColumnIsDummy = { false, false, false };

	public boolean[] lastRowIsDummy = { false, false, false };

	public int Ss = 0;

	public int Se = 63;

	public int Ah = 0;

	public int Al = 0;

	public int compWidth[], compHeight[];

	public int MaxHsampFactor;

	public int MaxVsampFactor;

	public JpegInfo(Image image, int componentCount)

	{
                NumberOfComponents=componentCount;
		Components = new Object[NumberOfComponents];

		compWidth = new int[NumberOfComponents];

		compHeight = new int[NumberOfComponents];

		BlockWidth = new int[NumberOfComponents];

		BlockHeight = new int[NumberOfComponents];

		imageobj = image;


		imageWidth = image.getWidth(null);

		imageHeight = image.getHeight(null);
		
//image.getProperty(Comment, null)
		Comment = "  ";

		//getYCCArray();

	}

	public void setComment(String comment) {

		Comment.concat(comment);

	}

	public String getComment() {

		return Comment;

	}

	/*

	 * This method creates and fills three arrays, Y, Cb, and Cr using the

	 * input image.

	 */

	public void getYCCArray()

	{

		int values[] = new int[imageWidth * imageHeight];

		int r, g, b, y, x;

		// In order to minimize the chance that grabPixels will throw an exception

		// it may be necessary to grab some pixels every few scanlines and process

		// those before going for more.  The time expense may be prohibitive.

		// However, for a situation where memory overhead is a concern, this may be

		// the only choice.

		PixelGrabber grabber = new PixelGrabber(imageobj, 0, 0,
				imageWidth, imageHeight, values, 0, imageWidth);

		MaxHsampFactor = 1;

		MaxVsampFactor = 1;

		for (y = 0; y < NumberOfComponents; y++) {

			MaxHsampFactor = Math.max(MaxHsampFactor, HsampFactor[y]);

			MaxVsampFactor = Math.max(MaxVsampFactor, VsampFactor[y]);

		}

		for (y = 0; y < NumberOfComponents; y++) {

			compWidth[y] = (((imageWidth % 8 != 0) ? ((int) Math
					.ceil((double) imageWidth / 8.0)) * 8 : imageWidth) / MaxHsampFactor)
					* HsampFactor[y];

			if (compWidth[y] != ((imageWidth / MaxHsampFactor) * HsampFactor[y])) {

				lastColumnIsDummy[y] = true;

			}

			// results in a multiple of 8 for compWidth

			// this will make the rest of the program fail for the unlikely

			// event that someone tries to compress an 16 x 16 pixel image

			// which would of course be worse than pointless

			BlockWidth[y] = (int) Math.ceil((double) compWidth[y] / 8.0);

			compHeight[y] = (((imageHeight % 8 != 0) ? ((int) Math
					.ceil((double) imageHeight / 8.0)) * 8 : imageHeight) / MaxVsampFactor)
					* VsampFactor[y];

			if (compHeight[y] != ((imageHeight / MaxVsampFactor) * VsampFactor[y])) {

				lastRowIsDummy[y] = true;

			}

			BlockHeight[y] = (int) Math.ceil((double) compHeight[y] / 8.0);

		}

		try

		{

			if(grabber.grabPixels() != true)

			{
 
				try

				{

					throw new AWTException("Grabber returned false: "
							+ grabber.status());

				}

				catch (Exception e) {
				e.printStackTrace();
				}
				;

			}

		}

		catch (InterruptedException e) {
			e.printStackTrace();

		}
		;

		float Y[][] = new float[compHeight[0]][compWidth[0]];

		float Cr1[][] = new float[compHeight[0]][compWidth[0]];

		float Cb1[][] = new float[compHeight[0]][compWidth[0]];

	//	float Cb2[][] = new float[compHeight[1]][compWidth[1]];

//		float Cr2[][] = new float[compHeight[2]][compWidth[2]];

		int index = 0;

		for (y = 0; y < imageHeight; ++y)

		{

			for (x = 0; x < imageWidth; ++x)

			{

				r = ((values[index] >> 16) & 0xff);

				g = ((values[index] >> 8) & 0xff);

				b = (values[index] & 0xff);

				// The following three lines are a more correct color conversion but

				// the current conversion technique is sufficient and results in a higher

				// compression rate.

				//                Y[y][x] = 16 + (float)(0.8588*(0.299 * (float)r + 0.587 * (float)g + 0.114 * (float)b ));

				//                Cb1[y][x] = 128 + (float)(0.8784*(-0.16874 * (float)r - 0.33126 * (float)g + 0.5 * (float)b));

				//                Cr1[y][x] = 128 + (float)(0.8784*(0.5 * (float)r - 0.41869 * (float)g - 0.08131 * (float)b));

				Y[y][x] = (float) ((0.299 * (float) r + 0.587 * (float) g + 0.114 * (float) b));

				Cb1[y][x] = 128 + (float) ((-0.16874 * (float) r - 0.33126
						* (float) g + 0.5 * (float) b));

				Cr1[y][x] = 128 + (float) ((0.5 * (float) r - 0.41869
						* (float) g - 0.08131 * (float) b));

				index++;

			}

		}

		// Need a way to set the H and V sample factors before allowing downsampling.

		// For now (04/04/98) downsampling must be hard coded.

		// Until a better downsampler is implemented, this will not be done.

		// Downsampling is currently supported.  The downsampling method here

		// is a simple box filter.

		Components[0] = Y;

		//        Cb2 = DownSample(Cb1, 1);

		Components[1] = Cb1;

		//        Cr2 = DownSample(Cr1, 2);

		Components[2] = Cr1;

	}

        	public void getYArray()

	{

		int values[] = new int[imageWidth * imageHeight];

		int r, y, x;

		// In order to minimize the chance that grabPixels will throw an exception

		// it may be necessary to grab some pixels every few scanlines and process

		// those before going for more.  The time expense may be prohibitive.

		// However, for a situation where memory overhead is a concern, this may be

		// the only choice.

		PixelGrabber grabber = new PixelGrabber(imageobj.getSource(), 0, 0,
				imageWidth, imageHeight, values, 0, imageWidth);

		MaxHsampFactor = 1;

		MaxVsampFactor = 1;

		for (y = 0; y < NumberOfComponents; y++) {

			MaxHsampFactor = Math.max(MaxHsampFactor, HsampFactor[y]);

			MaxVsampFactor = Math.max(MaxVsampFactor, VsampFactor[y]);

		}

		for (y = 0; y < NumberOfComponents; y++) {

			compWidth[y] = (((imageWidth % 8 != 0) ? ((int) Math
					.ceil((double) imageWidth / 8.0)) * 8 : imageWidth) / MaxHsampFactor)
					* HsampFactor[y];

			if (compWidth[y] != ((imageWidth / MaxHsampFactor) * HsampFactor[y])) {

				lastColumnIsDummy[y] = true;

			}

			// results in a multiple of 8 for compWidth

			// this will make the rest of the program fail for the unlikely

			// event that someone tries to compress an 16 x 16 pixel image

			// which would of course be worse than pointless

			BlockWidth[y] = (int) Math.ceil((double) compWidth[y] / 8.0);

			compHeight[y] = (((imageHeight % 8 != 0) ? ((int) Math
					.ceil((double) imageHeight / 8.0)) * 8 : imageHeight) / MaxVsampFactor)
					* VsampFactor[y];

			if (compHeight[y] != ((imageHeight / MaxVsampFactor) * VsampFactor[y])) {

				lastRowIsDummy[y] = true;

			}

			BlockHeight[y] = (int) Math.ceil((double) compHeight[y] / 8.0);

		}

		try

		{

			if (grabber.grabPixels() != true)

			{

				try

				{

					throw new AWTException("Grabber returned false: "
							+ grabber.status());

				}

				catch (Exception e) {
				}
				;

			}

		}

		catch (InterruptedException e) {
		}
		

		float Y[][] = new float[compHeight[0]][compWidth[0]];


		int index = 0;

		for (y = 0; y < imageHeight; ++y)

		{

			for (x = 0; x < imageWidth; ++x)

			{

				r = ((values[index] >> 16) & 0xff);


				Y[y][x] = r;


				index++;

			}

		}

		// Need a way to set the H and V sample factors before allowing downsampling.

		// For now (04/04/98) downsampling must be hard coded.

		// Until a better downsampler is implemented, this will not be done.

		// Downsampling is currently supported.  The downsampling method here

		// is a simple box filter.

		Components[0] = Y;

		//        Cb2 = DownSample(Cb1, 1);


	}

        
        
        
	float[][] DownSample(float[][] C, int comp)

	{

		int inrow, incol;

		int outrow, outcol;

		float output[][];

		int temp;

		int bias;

		inrow = 0;

		incol = 0;

		output = new float[compHeight[comp]][compWidth[comp]];

		for (outrow = 0; outrow < compHeight[comp]; outrow++) {

			bias = 1;

			for (outcol = 0; outcol < compWidth[comp]; outcol++) {

				output[outrow][outcol] = (C[inrow][incol++]
						+ C[inrow++][incol--] + C[inrow][incol++]
						+ C[inrow--][incol++] + (float) bias)
						/ (float) 4.0;

				bias ^= 3;

			}

			inrow += 2;

			incol = 0;

		}

		return output;

	}

}
