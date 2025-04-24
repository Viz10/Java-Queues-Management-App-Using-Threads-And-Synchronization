package MODEL;

import BUSINESS_LOGIC.AverageNumbersClass;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CyclicBarrier;

public class Server implements Runnable {

    private final BlockingQueue<Task> queue;
    private final AtomicInteger waitingPeriod;
    private final AtomicInteger size;
    private final int milliseconds;
    private CyclicBarrier barrier;
    private AverageNumbersClass averageNumbersClass;
    /// Barrier for synchronization to write after decrementing the heads

    public Server(int milliseconds) {
        averageNumbersClass=AverageNumbersClass.getInstance();
        this.milliseconds = milliseconds;
        queue = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
        size = new AtomicInteger(0);
    }
    public synchronized void addTask(Task task) {
        queue.add(task);
        size.incrementAndGet();
        waitingPeriod.addAndGet(task.getServiceTime());
    }
    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }
    public synchronized BlockingQueue<Task> getQueueSnapshot() {
        return new LinkedBlockingQueue<>(queue);
    }
    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
    public AtomicInteger getQueueSize() {
        return size;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {

                Task task;
                synchronized (queue) { /// critical section
                    if (!queue.isEmpty()) {

                        task = queue.peek();
                        task.decServiceTime();
                        waitingPeriod.decrementAndGet();

                        if (task.getServiceTime() == 0) {
                            averageNumbersClass.incCntService(task.getServiceTimeUnchanged()); /// add to avg serv time
                            averageNumbersClass.incServedClients();
                            size.decrementAndGet();
                            queue.poll();
                        }
                    }
                }
                if (barrier != null) { /// only after decrement display , must sync with sim manager thread
                    barrier.await();
                }

                Thread.sleep(milliseconds); /// for each iteration peek , decrement it and when 0 poll

            } catch (Exception e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}