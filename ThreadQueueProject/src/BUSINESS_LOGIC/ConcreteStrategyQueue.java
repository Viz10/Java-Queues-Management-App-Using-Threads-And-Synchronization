package BUSINESS_LOGIC;

import MODEL.Server;
import MODEL.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteStrategyQueue implements Strategy{

    @Override
    public void addTask(List<Server> servers, Task task) { /// adds to the queue with min nr of clients
        Server result=null;
        int min_size=Integer.MAX_VALUE;
        for(Server server : servers){
                AtomicInteger var = server.getQueueSize();
                int size=var.get();
                if(size<min_size){
                    min_size=size;
                    result=server;
                }
        }
        if(result!=null)
            result.addTask(task); /// access common resource with server thread
    }
}
