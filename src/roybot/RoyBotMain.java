package roybot;

import roybot.camera.impl.webcam.WebCamera;

public class RoyBotMain {

	public static void main(String[] args) 
	{
		try
		{
			RoyBot lRoyBot = new RoyBot();
			
			WebCamera lWebCamera = new WebCamera(320, 240, 60);
			lRoyBot.setCamera(lWebCamera);
			
			
			lRoyBot.run();
			
			
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

}
