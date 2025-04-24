package GUI;

import BUSINESS_LOGIC.SelectionPolicy;
import BUSINESS_LOGIC.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputData extends JFrame implements ActionListener {

    JLabel title1 = new JLabel("Enter number of clients:");
    JLabel title2 = new JLabel("Enter number of queues:");
    JLabel title3 = new JLabel("Enter simulation interval:");
    JLabel title4 = new JLabel("Enter minimum and maximum arrival time:");
    JLabel title5 = new JLabel("Enter minimum and maximum service time:");
    JLabel title6 = new JLabel("Select client queueing method:");
    JLabel title7 = new JLabel("Enter milliseconds for each simulation step:");

    JButton button =new JButton("Check Data and run");
    JComboBox<String> comboBox;
    JComboBox<String> ending;

    JTextField clients = new JTextField("10");
    JTextField queues = new JTextField("3");
    JTextField simulation_interval = new JTextField("60");
    JTextField min_arrival = new JTextField("1");
    JTextField max_arrival = new JTextField("3");
    JTextField min_service = new JTextField("1");
    JTextField max_service = new JTextField("3");
    JTextField milliseconds = new JTextField("500");

    int clientNum;
    int queueNum;
    int simInterval;
    int minArrival;
    int maxArrival;
    int minService;
    int maxService;
    int mili;

    public InputData(){

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        clients.setMaximumSize(new Dimension(200, 30));
        queues.setMaximumSize(new Dimension(200, 30));
        simulation_interval.setMaximumSize(new Dimension(200, 30));
        min_arrival.setMaximumSize(new Dimension(200, 30));
        max_arrival.setMaximumSize(new Dimension(200, 30));
        min_service.setMaximumSize(new Dimension(200, 30));
        max_service.setMaximumSize(new Dimension(200, 30));
        milliseconds.setMaximumSize(new Dimension(200, 30));


        Font labelFont = new Font(null, Font.PLAIN, 16);
        title1.setFont(labelFont);
        title2.setFont(labelFont);
        title3.setFont(labelFont);
        title4.setFont(labelFont);
        title5.setFont(labelFont);
        title6.setFont(labelFont);
        title7.setFont(labelFont);


        button.setPreferredSize(new Dimension(50, 50));
        button.setFocusable(false);

        comboBox = new JComboBox<>(new String[]{"SHORTEST_QUEUE","SHORTEST_TIME"});
        comboBox.addActionListener(this);
        comboBox.setMaximumSize(new Dimension(200, 30));

        ending = new JComboBox<>(new String[]{"QUEUE EMPTY","TIME"});
        ending.addActionListener(this);
        ending.setMaximumSize(new Dimension(200, 30));

        title1.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        title3.setAlignmentX(Component.CENTER_ALIGNMENT);
        title4.setAlignmentX(Component.CENTER_ALIGNMENT);
        title5.setAlignmentX(Component.CENTER_ALIGNMENT);
        title6.setAlignmentX(Component.CENTER_ALIGNMENT);
        title7.setAlignmentX(Component.CENTER_ALIGNMENT);
        clients.setAlignmentX(Component.CENTER_ALIGNMENT);
        queues.setAlignmentX(Component.CENTER_ALIGNMENT);
        simulation_interval.setAlignmentX(Component.CENTER_ALIGNMENT);
        min_arrival.setAlignmentX(Component.CENTER_ALIGNMENT);
        max_arrival.setAlignmentX(Component.CENTER_ALIGNMENT);
        min_service.setAlignmentX(Component.CENTER_ALIGNMENT);
        min_service.setAlignmentX(Component.CENTER_ALIGNMENT);
        max_service.setAlignmentX(Component.CENTER_ALIGNMENT);
        milliseconds.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        ending.setAlignmentX(Component.CENTER_ALIGNMENT);


        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(title1);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(clients);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(title2);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(queues);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(title3);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(simulation_interval);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(title4);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(min_arrival);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(max_arrival);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(title5);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(min_service);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(max_service);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(title6);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(comboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(ending);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(title7);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(milliseconds);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        button.addActionListener(this);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("Input Data");
        this.setSize(600,1000);
        this.add(panel);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
               System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button){
            if(!check_data()){
                clients.setText("");queues.setText("");
                simulation_interval.setText("");min_arrival.setText("");
                max_arrival.setText("");min_service.setText("");max_service.setText("");milliseconds.setText("");
            }
            else {

                SimulationManager simulationManager = new SimulationManager(simInterval, queueNum, clientNum, maxService, minService,
                        minArrival, maxArrival, comboBox.getSelectedItem().toString(),ending.getSelectedItem().toString(),mili);

                Thread thread = new Thread(simulationManager); /// one thread for sim manager logic , inside it will launch Q threads of service queues
                thread.start();

                new SimulationFrame(simulationManager); /// Event Dispatch Thread is running the GUI updates and frame thanks to the Timer that runs at a constant interval

                this.dispose();
            }

        }
    }

    private boolean check_data(){
        try {

            if (clients.getText().isEmpty() ||
                    queues.getText().isEmpty() || simulation_interval.getText().isEmpty() ||
                    min_arrival.getText().isEmpty() || max_arrival.getText().isEmpty() ||
                    min_service.getText().isEmpty() || max_service.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

             clientNum = Integer.parseInt(clients.getText());
             queueNum = Integer.parseInt(queues.getText());
             simInterval = Integer.parseInt(simulation_interval.getText());
             minArrival = Integer.parseInt(min_arrival.getText());
             maxArrival = Integer.parseInt(max_arrival.getText());
             minService = Integer.parseInt(min_service.getText());
             maxService = Integer.parseInt(max_service.getText());
             mili=Integer.parseInt(milliseconds.getText());

            if (clientNum <= 0 || queueNum <= 0 || simInterval <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter positive numbers", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(mili<0){
                JOptionPane.showMessageDialog(this, "Please enter positive milliseconds", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (minArrival > maxArrival) {
                JOptionPane.showMessageDialog(this, "Minimum arrival time must be <= maximum arrival time.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (minService > maxService) {
                JOptionPane.showMessageDialog(this, "Minimum service time must be <= maximum service time.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

}

