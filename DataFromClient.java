import java.io.*;

//Esta classe possibilita a recepção dos dados vindos do Cliente.

public class DataFromClient extends Thread  {
	
	private Board B;
	private InputStream StreamFromClient;
	
	//Construtor
	public DataFromClient (Board B, InputStream StreamFromClient)
	{
		this.B = B;
		this.StreamFromClient = StreamFromClient;
	}
	
	public void run()
	{
		int bytesRead;
		byte[] reply = new byte[1];
		String message = new String ("");
		while(true)
		{
			System.out.println("ESTOU NO WHILE!");
			try
			{
				System.out.println("ESTOU NO Try!");
				bytesRead = StreamFromClient.read(reply);
				System.out.println("Passei do Read!");
				message = new String (reply, "US-ASCII");
				System.out.println("Message = " + message);
				if (message.compareTo("L") == 0)
					B.TurnLeft();
				else if (message.compareTo("R") == 0)
					B.TurnRight();
				else if (message.compareTo("U") == 0)
					B.TurnUp();
				else if (message.compareTo("D") == 0)
					B.TurnDown();
			}
			catch (IOException e)
			{
				;
			}
		}
	}

}
