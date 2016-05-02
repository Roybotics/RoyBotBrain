package roybot.tracking;

import java.awt.Color;

public class ImageProcessing
{

	public static void highlightColor(int pWidth,
																		int pHeight,
																		byte[] pBuffer)
	{
		float[] lHSV = new float[3];

		for (int y = 0; y < pHeight; y++)
			for (int x = 0; x < pWidth; x++)
			{
				int lIndex = x + pWidth * y;
				getHSB(pBuffer, lIndex, lHSV);

				if (lHSV[1] < 0.45) // lHSV[1]<0.6 ||
				{
					//System.out.println(lHSV[1]);
					set(pBuffer, lIndex, (byte) 0, (byte) 0, (byte) 0);
				}
			}

	}

	private static void set(byte[] pBuffer,
													int pIndex,
													byte pR,
													byte pG,
													byte pB)
	{
		pBuffer[3 * pIndex + 0] = pR;
		pBuffer[3 * pIndex + 1] = pG;
		pBuffer[3 * pIndex + 2] = pB;
	}

	public static void findBlob(Color pColor,
															int pWidth,
															int pHeight,
															byte[] pBuffer)
	{
		float[] lHSV = new float[3];

		for (int y = 0; y < pHeight; y++)
			for (int x = 0; x < pWidth; x++)
			{
				int lIndex = x + pWidth * y;
				getHSB(pBuffer, lIndex, lHSV);

			}

	}

	public static float[] getHSB(	byte[] pBuffer,
																int pIndex,
																float[] hsbvals)
	{
		int r = pBuffer[3 * pIndex] & 0xFF;
		int g = pBuffer[3 * pIndex + 1] & 0xFF;
		int b = pBuffer[3 * pIndex + 2] & 0xFF;

		convertRGBtoHSB(r, g, b, hsbvals);

		return hsbvals;

	}

	public static float[] convertRGBtoHSB(int r,
																				int g,
																				int b,
																				float[] hsbvals)
	{

		float rf = r / 255f;
		float gf = g / 255f;
		float bf = b / 255f;

		float hue, saturation, value;

		float minRGB = Math.min(rf, Math.min(gf, bf));
		float maxRGB = Math.max(rf, Math.max(gf, bf));

		if (minRGB == maxRGB)
		{
			// Black-gray-white:
			hue = 0f;
			saturation = 0f;
			value = minRGB;
		}
		else
		{
			// Colors other than black-gray-white:
			float d = (r == minRGB) ? g - b : ((b == minRGB) ? r - g
																											: b - r);
			float h = (r == minRGB) ? 3 : ((b == minRGB) ? 1 : 5);

			hue = 60 * (h - d / (maxRGB - minRGB));
			saturation = (maxRGB - minRGB) / maxRGB;
			value = maxRGB;
		}

		hsbvals[0] = hue;
		hsbvals[1] = saturation;
		hsbvals[2] = value;
		return hsbvals;
	}

}
