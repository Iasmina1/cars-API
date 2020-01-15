package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;


public class CarRental {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String,Integer> carsCapacity;
	private Object gson;
	private Map<String, Double> finalRes;
	
	public CarRental() {
		carsCapacity = new HashMap<String,Integer>();
		finalRes = new HashMap<String,Double>();
		addSomeCars();
		gson = new Gson();
	}
	
	private void addSomeCars() {
		carsCapacity.put("STANDARD",4);
		carsCapacity.put("EXECUTIVE",4);
		carsCapacity.put("LUXURY",4);
		carsCapacity.put("PEOPLE_CARRIER",6);
		carsCapacity.put("LUXURY_PEOPLE_CARRIER",6);
		carsCapacity.put("MINIBUS",13);
	}
	
	private static class Coordinates {
		double lat;
		double longi;
		public Coordinates(double lat, double longi) {
			this.lat = lat;
			this.longi = longi; 
		}
	}
	
	private static Map<String, Double> sortByPrice(Map<String, Double> mycars) { 
	     
	        List<Map.Entry<String, Double> > list = 
	               new LinkedList<Map.Entry<String, Double> >(mycars.entrySet()); 
	
	        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() { 
	            public int compare(Map.Entry<String, Double> o1,  
	                               Map.Entry<String, Double> o2) 
	            { 
	                return (o1.getValue()).compareTo(o2.getValue()); 
	            } 
	        }); 

	        Map<String, Double> mycarsCopy = new LinkedHashMap<String, Double>(); 
	        for (Map.Entry<String, Double> record : list) { 
	            mycarsCopy.put(record.getKey(), record.getValue()); 
	        } 
	        return mycarsCopy; 
	} 
	
	private static void prettyPrint(Map<String,Double> mycars) {
		System.out.println();
		if(mycars.isEmpty()) {
			System.out.println("Oh No! There are no cars available for this provider! Try Again Later.\n");
		}
		 for (Map.Entry<String, Double> e : mycars.entrySet()) { 
	            System.out.println(e.getKey() + " - " + e.getValue()); 
	        } 
	}
	
	private Map<String, Double> unwrap(String content, int passengers) {
	
		Map<String,Double> mycars = new HashMap<String,Double>();
		
		JSONObject obj;
		try {
			obj = new JSONObject(content);
			
			JSONArray jsonArray = obj.getJSONArray("options");
			
		     for(int i = 0 ; i < jsonArray.length(); i++) {
		    	 String car_type = jsonArray.getJSONObject(i).getString("car_type");
		    	 double price = jsonArray.getJSONObject(i).getDouble("price");
		    	 
		    	 // Part 1. Return relevant information to the client, taking into account the number of passengers.
		    	 int noSeats = carsCapacity.get(car_type);
		 		 if(passengers <= noSeats) {
		 			mycars.put(car_type,price);
		 		}
		      }
		      
		    
		} catch (JSONException e) {
		//	e.printStackTrace();
		}
		
		return mycars;
	}
	
	private Map<String, Double> getCars(String owner, Coordinates pickup, Coordinates dropoff, int passengers, int timeout) {
		URL url;
		Map<String,Double> results = new HashMap<String,Double>();
		String name = owner.substring(0,1).toUpperCase() + owner.substring(1).toLowerCase();
		try {
			
			url = new URL("https://techtest.rideways.com/" + owner + 
					"?pickup=" + pickup.lat + "," + pickup.longi +"&dropoff=" + dropoff.lat + "," + dropoff.longi );
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("GET");
			con.setReadTimeout(timeout);
	
			int status = con.getResponseCode();
			 
			Reader streamReader = null;
			 
			if (status > 299) {
			    streamReader = new InputStreamReader(con.getErrorStream());
			} else {
			    streamReader = new InputStreamReader(con.getInputStream());
			}
			
			BufferedReader in = new BufferedReader(streamReader);
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			in.close();
			
			con.disconnect();

			results = unwrap(content.toString(), passengers);
			
			System.out.println("\n" + name + " has:");
			prettyPrint(results);

		} catch (MalformedURLException e) {
			//e.printStackTrace();
		} catch (java.net.SocketTimeoutException e) {
				System.out.println("\n" + name + " was too slow to respond.");
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		
		return results;
		
	}
	
	private Map<String,Double> bestProvider(Map<String,Double> a, Map<String,Double> b){
		Map<String,Double> result = new HashMap<String,Double>();
		
		// Get all cars in (A \ B) or ( A n B )
		for (Map.Entry<String, Double> e : a.entrySet()) { 
		
			// If both providers have the same car available, choose the one that is cheaper.
            if(b.get(e.getKey())!= null && b.get(e.getKey()) < e.getValue()) {
            	result.put(e.getKey(), b.get(e.getKey()));
            }
            else {
            	result.put(e.getKey(), e.getValue());
            }
		} 
		
		// Get all cars in (B \ A) 
		for (Map.Entry<String, Double> e : b.entrySet()) { 
			if(result.get(e.getKey()) == null) {
				result.put(e.getKey(), e.getValue());
			}
		}
		return result;
	}

	private Map<String, Double> start(Coordinates pickup, Coordinates dropoff, int passengers) {
		
			int time = 2000; 
			finalRes =	sortByPrice( bestProvider( bestProvider( getCars("dave", pickup, dropoff, passengers, time) ,
														getCars("eric", pickup, dropoff, passengers, time) ) ,
														getCars("jeff", pickup, dropoff, passengers, time) )
							);
			return finalRes;
		
	}
	
	private void dave(Coordinates pickup, Coordinates dropoff, int passengers) {

		prettyPrint( sortByPrice( getCars("dave", pickup, dropoff, passengers, 100000) ));
	}	

	public static void main(String[] args) {
		CarRental r = new CarRental();
		double lat, longi; 
		int passengers;
		
		if(args.length == 5) { 
			lat = Double.parseDouble(args[0]);
			longi = Double.parseDouble(args[1]);
			Coordinates pickup = new Coordinates(lat, longi);
			
			lat = Double.parseDouble(args[2]);
			longi = Double.parseDouble(args[3]);
			Coordinates dropoff = new Coordinates(lat, longi);
			
			passengers = Integer.parseInt(args[4]); 
			
			System.out.println("Part 1.1 \nDave's cars:");
			r.dave(pickup, dropoff, passengers);
			
			System.out.println("\n\nPart 1.2 \nAll providers:"); 
			System.out.println("\nFinal results:");
			prettyPrint(r.start(pickup, dropoff, passengers));
			
		} else {
			System.out.println("Incorrect Arguments!"); 
		}
	}
}
