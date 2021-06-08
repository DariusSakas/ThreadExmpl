import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        } finally {
            callBox.release();
        }
    }
}

class CountDownLatchEx {
    static CountDownLatch countDownLatch = new CountDownLatch(3);

    public static void isStaffInMarktet() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Market staff came to work");
        countDownLatch.countDown();
        System.out.println("countDownLatch: " + countDownLatch.getCount());

    }

    public static void isMarketReady() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("Market getting prepared");
        countDownLatch.countDown();
        System.out.println("countDownLatch: " + countDownLatch.getCount());

    }

    public static void isMarketOpen() throws InterruptedException {
        Thread.sleep(4000);
        System.out.println("Market gets opened");
        countDownLatch.countDown();
        System.out.println("countDownLatch: " + countDownLatch.getCount());

    }

    public static void main(String[] args) throws InterruptedException {
        new Friend("F1", countDownLatch);
        new Friend("F2", countDownLatch);
        new Friend("F3", countDownLatch);
        new Friend("F4", countDownLatch);
        new Friend("F5", countDownLatch);

        isStaffInMarktet();
        isMarketReady();
        isMarketOpen();
    }
}

class Friend extends Thread {
    String name;
    CountDownLatch countDownLatch;

    public Friend(String name, CountDownLatch countDownLatch) {
        this.name = name;
        this.countDownLatch = countDownLatch;
        this.start();
    }

    public void run() {
        try {
            countDownLatch.await();
            System.out.println("buying something, person: " + name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class ExchangerExmpl {

    public static void main(String[] args) {
        Exchanger<Action> exchanger = new Exchanger<>();
        List<Action> f1Actions = new ArrayList<>();
        f1Actions.add(Action.SCISSOR);
        f1Actions.add(Action.PAPER);
        f1Actions.add(Action.SCISSOR);

        List<Action> f2Actions = new ArrayList<>();
        f2Actions.add(Action.PAPER);
        f2Actions.add(Action.ROCK);
        f2Actions.add(Action.ROCK);

        new SomeNewFriend("F1", f1Actions, exchanger);
        new SomeNewFriend("F2", f2Actions, exchanger);
    }
}

enum Action {
    ROCK, SCISSOR, PAPER;
}

class SomeNewFriend extends Thread {
    private String name;
    private List<Action> myActions;
    private Exchanger<Action> exchanger;

    public SomeNewFriend(String name, List<Action> myActions, Exchanger<Action> exchanger) {
        this.name = name;
        this.myActions = myActions;
        this.exchanger = exchanger;
        this.start();
    }

    private void whoWins(Action myAction, Action friendsAction) {
        if ((myAction == Action.ROCK && friendsAction == Action.SCISSOR)
                || (myAction == Action.SCISSOR && friendsAction == Action.PAPER)
                || (myAction == Action.PAPER && friendsAction == Action.ROCK)) {
            System.out.println("WINNER: " + name);
        }
    }

    public void run() {
        Action reply;
        for (Action action : myActions) {
            try {
                reply = exchanger.exchange(action);
                whoWins(action, reply);
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class AtomicIntegerExmpl {
    // static  int counter = 0;

    static AtomicInteger counter = new AtomicInteger(0); //0 by default not necessary

    public static void increment() {
        //  counter++;
        counter.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new MyRunnableImplementation());
        Thread thread2 = new Thread(new MyRunnableImplementation());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(counter);
    }
}

class MyRunnableImplementation implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            AtomicIntegerExmpl.increment();
        }
    }
}

class SynchronizedcollectionExmpl {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer> source = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            source.add(i);
        }

        // ArrayList<Integer > target = new ArrayList<>();
        List<Integer> syncList =
                Collections.synchronizedList(new ArrayList<>());
        Runnable runnable = () -> {
            syncList.addAll(source);
        };

        Thread thread1 = new Thread(runnable);

        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(syncList);
    }
}

class SynchronizedcollectionExmpl2 {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer> source = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            source.add(i);
        }

        List<Integer> synchedList = Collections.synchronizedList(source);

        Runnable runnable1 = () -> {
            Iterator<Integer> iterator = synchedList.iterator();

            synchronized (synchedList) {
                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                }
            }
        };
        Runnable runnable2 = () -> {
            synchedList.remove(10);

        };

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(source);
    }

}

class ConcurrentHasMapExmpl {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
        map.put(1, "P1");
        map.put(2, "P2");
        map.put(3, "P3");
        map.put(4, "P4");
        map.put(5, "P5");
        map.put(6, "P6");
        System.out.println(map);

        Runnable runnable1 = () -> {
            Iterator<Integer> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer i = iterator.next();
                System.out.println(i + " : " + map.get(i));
            }
        };

        Runnable runnable2 = () -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(7, "New Added Person");

        };
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(map);
    }
}

class CopyOnwriteArrayListEx {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<String> list = new ArrayList<>();
        list.add("P1");
        list.add("P2");
        list.add("P3");
        list.add("P4");
        list.add("P5");
        list.add("P6");

        System.out.println(list);
        Runnable runnable1 = () -> {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(iterator.hasNext());
            }
        };
        Runnable runnable2 = () -> {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.remove(4);
            list.add("Adding new P");

        };
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(list );
    }
}
