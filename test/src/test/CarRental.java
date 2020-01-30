package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
	private ArrayList<Car> finalRes;
	
	public CarRental() {
		carsCapacity = new HashMap<String,Integer>();
		finalRes = new ArrayList<Car>();
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
	
	private ArrayList<Car> sortByPrice(ArrayList<Car> mycars) { 
	        Collections.sort(mycars);
	        
	        return mycars;
	} 
	
	private static void prettyPrint(ArrayList<Car> cars) {

		if(cars.isEmpty()) {
			//System.out.println("Oh No! There are no cars available for this provider! Try Again Later.\n");
		}
		
		for(Car car : cars) {
			String name = car.getProvider().substring(0,1).toUpperCase() + car.getProvider().substring(1).toLowerCase();
			System.out.println(car.getType() + " - " + name + " - " + car.getPrice()); 
		}
	}
	
	private ArrayList<Car> unwrap(String provider, String content, int passengers) {
	
		//Map<String,Double> mycars = new HashMap<String,Double>();
		ArrayList<Car> mycars = new ArrayList<Car>();
		
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
		 			//mycars.put(car_type,price);
		 			 Car car = new Car(provider, car_type, price);
		 			 mycars.add(car);
		 		}
		      }
		      
		    
		} catch (JSONException e) {
		//	e.printStackTrace();
		}
		
		return mycars;
	}
	
	private ArrayList<Car> getCars(String owner, Coordinates pickup, Coordinates dropoff, int passengers, int timeout) {
		URL url;
		ArrayList<Car> results = new ArrayList<Car>();
	
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

			results = unwrap(owner, content.toString(), passengers);
			
			//prettyPrint(results, owner);

		} catch (MalformedURLException e) {
			//e.printStackTrace();
		} catch (java.net.SocketTimeoutException e) {
				//System.out.println("\n" + name + " was too slow to respond.");
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		
		return results;
		
	}
	
	private ArrayList<Car> bestProvider(List<Car> allCars){

		HashMap<String, Car> mp = new HashMap<String, Car>();
		
		for(Car car : allCars){
			
			if(mp.get(car.getType())!=null && mp.get(car.getType()).getPrice() > car.getPrice() ||
				(mp.get(car.getType())==null)) {
			
					mp.put(car.getType(), car); 		
			}
	
		}
		
		return new ArrayList<Car>(mp.values());
	}

	private ArrayList<Car> start(Coordinates pickup, Coordinates dropoff, int passengers) {
		
			int time = 2000; 
			
			List<Car> dave = getCars("dave", pickup, dropoff, passengers, time);
			List<Car> eric = getCars("eric", pickup, dropoff, passengers, time);
			List<Car> jeff = getCars("jeff", pickup, dropoff, passengers, time); 
			
			dave.addAll(eric);
			dave.addAll(jeff);
			
			finalRes =	sortByPrice( bestProvider( dave ) );
			
			return finalRes;
		
	}
	
	private void individual(String provider, Coordinates pickup, Coordinates dropoff, int passengers) {

		prettyPrint( sortByPrice( getCars(provider, pickup, dropoff, passengers, 100000) ));
	}	
	

	public static void main(String[] args) {
		CarRental r = new CarRental();
		double lat, longi; 
		int passengers;
		String provider;
		
		if(args.length == 6) { 
			int n = 0; 
			provider = args[n]; 
			lat = Double.parseDouble(args[n+1]);
			longi = Double.parseDouble(args[n+2]);
			Coordinates pickup = new Coordinates(lat, longi);
			
			lat = Double.parseDouble(args[n+3]);
			longi = Double.parseDouble(args[n+4]);
			Coordinates dropoff = new Coordinates(lat, longi);
			
			passengers = Integer.parseInt(args[n+5]); 
			
			if(provider.equalsIgnoreCase("Dave")) {
				
				r.individual("dave", pickup, dropoff, passengers);
			
			} else if (provider.equalsIgnoreCase("Jeff")) {
				
				r.individual("jeff", pickup, dropoff, passengers);
				
			} else if (provider.equalsIgnoreCase("Eric")) {
				
				r.individual("eric", pickup, dropoff, passengers);
				
			} else if(provider.equalsIgnoreCase("All")) {
				
				prettyPrint(r.start(pickup, dropoff, passengers));
			}
			
		} else {
			System.out.println("Incorrect Arguments!"); 
		}
	}
}
