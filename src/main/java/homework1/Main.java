package homework1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerWindow serverWindow = new ServerWindow();
        new ClientGUI(serverWindow);
        new ClientGUI(serverWindow);
    }
}
