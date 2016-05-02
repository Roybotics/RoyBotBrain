package roybot.camera.impl.raspi.demo;

import roybot.camera.impl.raspi.RaspiCamera;

public class RaspiCameraDemo
{

	public static void main(String[] args) throws InterruptedException
	{
		try
		{

			RaspiCamera lRaspiCamera = new RaspiCamera(320, 200, 60);

			lRaspiCamera.open();

			lRaspiCamera.start();
			Thread.sleep(60000);
			lRaspiCamera.stop();
			System.out.println(lRaspiCamera.getCount());

			lRaspiCamera.close();
			System.exit(0);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

	}

}
