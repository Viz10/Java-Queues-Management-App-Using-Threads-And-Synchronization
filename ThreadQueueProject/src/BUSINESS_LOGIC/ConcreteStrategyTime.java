package BUSINESS_LOGIC;

import MODEL.Server;
import MODEL.Task;
import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task task) { /// add task to service queue with min waiting time
        Server result=null;
        int min_time=Integer.MAX_VALUE;
        for(Server server : servers){
            int time = server.getWaitingPeriod().get();
            if(time<min_time){
                min_time=time;
                result=server;
            }
        }
        if(result!=null)
            result.addTask(task);
    }
}
