package roybot.examples;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;

public class WebCamExample
{

	public static void main(String[] args) throws InterruptedException
	{
		
		
		V4l4jDriver lV4l4jDriver = new V4l4jDriver();
		
		Webcam.setDriver(lV4l4jDriver);
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		
		webcam.addWebcamListener(new WebcamListener()
		{
			
			@Override
			public void webcamOpen(WebcamEvent pWe)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void webcamImageObtained(WebcamEvent pWe)
			{
				System.out.println("image");
			}
			
			@Override
			public void webcamDisposed(WebcamEvent pWe)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void webcamClosed(WebcamEvent pWe)
			{
				// TODO Auto-generated method stub
				
			}
		});

		
		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);

		JFrame window = new JFrame("Test webcam panel");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
}