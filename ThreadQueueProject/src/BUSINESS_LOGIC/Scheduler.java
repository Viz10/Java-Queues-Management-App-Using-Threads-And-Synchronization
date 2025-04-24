package BUSINESS_LOGIC;

import MODEL.Server;
import MODEL.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
    private final List<Server> servers;
    private final ExecutorService executorService;
    private final Strategy strategy;
    private final CyclicBarrier barrier;

    public Scheduler(int maxNoServers, SelectionPolicy policy, int milliseconds) {
        servers = new ArrayList<>();
        barrier = new CyclicBarrier(maxNoServers + 1);
        this.strategy = createStrategy(policy);
        executorService = Executors.newFixedThreadPool(maxNoServers);

        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(milliseconds);
            server.setBarrier(barrier);
            servers.add(server);
            executorService.submit(server);
        }
    }
    public void dispatchTask(Task t) {
        strategy.addTask(servers, t);
    }
    public List<Server> getServers() {
        return servers;
    }
    public CyclicBarrier getBarrier() {
        return barrier;
    } /// barrier used for sync
    public void shutdownScheduler() {
        executorService.shutdownNow(); /// attempt to terminate all service threads
    }
    private Strategy createStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            return new ConcreteStrategyQueue();
        }
        return new ConcreteStrategyTime();
    }
}