# cars-API

Java 12 
IDE: Eclipse Oxygen 

Dependencies: 
gson-2.2.2.jar 
servlet-api.jar
java-json.jar 

downloads:
servlet-api.jar: https://tomcat.apache.org/download-90.cgi 

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
