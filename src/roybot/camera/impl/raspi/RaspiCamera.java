package roybot.camera.impl.raspi;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.ControlList;
import au.edu.jcu.v4l4j.ImageFormat;
import au.edu.jcu.v4l4j.RGBFrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;
import roybot.camera.CameraBase;
import roybot.camera.CameraInterface;
import roybot.camera.CameraListenerInterface;

public class RaspiCamera extends CameraBase	implements
																						CameraInterface
{
	static
	{

		new V4l4jDriver();
		/*
		 * V4l4jDriver v4l4jDriver = new V4l4jDriver();
		 * Webcam.setDriver(v4l4jDriver); // this is important /
		 **/
	}

	private RGBFrameGrabber mRGBFrameGrabber;
	private VideoDevice mVideoDevice;
	private JFrame mJframe;
	private Component mLabel;

	public RaspiCamera(int pWidth, int pHeight, int pFPS)
	{
		super(pWidth, pHeight, pFPS);
	}

	@Override
	public boolean open()
	{
		try
		{
			mVideoDevice = new VideoDevice("/dev/video0");
			ControlList lControlList = mVideoDevice.getControlList();

			List<ImageFormat> fmts = mVideoDevice.getDeviceInfo()
																						.getFormatList()
																						.getNativeFormats();
			ImageFormat lImageFormat = fmts.get(2);
			System.out.println(lImageFormat);

			int std = V4L4JConstants.STANDARD_WEBCAM;

			mRGBFrameGrabber = mVideoDevice.getRGBFrameGrabber(	mWidth,
																													mHeight,
																													0,
																													std,
																													lImageFormat);

			CameraInterface lThis = this;
			
			mRGBFrameGrabber.setCaptureCallback(new CaptureCallback()
			{

				@Override
				public void nextFrame(VideoFrame pFrame)
				{
					long lIndex = pFrame.getSequenceNumber();
					long lTime = pFrame.getCaptureTime();
					byte[] lBuffer = pFrame.getBytes();

					for (CameraListenerInterface lRaspiCameraListener : mCameraListenerList)
					{
						lRaspiCameraListener.newFrame(lThis,
						                              pFrame,
																					lIndex,
																					lTime,
																					mWidth,
																					mHeight,
																					lBuffer);
					}

					pFrame.recycle();
					mCount++;
				}

				@Override
				public void exceptionReceived(V4L4JException pE)
				{
					pE.printStackTrace();
				}
			});
			mRGBFrameGrabber.setFrameInterval(1, mFPS);

			mWidth = mRGBFrameGrabber.getWidth();
			mHeight = mRGBFrameGrabber.getHeight();

			System.out.format("width=%d, height=%d \n", mWidth, mHeight);

			return true;
		}
		catch (V4L4JException e)
		{
			e.printStackTrace();
			return false;
		}

	}

	
	@Override
	public Graphics2D getDisplayGraphics()
	{
		return (Graphics2D) mLabel.getGraphics();
	}
	
	@Override
	public void debugDisplay()
	{
		mJframe = new JFrame();
		mLabel = new JLabel();
		mJframe.getContentPane().add(mLabel);
		mJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mJframe.setTitle("RaspiCam");
		mJframe.setVisible(true);
		mJframe.setSize(mWidth, mHeight);

		addListener((	pCameraInterface,
									pFrame,
									pIndex,
									pTime,
									pWidth,
									pHeight,
									pBuffer) -> {
			pCameraInterface.getDisplayGraphics()
											.drawImage(	((VideoFrame) pFrame).getBufferedImage(),
																	0,
																	0,
																	mWidth,
																	mHeight,
																	null);
		});
	}

	@Override
	public void start() throws V4L4JException
	{
		mRGBFrameGrabber.startCapture();
	}

	@Override
	public void stop()
	{
		mRGBFrameGrabber.stopCapture();
	}

	@Override
	public void close() throws Exception
	{
		mVideoDevice.releaseFrameGrabber();
		mVideoDevice.release();
	}



}
