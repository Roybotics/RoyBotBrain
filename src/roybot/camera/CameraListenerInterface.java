package roybot.camera;

public interface CameraListenerInterface
{

	void newFrame(CameraInterface pCameraInterface,
								Object pObject,
								long pIndex,
								long pTime,
								int pWidth,
								int pHeight,
								byte[] pBuffer);

}
