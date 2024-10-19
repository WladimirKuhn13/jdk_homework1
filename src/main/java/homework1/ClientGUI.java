package homework1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 555;
    private static final int HEIGHT = 507;
    private ServerWindow serverWindow;
    private JTextArea textArea;
    private JButton buttonSend;
    private JButton connectToServer;
    private JTextField login;
    private JPasswordField password;
    private JTextField serverIP;
    private JTextField port;
    private JTextField message;
    private boolean connectionStatus = false;


    public ClientGUI(ServerWindow serverWindow) {

        this.serverWindow = serverWindow;

        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat client");
        setResizable(false);

        buttonSend = new JButton("Send");
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!message.getText().isEmpty() && connectionStatus) {
                    String textMessage = login.getText() + ":" + " " + message.getText() + "\n";
                    transfetMessageToServer(textMessage);
                } else {
                    JOptionPane.showMessageDialog(ClientGUI.this, "Сообщение пусто, либо вы не подключены к серверу!");
                }
            }
        });
        connectToServer = new JButton("Login");
        connectToServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(login.getText().isEmpty()) && (password.getPassword().length > 0) &&
                        !(serverIP.getText().isEmpty()) && !(port.getText().isEmpty()) && serverWindow.checkServerStatus()) {
                    JOptionPane.showMessageDialog(ClientGUI.this, "Данные приняты, вы подключены к серверу");
                    connectionStatus = true;
                    transferClientDataToServer();
                } else {
                    textArea.append("Подключение не удалось!" + "\n");
                }
            }
        });

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        login = new JTextField();
        login.setToolTipText("Введите логин");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Логин принят");
            }
        });
        password = new JPasswordField();
        password.setEchoChar('*');
        password.setToolTipText("Введите пароль");
        password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Пароль принят");
            }
        });
        serverIP = new JTextField();
        serverIP.setToolTipText("Введите IP сервера");
        serverIP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ClientGUI.this, "IP сервера принят");
            }
        });
        port = new JTextField();
        port.setToolTipText("Введите порт");
        port.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Порт принят");
            }
        });
        message = new JTextField();


        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(textArea, BorderLayout.CENTER);
        add(textPanel, BorderLayout.CENTER);


        JPanel serverPanel = new JPanel(new GridLayout(2, 3));
        serverPanel.add(login);
        serverPanel.add(password);
        serverPanel.add(serverIP);
        serverPanel.add(port);
        serverPanel.add(connectToServer);
        add(serverPanel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(message, BorderLayout.CENTER);
        messagePanel.add(buttonSend, BorderLayout.EAST);
        add(messagePanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public String getClientLogin() {
        return login.getText();
    }

    private void transferClientDataToServer() {
        serverWindow.addClient(this);
        requestMessageHistory();
    }

    private void printMessage(String message) {
        textArea.append(message);
    }

    public void getMessageFromServer(String message) {
        printMessage(message);
    }

    private void transfetMessageToServer(String message) {
        serverWindow.getMessageFromClient(message);
    }

    private void requestMessageHistory() {
        serverWindow.getHistoryMessage(this);
    }

}
