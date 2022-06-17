package kz.halykacademy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Scanner;

/**
 * Hello world!
 *
 */

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

    public static AtomicInteger atomicInteger = new AtomicInteger(0);

    static public void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(1);
        CountDownLatch forShowFinalSize = new CountDownLatch(1);//CountDownLatch чтобы вывести конечный размер нашего BlockingQueue
        BlockingQueue<String> blockingQueue  = new LinkedBlockingDeque<>(20);//Максимальное количество элементов

        Runnable fillQueue = ()->{
            System.out.println(Thread.currentThread().getName()+" STARTED TO FILL");
            try {
                addElement(blockingQueue);
                System.out.println("\n"+"######################################");
                System.out.println("INITIAL SIZE OF BlockingQueue IS : "+blockingQueue.size());
                count.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        Runnable read = ()->{
            try {
                count.await();
                System.out.println(Thread.currentThread().getName()+" STARTED");
                takeElement(blockingQueue);
                forShowFinalSize.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        //Runnable для того чтобы вывести конечный размер blockingQueue
        Runnable showFinalSize = ()->{
            try {
                forShowFinalSize.await();
                System.out.println("\n"+"####################################");
                System.out.println("FINAL SIZE OF BLOCKINGQUEUE IS : "+blockingQueue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(fillQueue).start();

        for (int i = 0; i <3 ; i++) {
            new Thread(read).start();
        }

        new Thread(showFinalSize).start();
    }

    synchronized static void addElement(BlockingQueue blockingQueue) throws InterruptedException {
        while (blockingQueue.remainingCapacity()>0){
            System.out.println(Thread.currentThread().getName()+" using blockingQueue & size : "+blockingQueue.size());
            blockingQueue.add("WORD #"+atomicInteger.incrementAndGet());
//            Thread.sleep(100);
        }
    }

    static void takeElement(BlockingQueue blockingQueue) throws InterruptedException {
        while (!blockingQueue.isEmpty()){
            System.out.println(Thread.currentThread().getName()+" TAKING "+blockingQueue.take());
//            Thread.sleep(200);
        }
    }
}
