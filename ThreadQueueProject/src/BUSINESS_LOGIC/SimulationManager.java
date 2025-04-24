package BUSINESS_LOGIC;

import MODEL.LogFileWriter;
import MODEL.Server;
import MODEL.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;

public class SimulationManager implements Runnable {

    public int numberOfServers;
    public volatile boolean stop = false;

    private AverageNumbersClass averageNumbersClass;
    public Scheduler scheduler;
    private String ending;
    private int numberOfClients;
    private int timeLimit;
    private int miliseconds;
    private int currentTime = 0;
    private SelectionPolicy selectionPolicy;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int minArrivalTime;
    private int maxArrivalTime;

    private final List<Task> generatedTasks;
    private final List<Task> copyGeneratedTasks;


    public SimulationManager(int simInterval, int queueNum, int clientNum, int maxService, int minService, int minArrival, int maxArrival, String selectionPolicyStr,String ending, int mili) {

        initializeSimulation(simInterval, queueNum, clientNum, maxService, minService, minArrival, maxArrival, selectionPolicyStr,ending,mili);
        generatedTasks = new ArrayList<>();

        generateNRandomTasks();

        averageNumbersClass = AverageNumbersClass.getInstance();
        copyGeneratedTasks = new ArrayList<>(generatedTasks); ///shallow copy of tasks to check later avg wait
        averageNumbersClass.setCopyGeneratedQueue(copyGeneratedTasks);

        scheduler = new Scheduler(queueNum, selectionPolicy, miliseconds);
    }
    private void initializeSimulation(int simInterval, int queueNum, int clientNum, int maxService, int minService, int minArrival, int maxArrival, String selectionPolicyStr,String ending, int mili) {
        this.timeLimit = simInterval;
        this.numberOfServers = queueNum;
        this.numberOfClients = clientNum;
        this.maxProcessingTime = maxService;
        this.minProcessingTime = minService;
        this.minArrivalTime = minArrival;
        this.maxArrivalTime = maxArrival;
        this.selectionPolicy = SelectionPolicy.valueOf(selectionPolicyStr);
        this.miliseconds = mili;
        this.ending=ending;
    }
    private void generateNRandomTasks() {
        Random rand = new Random();

        for (int i = 0; i < numberOfClients; i++) {

            int serviceTime = rand.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
            int arrivalTime = rand.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            generatedTasks.add(new Task(i + 1, arrivalTime, serviceTime));
        }

        generatedTasks.sort((Task t1, Task t2) -> t1.getArrivalTime() - t2.getArrivalTime());

    }
    public  synchronized  List<Task>  getGeneratedTasksSnapshot() {
        return new ArrayList<>(generatedTasks); /// copy , avoid sync issue in frame update
    }

    @Override
    public void run() {
        System.out.println("Simulation started");

        if (generatedTasks.isEmpty() && ending.equals("QUEUE EMPTY")) {
            stop = true;
        }

        while ((currentTime < timeLimit) && !stop) {

            Iterator<Task> iter = generatedTasks.iterator();
            ArrayList<Task> toBeAdded = new ArrayList<>();
            while (iter.hasNext()) {
                Task task = iter.next();
                if (task.getArrivalTime() == currentTime) {
                    toBeAdded.add(task);
                    iter.remove();
                } else {
                    break;
                }
            }
            try {
                scheduler.getBarrier().await(); /// display/write/add after done processing queues
                for (Task task : toBeAdded) {
                    scheduler.dispatchTask(task);
                }
                LogFileWriter.getInstance().writeLog(textWrite(currentTime, generatedTasks, scheduler).toString());

                if (generatedTasks.isEmpty() && ending.equals("QUEUE EMPTY")) {
                    stop = true;
                    break;
                } /// end sim

                System.out.println(currentTime + "| Processing ...");
                currentTime++;
                Thread.sleep(miliseconds);

            } catch (IOException | InterruptedException | BrokenBarrierException e) {
                scheduler.shutdownScheduler();
                Thread.currentThread().interrupt();
                break;
            }
        }
        try {
            stop = true;
            scheduler.shutdownScheduler();
            System.out.println("Simulation ended");
            LogFileWriter.getInstance().writeLog("Simulation ended\n\n" + writeEndFile());
            averageNumbersClass.endMessage();
            LogFileWriter.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private StringBuilder textWrite(int currentTime, List<Task> generatedTasks, Scheduler scheduler) {
        StringBuilder text = new StringBuilder();
        text.append(currentTime).append("\n");
        int sumPeakHour = 0;

        text.append("Waiting clients: ");
        for (Task task : generatedTasks) {
            text.append(task.toString()).append("; ");
        }

        List<Server> servers = scheduler.getServers();
        int i = 1;

        text.append("\n");
        for (Server server : servers) {

            sumPeakHour += server.getQueueSize().get();
            text.append("Queue ").append(i).append(" size: " + server.getQueueSize() + " wait " + server.getWaitingPeriod()).append(" : ");

            BlockingQueue<Task> snapshot = server.getQueueSnapshot();
            if (snapshot.isEmpty()) {
                text.append("empty");
            } else {
                for (Task task : snapshot) {
                    text.append(task.toString()).append("; ");
                    task.incTimeInQueue();
                }
            }
            text.append("\n");
            i++;
        }
        if (sumPeakHour > averageNumbersClass.getHeavy_hour()) {
            averageNumbersClass.setHeavy_hour(sumPeakHour);
        }
        return text;
    }
    private StringBuilder writeEndFile() {
        StringBuilder sb = new StringBuilder();

        sb.append("Peak hour: ").append(averageNumbersClass.getHeavy_hour()).append("\n");
        sb.append("Avg wait: ").append(averageNumbersClass.AvgWaitTime()).append("\n");
        sb.append("Avg service: ").append(averageNumbersClass.AvgServTime()).append("\n");
        sb.append("Served,their serv time: ").append(averageNumbersClass.getServedClients()).append(" ").append(averageNumbersClass.getCntService()).append("\n");

        return sb;
    }
}