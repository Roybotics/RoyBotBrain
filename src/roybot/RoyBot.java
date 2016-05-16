package roybot;

import java.io.IOException;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;
import roybot.arduino.SerialCom;
import roybot.camera.CameraInterface;
import roybot.gui.RoyBotGui;
import roybot.server.TelnetServer;
import roybot.tracking.BlobTracker;

public class RoyBot
{

	private static final int cTelnetPort = 1234;

	private TelnetServer mTelnetServer;
	private RoyBotGui mRoyBotGui;
	private CameraInterface mCamera;
	private SerialCom mSerialCom;
	
	private volatile boolean mRunning =true;

	public RoyBot() throws IOException
	{
		super();
		
		mTelnetServer = new TelnetServer(cTelnetPort);
		addTelnetListener();
		
		mRoyBotGui = new RoyBotGui(640,480);
		
		mSerialCom = new SerialCom();
		
	}

	public void setCamera(CameraInterface pCamera)
	{
		mCamera = pCamera;
		
		BlobTracker lBlobTracker = new BlobTracker();
		mCamera.addListener(lBlobTracker);
		mCamera.addListener(mRoyBotGui); 
	}

	public void run()
	{

		try
		{
			start();
			
			while(mRunning)
				Thread.sleep(10);
			
			stop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void start() throws IOException, V4L4JException
	{
		try
		{
			mTelnetServer.start();
			mCamera.open();
			//mCamera.debugDisplay();
			mCamera.start();
			mSerialCom.start();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	private void stop() 
	{
		try
		{
			mSerialCom.stop();
			mCamera.stop();
			mCamera.close();
			mTelnetServer.stop();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}



	private void addTelnetListener()
	{
		mTelnetServer.addListener((s, command) -> {
			
			String lCommand = command.toLowerCase().trim();
			
			if(lCommand.equals("stop"))
			{
				stop();
				System.exit(0);
			}
			
			return "OK";
		});

	}

}
