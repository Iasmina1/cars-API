package test;

public class Car implements Comparable<Car> {
	
	private String provider;
	private String type;
	private Double price;
	
	public Car(String provider, String type, Double price) {
		this.provider= provider;
		this.type = type;
		this.price = price; 
	}
	
	public String getType(){
		return type;
	}
	
	public String getProvider() {
		return provider;
	}
	
	public Double getPrice() {
		return price;
	}

	@Override
	public int compareTo(Car o) {
		return (int) ( this.price - o.getPrice() ); 
	}

}
