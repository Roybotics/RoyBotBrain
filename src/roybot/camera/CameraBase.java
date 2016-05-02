package roybot.camera;

import java.awt.Graphics2D;
import java.util.ArrayList;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public abstract class CameraBase implements CameraInterface
{
	protected int mWidth;
	protected int mHeight;
	protected int mFPS;
	protected volatile long mCount;
	protected ArrayList<CameraListenerInterface> mCameraListenerList = new ArrayList<>();

	public CameraBase(int pWidth, int pHeight, int pFPS)
	{
		super();
		mWidth = pWidth;
		mHeight = pHeight;
		mFPS = pFPS;
	}

	@Override
	public void addListener(CameraListenerInterface pCameraListener)
	{
		mCameraListenerList.add(pCameraListener);
	}

	@Override
	public void removeListener(CameraListenerInterface pCameraListener)
	{
		mCameraListenerList.remove(pCameraListener);
	}

	@Override
	public long getCount()
	{
		return mCount;
	}

	@Override
	public void debugDisplay()
	{
	}

	@Override
	public void close() throws Exception
	{

	}

	@Override
	public boolean open()
	{
		return true;
	}

	@Override
	public void start() throws V4L4JException
	{
	}

	@Override
	public void stop()
	{
	}

	@Override
	public abstract Graphics2D getDisplayGraphics();

}
