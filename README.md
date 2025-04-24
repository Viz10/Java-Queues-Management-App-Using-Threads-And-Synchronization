# Java-Queues-Management-App-Using-Threads-And-Synchronization
Implementation of a queues management application which assigns clients to queues such that the waiting time is minimized
The queues management application should simulate (by defining a simulation time 𝑡𝑠𝑖𝑚𝑢𝑙𝑎𝑡𝑖𝑜𝑛) a
series of N clients arriving for service, entering Q queues, waiting, being served and finally leaving
the queues. All clients are generated when the simulation is started, and are characterized by three
parameters: ID (a number between 1 and N), 𝑡𝑎𝑟𝑟𝑖𝑣𝑎𝑙 (simulation time when they are ready to enter
the queue) and 𝑡𝑠𝑒𝑟𝑣𝑖𝑐𝑒 (time interval or duration needed to serve the client; i.e. waiting time when
the client is in front of the queue). The application tracks the total time spent by every client in the
queues and computes the average waiting time. Each client is added to the queue with the minimum
waiting time when its 𝑡𝑎𝑟𝑟𝑖𝑣𝑎𝑙 time is greater than or equal to the simulation time (𝑡𝑎𝑟𝑟𝑖𝑣𝑎𝑙 ≥
𝑡𝑠𝑖𝑚𝑢𝑙𝑎𝑡𝑖𝑜𝑛). 
