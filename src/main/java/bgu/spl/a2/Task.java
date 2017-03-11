package bgu.spl.a2;

import java.util.Collection;

/**
 * an abstract class that represents a task that may be executed using the
 * {@link WorkStealingThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the task result type
 */
public abstract class Task<R> {
    private Deferred<R> myDeferred=new Deferred<>();
    private Processor myProcessor;
    private Runnable callback=null;
    private int numOfTasksLeft=0;
//	private ArrayList<Task> tasksDependOn;



    /**
     * start handling the task - note that this method is protected, a handler
     * cannot call it directly but instead must use the
     * {@link #handle(bgu.spl.a2.Processor)} method
     */
    protected abstract void start();

    /**
     *
     * start/continue handling the task
     *
     * this method should be called by a processor in order to start this task
     * or continue its execution in the case where it has been already started,
     * any sub-tasks / child-tasks of this task should be submitted to the queue
     * of the handler that handles it currently
     *
     * IMPORTANT: this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * @param handler the handler that wants to handle the task
     */
    /*package*/ final void handle(Processor handler) {
        // If it's the first time of the task - run it. Else, run the callback
        myProcessor=handler;
        if (callback==null) start();
        else callback.run();
    }

    /**
     * This method schedules a new task (a child of the current task) to the
     * same processor which currently handles this task.
     *
     * @param task the task to execute
     */
    protected final void spawn(Task<?>... task) {
        // Gets a list of tasks, make a forLoop, and add it the the queue of that specific task
        for (int i=0; i<task.length; i++){
            // We want the pool to add a given task.
            // pool.addTask(task[i], id); <- this function needs to increase aswell.
            myProcessor.getPool().addTask(task[i], myProcessor.getProcessorID());
        //	System.out.println("Spawned " + i + " new tasks");
        }
    }

    /**
     * add a callback to be executed once *all* the given tasks results are
     * resolved
     *
     * Implementors note: make sure that the callback is running only once when
     * all the given tasks completed.
     *
     * @param tasks
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void whenResolved(Collection<? extends Task<?>> tasks, Runnable callback) {
    	numOfTasksLeft = tasks.size();
        Object[] tmpArr = tasks.toArray();
        this.callback=callback;
//    	for (Task<?> t : tasks) {
//
//		}
        
        for (int i=0; i<tmpArr.length; i++){
            ((Task<?>)tmpArr[i]).getResult().whenResolved(()->{
            	synchronized (this) { // we need to synchronize in order to prevent a situation where two threads adds the same resolved task
            	numOfTasksLeft--;
                if (numOfTasksLeft <= 0)
                    // Add to my Queue
                    myProcessor.getPool().addTask(this, myProcessor.getProcessorID());
            	}
            });
        }
        
        
        // I get tasks, and callbacks. When all the tasks are completed, run the callback
        // @pre-condition - all of the tasks needs to be resolved <-> getResult!=null
//   	Iterator<? extends Task<?>> it=tasks.iterator();
//    	while (it.hasNext()) {
//    		if (it.next().getResult()==null)
//				try {
//					// Option for OBJECT in wait
//					wait();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    	}
    }

    /**
     * resolve the internal result - should be called by the task derivative
     * once it is done.
     *
     * @param result - the task calculated result
     */
    protected final void complete(R result) {
        myDeferred.resolve(result);
    }

    /**
     * @return this task deferred result
     */
    public final Deferred<R> getResult() {
        return myDeferred;
    
    }
}