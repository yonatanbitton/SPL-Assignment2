package bgu.spl.a2.sim.conf;


/**
 * A class that represents a single request
 */
public class Request{
	private String product;
	private int qty;
	private long startId;
	

/** Constructor
 *@param product - a string of the product requested
 *@param startId - the id to request

 */
	public Request(String pruduct, int qty, long startId){
		this.product=pruduct; // Shallow copy
		this.qty=qty;
		this.startId=startId;
	}
	
	public String getProduct(){
		return this.product;
	}
	
	public int getQty(){
		return this.qty;
	}

	public long startId(){
		return this.startId;
	}
	
	public String toString(){
		String s = ("Product is: " + product + " qty is: " + qty  + " and the startId is " + startId + '\n');
		return s;
	}
}

