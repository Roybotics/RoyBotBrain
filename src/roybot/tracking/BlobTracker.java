package roybot.tracking;

import java.awt.Color;

import roybot.camera.CameraInterface;
import roybot.camera.CameraListenerInterface;

public class BlobTracker implements CameraListenerInterface
{

	private Color mColor;

	public BlobTracker(Color pColor)
	{
		super();
		mColor = pColor;

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
		ImageProcessing.findBlob(mColor, pWidth, pHeight, pBuffer);

	}
}
