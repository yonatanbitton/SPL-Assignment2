package bgu.spl.a2;

//import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

	private final WorkStealingThreadPool pool;
	private final int id;

	/**
	 * constructor for this class
	 *
	 * IMPORTANT: 1) this method is package protected, i.e., only classes inside
	 * the same package can access it - you should *not* change it to
	 * public/private/protected
	 *
	 * 2) you may not add other constructors to this class nor you allowed to
	 * add any other parameter to this constructor - changing this may cause
	 * automatic tests to fail..
	 *
	 * @param id
	 *            - the processor id (every processor need to have its own
	 *            unique id inside its thread pool)
	 * @param pool
	 *            - the thread pool which owns this processor
	 */
	/* package */ Processor(int id, WorkStealingThreadPool pool) {
		this.id = id;
		this.pool = pool;
	}

	/** run method. Run the tasks on the threaed's queue, and steal if possible.
	* @throws - {@link InterruptedException} if the thread is interrupted.
	*/
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				int version=pool.getVM().getVersion();
				runMyTasks(); // 1
				steal(version); // 2
			}
		} catch (InterruptedException e) {

		}
	}

	/** Steal from other queue method
	* @param version - the version of the original Version Monitor
	* @throws - {@link InterruptedException} if the thread is interrupted.
	*/
	void steal(int version) throws InterruptedException {
	//	int version = pool.getVM().getVersion(); //3
		if (!stealFromVictims()) { // If I didn't find a victim, i'm
			// going to sleep
			//System.out.println("Going to sleep at Procerssor::steal");
			pool.getVM().await(version); //3
			// If there is a victim to steal from
		}
	}

	/** Steal from other queue method. This function locates a victim which has more then 0 tasks in his queue.
 	* @return - true if it is possible to steal and the function has done it.
	*/
	boolean stealFromVictims() {
		int placeForSearch = ((id + 1) % (pool.getQueuesArray().length));
		while (placeForSearch != id) {
			int howMany = pool.getQueuesArray()[placeForSearch].size() / 2;
			if (pool.getQueuesArray()[placeForSearch].size() > 0 && stealFromVictim(placeForSearch, howMany))
				return true;
			else
				placeForSearch = ((placeForSearch + 1) % (pool.getQueuesArray().length));
		}
		return false;
	}

	/** Steal from other queue method. 	// This function steals one task from a given victim's queue.
	* @param - int VictimID - a possible queue to steal one task from it.
 	* @return - true if it is possible to steal and the function has done it.
	*/
	boolean stealOneTask(int VictimID) {
		Task<?> t = pool.getQueuesArray()[VictimID].pollLast();
		if (t != null) {
			pool.getQueuesArray()[id].addFirst(t);
			return true;
		}
		return false;
	}

	/** Steal from other queue method. 	This function steals "HowMany" tasks from the VictimID's queue
	* @param - int VictimID, int HowMany
 	* @return - true if it is possible to steal and the function has done it.
	*/
	boolean stealFromVictim(int VictimID, int HowMany) {
		int stole = 0;
		while (HowMany > 0 && pool.getQueuesArray()[VictimID].size() > 0) {
			if(!stealOneTask(VictimID)) { break; }
			HowMany--;
			stole++;
		}

		return stole != 0;
	}

	/**
	 * While I have tasks in my Queue, i'll handle them.
	 */
	void runMyTasks() {
		Task<?> t = pool.getQueuesArray()[id].poll();
		while (t != null) {
			t.handle(this);
			t = pool.getQueuesArray()[id].poll();
		}
	}


	/**
	 * Getter for the pool
	 */
	WorkStealingThreadPool getPool() {
		return pool;
	}


	/**
	 * Getter for the ProcessorID
	 */
	int getProcessorID() {
		return id;
	}

}