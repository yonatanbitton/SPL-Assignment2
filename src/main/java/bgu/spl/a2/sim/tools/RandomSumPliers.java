package bgu.spl.a2.sim.tools;

import java.util.Random;

import bgu.spl.a2.sim.Product;

/**
* This class represents the RandomSumPliers tool.
*/
public class RandomSumPliers implements Tool{
	private String type="rs-pliers";

	/**
	* @return the type of the tool 
	*/
	@Override
	public String getType() {
		return "rs-pliers";
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
	* @return the toString
	*/
	@Override
	public String toString() {
		return "RandomSumPliers{" +
				"type='" + type + '\'' +
				'}';
	}

	/**
	* @param long id - the id of the product to calculate
	* @return the output of the fourmula for sum for (long i = 0; i < id % 10000; i++) { sum += r.nextInt();} when r is a random number
	*/
	public long func(long id){
    	Random r = new Random(id);
        long  sum = 0;
        for (long i = 0; i < id % 10000; i++) {
            sum += r.nextInt();
        }

        return sum;
    }

}