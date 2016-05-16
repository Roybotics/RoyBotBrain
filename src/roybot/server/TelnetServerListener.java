package roybot.server;

public interface TelnetServerListener
{
	String receivedCommand(TelnetServer pSource, String pCommand);
}
