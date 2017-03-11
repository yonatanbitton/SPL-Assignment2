package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

/**
 * A class that represents a ToolTask which uses the tool on a product (useOn) 
 */
public class ToolTask extends Task<Tool> {
	private String name;
	private Product product;
	private Warehouse warehouse;
	

	/**
	* Constructor for the Tool task. 
	* @param toolname, the tool to make a task to, the product, and a warehouse
	*/
	public ToolTask(String toolName, Product product, Warehouse warehouse){
		this.name=toolName;
		this.product=product;
		this.warehouse=warehouse;
	}
	
	@Override
	/**
	* This function is for using the tool when it's available on the product, set it's finalId and complete it. 
	*/
	protected void start() {
		Deferred<Tool> d = warehouse.acquireTool(name);
		d.whenResolved(()->{
			//System.out.println("resolved aquireTool ~~~~~~~~~");
			long result=d.get().useOn(product);
			addToFinalId(result);
			warehouse.releaseTool(d.get());
			//product.setFinalId(currentFinalId);
			complete(d.get());
		});
	}
	

	/**
	* This function adds the output of the useOn function to the finalId;
	*/
	void addToFinalId(long sum){
		product.setFinalId(product.getFinalId()+sum);
	}

}


