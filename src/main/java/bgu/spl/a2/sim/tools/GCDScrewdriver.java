package bgu.spl.a2.sim.tools;

import java.math.BigInteger;

import bgu.spl.a2.sim.Product;

/**
* This class represents the GCDScrewdriver tool.
*/
public class GCDScrewdriver implements Tool{
	private String type="gs-driver";
	

	/**
	* @return the type of the tool 
	*/
	@Override
	public String getType() {
		return "gs-driver";
	}
	

	/**
	* @param Product p - the product to use on
	* @return long number, the output of the formulaa value+=Math.abs(func(part.getFinalId()))
	*/
	@Override
	public long useOn(Product p){
		long value=0;
    	for(Product part : p.getParts()){
    		value+=Math.abs(func(part.getFinalId()));
    		
    	}
      return value;
    }

	/**
	* @param long id - the id of the product to calculate
	* @return long number, the output of theformula (b1.gcd(b2)).longValue()
	*/
	public long func(long id){
		BigInteger b1 = BigInteger.valueOf(id);
        BigInteger b2 = BigInteger.valueOf(reverse(id));
        long value= (b1.gcd(b2)).longValue();
        return value;
	}
	/**
	* @param long n
	* @return the number at reverse
	*/
	  public long reverse(long n){
	  long reverse=0;
	    while( n != 0 ){
	        reverse = reverse * 10;
	        reverse = reverse + n%10;
	        n = n/10;
	    }
	    return reverse;
	  }

	  /**
		* @return the String of the tool
		*/
	@Override
	public String toString() {
		return "GCDScrewdriver{" +
				"type='" + type + '\'' +
				'}';
	}
}