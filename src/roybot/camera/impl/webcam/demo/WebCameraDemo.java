package roybot.camera.impl.webcam.demo;

import roybot.camera.CameraInterface;
import roybot.camera.impl.webcam.WebCamera;
import roybot.tracking.BlobTracker;
import roybot.tracking.ImageProcessing;

public class WebCameraDemo
{

	public static void main(String[] args) throws InterruptedException
	{
		try
		{

			WebCamera lWebCamera = new WebCamera(320, 240, 60);

			lWebCamera.open();

			BlobTracker lBlobTracker = new BlobTracker();
			lWebCamera.addListener(lBlobTracker);

			lWebCamera.addListener((CameraInterface pCameraInterface,
															Object pObject,
															long pIndex,
															long pTime,
															int pWidth,
															int pHeight,
															byte[] pBuffer) -> {

				//ImageProcessing.highlightColor(pWidth, pHeight, pBuffer, 114);

			});
			lWebCamera.debugDisplay();

			lWebCamera.start();
			Thread.sleep(160000);
			lWebCamera.stop();
			System.out.println(lWebCamera.getCount());

			lWebCamera.close();
			System.exit(0);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

	}

}
