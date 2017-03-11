package bgu.spl.a2;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {
	private int myVersion;

	/** Constructor - initialize myVersion to 0.
	*/
	public VersionMonitor() {
		myVersion = 0;
	}

	/** Setter for myVersion
	*/
	synchronized void setVersion(int version) {
		myVersion = version;
	}

	/** Getter for myVersion
	*/
	synchronized int getVersion() {
		return myVersion;
	}

	/** When there's a new task, increase the Version Monitor. 
	*/
	public synchronized void inc() {
		// t1 starts
		myVersion = myVersion + 1;
		// t1 pauses, t2
		notifyAll();
	}

	/** When there's a new task, increase the Version Monitor. 
	* @throws InterruptedException a thread was interrupted.
	*/
	public synchronized void await(int version) throws InterruptedException {
		while (myVersion == version) {
			wait();
		}
	}
}