package roybot.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.sarxos.webcam.WebcamEvent;

import roybot.camera.CameraInterface;
import roybot.camera.CameraListenerInterface;

public class RoyBotGui extends JFrame	implements
																			CameraListenerInterface
{

	private static final long serialVersionUID = 1L;

	private JLabel mLabel;
	private int mWidth, mHeight;

	public RoyBotGui(int pWidth, int pHeight)
	{
		super("RoyBot GUI");
		mWidth = pWidth;
		mHeight = pHeight;

		Container lPane = getContentPane();
		lPane.setLayout(new BorderLayout());

		mLabel = new JLabel();
		mLabel.setPreferredSize(new Dimension(mWidth, mHeight));
		lPane.add(mLabel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(mWidth, mHeight);

		pack();

		setVisible(true);

	}

	@Override
	public void newFrame(	CameraInterface pCameraInterface,
												Object pWebcameEvent,
												long pIndex,
												long pTime,
												int pWidth,
												int pHeight,
												byte[] pBuffer)
	{
		Graphics2D lGraphics2D = (Graphics2D) mLabel.getGraphics();
		lGraphics2D.drawImage(((WebcamEvent) pWebcameEvent).getImage(),
													0,
													0,
													mWidth,
													mHeight,
													null);

	}

}
