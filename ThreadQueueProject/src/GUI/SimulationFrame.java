package GUI;

import BUSINESS_LOGIC.Scheduler;
import BUSINESS_LOGIC.SimulationManager;
import MODEL.Server;
import MODEL.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SimulationFrame {

    private JFrame frame = new JFrame("Simulation Frame");
    private final JPanel leftPanel;
    private final JTextArea rightTextArea;
    private final int servers_size;

    private SimulationManager simulationManager;
    private Timer timer;

    private ActionListener actionListener = new ActionListener() { /// run action listener on different thread

        @Override
        public void actionPerformed(ActionEvent e) {
            if (simulationManager.stop) { /// signal from sim. manager
                timer.stop();
            }
            update(); /// each 10 millisecond update , can be changed
        }
    };

    public SimulationFrame(SimulationManager simulationManager) {


        this.simulationManager = simulationManager;
        this.servers_size = simulationManager.numberOfServers;

        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(960, 1080);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.gray);

        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane.setPreferredSize(new Dimension(700, 1080));

        rightTextArea = new JTextArea();
        rightTextArea.setEditable(false);
        JScrollPane rightScrollPane = new JScrollPane(rightTextArea);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setPreferredSize(new Dimension(300, 1080));

        frame.add(leftScrollPane, BorderLayout.CENTER);
        frame.add(rightScrollPane, BorderLayout.EAST);

        timer = new Timer(10, actionListener);
        timer.setInitialDelay(0);
        timer.start();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        frame.setLocation(screenWidth / 2, 0); /// centre the frame so the console`s check text is visible

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        initializeQueuePanels(); /// at start init frame then update using timer
        frame.setVisible(true);
    }
    private void initializeQueuePanels() {
        for (int i = 0; i < servers_size; i++) {

            JPanel queuePanel = new JPanel();
            queuePanel.setLayout(new BorderLayout());
            queuePanel.setBorder(BorderFactory.createTitledBorder("Queue " + (i + 1)));
            queuePanel.setBackground(Color.lightGray);

            JTextArea queueTextArea = new JTextArea();
            queueTextArea.setEditable(false);
            queueTextArea.setRows(5); /// minimize the space

            JScrollPane queueScrollPane = new JScrollPane(queueTextArea);
            queueScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            queueScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            queuePanel.add(queueScrollPane, BorderLayout.CENTER);
            queuePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            queuePanel.putClientProperty("queueTextArea", queueTextArea);

            leftPanel.add(queuePanel);
        }

        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
    private void update() {

        StringBuilder bigQueueContent = new StringBuilder("Waiting Tasks:\n");
        Scheduler scheduler = simulationManager.scheduler;
        List<Task> snapshot = simulationManager.getGeneratedTasksSnapshot();

        for (Task task : snapshot) {
            bigQueueContent.append(task.toString()).append("\n"); /// right panel having waiting clients
        }
        rightTextArea.setText(bigQueueContent.toString());

        List<Server> servers = scheduler.getServers();
        Component[] queuePanels = leftPanel.getComponents();

        for (int i = 0; i < servers.size(); i++) {

            if (!(queuePanels[i] instanceof JPanel queuePanel)) continue;

            Server server = servers.get(i);
            JTextArea queueTextArea = (JTextArea) queuePanel.getClientProperty("queueTextArea");

            StringBuilder queueContent = new StringBuilder();

            for (Task task : server.getQueueSnapshot()) {
                queueContent.append(task.toString()).append("\n");
            }
            queueTextArea.setText(queueContent.toString());
        }

        leftPanel.revalidate();
        leftPanel.repaint(); /// ensure proper update on frame
    }

}
