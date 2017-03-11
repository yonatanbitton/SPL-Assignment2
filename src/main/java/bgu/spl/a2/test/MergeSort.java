/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

	private final int[] A;

	public MergeSort(int[] A) {
		this.A = A;
	}

	@Override
	protected void start() {
		//System.out.println ("Print4Assaf " + Thread.currentThread().getId());
		if (A.length <= 1)
			complete(A);
		else {
			ArrayList<Task<int[]>> tasks = new ArrayList<>();
			int[] A1 = new int[A.length / 2 + (A.length % 2)];
			int[] A2 = new int[(A.length / 2)];
			
			for (int i = 0; i < A1.length; i++) {
				A1[i] = A[i + (A.length / 2)];
			}
			
			for (int i = 0; i < A2.length; i++) {
				A2[i] = A[i];
			}
			
			MergeSort LeftSide = new MergeSort(A1);
			MergeSort RightSide = new MergeSort(A2);
			tasks.add(LeftSide);
			tasks.add(RightSide);
			spawn(LeftSide, RightSide);
			
			whenResolved(tasks, () -> {
// 				int size = tasks.get(0).getResult().get().length;
//				size += tasks.get(1).getResult().get().length;
				int[] sortedArray = new int[A.length];// = new int[];
				int counterA = 0, counterB = 0, ind = 0;

				while (counterA < tasks.get(0).getResult().get().length
						& counterB < tasks.get(1).getResult().get().length) {
					if (tasks.get(0).getResult().get()[counterA] < tasks.get(1).getResult().get()[counterB]) {
						sortedArray[ind] = tasks.get(0).getResult().get()[counterA];
						counterA++;
					} else {
						sortedArray[ind] = tasks.get(1).getResult().get()[counterB];
						counterB++;
					}
					ind++;
				}
				for (int i = counterA; i < tasks.get(0).getResult().get().length; i++) {
					sortedArray[ind] = tasks.get(0).getResult().get()[i];
					ind++;
				}
				for (int j = counterB; j < tasks.get(1).getResult().get().length; j++) {
					sortedArray[ind] = tasks.get(1).getResult().get()[j];
					ind++;
				}
				complete(sortedArray);
			});
		}
	}

	public static void main(String[] args) throws InterruptedException {
		WorkStealingThreadPool pool = new WorkStealingThreadPool(20);
		pool.start();

		for (int i=0; i<10; i++){
		// System.out.println("1");
		int n = 10000;
		int[] A = new Random().ints(n).toArray();
		// System.out.println("2");

		MergeSort task = new MergeSort(A);
		// System.out.println("3");

		CountDownLatch l = new CountDownLatch(1);
		// System.out.println("4");

		// System.out.println("5");

		pool.submit(task);
		// System.out.println("6");
		task.getResult().whenResolved(() -> {
			//System.out.println("7");
			// warning - a large print!! - you can remove this line if you wish
			System.out.println(Arrays.toString(task.getResult().get()));
			System.out.println("Finished");
			l.countDown();
		});
		//System.out.println("After when resolved");

		l.await();
		//System.out.println("After l.await()");

		System.out.println("Finished 2 ");

		System.out.println("After shutdown");
		System.out.println(i+ "THE PRINT NUMBERRRRRR");

	}
		pool.shutdown();


	}

}