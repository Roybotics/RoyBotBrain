package roybot.tracking;

import java.util.ArrayList;

import roybot.camera.CameraInterface;
import roybot.camera.CameraListenerInterface;

public class BlobTracker implements CameraListenerInterface
{

	boolean[] mMask;
	
	private int mHue = 0;
	private int mHueMaxDistance = 20;
	private int mSaturationThreshold = 180;
	private int mValueThreshold = 60;
	
	private int mMinRadius = 2;
	private int mMaxRadius = 400;


	public BlobTracker()
	{
		super();
	}

	@Override
	public void newFrame(	CameraInterface pCameraInterface,
												Object pObject,
												long pIndex,
												long pTime,
												int pWidth,
												int pHeight,
												byte[] pBuffer)
	{

		/*
		int pWidth,
		int pHeight,
		byte[] pBuffer,
		int pStart,
		boolean[] pMask,
		final int pSaturationThreshold,
		final int pMaxRadius,
		final int[] pBlobInfo
		/**/

		if (mMask == null || mMask.length != pWidth * pHeight)
		{
			mMask = new boolean[pWidth * pHeight];
		}

		ImageProcessing.fill(mMask, false);

		float[] lBlobInfo = new float[2 + 4 +4];

		ArrayList<Blob> lBlobList = new ArrayList<>();

		
		int lIndex = 0;
		while ((lIndex = ImageProcessing.findBlob(pWidth,
																							pHeight,
																							pBuffer,
																							mMask,
																							lIndex,
																							mHue,
																							mHueMaxDistance,
																							mSaturationThreshold,
																							mValueThreshold,
																							mMinRadius,
																							mMaxRadius,
																							lBlobInfo)) > 0)
		{
			Blob lBlob = new Blob(lBlobInfo);
			if(lBlob.getRadius()>=mMinRadius)
				lBlobList.add(lBlob);
		}
		/**/

		ImageProcessing.highlightParticularColor(	pWidth,
																							pHeight,
																							pBuffer,
																							mHue,
																							mHueMaxDistance,
																							mSaturationThreshold,
																							mValueThreshold);/**/

		ImageProcessing.applyMask(pWidth, pHeight, pBuffer, mMask, 4);/**/

		// System.out.println(lBlobList);

		/**/

	}
}
