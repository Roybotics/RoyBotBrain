package roybot.camera;

import java.awt.Graphics2D;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public interface CameraInterface extends AutoCloseable
{

	void addListener(CameraListenerInterface pCameraListener);

	void removeListener(CameraListenerInterface pCameraListener);

	boolean open();

	void start() throws V4L4JException;

	void stop();

	long getCount();

	void debugDisplay();

	Graphics2D getDisplayGraphics();

}
