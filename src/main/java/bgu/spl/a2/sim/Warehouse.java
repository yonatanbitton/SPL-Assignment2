package bgu.spl.a2.sim;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import bgu.spl.a2.sim.conf.ToolClass;
import bgu.spl.a2.sim.tools.GCDScrewdriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.Deferred;

import bgu.spl.a2.sim.conf.ManufactoringPlan;

/**
 * A class representing the warehouse in your simulation
 * 
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse { // Concurrent for the tools and their deferred. Vector of manufacturing plan
	private LinkedList<Deferred<Tool>> GCDDeferred=new LinkedList<Deferred<Tool>>();
	private LinkedList<Deferred<Tool>> NextPrimDeferreds=new LinkedList<Deferred<Tool>>();
	private LinkedList<Deferred<Tool>> RandSumDeferreds=new LinkedList<Deferred<Tool>>();
	private int gcdCounter=0;
	private int nextPrimeCounter=0;
	private int randSumCounter=0;
	private ManufactoringPlan[] plans;
	private ToolClass[] tools;
	private GCDScrewdriver gcdscrew=new GCDScrewdriver();
	private NextPrimeHammer nextprim=new NextPrimeHammer();
	private RandomSumPliers randsum=new RandomSumPliers();

	/**
	* Constructor
	* @param plans_par - the plans array parameter
	* @param tools_par - the tools array parameter
	*/
    public Warehouse(ManufactoringPlan[] plans_par, ToolClass[] tools_par) {
    	plans=plans_par;
    	tools=tools_par;
    	for (int i=0; i<tools.length; i++){
    		if (tools[i].getTool().equals("gs-driver")) gcdCounter=tools[i].getQty();
    		if (tools[i].getTool().equals("np-hammer")) nextPrimeCounter=tools[i].getQty();
    		if (tools[i].getTool().equals("rs-pliers")) randSumCounter=tools[i].getQty();
    	}
	}

	/**
	* Tool acquisition procedure
	* Note that this procedure is non-blocking and should return immediatly
	* @param type - string describing the required tool
	* @return a deferred promise for the  requested tool
	*/
    public synchronized Deferred<Tool> acquireTool(String type) { // TODO: 2
		Deferred<Tool> d = new Deferred<>();
		if (type.equals("gs-driver")) {
				if (gcdCounter > 0) {
					gcdCounter=gcdCounter-1;
					d.resolve(gcdscrew);
				} else {
					GCDDeferred.push(d);
				}
		}
		else if (type.equals("np-hammer")) {
				//System.out.println("I'm at the aquireTool ~~~~~~~~~");
				if (nextPrimeCounter>0) {
					nextPrimeCounter=nextPrimeCounter-1;
					d.resolve(nextprim);
				}
				else {
					NextPrimDeferreds.push(d);
				}
		}
		else if (type.equals("rs-pliers")) {
				if (randSumCounter>0){
					randSumCounter=randSumCounter-1;
					d.resolve(randsum);
				}
				else {
					RandSumDeferreds.push(d);
				}
		}
		else throw new NoSuchElementException("No such tool to aquire");
		
		return d;
 	}

	/**
	* Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	* @param tool - The tool to be returned
	*/

	// The deferred queue symbolize someone that was waiting to get the tool. So if someone was waiting, I give it back.
    public synchronized void releaseTool(Tool tool) {
		if (tool.getType().equals("gs-driver")) {
				Deferred<Tool> g = GCDDeferred.poll();
				if (g!=null) g.resolve(tool);
					else gcdCounter=gcdCounter+1;
			
 		}
		if (tool.getType().equals("np-hammer")) {
			Deferred<Tool> g = NextPrimDeferreds.poll();
			if (g!=null) g.resolve(tool);
			else nextPrimeCounter=nextPrimeCounter+1;
			
		}
		if (tool.getType().equals("rs-pliers")) {
			Deferred<Tool> g = RandSumDeferreds.poll();
			if (g!=null) g.resolve(tool);
			else randSumCounter=randSumCounter+1;
		
		}
    }

	
	/**
	* Getter for ManufactoringPlans
	* @param product - a string with the product name for which a ManufactoringPlan is desired
	* @return A ManufactoringPlan for product
	*/
    public ManufactoringPlan getPlan(String product){
		for(int i=0; i<plans.length;i++){
			if(plans[i].getProduct().equals(product))
				return plans[i];
		}
		throw new NoSuchElementException("No plan for this product");
	}
    // 
	
	/**
	* Store a ManufactoringPlan in the warehouse for later retrieval
	* @param plan - a ManufactoringPlan to be stored
	*/
    public void addPlan(ManufactoringPlan plan){
		ManufactoringPlan[] newPlans = new ManufactoringPlan[plans.length+1];
		for (int i=0; i<plans.length; i++)
			newPlans[i]=plans[i];
		newPlans[plans.length]=plan;
		plans=newPlans;
	}
    
	/**
	* Store a qty Amount of tools of type tool in the warehouse for later retrieval
	* @param tool - type of tool to be stored
	* @param qty - amount of tools of type tool to be stored
	*/
    public void addTool(Tool tool, int qty){ // Whats more!!??!!??
    	if (tool.getType().equals("gs-driver")){ gcdCounter=gcdCounter+qty; } // TODO: 1  
    	if (tool.getType().equals("np-hammer")){ nextPrimeCounter=nextPrimeCounter+qty; } 
    	if (tool.getType().equals("rs-pliers")){ randSumCounter=randSumCounter+qty; } 
    }

}
