package bgu.spl.a2;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {
	private ArrayList<Thread> threadsArray;
	private ArrayList<Processor> processorsArray;
	private VersionMonitor VM;
	private ConcurrentLinkedDeque<Task<?>>[] queuesArray;
	private boolean isShutDown;
	// private ArrayList<Thread> threadsArray = new ArrayList<>();
	// private ArrayList<Processor> processorsArray = new ArrayList<>();
	// private VersionMonitor VM;
	// private ConcurrentLinkedQueue<Task<?>>[] queuesArray = new
	// ConcurrentLinkedQueue[0];
	// private ArrayList<ConcurrentLinkedQueue> queuesArray = new ArrayList<>();

	/**
	 * creates a {@link WorkStealingThreadPool} which has nthreads
	 * {@link Processor}s. Note, threads should not get started until calling to
	 * the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	//@SuppressWarnings("unchecked")
	public WorkStealingThreadPool(int nthreads) {
		threadsArray = new ArrayList<>(nthreads);
		processorsArray = new ArrayList<>(nthreads);
		queuesArray = new ConcurrentLinkedDeque[nthreads];
		for (int i = 0; i < nthreads; i++) {
			Processor p = new Processor(i, this);
			processorsArray.add(p);
			threadsArray.add(new Thread(p));
			queuesArray[i] = new ConcurrentLinkedDeque<>();
		}
		VM = new VersionMonitor();
		isShutDown = false;
	}

	/**
	 * submits a task to be executed by a processor belongs to this thread pool
	 *
	 * @param task
	 *            the task to execute
	 */
	public void submit(Task<?> task) {
		double tryRandomID = Math.random() * processorsArray.size();
		int randomID = ((int) tryRandomID);
		queuesArray[randomID].add(task);
		// System.out.println("The thread currently running " +
		// Thread.currentThread().getId());
		//System.out.println("~~~~~~~~~~~~~~~~Increased VM at pool::submit~~~~~~~~~~~~~~");
		VM.inc();
		// Array that hold the numbers of element in the queues.
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and wait
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 * @throws UnsupportedOperationException
	 *             if the thread that attempts to shutdown the queue is itself a
	 *             processor of this queue
	 */
	public void shutdown() throws InterruptedException {
		if (isShutDown)
			throw new InterruptedException("I was got shutdown");
		else
			isShutDown = true;
		for (int i = 0; i < threadsArray.size(); i++) {
			if (threadsArray.get(i) == Thread.currentThread())
				throw new InterruptedException("I can't shutdown myself!");
		}
		for (int i = 0; i < threadsArray.size(); i++)
			threadsArray.get(i).interrupt();
		for (int i = 0; i < threadsArray.size(); i++)
			threadsArray.get(i).join();
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for (int i = 0; i < threadsArray.size(); i++) {
			threadsArray.get(i).start();
			// System.out.println("Thread " + (i+1) + " is running");
		}
	}

	/**
	 * Getter for the queuesArray
	 */
	ConcurrentLinkedDeque<Task<?>>[] getQueuesArray() {
		return queuesArray;
	}

	/**
	 * Getter for the Version Monitor
	 */
	VersionMonitor getVM() {
		return VM;
	}

	/**
	 * This function adds Task to a given ID
//	 * @param ToWhodID - the queue to add the task to.
	 */
	void addTask(Task<?> taskToAdd, int ToWhomID) {
		queuesArray[ToWhomID].addFirst(taskToAdd);
		//System.out.println("Increase VM at addTask");
		VM.inc();
	}

}