package roybot.examples;

import au.edu.jcu.v4l4j.*;
import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 *
 * @author madushanka
 *
 */
public class TestV4l4j extends WindowAdapter implements CaptureCallback {

	private static int width = 640, height = 480, std = V4L4JConstants.STANDARD_WEBCAM, channel = 0;
	private static String device = "/dev/video0"; // getting device this is the
													// path of device
	private VideoDevice videoDevice;
	private FrameGrabber frameGrabber;
	private JLabel label;
	private JFrame frame;
	private JButton button;
	BufferedImage bf;

	public static void main(String args[]) throws UnsupportedLookAndFeelException {

		// UIManager.setLookAndFeel(new GTKLookAndFeel());
		new TestV4l4j();

	}

	public TestV4l4j() {

		try {
			initFrameGrabber(); // creating frame grabber
		} catch (V4L4JException e1) {
			System.err.println("Error setting up capture");
			e1.printStackTrace();

			cleanupCapture();
			return;
		}

		initGUI(); // creating Jframe

		try {
			frameGrabber.startCapture(); // Starting cam
		} catch (V4L4JException e) {
			System.err.println("Error starting the capture");
			e.printStackTrace();
		}
	}

	private void initFrameGrabber() throws V4L4JException { // Setting
															// Framegrabber
		videoDevice = new VideoDevice(device); // getting the webcam
		frameGrabber = videoDevice.getJPEGFrameGrabber(width, height, channel, std, 80);
		frameGrabber.setCaptureCallback(this);
		width = frameGrabber.getWidth();
		height = frameGrabber.getHeight();

	}

	private void initGUI() { // setting JFrame
		frame = new JFrame();
		label = new JLabel();
		button = new JButton("Capture");
		frame.getContentPane().add(label);
		frame.getContentPane().add(button, BorderLayout.PAGE_END);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(this);
		frame.setTitle("WebCam Example");
		frame.setVisible(true);
		frame.setSize(width, height);
		button.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				System.out.println(bf);
				try {
					ImageIO.write(bf, "png", new File("out.png")); // saving
																	// image
					JOptionPane.showMessageDialog(null, "Saved !!!");
				} catch (IOException ex) {
					Logger.getLogger(TestV4l4j.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		});
	}

	// this method is use for turn off cam and release framegrabber and device
	private void cleanupCapture() {
		try {
			frameGrabber.stopCapture();
		} catch (StateException ex) {
		}

		videoDevice.releaseFrameGrabber();
		videoDevice.release();
	}

	@Override
	public void exceptionReceived(V4L4JException e) {

		e.printStackTrace();
	}

	// this method is call from startCapture() method
	// getting the frame from framegrabber and draw it on JLable to show the
	// user and recycle frame
	// again getting frame , showing it , recycle it
	@Override
	public void nextFrame(VideoFrame frame) {
		setImage(frame.getBufferedImage()); // get the captured frame to
											// Buffered Image

		label.getGraphics().drawImage(frame.getBufferedImage(), 0, 0, width, height, null);

		frame.recycle();

	}

	public void setImage(BufferedImage b) {
		bf = b;
	}

	public BufferedImage getImage() {
		return bf;
	}
}
