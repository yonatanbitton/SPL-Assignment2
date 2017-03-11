package bgu.spl.a2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import bgu.spl.a2.test.MergeSort;

public class MatrixTest {

    // The complier expects me to create them as an instance of matrix test.
    // Why? Because in inner class, we have advantage of approaching the variables.
    // If I have Matrix Test code, I can approach SumMatrix variables.  (Instance of SumMatrix - I can approach from down to up)
    // If I have matrixTest one, I can approach it from SumMatrix. So when I create this variable it doresh from me for it static.
    public static class SumRow extends Task<Integer> {
        private int[][] array;
        private int r;

        public SumRow(int[][] array,int r) {
            this.array = array;
            this.r=r;
        }

        protected void start(){
            int sum=0;
            for(int j=0 ;j<array[0].length;j++)
                sum+=array[r][j];
            complete(sum);
        }

    }

    public static class SumMatrix extends Task<int[]>{
        private int[][] array;
        public SumMatrix(int[][] array) {
            this.array = array;
        }

        protected void start(){
            int sum=0;
            List<Task<Integer>> tasks = new ArrayList<>();
            int rows = array.length;
            for(int i=0;i<rows;i++){
                SumRow newTask=new SumRow(array,i);
                spawn(newTask);
                tasks.add(newTask);
            }
            whenResolved(tasks,()->{
                        int[] res = new int[rows];
                        for(int j=0; j< rows; j++){
                            res[j] = tasks.get(j).getResult().get();
                        }
                        complete(res);
                    }
            );
        }
    }



//
//    public static void main(String[] args) throws InterruptedException {
//        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
//        int[][] array = new int[5][10];
//        // some stuff - fill stuff in the matrix
//        for (int i=0; i<array[0].length; i++){
//            array[0][i]=1; // Sum is 10
//            array[1][i]=2; // Sum is 20
//            array[2][i]=3; // Sum is 30
//            array[3][i]=4; // Sum is 40
//            array[4][i]=5; // Sum is 50
//        }
//        SumMatrix myTask = new SumMatrix(array);
//        CountDownLatch l = new CountDownLatch(1); // 1
//        pool.start();
//        pool.submit(myTask);
//
//        //some stuff
//        // if myTask got result - activate the callback. Else, if no result yet, add it to the Deferred's callbacks[] field
//
//        myTask.getResult().whenResolved(() -> { // <<< This is the callback!
//            //warning - a large print!! - you can remove this line if you wish
//            System.out.println(Arrays.toString(myTask.getResult().get()));
//            l.countDown(); // 2
//        });
//        l.await(); // 3
//        pool.shutdown(); //stopping all the threads
//        System.out.println("Finished");
//
//    }
}
