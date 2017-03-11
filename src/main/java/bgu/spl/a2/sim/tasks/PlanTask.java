package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A class that represents a PlanTask which constructs a single Product. 
 */
public class PlanTask extends Task<Product> {
    private Product product;
    private long myID;
    private String[] productsDependOnStringArr;
    private ConcurrentLinkedQueue<PlanTask> partsTasksDependOnQueue = new ConcurrentLinkedQueue<PlanTask>();
    // This queue is necessary for the assembling parts - useOn.
    private ConcurrentLinkedQueue<Product> productsDependOnQueue = new ConcurrentLinkedQueue<Product>();
    private String[] necessaryToolsStringArr;
    private Warehouse warehouse;
    private AtomicInteger toolsUsed=new AtomicInteger();
    /**
	* Constructor for the plan task. 
	* @param product_par, the Product to make a task to, it's startId, and a warehouse
	*/
    public PlanTask(Product product_par, long startId, Warehouse warehouse) {
        product = product_par;
        myID = startId;
        toolsUsed.set(0);
        this.warehouse = warehouse;
        ManufactoringPlan plan = warehouse.getPlan(product.getName());
        productsDependOnStringArr = plan.getParts();
        necessaryToolsStringArr = plan.getTools();
    }


	/**
	* This function is for making a single product type
	*/
    @Override
    protected void start() {
        createDependedProductsAndTasks();
        baseCase();
        spawnPlanTasks();
        
        // When all the parts I depend on are resolved (Assembled), I can assemble myself.
        whenResolved(partsTasksDependOnQueue, () -> {
            addCompletedPartsToProduct();
            
            // Check if all requested parts acquired
            useEachToolOnTheProductAndComplete();
        });
    }
    

	/**
	* This function handles with the baseCase, meaning the cases when the product does not depend on other products or another tools
	*/
    void baseCase(){
        if (productsDependOnQueue.size()==0) {
        	if (necessaryToolsStringArr.length==0){
        		product.setFinalId(myID);
        		complete(product);
        		System.out.println("Solved: " + product.getName());
        	}
        	else {
        		complete(product);
        		System.out.println("Solved: " + product.getName());
         	}
        }
    }


	/**
	* This function for each tool necessary, use on the product, and then completed the product
	*/
    void useEachToolOnTheProductAndComplete() {
    	ArrayList<ToolTask> toolTasks = new ArrayList<ToolTask>();
    	
        for (int i = 0; i < necessaryToolsStringArr.length; i++) {
        	ToolTask t = new ToolTask (necessaryToolsStringArr[i], product, warehouse);
        	toolTasks.add(t);
        	spawn(t);
        }
        
        whenResolved(toolTasks, ()->{ 
    		System.out.println("Solved: " + product.getName());
        	complete(product);        	
        });
        
        if (necessaryToolsStringArr.length==0) {
        	complete(product);
    		System.out.println("Solved: " + product.getName());
        }
      }



	/**
	* This function create data structures for the depended products and tasks
	*/
    void createDependedProductsAndTasks() {
    	
        for (int i = 0; i < productsDependOnStringArr.length; i++) {
            // For being able to manufacture a new PlanTask for the parts I need.
            Product p = new Product(product.getStartId() + 1, productsDependOnStringArr[i]);
            productsDependOnQueue.add(p);
            partsTasksDependOnQueue.add(new PlanTask(p, p.getStartId(), warehouse));
        }
    }


	/**
	* This function spawns new plan tasks 
	*/
    void spawnPlanTasks() {
        Iterator<PlanTask> it = partsTasksDependOnQueue.iterator();
        while (it.hasNext()) {
            spawn(it.next());
        }
    }


	/**
	* This function adds the completed parts to the product
	*/
    void addCompletedPartsToProduct() {
        Product p = productsDependOnQueue.poll();
        while (p != null) {
            product.addPart(p);
            p = productsDependOnQueue.poll();
        }
    }
}