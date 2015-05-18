package common;

import java.util.HashMap;
import java.util.Map;

public class Ping {

	private static double highestPing;
	private static Map<String, Double> pings = new HashMap<String, Double>();

	public static void setPing(double currentPing){
		if (currentPing > highestPing){
			highestPing = currentPing;
		}
	}
	public static void addPing(String name, double ping){
		pings.put(name, ping);
	}
	

	public static double getPing(){
		return highestPing;
	}
	public static Map getPings(){
		return pings;
	}

}
