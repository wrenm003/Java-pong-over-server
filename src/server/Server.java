package server;

import java.lang.*;
import java.net.*;
import java.util.concurrent.*;
import common.*;
import java.util.HashMap;
import java.util.Map;

class Server
{

	public static void main( String args[] )
	{
		final int port = 50000;
		( new Server() ).process( port );
	}
	public void process( final int port )
	{
		try
		{
			ExecutorService es = 
					Executors.newFixedThreadPool(4);
			ServerSocket ss = new ServerSocket(port);
			while ( true )
			{
				Socket socket    = ss.accept();
				Runnable process = new ProcessRequest(socket);
				es.execute( process );
			}

		}
		catch ( Exception e )
		{
			System.out.printf("Server.process(): %s\n",
					e.getMessage() );
		}
	}
}



class ProcessRequest implements Runnable
{
	private NetTCPReader in;                // Input stream 
	private NetTCPWriter out;               // Output stream 
	private static int      uniqueNo=1;
	private String          name;           // Name of Thread 
	private Socket          socket;         // Socket 
	public ProcessRequest( Socket ss )      // Construct 
	{
		socket = ss;                          // Remember socket 
		name = "Thread  " + uniqueNo++ + " "; // Name 
	}
	private void pingClient(){
		long start = System.nanoTime();						//Get current time
		out.put("a");
		in.get();
		long end = System.nanoTime();						//Get current time
		double ping = (double)(end-start)/1_000_000_000;	//Compare times
		Ping.addPing(name, ping);							//Add thread name and ping to Map Ping
		System.out.println(Ping.getPings());				//Print Ping Map
		Ping.setPing(ping);									//Check if this ping is the new highest ping
		System.out.println(name + "'s Ping: " + ping);		//Prints the thread name and ping
		out.put("Your ping is; " + ping + ", " + "highest ping to the server is " + Ping.getPing());
		out.put(Double.toString(ping));						//Send ping to client
		out.put(Double.toString(Ping.getPing()));			//Send Highest ping to client



	} 

	public void run()                       // Execution 
	{ 
		try
		{
			in  = new NetTCPReader(socket);     // Input 
			out = new NetTCPWriter(socket);     // Output 
			pingClient();
			while ( true )                      // Loop 
			{
				String message = in.get();       // From Client 
				if ( message == null ) break;    // No more data 
				if(!message.equals("a")){
					System.out.println(name + message);

					out.put( "" + message.length() );// Return length 
				}

			}
			in.close();                         // Close Read 
			out.close();                      // Close Write 

			socket.close();                     // Close Socket 
		}
		catch ( Exception err ) {}            // Abandon 
	}
}