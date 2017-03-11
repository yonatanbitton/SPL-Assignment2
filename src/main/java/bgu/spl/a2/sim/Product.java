package bgu.spl.a2.sim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A class that represents a product produced during the simulation.
 */
@SuppressWarnings("serial")
public class Product implements Serializable {
	private long startId;
	private String name;
	private long finalId;
	private ArrayList<Product> productsDependOn = new ArrayList<Product>();

	/**
	* Constructor
	* @param startId - Product start id
	* @param name - Product name
	*/
    public Product(long startId, String name){
    	this.startId=startId;
    	this.name=name;
    	this.finalId=startId;
    }

	/**
	* @return The product name as a string
	*/
    public String getName() {
		return name;
	}

	/**
	* @return The product start ID as a long. start ID should never be changed.
	*/
    public long getStartId(){
    	return startId;
    }
    

	/**
	* Set the finalId.
	* @param finalId - The finalId to set

	*/
    public void setFinalId(long finalId){
    	this.finalId=finalId;
    }

	/**
	* @return The product final ID as a long. 
	* final ID is the ID the product received as the sum of all UseOn(); 
	*/
    public long getFinalId(){
    	if (finalId!=0) return finalId;
    	else if (finalId==0) return startId;
    	else throw new RuntimeException("Not resolved yet ");
    }

	/**
	* @return Returns all parts of this product as a List of Products
	*/
    public List<Product> getParts() {
		return productsDependOn;
	}

	/**
	* Add a new part to the product
	* @param p - part to be added as a Product object
	*/
    public void addPart(Product p) {
		productsDependOn.add(p);
	}

	@Override

	/**
	* toString 
	*/
	public String toString() {
		return "Product{" +
				"startId=" + startId +
				", name='" + name + '\'' +
				", finalId=" + finalId +
				", productsDependOn=" + productsDependOn.toString() +
				'}';
	}
	
	
}