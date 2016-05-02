package roybot.camera.impl.webcam.demo;

import roybot.camera.impl.webcam.WebCamera;

public class WebCameraDemo
{

	public static void main(String[] args) throws InterruptedException
	{
		try
		{

			WebCamera lWebCamera = new WebCamera(320, 240, 60);

			lWebCamera.open();
			lWebCamera.debugDisplay();

			lWebCamera.start();
			Thread.sleep(60000);
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
