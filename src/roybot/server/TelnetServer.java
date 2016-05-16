package roybot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TelnetServer
{

	private volatile boolean mRunning = true;
	private ServerSocket mServerSocket;

	private ArrayList<TelnetServerListener> mServerListenerList = new ArrayList<>();

	public TelnetServer(int pPortNumber) throws IOException
	{
		super();

		System.out.println("Creating server socket on port " + pPortNumber);
		mServerSocket = new ServerSocket(pPortNumber);
	}

	public void addListener(TelnetServerListener pTelnetServerListener)
	{
		mServerListenerList.add(pTelnetServerListener);
	}

	public void start() throws IOException
	{
		Runnable lRunnable = () -> {

			while (mRunning)
			{
				if (mServerSocket != null && !mServerSocket.isClosed())
					try
					{
						Socket socket = mServerSocket.accept();
						OutputStream os = socket.getOutputStream();
						PrintWriter pw = new PrintWriter(os, true);
						pw.println("RoyBot Telnet Server.");

						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

						String lCommand;
						while (!(lCommand = br.readLine()).toLowerCase()
																							.trim()
																							.equals("exit"))
						{
							pw.println("Received command: '" + lCommand + "'");
							for (TelnetServerListener lTelnetServerListener : mServerListenerList)
								pw.println(lTelnetServerListener.receivedCommand(	this,
																																	lCommand));
						}

						pw.close();
						socket.close();

						System.out.println("Telnet server closed.");
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
			}
		};

		Thread lTelnetServerDeamonThread = new Thread(lRunnable,
																									"TelnetServerDeamonThread");

	}

	public void stop()
	{
		mRunning = false;
	}

}
