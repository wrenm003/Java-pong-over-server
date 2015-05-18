package client;

import java.net.*;
import java.util.*;

import common.*;
class Client
{
	public static void main( String args[] )
	{
		System.out.println( "Client" );
		final String host = "localhost";           // Host name  
		final int    port = 50000;                 // Port used  
		try
		{
			NetStringWriter out;                     // String output 
			NetStringReader in;                      // String input 
			Socket socket = new Socket( host, port );// Socket  
			out = new NetTCPWriter( socket );        // Output  
			in  = new NetTCPReader( socket );        // Input  
			while(true)
			{
				out.put( "a" );                    //   to Server  
				String response = in.get();            //   Response  
				System.out.println(response);
				if ( response == null ) break;         // Failure  
			}

			String pingString = in.get();
			double ping = Double.parseDouble(pingString);
			String highestString = in.get();
			double highestPing = Double.parseDouble(highestString);
			
			System.out.println("your ping"+ping+"highest"+highestPing);
			
			

			out.close();                             // Close stream 
		}
		catch ( Exception e )
		{
			DEBUG.error("Error:\n%s", e.getMessage() );
		}
	}
}