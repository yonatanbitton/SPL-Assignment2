package bgu.spl.a2.sim.conf;

/**
 * this class represents a single wave
 *
 */
public class Wave {
	private Request[] requests;
	

	/**
	* Constructor
	* @param requests - the requests array parameter
	*/
	public Wave(Request[] requests){
		this.requests=requests; // Shallow copy
	}
	

	/**
	* getter for the requests array
	*/
	public Request[] getWave(){
		return this.requests;
	}
	
	/**
	* toString for this class
	*/
	public String toString(){
		String s=null;
		for (int i=0; i<requests.length; i++){
			s = ("Products is: " + requests[i].getProduct() + " qty is: " + requests[i].getQty()  + " and the startId is " );
		}
		return s;
	}
}

