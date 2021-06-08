import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    static int factorialResult;


    public static void main(String[] args) throws InterruptedException {
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//
//        for (int i = 0; i < 10; i++) {
//        executorService.execute(new RunnableImpl1000());
//        }
//        System.out.println("Main");
//        executorService.shutdown();
//        executorService.awaitTermination(5, TimeUnit.SECONDS);


//        ScheduledExecutorService scheduledExecutorService =
//                Executors.newScheduledThreadPool(1);
////
//        for (int i = 0; i < 10; i++) {
//            scheduledExecutorService.execute(new RunnableImpl1200());
//        }

//        scheduledExecutorService.schedule(new RunnableImpl1200(), 3, TimeUnit.SECONDS );
//        scheduledExecutorService.shutdown();

        // scheduledExecutorService.scheduleAtFixedRate(new RunnableImpl1200(), 3,1, TimeUnit.SECONDS);

//        scheduledExecutorService.scheduleWithFixedDelay(
//                new RunnableImpl1200(), 3, 1, TimeUnit.SECONDS);


//        Thread.sleep(20000);
//        scheduledExecutorService.shutdown();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Factorial factorial = new Factorial(5);

        executorService.execute(factorial);
        ;
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println(factorialResult);
    }
}

class CallableFactorial {
    static int factorialResult;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Factorial2 factorial2 = new Factorial2(3);
        Future<Integer> future = executorService.submit(factorial2);

        try {

            System.out.println(future.isDone());

            System.out.println("Trying to get result");
            factorialResult = future.get();
            System.out.println(future.isDone());
            System.out.println("Got result ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(e.getCause());
        } finally {
            executorService.shutdown();
        }
        System.out.println(factorialResult);
    }
}

class Factorial2 implements Callable<Integer> {
    int f;


    public static void testingNewKeyboard() {
        System.out.println("And i kinda like it :D ");
        System.out.println("Lets write some text to " +
                "keep improving and its getting better and better :D");

    }

    public Factorial2(int f) {
        this.f = f;
    }

    @Override
    public Integer call() throws Exception {
        if (f <= 0) {
            throw new Exception("Wrong number");
        }
        int result = 1;
        for (int i = 1; i <= f; i++) {
            result *= i;
            Thread.sleep(1000);
        }
        return result;
    }
}

class Factorial implements Runnable {
    int f;

    public Factorial(int f) {
        this.f = f;
    }

    @Override
    public void run() {
        if (f <= 0) {
            System.out.println("Wrong number");
            return;
        }
        int result = 1;
        for (int i = 1; i <= f; i++) {
            result *= i;
        }
        Main.factorialResult = result;
    }
}

class RunnableImpl1000 implements Runnable {


    @Override
    public void run() {
        System.out.println("Current thread begins " + Thread.currentThread().getName());
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("Current thread ends " + Thread.currentThread().getName());
    }
}

class RunnableImpl1200 implements Runnable {


    @Override
    public void run() {
        System.out.println("Current thread begins " + Thread.currentThread().getName());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Current thread ends " + Thread.currentThread().getName());
    }
}

class SumNumbers {
    private static long value = 1_000_000_000L;

    private static long sum = 0;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Long>> futureResults = new ArrayList<>();
        long valueDividedBy10 = value / 10;
        for (int i = 0; i < 10; i++) {
            long from = valueDividedBy10 * i + 1;
            long to = valueDividedBy10 * (i + 1);
            PartialSum task = new PartialSum(from, to);
            Future<Long> futurePartSum = executorService.submit(task);

            futureResults.add(futurePartSum);
        }
        for (Future<Long> result : futureResults) {
            sum += result.get();
        }
        System.out.println("Total sum: " + sum);
    }

}

class PartialSum implements Callable<Long> {
    long from;
    long to;
    long localSum;

    public PartialSum(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public Long call() {
        for (long i = from; i <= to; i++) {
            localSum += i;
        }
        System.out.println("Sum from " + from + " to " + to + " = " + localSum);
        return localSum;
    }
}

class SemaphoreExmpl {
    public static void main(String[] args) {
        Semaphore callBox = new Semaphore(2);

        new Person("P1", callBox);
        new Person("P2", callBox);
        new Person("P3", callBox);
        new Person("P4", callBox);
        new Person("P5", callBox);

    }
}

class Person extends Thread {
    String name;
    private Semaphore callBox;

    public Person(String name, Semaphore callBox) {
        this.name = name;
        this.callBox = callBox;
        this.start();
    }

    public void run() {
        try {
            System.out.println(name + " waiting ");

            callBox.acquire();

            System.out.println(name + " using phone");

            sleep(2000);

            System.out.println(name + " done call");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            callBox.release();
        }
    }
}
