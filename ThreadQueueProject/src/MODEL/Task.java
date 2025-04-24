package MODEL;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {

    private int id=0;
    private int arrivalTime=0;
    private int serviceTime=0;
    private final int serviceTimeUnchanged;
    private AtomicInteger timeInQueue;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.serviceTimeUnchanged = serviceTime;
        this.timeInQueue = new AtomicInteger(0);
    }
    
    public int getServiceTimeUnchanged() {
        return serviceTimeUnchanged;
    }
    public void decServiceTime(){
        serviceTime--;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public void incTimeInQueue(){
        timeInQueue.incrementAndGet();
    }
    public int getTimeInQueue(){
        return timeInQueue.get();
    }

    @Override
    public String toString() {
        return "(id: "+id+",arrival: "+arrivalTime+",service: "+serviceTime+")";
    }
}
