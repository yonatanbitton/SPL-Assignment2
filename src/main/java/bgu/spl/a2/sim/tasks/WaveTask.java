package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.Request;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class that represents a WaveTask which completes and Requests array . 
 */
public class WaveTask extends Task<ArrayList<Product>> {
    private Request[] requestsArray;
    private ArrayList<Product> productsArray = new ArrayList<Product>();
    private ArrayList<PlanTask> planTasks = new ArrayList<PlanTask>();
    private Warehouse warehouse;


	/**
	* Constructor
	* @param requestsArray_par - the given requests array
	* @param warehouse - the warehouse. 
	*/
    public WaveTask(Request[] requestsArray_par, Warehouse warehouse) {
        this.requestsArray = requestsArray_par;
        this.warehouse = warehouse;
    }

    @Override

/**
 * This function create requests array and plan tasks to perform them, and completes the products array when the planTasks are resolved. 
 */
    protected void start() {
        createRequestsArrayAndPlanTasks();
        spawnPlanTasks();
        whenResolved(planTasks, () -> {
        	System.out.println("WhenResolved -> WaveTask");
            complete(productsArray);
           // notifyAll(); // In order to move the next wave
        });
    }


/**
 * This function creates requests array and plan tasks in order to perform them.
 */
    void createRequestsArrayAndPlanTasks() {
        for (int i = 0; i < requestsArray.length; i++) {
            for (long j = 0; j < requestsArray[i].getQty(); j++) {
                Product product = new Product(requestsArray[i].startId() + j, requestsArray[i].getProduct());
                PlanTask p = new PlanTask(product, requestsArray[i].startId() + j, warehouse);
                planTasks.add(p);
                productsArray.add(product);
            }
        }
    }


/**
 * This function spawn plan tasks
 */
    void spawnPlanTasks(){
        Iterator<PlanTask> it = planTasks.iterator();
        while (it.hasNext()) {
            spawn(it.next());
        }
    }
}