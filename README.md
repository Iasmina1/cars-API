# cars-API

**Part 1: Console Application** 

Java 12  
IDE: Eclipse Oxygen  

Dependencies:  
java-json.jar  

command line:   
javac -cp path\java-json.jar CarRental.java Car.java   
java CarRental provider pickup_lat pickup_long dropoff_lat dropoff_long no_passengers  

Example command line:   
java CarRental All 3.410632 -2.157533 3.410632 -2.157533 4  
java CarRental Dave 3.410632 -2.157533 3.410632 -2.157533 2    
etc.    

Types of providers possible: All, Dave, Jeff or Eric   

Example output:   
  
STANDARD - Jeff - 77697.0  
MINIBUS - Jeff - 445357.0  
EXECUTIVE - Jeff - 507783.0  
LUXURY - Jeff - 665694.0  

**Part 2: API**  

Node.js  
Express framework  
  
Dependencies:
cars-api.jar  (java console application from Part 1 exported as a Runnable jar). 

Start server using command line:  
node server.js 

Send request in browser: 
http://localhost:4000/api/cars/provider/pickup_latitude&pickup_longitude/dropoff_latitude&dropoff_longitude/no_passengers  
Example:  
http://localhost:4000/api/cars/All/51.470020&-0.454295/51.470020&-0.454295/5
  
