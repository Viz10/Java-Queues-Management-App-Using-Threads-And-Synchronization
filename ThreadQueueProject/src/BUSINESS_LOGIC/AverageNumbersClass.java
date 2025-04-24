package BUSINESS_LOGIC;

import MODEL.Task;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AverageNumbersClass {

    private static AverageNumbersClass instance = null;

    private int heavy_hour = 0;
    private AtomicInteger cntService;
    private AtomicInteger servedClients;
    private List<Task> generatedQueue;

    public void setCopyGeneratedQueue(List<Task> generatedQueue) {
        this.generatedQueue = generatedQueue;
    }
    public AtomicInteger getCntService() {
        return cntService;
    }
    public AtomicInteger getServedClients() {
        return servedClients;
    }
    public void incServedClients() {
        this.servedClients.incrementAndGet();
    }
    public void incCntService(int increment) {
        this.cntService.addAndGet(increment);
    }
    public void setHeavy_hour(int heavy_hour) {
        this.heavy_hour = heavy_hour;
    }
    private AverageNumbersClass() {
        cntService = new AtomicInteger(0);
        servedClients = new AtomicInteger(0);
    }
    public static AverageNumbersClass getInstance() {
        if (instance == null) {
            instance = new AverageNumbersClass();
        }
        return instance;
    }

    ////////////////////////////////////////////

    public double AvgServTime() {
        double avgServiceTime = (double) cntService.get() / (double) servedClients.get();
        return Math.round(avgServiceTime * 100.0) / 100.0;
    }

    public synchronized double AvgWaitTime() {

        int sum = 0;
        int cnt = 0;

        for (Task task : generatedQueue) {
            if (task.getTimeInQueue() != 0) {
                sum += task.getTimeInQueue();
                cnt++;
            }
        }
        if (cnt == 0) {
            return 0.0;
        }

        double rez = (double) sum / (double) cnt;
        return Math.round(rez * 100.0) / 100.0;

    }

    public int getHeavy_hour() {
        return heavy_hour;
    }

    public void endMessage() {

        StringBuilder sb = new StringBuilder();
        sb.append("Peak hour: ").append(getHeavy_hour()).append("\n");
        sb.append("Avg wait: ").append(AvgWaitTime()).append("\n");
        sb.append("Avg service: ").append(AvgServTime()).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());

    }
}

