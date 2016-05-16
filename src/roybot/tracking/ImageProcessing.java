package roybot.tracking;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;

public class ImageProcessing
{

	public static void highlightAnyColor(	final int pWidth,
																				final int pHeight,
																				final byte[] pBuffer,
																				final int pSaturationThreshold,
																				final int pValueThreshold)
	{
		final int lLength = pWidth * pHeight;
		for (int i = 0; i < lLength; i++)
			if (!detectSV(pBuffer, i, pSaturationThreshold, pValueThreshold))
			{
				set(pBuffer, i, (byte) 0, (byte) 0, (byte) 0);
			}

	}

	public static void highlightParticularColor(final int pWidth,
																							final int pHeight,
																							final byte[] pBuffer,
																							final int pHue,
																							final int pMaxHueDistance,
																							final int pSaturationThreshold,
																							final int pValueThreshold)
	{
		final int lLength = pWidth * pHeight;
		for (int i = 0; i < lLength; i++)
			if (!detectHSV(	pBuffer,
											i,
											pHue,
											pMaxHueDistance,
											pSaturationThreshold,
											pValueThreshold))
			{
				div(pBuffer, i, 1);
			}

	}

	public static int findBlob(	final int pWidth,
															final int pHeight,
															final byte[] pBuffer,
															final boolean[] pMask,
															final int pStart,
															final int pHue,
															final int pHueMaxDistance,
															final int pSaturationThreshold,
															final int pValueThreshold,
															final int pMinRadius,
															final int pMaxRadius,
															final float[] pBlobInfo)
	{
		final int lLength = pWidth * pHeight;
		for (int i = pStart; i < lLength; i += 5)
		{
			if (!pMask[i] && detectHSV(	pBuffer,
																	i,
																	pHue,
																	pHueMaxDistance,
																	pSaturationThreshold,
																	pValueThreshold))
			{
				// setPoint(pWidth, pHeight, pBuffer, i, 255, 0, 0);

				int lNewIndex = recenter(	pWidth,
																	pHeight,
																	pBuffer,
																	pMask,
																	pHue,
																	pHueMaxDistance,
																	pSaturationThreshold,
																	pValueThreshold,
																	pMaxRadius,
																	i,
																	pBlobInfo);

				// setPoint(pWidth, pHeight, pBuffer, lNewIndex, 0, 255, 0);

				lNewIndex = recenter(	pWidth,
															pHeight,
															pBuffer,
															pMask,
															pHue,
															pHueMaxDistance,
															pSaturationThreshold,
															pValueThreshold,
															pMaxRadius,
															lNewIndex,
															pBlobInfo);

				if (Blob.getRadius(pBlobInfo) >= pMinRadius)
					markBlob(pWidth, pHeight, pBlobInfo, pMask);

				// setPoint(pWidth, pHeight, pBuffer, lNewIndex, 0, 0, 255);

				return (i += 5 + pBlobInfo[4]);

			}
		}
		return -1;
	}

	private static void markBlob(	int pWidth,
																int pHeight,
																float[] pBlobInfo,
																boolean[] pMask)
	{
		int gxi = (int) pBlobInfo[0];
		int gyi = (int) pBlobInfo[1];
		int s = (int) (pBlobInfo[2] * 1.2);
		int n = (int) (pBlobInfo[3] * 1.2);
		int e = (int) (pBlobInfo[4] * 1.2);
		int w = (int) (pBlobInfo[5] * 1.2);

		for (int y = max(gyi - n, 0); y < min(gyi + s, pHeight); y++)
			for (int x = max(gxi - w, 0); x < min(gxi + e, pWidth); x++)
			{
				int lIndex = pWidth * y + x;
				pMask[lIndex] = true;
			}
	}

	public static void applyMask(	int pWidth,
																int pHeight,
																byte[] pBuffer,
																boolean[] pMask,
																int pDiv)
	{
		final int lLength = pMask.length;
		for (int i = 0; i < lLength; i++)
			if (!pMask[i])
				div(pBuffer, i, 1);
	}

	private static int recenter(int pWidth,
															int pHeight,
															byte[] pBuffer,
															boolean[] pMask,
															final int pHue,
															final int pHueMaxDistance,
															final int pSaturationThreshold,
															final int pValueThreshold,
															final int pMaxRadius,
															int i,
															float[] pBlobInfo)
	{
		int sx = i % pWidth;
		int sy = i / pWidth;

		int lEast = distance(	pWidth,
													pHeight,
													pBuffer,
													pMask,
													sx,
													sy,
													pHue,
													pHueMaxDistance,
													pSaturationThreshold,
													pValueThreshold,
													pMaxRadius,
													1,
													0);

		int lSouth = distance(pWidth,
													pHeight,
													pBuffer,
													pMask,
													sx,
													sy,
													pHue,
													pHueMaxDistance,
													pSaturationThreshold,
													pValueThreshold,
													pMaxRadius,
													0,
													1);

		int lWest = distance(	pWidth,
													pHeight,
													pBuffer,
													pMask,
													sx,
													sy,
													pHue,
													pHueMaxDistance,
													pSaturationThreshold,
													pValueThreshold,
													pMaxRadius,
													-1,
													0);

		int lNorth = distance(pWidth,
													pHeight,
													pBuffer,
													pMask,
													sx,
													sy,
													pHue,
													pHueMaxDistance,
													pSaturationThreshold,
													pValueThreshold,
													pMaxRadius,
													0,
													-1);

		int lSouthWest = distance(pWidth,
															pHeight,
															pBuffer,
															pMask,
															sx,
															sy,
															pHue,
															pHueMaxDistance,
															pSaturationThreshold,
															pValueThreshold,
															pMaxRadius,
															-1,
															1);

		int lSouthEast = distance(pWidth,
															pHeight,
															pBuffer,
															pMask,
															sx,
															sy,
															pHue,
															pHueMaxDistance,
															pSaturationThreshold,
															pValueThreshold,
															pMaxRadius,
															1,
															1);

		int lNorthWest = distance(pWidth,
															pHeight,
															pBuffer,
															pMask,
															sx,
															sy,
															pHue,
															pHueMaxDistance,
															pSaturationThreshold,
															pValueThreshold,
															pMaxRadius,
															-1,
															-1);

		int lNorthEast = distance(pWidth,
															pHeight,
															pBuffer,
															pMask,
															sx,
															sy,
															pHue,
															pHueMaxDistance,
															pSaturationThreshold,
															pValueThreshold,
															pMaxRadius,
															1,
															-1);
		/**/

		float gx = sx + 0.5f * (lEast - lWest);
		float gy = sy + 0.5f * (lSouth - lNorth);

		int gxi = Math.round(gx);
		int gyi = Math.round(gy);

		pBlobInfo[0] = gx;
		pBlobInfo[1] = gy;
		pBlobInfo[2] = lSouth;
		pBlobInfo[3] = lNorth;
		pBlobInfo[4] = lEast;
		pBlobInfo[5] = lWest;

		pBlobInfo[6] = lSouthWest;
		pBlobInfo[7] = lSouthEast;
		pBlobInfo[8] = lNorthWest;
		pBlobInfo[9] = lNorthEast;

		/*setPoint(	pWidth,
							pHeight,
							pBuffer,
							gxi + lEast + pWidth * gyi,
							0,
							255,
							255);

		setPoint(	pWidth,
							pHeight,
							pBuffer,
							gxi - lWest + pWidth * gyi,
							0,
							255,
							255);

		setPoint(	pWidth,
							pHeight,
							pBuffer,
							gxi + pWidth * (gyi + lSouth),
							0,
							255,
							255);

		setPoint(pWidth, pHeight, pBuffer, gxi - lWest
																				+ pWidth
																				* (gyi - lNorth), 0, 255, 255);/**/

		// System.out.format("(gx,gy)=(%d,%d)\n",gxi,gyi);

		int lNewIndex = gxi + pWidth * gyi;
		return lNewIndex;
	}

	private static int distance(final int pWidth,
															final int pHeight,
															final byte[] pBuffer,
															final boolean[] pMask,
															final int sx,
															final int sy,
															final int pHue,
															final int pHueMaxDistance,
															final int pSaturationThreshold,
															final int pValueThreshold,
															final int pMaxRadius,
															final int dx,
															final int dy)
	{
		int lIndex = sx + pWidth * sy;
		int i = 0;
		for (int x = sx, y = sy; !pMask[lIndex] && detectHSV(	pBuffer,
																													lIndex,
																													pHue,
																													pHueMaxDistance,
																													pSaturationThreshold,
																													pValueThreshold)
															&& i < pMaxRadius; i++)
		{

			x += dx;
			y += dy;
			lIndex = x + pWidth * y;

			if (x < 0 || x >= pWidth || y < 0 || y >= pHeight)
				break;

		}
		return i;
	}

	private static void setPoint(	int pWidth,
																int pHeight,
																byte[] pBuffer,
																int pIndex,
																int pR,
																int pG,
																int pB)
	{
		pBuffer[3 * pIndex + 0] = (byte) pR;
		pBuffer[3 * pIndex + 1] = (byte) pG;
		pBuffer[3 * pIndex + 2] = (byte) pB;

		pBuffer[3 * (pIndex - 1) + 0] = (byte) pR;
		pBuffer[3 * (pIndex - 1) + 1] = (byte) pG;
		pBuffer[3 * (pIndex - 1) + 2] = (byte) pB;

		pBuffer[3 * (pIndex + 1) + 0] = (byte) pR;
		pBuffer[3 * (pIndex + 1) + 1] = (byte) pG;
		pBuffer[3 * (pIndex + 1) + 2] = (byte) pB;

		pBuffer[3 * (pIndex - pWidth) + 0] = (byte) pR;
		pBuffer[3 * (pIndex - pWidth) + 1] = (byte) pG;
		pBuffer[3 * (pIndex - pWidth) + 2] = (byte) pB;

		pBuffer[3 * (pIndex + pWidth) + 0] = (byte) pR;
		pBuffer[3 * (pIndex + pWidth) + 1] = (byte) pG;
		pBuffer[3 * (pIndex + pWidth) + 2] = (byte) pB;

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

	private static void div(byte[] pBuffer, int pIndex, int pDiv)
	{
		pBuffer[3 * pIndex + 0] = (byte) ((pBuffer[3 * pIndex + 0] & 0xFF) >> pDiv);
		pBuffer[3 * pIndex + 1] = (byte) ((pBuffer[3 * pIndex + 1] & 0xFF) >> pDiv);
		pBuffer[3 * pIndex + 2] = (byte) ((pBuffer[3 * pIndex + 2] & 0xFF) >> pDiv);
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

	public static boolean detectHSV(byte[] pBuffer,
																	int pIndex,
																	int pHue,
																	int pHueMaxDistance,
																	int pSaturationThreshold,
																	int pValueThreshold)
	{

		final int r = pBuffer[3 * pIndex] & 0xFF;
		final int g = pBuffer[3 * pIndex + 1] & 0xFF;
		final int b = pBuffer[3 * pIndex + 2] & 0xFF;

		final int minRGB = Math.min(r, Math.min(g, b));
		final int maxRGB = Math.max(r, Math.max(g, b));

		if (minRGB == maxRGB)
			return false;
		else
		{
			final int v = maxRGB;
			if (v > pValueThreshold)
			{
				final int s = (255 * (maxRGB - minRGB)) / maxRGB;
				if (s > pSaturationThreshold)
				{
					final int d = (r == minRGB)	? g - b
																			: ((b == minRGB) ? r - g
																											: b - r);
					final int p = (r == minRGB)	? 60 * 3
																			: ((b == minRGB) ? 60 * 1
																											: 60 * 5);

					final int h = (p - (60 * d) / (maxRGB - minRGB));

					final int distance = (h < pHue)	? min(abs(360 + h - pHue),
																								abs(h - pHue))
																					: min(abs(h - (360 + pHue)),
																								abs(h - pHue));

					if (distance < pHueMaxDistance)
						return true;
				}
			}
		}
		return false;

	}

	public static boolean detectSV(	byte[] pBuffer,
																	int pIndex,
																	int pSaturationThreshold,
																	int pValueThreshold)
	{

		final int r = pBuffer[3 * pIndex] & 0xFF;
		final int g = pBuffer[3 * pIndex + 1] & 0xFF;
		final int b = pBuffer[3 * pIndex + 2] & 0xFF;

		final int minRGB = Math.min(r, Math.min(g, b));
		final int maxRGB = Math.max(r, Math.max(g, b));

		if (minRGB == maxRGB)
			return false;
		else
		{
			int s = (255 * (maxRGB - minRGB)) / maxRGB;
			int v = maxRGB;
			return s > pSaturationThreshold && v > pValueThreshold;
		}

	}

	public static int getS(byte[] pBuffer, int pIndex)
	{

		final int r = pBuffer[3 * pIndex] & 0xFF;
		final int g = pBuffer[3 * pIndex + 1] & 0xFF;
		final int b = pBuffer[3 * pIndex + 2] & 0xFF;

		final int minRGB = Math.min(r, Math.min(g, b));
		final int maxRGB = Math.max(r, Math.max(g, b));

		if (minRGB == maxRGB)
			return 0;
		else
			return (255 * (maxRGB - minRGB)) / maxRGB;
	}

	public static int getV(byte[] pBuffer, int pIndex)
	{

		final int r = pBuffer[3 * pIndex] & 0xFF;
		final int g = pBuffer[3 * pIndex + 1] & 0xFF;
		final int b = pBuffer[3 * pIndex + 2] & 0xFF;

		final int maxRGB = Math.max(r, Math.max(g, b));

		return maxRGB;
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

	/*
	 * initialize a smaller piece of the array and use the System.arraycopy 
	 * call to fill in the rest of the array in an expanding binary fashion
	 */
	public static void fill(byte[] array, byte value)
	{
		int len = array.length;

		if (len > 0)
		{
			array[0] = value;
		}

		for (int i = 1; i < len; i += i)
		{
			System.arraycopy(	array,
												0,
												array,
												i,
												((len - i) < i) ? (len - i) : i);
		}
	}

	/*
	 * initialize a smaller piece of the array and use the System.arraycopy 
	 * call to fill in the rest of the array in an expanding binary fashion
	 */
	public static void fill(boolean[] array, boolean value)
	{
		int len = array.length;

		if (len > 0)
		{
			array[0] = value;
		}

		for (int i = 1; i < len; i += i)
		{
			System.arraycopy(	array,
												0,
												array,
												i,
												((len - i) < i) ? (len - i) : i);
		}
	}
}
