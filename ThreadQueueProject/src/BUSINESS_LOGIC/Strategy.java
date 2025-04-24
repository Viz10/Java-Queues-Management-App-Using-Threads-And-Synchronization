package BUSINESS_LOGIC;

import MODEL.Server;
import MODEL.Task;

import java.util.List;

public interface Strategy {
     void addTask(List<Server> servers, Task task);
}
