package bgu.spl.a2.sim.conf;

import java.util.Arrays;

/**
 * a class that represents a manufacturing plan.
 *
 **/
public class ManufactoringPlan {
	private String product;
	private String[] tools;

	@Override
	public String toString() {
		return "ManufactoringPlan{" +
				"product='" + product + '\'' +
				", tools=" + Arrays.toString(tools) +
				", parts=" + Arrays.toString(parts) +
				'}';
	}

	private String[] parts;
	
	/** ManufactoringPlan constructor
	* @param product - product name
	* @param parts - array of strings describing the plans part names
	* @param tools - array of strings describing the plans tools names
	*/
    public ManufactoringPlan(String product, String[] parts, String[] tools){
    	this.product=product; // Shallow copy
    	this.parts=parts; // Shallow copy
    	this.tools=tools;
    }

	/**
	* @return array of strings describing the plans part names
	*/
    public String[] getParts(){
    	return parts;
    }

	/**
	* @return string containing product name
	*/
    public String getProductName(){
    	return product;
    }
	/**
	* @return array of strings describing the plans tools names
	*/
    public String[] getTools(){
    	return tools; 
    }

    /**
	* @return a String of the product
	*/
	public String getProduct() {
		return product;
	}
}