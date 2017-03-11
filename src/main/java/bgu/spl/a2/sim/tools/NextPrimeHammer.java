
package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

/**
* This class represents the NextPrimeHammer tool.
*/
public class NextPrimeHammer implements Tool{
	private String type="np-hammer";


	/**
	* @return the type of the tool 
	*/
	@Override
	public String getType() {
		return "np-hammer";
	}
	/**
	* @return the toString of the tool 
	*/
	@Override
	public String toString() {
		return "NextPrimeHammer{" +
				"type='" + type + '\'' +
				'}';
	}

	/**
	* @param Product p - the product to use on
	* @return long number, the output of the formulaa value+=Math.abs(func(part.getFinalId()))
	*/
	@Override
	public long useOn(Product p){
		//System.out.println("I'm using the NextPrimeHammer!");
		long value=0;
		for(Product part : p.getParts()){
			value+=Math.abs(func(part.getFinalId()));

		}
		return value;
	}

	/**
	* @param long id - the id of the product to calculate
	* @return the first prime number greater then the product ip
	*/
	 public long func(long id) {
	    	
	        long v =id + 1;
	        while (!isPrime(v)) {
	            v++;
	        }

	        return v;
	    }
	 /**
		* @param long value 
		* @return true if the value is prime
		*/
	 private boolean isPrime(long value) {
		 if(value < 2) return false;
	    	if(value == 2) return true;
	        long sq = (long) Math.sqrt(value);
	        for (long i = 2; i <= sq; i++) {
	            if (value % i == 0) {
	                return false;
	            }
	        }

	        return true;
	    }



}