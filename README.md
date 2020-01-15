# cars-API

Java 12  
IDE: Eclipse Oxygen  

Dependencies:  
java-json.jar  
  
command line:   
javac CarRental.java   
java CarRental pickup_lat pickup_long dropoff_lat dropoff_long no_passengers  
OR  
javac -cp path\gson-2.2.2.jar;path\servlet-api.jar;path\java-json.jar; CarRental.java  
java CarRental pickup_lat pickup_long dropoff_lat dropoff_long no_passengers  

Example of args in Eclipse:
3.410632 -2.157533 3.410632 -2.157533 4

Example command line: 
java CarRental 3.410632 -2.157533 3.410632 -2.157533 4

Example output:   
  
Part 1.1   
Dave's cars:  

Dave has:  

PEOPLE_CARRIER - 246632.0  
EXECUTIVE - 691348.0  
LUXURY - 377879.0  
LUXURY_PEOPLE_CARRIER - 471228.0  
STANDARD - 299853.0  
  
PEOPLE_CARRIER - 246632.0  
STANDARD - 299853.0  
LUXURY - 377879.0  
LUXURY_PEOPLE_CARRIER - 471228.0  
EXECUTIVE - 691348.0  
  
  
Part 1.2   
All providers:  
  
Final results:  

Dave has:  

PEOPLE_CARRIER - 980267.0  
LUXURY - 966578.0  
STANDARD - 986907.0  
  
Eric was too slow to respond.  
  
Jeff has:  
  
PEOPLE_CARRIER - 958267.0  
LUXURY_PEOPLE_CARRIER - 349950.0  
  
LUXURY_PEOPLE_CARRIER - 349950.0  
PEOPLE_CARRIER - 958267.0  
LUXURY - 966578.0  
STANDARD - 986907.0  
  
