package roybot.camera.impl.webcam;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;
import roybot.camera.CameraBase;
import roybot.camera.CameraInterface;
import roybot.camera.CameraListenerInterface;
import roybot.tracking.ImageProcessing;

public class WebCamera extends CameraBase implements CameraInterface
{

	private Webcam mWebcam;
	private JFrame mJframe;
	private JLabel mLabel;
	private volatile boolean mStop = false;

	public WebCamera(int pWidth, int pHeight, int pFPS)
	{
		super(pWidth, pHeight, pFPS);

		String lProperty = System.getProperty("os.name");
		if (!lProperty.toLowerCase().contains("mac"))
		{
			V4l4jDriver lV4l4jDriver = new V4l4jDriver();
			Webcam.setDriver(lV4l4jDriver);
		}
	}

	@Override
	public boolean open()
	{
		mWebcam = Webcam.getDefault();

		Dimension lWebcamResolution = new Dimension(mWidth, mHeight);
		mWebcam.setViewSize(lWebcamResolution);

		CameraInterface lThis = this;

		mWebcam.addWebcamListener(new WebcamListener()
		{

			@Override
			public void webcamOpen(WebcamEvent pWe)
			{
			}

			@Override
			public void webcamImageObtained(WebcamEvent pWe)
			{
				try
				{
					BufferedImage lBufferedImage = pWe.getImage();

					long lIndex = mCount;
					long lTime = System.currentTimeMillis();
					byte[] lBuffer = ((DataBufferByte) lBufferedImage.getRaster()
																														.getDataBuffer()).getData();

					for (CameraListenerInterface lRaspiCameraListener : mCameraListenerList)
					{
						lRaspiCameraListener.newFrame(lThis,
																					pWe,
																					lIndex,
																					lTime,
																					mWidth,
																					mHeight,
																					lBuffer);
					}

					mCount++;
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}

			}

			@Override
			public void webcamDisposed(WebcamEvent pWe)
			{
			}

			@Override
			public void webcamClosed(WebcamEvent pWe)
			{
			}
		});

		mWebcam.open();

		return true;

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
		Container lPane = mJframe.getContentPane();
		lPane.setLayout(new BorderLayout());

		mLabel = new JLabel();
		mLabel.setPreferredSize(new Dimension(2*mWidth, 2*mHeight));
		lPane.add(mLabel, BorderLayout.CENTER);
		mJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mJframe.setTitle("RaspiCam");
		mJframe.setSize(2*mWidth, 2*mHeight);
		
		mJframe.pack();
		
		mJframe.setVisible(true);

		addListener((	pCameraInterface,
									pWebcameEvent,
									pIndex,
									pTime,
									pWidth,
									pHeight,
									pBuffer) -> {

			pCameraInterface.getDisplayGraphics()
											.drawImage(	((WebcamEvent) pWebcameEvent).getImage(),
																	0,
																	0,
																	2*mWidth,
																	2*mHeight,
																	null);
		});
	}

	@Override
	public void start() throws V4L4JException
	{
		mStop = false;
		Runnable lRunnable = () -> {
			while (!mStop)
			{
				// System.out.println("getimage!");
				mWebcam.getImage();
			}
		};
		Thread lThread = new Thread(lRunnable, "WebCamImageFetcher");
		lThread.start();
	}

	@Override
	public void stop()
	{
		mStop = true;
	}

	@Override
	public void close() throws Exception
	{
		mWebcam.close();
	}

}
