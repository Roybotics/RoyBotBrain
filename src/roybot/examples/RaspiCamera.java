package roybot.examples;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.ImageFormat;
import au.edu.jcu.v4l4j.RGBFrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class RaspiCamera implements CaptureCallback, AutoCloseable {

	static {

		V4l4jDriver v4l4jDriver = new V4l4jDriver();

		/*
		 * V4l4jDriver v4l4jDriver = new V4l4jDriver();
		 * Webcam.setDriver(v4l4jDriver); // this is important /
		 **/
	}

	private int mWidth;
	private int mHeight;
	private RGBFrameGrabber mRGBFrameGrabber;
	private JFrame mJframe;
	private JLabel mLabel;
	private VideoDevice mVideoDevice;
	private long mCounter = 0;

	public RaspiCamera() {
		super();
		try {
			mVideoDevice = new VideoDevice("/dev/video0");

			List<ImageFormat> fmts = mVideoDevice.getDeviceInfo().getFormatList().getNativeFormats();
			ImageFormat lImageFormat = fmts.get(2);
			System.out.println(lImageFormat);

			int std = V4L4JConstants.STANDARD_WEBCAM;

			mRGBFrameGrabber = mVideoDevice.getRGBFrameGrabber(320, 200, 0, std, lImageFormat);
			mRGBFrameGrabber.setCaptureCallback(this);
			mRGBFrameGrabber.setFrameInterval(1, 60);
			

			mWidth = mRGBFrameGrabber.getWidth();
			mHeight = mRGBFrameGrabber.getHeight();

			System.out.format("width=%d, height=%d \n", mWidth, mHeight);

			mJframe = new JFrame();
			mLabel = new JLabel();
			mJframe.getContentPane().add(mLabel);
			mJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mJframe.setTitle("WebCam Example");
			mJframe.setVisible(true);
			mJframe.setSize(mWidth, mHeight);

		} catch (V4L4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() throws V4L4JException {
		mRGBFrameGrabber.startCapture();
	}

	public void stop() {
		mRGBFrameGrabber.stopCapture();
	}

	@Override
	public void close() throws Exception {
		mVideoDevice.releaseFrameGrabber();
		mVideoDevice.release();
	}

	@Override
	public void nextFrame(VideoFrame frame) {

		mLabel.getGraphics().drawImage(frame.getBufferedImage(), 0, 0,mWidth, mHeight, null);
		// System.out.println(frame);
		// System.out.println(frame.getBytes()[0]);
		frame.recycle();

		mCounter++;

	}

	public long getCount() {
		return mCounter;
	}

	@Override
	public void exceptionReceived(V4L4JException e) {
		System.out.println(e);
		e.printStackTrace();
	}

	public static void main(String[] args) throws InterruptedException {
		/*
		 * JFrame frame = new JFrame("Hello From RasPi Camera Module");
		 * frame.add(new WebcamPanel(Webcam.getDefault())); frame.pack();
		 * frame.setVisible(true);
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /
		 **/

		try {

			RaspiCamera lTestCamera = new RaspiCamera();

			lTestCamera.start();
			Thread.sleep(60000);
			lTestCamera.stop();
			System.out.println(lTestCamera.getCount());

			lTestCamera.close();
			System.exit(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

}
