/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.WorkStealingThreadPool;
///
import bgu.spl.a2.sim.conf.Request;
import bgu.spl.a2.sim.conf.Source;
import bgu.spl.a2.sim.tasks.WaveTask;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	public static Warehouse warehouse;
	static Source json;
	static Source realJAS;
	static WorkStealingThreadPool myPool;
	static ConcurrentLinkedQueue<Product> productsQueue = new ConcurrentLinkedQueue<>();
	static ConcurrentLinkedQueue<WaveTask> waveTasks = new ConcurrentLinkedQueue<WaveTask>();

	public Simulator(){}

	/**
	* Begin the simulation
	* Should not be called before attachWorkStealingThreadPool()
	*/

    public static ConcurrentLinkedQueue<Product> start() {
		addWaveTasks(); // waveTasks is the name of the collection
		submitNextWaveWhenTheCurrentIsResolved(); // productsQueue is the name of the queue to add the completed products to
	return productsQueue;
	}

    /**
	* This function submit wave, and moves to the next wave only if the submitted one was resolved. 
	*/
	static void submitNextWaveWhenTheCurrentIsResolved(){
		while (waveTasks.size()!=0) {
			//System.out.println("I'm at the " + iterations + " iteration");
			CountDownLatch latch=new CountDownLatch(1);
			WaveTask w = waveTasks.poll();
			Simulator.myPool.submit(w);
				Deferred<ArrayList<Product>> d = w.getResult(); // w extends tasks which returns Deferred at getResult
				// here only when the wave is reloved
				
				d.whenResolved(() -> {
					//System.out.println("Wave is resolved! " );
					while(d.get().size() > 0)
						productsQueue.add(d.get().remove(0));						
					latch.countDown();
				});
				try {
					latch.await();
					//System.out.println("WhatTheFuck1");
				} catch (InterruptedException e) {
					//System.out.println("WhatTheFuck2");
					return;} // After a thread was interrupted, he should stop.
			
				//System.out.println("Can go to the next wave ? ");
		}
		
	}

	/**
	* Adds waveTasks to the waveTasks field, this is a structure which holds the wave tasks
	*/
	static void addWaveTasks(){
		for (int i = 0; i < realJAS.waves.length; i++) {
			Request[] tmp;
			tmp = realJAS.waves[i];
			WaveTask w = new WaveTask(tmp, warehouse);
			waveTasks.add(w);
			// Don't proceed to the next wave until this wave is finished
		}
	}

	/**
	* attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	* @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
	*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){
		myPool=myWorkStealingThreadPool;
	}
	
	public static void main(String [] args) throws InterruptedException
	{
		BufferedReader reader = null;
		try {
		//System.out.println (args[0].length());
		reader = new BufferedReader(new FileReader(args[0]));
		Gson gson = new GsonBuilder().create();
		Source json = gson.fromJson(reader, Source.class);
		realJAS= new Source(json.getThreads(), json.getTools(), json.getPlans(), json.getRequests());
		Warehouse warehouse = new Warehouse(json.getPlans(), json.getTools());
		Simulator.warehouse=warehouse;
		WorkStealingThreadPool myWorkStealingThreadPool=new WorkStealingThreadPool(realJAS.getThreads());
		attachWorkStealingThreadPool(myWorkStealingThreadPool);
		System.out.println ("Flag 1");
		System.out.println(realJAS.getRequests()[0][0]);
		myWorkStealingThreadPool.start();
		ConcurrentLinkedQueue<Product> Output = start(); 
		Iterator<Product> it = Output.iterator();
		while (it.hasNext()){
			Product p=it.next();
			System.out.println(p);
		}
		System.out.println ("Flag 2");
		//int size = Output.size();
		FileOutputStream fout = new FileOutputStream("result.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(Output);
		oos.close();
		myWorkStealingThreadPool.shutdown();
			// TODO Auto-generated catch block
	
		// Read a wave from intputFile, Add a manufacturing task for all products in the current wave to the task queue.
		// Proceed to the next wave once all products in the current wave have been manufactured.	
		} catch (Exception e) {e.printStackTrace();
			System.out.println("Gotcha");}
	}

}