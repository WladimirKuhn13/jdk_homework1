package homework1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ServerWindow extends JFrame{
    private static final int WIDTH = 555;
    private static final int HEIGHT = 507;
    private ArrayList<ClientGUI> listOfClients = new ArrayList<ClientGUI>();
    private boolean isServerWorking = false;
    private String info = "";
    private File logFile = new File("log file.txt");
    private JTextArea textArea;
    private JButton buttonStart;
    private JButton buttonStop;

    public ServerWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);

        setTitle("Chat server");
        setResizable(false);

        buttonStart = new JButton("Start");
        buttonStop = new JButton("Stop");

        textArea = new JTextArea(10, 100);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerWorking) {
                    info = "Сервер уже запущен" + "\n";
                    textArea.append(info);
                } else {
                    info = "Сервер запущен" + "\n";
                    isServerWorking = true;
                    textArea.append(info);
                }

            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerWorking == false) {
                    info = "Сервер еще не запущен" + "\n";
                    textArea.append(info);
                } else {
                    info = "Сервер остановлен" + "\n";
                    isServerWorking = false;
                    textArea.append(info);
                }
            }
        });

        JPanel serverControlPanel = new JPanel(new GridLayout(1, 2));
        serverControlPanel.add(buttonStart);
        serverControlPanel.add(buttonStop);

        JPanel textPanel = new JPanel(new GridLayout(1,1));
        textPanel.add(textArea);

        add(new JScrollPane(textPanel));
        add(serverControlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public boolean checkServerStatus() {
        return isServerWorking;
    }

    public void sendMessageToClients(String message) {
        for (ClientGUI client : listOfClients) {
            client.getMessageFromServer(message);
        }

    }

    private void addLog(String log) {
        textArea.append(log);
    }

    public void addClient(ClientGUI client) {
        listOfClients.add(client);
        String clientConnectionMessage = client.getClientLogin() + " " + "подключился к серверу" + "\n";
        sendMessageToClients(clientConnectionMessage);
        textArea.append(clientConnectionMessage);
    }

    public void getMessageFromClient(String message) {
        sendMessageToClients(message);
        addLog(message);
        addLogToFile(message);
    }

    private void addLogToFile(String log) {
        try {
            FileWriter writer = new FileWriter("log file.txt", true);
            writer.write(log);
            writer.close();
        } catch (IOException e) {
            addLogToFile("Ошибка записи: " + " " + e.getMessage());
        }
    }

    public void getHistoryMessage(ClientGUI client) {
        try {
            if(logFile.length() != 0) {
                BufferedReader reader = new BufferedReader(new FileReader("log file.txt"));
                String historyMessages = reader.readLine();
                while (historyMessages != null) {
                    client.getMessageFromServer((historyMessages + "\n"));
                    historyMessages = reader.readLine();
                }
                reader.close();
            }
        } catch (IOException e) {
            addLogToFile("Ошибка чтения: " + " " + e.getMessage());
        }

    }
}
