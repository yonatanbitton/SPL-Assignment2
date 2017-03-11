package bgu.spl.a2.sim.conf;


/**
 * This class represent the ToolClass - A class for the tools input from the json file. 
 * This class is used only for the convertion from json.
 */
public class ToolClass {
	private String tool;
	private int qty;
	
	/** Constructor
	 *@param tool - tool to create
	 *@param qty - number of tools to construct from the tool's type
	 */
	public ToolClass(String tool, int qty){
		this.tool=tool; // Shallow copy
		this.qty=qty;
//		Tool t=null;
//		if (tool.equals("GCDScrewdriver")) 
//			 t = new GCDScrewdriver();
//		if (tool.equals("NextPrimeHammer")) 
//			 t = new NextPrimeHammer();
//		if (tool.equals("RandomSumPliers")) 
//			 t = new RandomSumPliers();
  	}
	

	/**
	 *@return a string of the tool type
	 */
	public String getTool(){
		return this.tool;
	}
	

	/**
	 *@return the quantity of the tool type
	 */
	public int getQty(){
		return this.qty;
	}

	//public abstract long useOn (Product p);
	
//	public long useOn(Product p){
//		long value=0;
//    	for(Product part : p.getParts()){
//    		value+=Math.abs(func(part.getFinalId())); // Which get ID? Final or initial? 
//    	}
//      return value;
//	}
//
//	private int func(long finalId) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	

	/**
	 *@return the toString of the tool type
	 */
	public String toString() {
		String s = ("Tool is: " + tool + '\n' + "qty is: " + qty);
		return s;
	}
}